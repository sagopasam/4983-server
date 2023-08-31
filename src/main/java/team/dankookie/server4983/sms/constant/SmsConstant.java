package team.dankookie.server4983.sms.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SmsConstant {

    @Value("${naver-cloud-sms.accessKey}")
    public String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    public String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    public String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    public String phone;

}
