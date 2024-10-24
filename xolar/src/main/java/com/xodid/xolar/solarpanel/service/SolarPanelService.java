package com.xodid.xolar.solarpanel.service;

import com.amazonaws.services.iot.model.*;
import com.xodid.xolar.bill.domain.Bill;
import com.xodid.xolar.bill.service.BillService;
import com.xodid.xolar.electronic.domain.Electronic;
import com.xodid.xolar.electronic.service.ElectronicService;
import com.xodid.xolar.global.config.AwsConfig;
import com.xodid.xolar.global.exception.CustomException;
import com.xodid.xolar.global.exception.ErrorCode;
import com.xodid.xolar.solarpanel.domain.SolarPanel;
import com.xodid.xolar.solarpanel.dto.SolarPanelListResponseDto;
import com.xodid.xolar.solarpanel.dto.SolarPanelResponseDto;
import com.xodid.xolar.solarpanel.repository.SolarPanelRepository;
import com.xodid.xolar.user.domain.User;
import com.xodid.xolar.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolarPanelService {
    private final SolarPanelRepository solarPanelRepository;
    private final ElectronicService electronicService;
    private final BillService billService;
    private final UserService userService;
    private final AwsConfig awsConfig;

    // panelId로 태양광 패널 상세 조회
    public SolarPanelResponseDto findSolarPanelById(Long panelId) {
        SolarPanel solarPanel = findById(panelId);
        List<Electronic> elecList = electronicService.findAllBySolarPanel(solarPanel);
        List<Bill> billList = billService.findAllBySolarPanel(solarPanel);

        // 현재 연도,월 가져오기
        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue();

        // 이번달 전력 생산량 및 전력 소비량 총합
        Integer elecGenerationSum =  elecGenerationSum(elecList, year, month);
        Integer elecConsumption = elecConsumption(elecList, year, month);

        // 이번달 예상 수입 및 전기요금 총합
        Integer billGenerationSum = billGenerationSum(billList, year, month);
        Integer billConsumption = billConsumption(billList, year, month);

        return SolarPanelResponseDto.from(solarPanel, elecGenerationSum, elecConsumption, billGenerationSum, billConsumption);
    }

    // 태양광 패널 목록 조회
    public List<SolarPanelListResponseDto> findAllSolarPanels(){
        User user = userService.findById(1L);
        List<SolarPanel> solarPanels = findAllSolarPanels(user);

        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 각 패널에 대한 전력 생산량과 상태 정보를 담은 DTO 생성
        return solarPanels.stream().map(solarPanel -> {
            List<Electronic> elecList = electronicService.findAllBySolarPanel(solarPanel);
            Integer todayGeneration =  todayElecGeneration(elecList, today);
            return SolarPanelListResponseDto.from(solarPanel, todayGeneration);
        }).collect(Collectors.toList());
    }

    // 각 패널에 대한 월별 전력 생산량 총합
    public Integer elecGenerationSum(List<Electronic> elecList, int year, int month){
        return elecList.stream()
                .filter(electronic -> electronic.getDateTime().getYear() == year &&
                        electronic.getDateTime().getMonthValue() == month)  // 이번 달에 해당하는 전력만 필터링
                .mapToInt(Electronic::getElecGeneration)  // 전력 생산량 추출
                .sum();
    }

    // 각 패널에 대한 월별 전력 소비량 총합
    public Integer elecConsumption(List<Electronic> elecList, int year, int month){
        return elecList.stream()
                .filter(electronic -> electronic.getDateTime().getYear() == year &&
                        electronic.getDateTime().getMonthValue() == month)  // 이번 달에 해당하는 전력만 필터링
                .mapToInt(Electronic::getElecConsumption)  // 전력 소비량 추출
                .sum();
    }

    // 각 패널에 대한 월별 예상 수입 총합
    public Integer billGenerationSum(List<Bill> billList, int year, int month){
        return billList.stream()
                .filter(bill -> bill.getDateTime().getYear() == year &&
                        bill.getDateTime().getMonthValue() == month)  // 이번 달에 해당하는 요금만 필터링
                .mapToInt(Bill::getBillGeneration)  // 수입 추출
                .sum();
    }

    // 각 패널에 대한 월별 예상 전기요금 총합
    public Integer billConsumption(List<Bill> billList, int year, int month){
        return billList.stream()
                .filter(bill -> bill.getDateTime().getYear() == year &&
                        bill.getDateTime().getMonthValue() == month)  // 이번 달에 해당하는 요금만 필터링
                .mapToInt(Bill::getBillConsumption)  // 전기요금 추출
                .sum();
    }

    // 각 패널에 대한 오늘 전력 생산량
    public Integer todayElecGeneration(List<Electronic> elecList, LocalDate today){
        return elecList.stream()
                .filter(e -> e.getDateTime().toLocalDate().isEqual(today))
                .map(Electronic::getElecGeneration)  // 전력 생산량 값만 추출
                .findFirst()  // 첫 번째 값 찾기
                .orElse(0);
    }

    /**
     * 자동으로 AWS IoT에 사물을 등록하는 메서드
     */
    public String createThingAutomatically(String thingName) {
        // 해당 이름의 사물이 이미 존재하는지 확인
        if(!describeThing(thingName)){
            // 사물이 존재하지 않는 경우, 사물 생성
            CreateThingResult response = awsConfig.getIotClient()
                    .createThing(new CreateThingRequest().withThingName(thingName));
            System.out.print("사물이 성공적으로 생성되었습니다.");
            return "사물이 성공적으로 생성되었습니다.";
        }
        // 사물이 이미 존재하는 경우, 아래 메세지 반환
        return "해당 이름의 사물이 이미 존재합니다.";
    }

    /**
     * 해당 이름의 사물이 존재하는지 확인하는 메서드
     */
    private boolean describeThing(String thingName) {
        // 사물 이름이 null이라면 사물이 없다는 의미이므로 false 반환
        if(thingName == null){
            return false;
        }
        try {
            // 사물이 AWS IoT에 존재하는지 확인 -> 예외가 발생하지 않는다면 사물이 존재함을 의미하므로 true 반환
            DescribeThingResponse(thingName);
            return true;
        } catch (ResourceNotFoundException e){
            // 사물이 존재하지 않는경우, 예외가 발생하으로 false 반환
            return false;
        }
    }

    /**
     * AWS IoT에 해당 이름으로 이미 등록된 사물이 있는지 확인하는 메서드
     */
    private DescribeThingResult DescribeThingResponse(String thingName) {
        // 해당 사물의 이름으로 DescribeThingRequest 객체를 생성
        DescribeThingRequest describeThingRequest = new DescribeThingRequest();
        describeThingRequest.setThingName(thingName);

        // AWS IoT 클라이언트를 사용하여 describeThing 요청을 보내고, 결과 반환
        return awsConfig.getIotClient().describeThing(describeThingRequest);
    }


    /* Transaction 함수들 */

    // id로 solar-panel 조회
    @Transactional(readOnly = true)
    public SolarPanel findById(Long panelId) {
        return solarPanelRepository.findById(panelId).orElseThrow(
                ()-> new CustomException(ErrorCode.PANEL_NOT_FOUND));
    }

    // user로 solar-panel 목록 조회
    @Transactional(readOnly = true)
    List<SolarPanel> findAllSolarPanels(User user){
        return solarPanelRepository.findAllByUser(user);
    }
}
