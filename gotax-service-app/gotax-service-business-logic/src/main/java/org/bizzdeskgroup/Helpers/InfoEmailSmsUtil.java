package org.bizzdeskgroup.Helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.api.SendEmailApi;
import com.infobip.api.SendSmsApi;
import com.infobip.model.*;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class InfoEmailSmsUtil {
    private static ApiClient InfoConfig(){
        ApiClient apiClient = new ApiClient();
        apiClient.setApiKeyPrefix("App");
        apiClient.setApiKey("550c7e6f96ebdcbed9dbdb748d2b53ad-225da415-a072-4d4a-9744-1c35f3fa0f82");
        apiClient.setBasePath("https://pwqyge.api.infobip.com");

        return apiClient;
    }

    public static void SendMail(String receiverEmail, String receiverName, String emailContent, String emailSubject){
        try {
            String receiver = receiverName +" <" + receiverEmail + ">";
            ApiClient apiClient = InfoConfig();
            SendEmailApi sendEmailApi = new SendEmailApi(apiClient);
//            File fileAttachment = new File("/path/report.csv");

            EmailSendResponse sendResponse = sendEmailApi
//                    .sendEmail("PaySure Digital <hello@paysuredigital.com>", receiver, emailSubject)
                    .sendEmail("PaySure Digital <hello@paysure.digital>", receiver, emailSubject)
//                    .sendEmail("hello@mail.bizzdeskgroup.com", receiverEmail, emailSubject)
                    .HTML(emailContent)
//                    .text("Test message with a file")
//                    .attachment(fileAttachment)
                    .execute();
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }
    }
    public static void SendSms(String message, String recipient) {
        ApiClient apiClient = InfoConfig();
        SendSmsApi sendSmsApi = new SendSmsApi(apiClient);
        SmsTextualMessage smsMessage = new SmsTextualMessage()
//                .from("ServiceSMS")
                .from("PaySureDigi")
                .addDestinationsItem(new SmsDestination().to(recipient))
                .text(message);

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest().messages(
                Collections.singletonList(smsMessage)
        );
        try {
            SmsResponse smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
            System.out.println(recipient);
            System.out.println(smsResponse);
        } catch (ApiException apiException) {
            System.out.println(apiException.getResponseBody());
        }
    }
    public static void SendSmsAlt(String message, String recipient) {

        try {
            RequestHandler sendSms = RestClientBuilder.newBuilder()
//            http://193.105.74.59/api/sendsms/plain?user=bizzdeskgroup&password=YBjTyq91@19&sender=PaySureDigi&SMSText=USSDServiceTest&GSM=23480669452749
//                    .baseUrl(new URL("http://193.105.74.59/api/sendsms/plain?user=bizzdeskgroup&password=YBjTyq91@19&sender=PaySureDigi" + "&SMSText=" + message + "&GSM=" + recipient))
                    .baseUrl(new URL("http://193.105.74.59/api/sendsms/plain"))
                    .build(RequestHandler.class);

            Response requests = sendSms.doSmsGetRequest("bizzdeskgroup", "YBjTyq91@19", "GetPaysure", message, recipient);


            System.out.println("==============="+requests.getHeaders());
            System.out.println("==============="+requests.getAllowedMethods());
            System.out.println("==============="+requests.getStatus());
            System.out.println("==============="+requests.getStatusInfo());
            System.out.println("==============="+requests.getMediaType());
            System.out.println("==============="+requests.getMediaType());
            System.out.println("==============="+requests.getEntity());

            String body = requests.readEntity(String.class);
            System.out.println("==============="+body);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
