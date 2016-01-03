package com.digitcreativestudio.safian.adadosen.Utils;

import android.content.ContentValues;

import com.digitcreativestudio.safian.adadosen.Data.DBContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
                result = Math.round(Math.ceil(diff / (24*60*60*1000))) +" hari yang lalu";
        }else if(diff / (60*1000) > 59){
            result = Math.round(Math.ceil(diff / (60*60*1000))) +" jam yang lalu";
        }else if(diff / (1000) > 59){
            result = Math.round(Math.ceil(diff / (60*1000))) +" menit yang lalu";
        }else if(diff / (1000) > 0){
            result = Math.round(Math.ceil(diff / (1000))) +" detik yang lalu";
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

    public static String shortenName(String name){
        String[] nameArray = name.split("\\s+");

        String newName = "";
        for(int i = 0; i < nameArray.length; i++){
            if(i>1){
                newName += " "+nameArray[i].charAt(0)+".";
            }else{
                newName += " "+nameArray[i];
            }
        }
        return newName;
    }

    public static ContentValues parseJsonLecturer(JSONObject jsonLecturer){
        ContentValues cv = new ContentValues();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            cv.put(DBContract.LecturerEntry._ID, jsonLecturer.getInt("id"));
            cv.put(DBContract.LecturerEntry.COLUMN_NIP, jsonLecturer.getString("nip"));
            cv.put(DBContract.LecturerEntry.COLUMN_NAME, jsonLecturer.getString("name"));
            cv.put(DBContract.LecturerEntry.COLUMN_STATUS, jsonLecturer.getString("status").equals("1"));
            cv.put(DBContract.LecturerEntry.COLUMN_LAST_MODIFY, sdf.parse(jsonLecturer.getString("last_modify")).getTime());
            cv.put(DBContract.LecturerEntry.COLUMN_MODIFIED_BY, shortenName(jsonLecturer.getString("modified_by")));
        }catch (JSONException je){
            je.printStackTrace();
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return cv;
    }
}
