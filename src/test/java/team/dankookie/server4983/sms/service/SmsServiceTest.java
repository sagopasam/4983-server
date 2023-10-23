package team.dankookie.server4983.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.client.MockRestServiceServer;
import team.dankookie.server4983.sms.constant.SmsConstant;

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

}

@SpringBootTest
class SmsApiTest {

    @Autowired
    SmsService smsService;

    @Disabled
    @Test
    void 실제_api_테스트() {
        smsService.sendCertificationNumberToPhoneNumber("01057490339");
    }

}