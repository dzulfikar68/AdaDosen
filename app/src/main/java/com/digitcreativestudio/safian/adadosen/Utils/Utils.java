package com.digitcreativestudio.safian.adadosen.Utils;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.digitcreativestudio.safian.adadosen.Data.DBContract;
import com.digitcreativestudio.safian.adadosen.Lecturers.LecturerUpdate;
import com.digitcreativestudio.safian.adadosen.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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

            cv.put(DBContract.LecturerEntry._ID, jsonLecturer.getInt("id"));
            cv.put(DBContract.LecturerEntry.COLUMN_NIP, jsonLecturer.getString("nip"));
            cv.put(DBContract.LecturerEntry.COLUMN_NAME, jsonLecturer.getString("name"));
            cv.put(DBContract.LecturerEntry.COLUMN_STATUS, jsonLecturer.getString("status").equals("1"));
            cv.put(DBContract.LecturerEntry.COLUMN_COMMENT, jsonLecturer.getString("comment"));
            cv.put(DBContract.LecturerEntry.COLUMN_LAST_MODIFY, sdf.parse(jsonLecturer.getString("last_modify")).getTime());
            cv.put(DBContract.LecturerEntry.COLUMN_MODIFIED_BY, shortenName(jsonLecturer.getString("modified_by")));
        }catch (JSONException je){
            je.printStackTrace();
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return cv;
    }

    public static void checkLecturerUpdate(Context mActivity, String id, String status, String position, String comment, Dialog commentDialog){
        Date lastModify = new Date();

        SessionManager session = new SessionManager(mActivity.getApplicationContext());

        if(!session.isLoggedIn()){
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("status", status);
            intent.putExtra("position", position);
            intent.putExtra("comment", comment);
            mActivity.startActivity(intent);
            Toast.makeText(mActivity, "Anda Belum Login", Toast.LENGTH_SHORT).show();
        }else{
            (new LecturerUpdate(mActivity, commentDialog)).execute(id, status, (session.getUserDetails()).get(SessionManager.KEY_ID), position);
        }
    }
}
