package org.bizzdeskgroup.Helpers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationMixer {

    public static String DoSmipleWork(String id, String code)
    {
        String userId = id;
        String xcode = code;
        //String combined = "userId=" + userId + "code="+ xcode;
        String combined = userId + "##gotaxUserService88##" + xcode;
        byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
        return new String(encodedBytes);


//        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
//        System.out.println("decodedBytes " + new String(decodedBytes));

        //throw new NotImplementedException();
    }

    public static String[] ReverseSimpleWork(String verify)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(verify);
        String message = new String(decodedBytes);
        return message.split( "##gotaxUserService88##");

        //throw new NotImplementedException();
    }

    public static Timestamp DateTime(){

        Calendar calendar = Calendar.getInstance();
        java.util.Date currentTime = calendar.getTime();
        long time = currentTime.getTime();

        return new Timestamp(time);
    }

    public static Timestamp ParseDate(String date) throws ParseException {

//        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        SimpleDateFormat sdf = new SimpleDateFormat(Locale.ROOT.toString());
        Date ss = sdf.parse(date);
        long longDate = ss.getTime()/1000;

        return new Timestamp(longDate);
    }

    public static String RandomNumbersString(int max, int min, int size){
        StringBuilder rand = new StringBuilder();
        for(int count = 0; count <= size; count++ ){
            rand.append((int) Math.floor(Math.random() * (max - min + 1) + min));
        }
        return rand.toString();
    }
}
