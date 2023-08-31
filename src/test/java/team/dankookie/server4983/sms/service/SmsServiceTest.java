package team.dankookie.server4983.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import team.dankookie.server4983.sms.constant.SmsConstant;
import team.dankookie.server4983.sms.dto.SmsCertificationNumber;
import team.dankookie.server4983.sms.dto.SmsResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({SmsService.class})
class SmsServiceTest  {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    SmsService smsService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SmsConstant smsConstant;

    @Test
    void test() throws JsonProcessingException {
        //given
        Mockito.when(smsConstant.getServiceId())
                .thenReturn("testServiceId");

        String apiServerUrl = "https://sens.apigw.ntruss.com/sms/v2/services/" + smsConstant.getServiceId() + "/messages";

        Long time = System.currentTimeMillis();

        SmsResponse apiResponse = SmsResponse
                .builder()
                .requestId("RSSA-1693420097041-6351-57658285-GcAgkOfY")
                .requestTime(LocalDateTime.now())
                .statusCode(HttpStatus.ACCEPTED.toString())
                .certificationNumber(123123)
                .build();

        mockServer.expect(requestTo(apiServerUrl))
                .andRespond(withSuccess(objectMapper.writeValueAsString(apiResponse), MediaType.APPLICATION_JSON));

        //when
        SmsCertificationNumber smsCertificationNumber = smsService.sendCertificationNumberToPhoneNumber("01073352306", time);

        //then
        assertThat(smsCertificationNumber.certificationNumber()).isNotEmpty();
    }

}

@SpringBootTest
class SmsApiTest {

    @Autowired
    SmsService smsService;

    @Disabled
    @Test
    void 실제_api_테스트() {
        Long time = System.currentTimeMillis();
        smsService.sendCertificationNumberToPhoneNumber("01057490339", time);
    }

}