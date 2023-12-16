package com.yitai.utils;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.yitai.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: SendMsgUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/30 15:42
 * @Version: 1.0
 */
@Slf4j
public class SendMsgUtil {

    private static final String ALIBABA_CLOUD_ACCESS_KEY_ID = "xxxxx";
    private static final String ALIBABA_CLOUD_ACCESS_KEY_SECRET ="xxxxx";
    private static final String TEMPLATE_CODE = "xxxxx";
    private static final String SIGN_NAME = "xxxxx";

    public static boolean sendMsg(String phone, String verifyCode) {
        try {
            Client client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(phone).
                    setTemplateCode(TEMPLATE_CODE).setSignName(SIGN_NAME).
                    setTemplateParam("{\"code\":\"" + verifyCode + "\"}");
//            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
//            System.out.println(sendSmsResponse.getBody().getCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("发送消息异常");
        }
        return true;
    }
    public static Client createClient() throws Exception{
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(ALIBABA_CLOUD_ACCESS_KEY_ID)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(ALIBABA_CLOUD_ACCESS_KEY_SECRET);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        // 特化请求客户端
        return new Client(config);
    }
}
