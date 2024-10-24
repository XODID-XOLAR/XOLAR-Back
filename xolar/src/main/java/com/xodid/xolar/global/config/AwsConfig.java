package com.xodid.xolar.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.AWSIotDataClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretKey;

    /**
     * AWS IoT 클라이언트를 빈으로 등록
     */
    @Bean
    public AWSIot getIotClient(){
        // AWS IoT 클라이언트 생성
        return AWSIotClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        // IAM 액세스 키 아이디와 시크릿키를 사용해 인증 정보 제공
                        new BasicAWSCredentials(accessKeyId, secretKey)))
                // AWS 리전을 서울로 설정
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    /**
     * AWS IoT Data 클라이언트를 빈으로 등록
     */
    @Bean
    public AWSIotDataClient getIotDataClient(){
        // AWS IoT Data 클라이언트 생성
        return (AWSIotDataClient) AWSIotDataClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new AWSCredentials() {
                    // IAM 액세스 키 아이디와 시크릿키를 사용해 인증 정보 설정
                    @Override
                    public String getAWSAccessKeyId() {
                        return accessKeyId;
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return secretKey;
                    }
                }))
                // AWS 리전을 서울로 설정
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
