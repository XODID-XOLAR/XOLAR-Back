package com.xodid.xolar.global.config;

import com.amazonaws.services.iotdata.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xodid.xolar.solarpanel.domain.EmergencyStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class MQTTConfig {
    private final AwsConfig awsConfig;

    /**
     * AWS IoT Device Shadow에 메세지 게시
     * : 패널의 고유 번호를 이름으로하는 사물의 shadow에 어떤 비상 작동 버튼이 눌렸는지 넣어 메세지 게시
     *
     * AWS IoT Device Shadow: 디바이스의 현재 상태를 저장하여 디바이스가 오프라인 상태일 때도 정보를 클라우드에 보존하여
     * 언제든지 상태 정보에 접근할 수 있도록 한다.
     */
    public void publishToShadow(String panelNumber, EmergencyStatus emergencyStatus) throws IOException{
        // 게시할 주제 설정 (패널 고유 번호를 이름으로 하는 사물의 shadow에 메세지를 게시하기 위해)
        String topic = "$aws/things/" + panelNumber + "/shadow/update";

        // EmergencyStatus 객체를 JSON 문자열로 변환 (payload는 JSON 형태여야 하기때문)
        ObjectMapper objectMapper = new ObjectMapper();
        String emergencyStatusJson = objectMapper.writeValueAsString(emergencyStatus);

        // Device Shadow 내용을 업데이트하는 payload 설정 (비상 작동 모드를 status에 넣어 보냄)
        String payload = "{\"state\":{\"reported\":{\"status\":" + emergencyStatusJson +"}}}";
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(payload);

        // PublishRequest 객체 생성
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.withPayload(byteBuffer);
        publishRequest.withTopic(topic);
        publishRequest.setQos(0);

        // AWS IoT Data Client를 통해 메세지 게시
        awsConfig.getIotDataClient().publish(publishRequest);
        System.out.println("성공적으로 메세지를 게시했습니다.");
    }
}
