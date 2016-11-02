package ru.universum.Loader;

import com.sun.deploy.association.utility.WinRegistryWrapper;
import ru.universum.Client.Client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
    public static String getUserLogin(){
        String login = "";
        if (Client.os_name.equals("Windows")) login = WinRegistryWrapper.WinRegQueryValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, "SOFTWARE\\NEOnline\\SysInfo", "user_login");
        if (login != null) {
            return login;
        }
        return "";
    }

    public static String getUserPass(){
        String pass = "";
        if (Client.os_name.equals("Windows")) pass = WinRegistryWrapper.WinRegQueryValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, "SOFTWARE\\NEOnline\\SysInfo", "user_pass");
        if (pass != null) {
            return pass;
        }
        return "";
    }

    public static void setAboutUser(String login, String pass){
        WinRegistryWrapper.WinRegSetValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, "SOFTWARE\\NEOnline\\SysInfo", "user_login" , login);
        WinRegistryWrapper.WinRegSetValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, "SOFTWARE\\NEOnline\\SysInfo", "user_pass" , pass);
    }

    public static void setRemData(boolean bool){
        WinRegistryWrapper.WinRegSetValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, "SOFTWARE\\NEOnline\\SysInfo", "user_remdata" , bool ? "true" : "false");
    }

    public static boolean getRemData() {
        String res = WinRegistryWrapper.WinRegQueryValueEx(WinRegistryWrapper.HKEY_CURRENT_USER, "SOFTWARE\\NEOnline\\SysInfo", "user_pass");
        return res != null && Boolean.getBoolean(res);
    }

    public static String getMD5(char[] pass) {
        String st = null;
        for (char pas : pass) {
            st += pas;
        }
        MessageDigest messageDigest;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            assert st != null;
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }
}


