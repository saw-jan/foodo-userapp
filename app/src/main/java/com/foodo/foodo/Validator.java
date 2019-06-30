package com.foodo.foodo;

import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static android.text.TextUtils.isDigitsOnly;

public class Validator {
    //check empty field
    public static boolean IsEmptyText(String input){
        if (TextUtils.isEmpty(input)) {
            return true;
        } else {
            return false;
        }
    }
    //checks, is numbers?
    public static boolean isNumeric(String str)
    {
        return isDigitsOnly(str);
        //return str.matches("-?\\d+(\\.\\d+)?");
    }
    //remove invalid chars
    public static String removeWords(String word, String remove){
        return word.replace(remove,"");
    }
    //check internet access
    public static boolean isNetAccess(){
        try{
            final InetAddress ipAddr = InetAddress.getByName("www.google.com");
            if(!ipAddr.equals("")){ return true;}
        }catch (UnknownHostException e){

            return false;
        }

        return false;
    }
}
