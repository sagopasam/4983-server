package team.dankookie.server4983.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.dankookie.server4983.sms.constant.SmsConstant;
import team.dankookie.server4983.sms.dto.SmsMessageDto;
import team.dankookie.server4983.sms.dto.SmsResponse;
import team.dankookie.server4983.sms.dto.SmsCertificationNumber;
import team.dankookie.server4983.sms.dto.ToSmsServerRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class SmsService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final SmsConstant smsConstant;

    public SmsService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper, SmsConstant smsConstant) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.smsConstant = smsConstant;
    }


    public SmsCertificationNumber sendCertificationNumberToPhoneNumber(String phoneNumber, Long time) {
        int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
        String content = "안녕하세요! 4983 입니다. \n인증번호는 " + randomNumber + " 입니다.";
        String apiServerUrl = "https://sens.apigw.ntruss.com/sms/v2/services/" + smsConstant.getServiceId() + "/messages";
        SmsMessageDto smsMessageDto = SmsMessageDto.of(phoneNumber, content);

        sendSms(time, apiServerUrl, smsMessageDto);

        return SmsCertificationNumber.of(String.valueOf(randomNumber));
        }

    private void sendSms(Long time, String apiServerUrl, SmsMessageDto smsMessageDto) {
        try {
            List<SmsMessageDto> messages = setMessagesToList(smsMessageDto);

            ToSmsServerRequest smsPayload = setToSmsServerPayload(smsMessageDto, messages);
            String payload = objectMapper.writeValueAsString(smsPayload);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", time.toString());
            headers.set("x-ncp-iam-access-key", smsConstant.getAccessKey());
            headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

            HttpEntity request = new HttpEntity(payload, headers);

            SmsResponse smsResponse = restTemplate.exchange(
                            apiServerUrl,
                            HttpMethod.POST,
                            request,
                            SmsResponse.class
                    )
                    .getBody();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Optional<SmsResponse> sendSms(SmsMessageDto smsMessageDto) {
        try {
            Long time = System.currentTimeMillis();

            List<SmsMessageDto> messages = setMessagesToList(smsMessageDto);

            ToSmsServerRequest smsPayload = setToSmsServerPayload(smsMessageDto, messages);
            String payload = objectMapper.writeValueAsString(smsPayload);


        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    private ToSmsServerRequest setToSmsServerPayload(SmsMessageDto smsMessageDto, List<SmsMessageDto> messages) {
        return ToSmsServerRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(smsConstant.getPhone())
                .content(smsMessageDto.content())
                .messages(messages)
                .build();
    }

    private List<SmsMessageDto> setMessagesToList(SmsMessageDto smsMessageDto) {
        return new ArrayList<>(Collections.singleton(smsMessageDto));
    }

    private String makeSignature(Long time)  {
        try {
            String space = " ";
            String newLine = "\n";
            String method = "POST";
            String url = "/sms/v2/services/" + smsConstant.getServiceId() + "/messages";

            String timestamp = time.toString();

            String message = method +
                    space +
                    url +
                    newLine +
                    timestamp +
                    newLine +
                    smsConstant.getAccessKey();

            SecretKeySpec signingKey = new SecretKeySpec(smsConstant.getSecretKey().getBytes(UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes(UTF_8));

            return Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";

    }
}
