package org.bizzdeskgroup.Helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsUtil {
    public static StringBuffer SendSms(String message, String recipient) throws Exception{
//        try {
            String query = "https://app.multitexter.com/v2/app/sendsms";
            String email = "your multitexter registered email ";
            String password = "Your password";
            String apiKey = "Bearer HZBptwd8Z4jihtgPfrGc9HYKxosMfTiyNpgtxgHKnJVRM6ogj0phXtpPzwFk";
            String xmessage = "message content";
//            String sender_name = "Gotax";
            String sender_name = "IMPORTANT";
            String recipients = "mobile numbers seperated by comma e.9 2348028828288,234900002000,234808887800";
            String xrecipient = "mobile numbers seperated by comma e.9 2348028828288,234900002000,234808887800";
            String forcednd = "set to 1 if you want DND numbers to ";
//            String param = "{\"email\":\""+email+"\",\"password\":\""+password+"\",\"message\":\""+message+"\",\"sender_name\":\""+sender_name+"\",\"recipients\":\""+recipients+"\",\"forcednd\":\""+forcednd+"\"}";
            String param = "{\"message\":\""+message+"\",\"sender_name\":\""+sender_name+"\",\"recipients\":\""+recipient+"\",\"forcednd\":\""+forcednd+"\"}";

            URL url = new URL (query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection ();
            conn.setConnectTimeout (5000);
            conn.setRequestProperty ("Content-Type",
                    "application/json; charset=UTF-8");
            conn.setRequestProperty("Authorization", apiKey);
            conn.addRequestProperty ("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            conn.setDoOutput (true);
            conn.setDoInput (true);
            conn.setRequestMethod ("POST");

            OutputStream os = conn.getOutputStream ();
            os.write (param.getBytes ("UTF-8"));
            os.close ();

            // read the response
            // InputStream in = new BufferedInputStream(conn.getInputStream());
            // String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

            BufferedReader in = new BufferedReader (new InputStreamReader(conn.getInputStream ()));
            String inputLine;
            StringBuffer response = new StringBuffer ();

            while ((inputLine = in.readLine ()) != null) {
                response.append (inputLine);
            }

            in.close ();
            conn.disconnect ();

            System.out.println(response);
            return response;
//        } catch (Exception e) {
//            System.out.println (e.toString ());
//        }
//        return null;
    }
}
