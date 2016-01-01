package com.digitcreativestudio.safian.adadosen.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by faqih_000 on 1/1/2016.
 */
public class Utils {
    public static String getFriendlyDate(Date date){
        String result;
        SimpleDateFormat resultDate;

        long diff = new Date().getTime() - date.getTime();

        if(diff / (365*24*3600000) > 0){
            resultDate = new SimpleDateFormat("d MMM yyyy");
            result = resultDate.format(date);
        }else if(diff / (7*24*3600000) > 6){
            resultDate = new SimpleDateFormat("d MMM");
            result = resultDate.format(date);
        }else if(diff / (3600000) > 23){
            if(diff / (24*3600000) < 2)
                result = "Kemarin";
            else
                result = Math.round((float) (diff / (24*60*60*1000))) +" hari yang lalu";
        }else if(diff / (60*1000) > 59){
            result = Math.round((float) (diff / (60*60*1000))) +" jam yang lalu";
        }else if(diff / (1000) > 59){
            result = Math.round((float) (diff / (60*1000))) +" menit yang lalu";
        }else if(diff / (1000) > 0){
            result = Math.round((float) (diff / (1000))) +" detik yang lalu";
        }else{
            result = "Baru saja";
        }
//
//        Calendar message = Calendar.getInstance();
//        message.setTimeInMillis(date.getTime());
//        Calendar now = Calendar.getInstance();
//
//        if(message.get(Calendar.YEAR) != now.get(Calendar.YEAR)){
//
//        }else if((message.get(Calendar.MONTH) != now.get(Calendar.MONTH)) || (message.get(Calendar.DAY_OF_MONTH) != now.get(Calendar.DAY_OF_MONTH))){
//            int diff = (now.get(Calendar.DAY_OF_MONTH) - message.get(Calendar.DAY_OF_MONTH));
//
//        }else if(message.get(Calendar.HOUR_OF_DAY) != now.get(Calendar.HOUR_OF_DAY)){
//            result = (now.get(Calendar.HOUR_OF_DAY) - message.get(Calendar.HOUR_OF_DAY))+" jam yang lalu";
//        }else if(message.get(Calendar.MINUTE) != now.get(Calendar.MINUTE)){
//            result = (now.get(Calendar.MINUTE) - message.get(Calendar.MINUTE))+" menit yang lalu";
//        }else if(message.get(Calendar.SECOND) != now.get(Calendar.SECOND)){
//            int diff = (now.get(Calendar.SECOND) - message.get(Calendar.SECOND));
//            if(diff <= 0) result = "Baru saja";
//            else result = diff+" detik yang lalu";
//        }else{
//            result = "Baru saja";
//        }

        return result;
    }
}
