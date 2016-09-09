package ru.universum.Printer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
    static Date date = new Date();
    static SimpleDateFormat formatter;
    String str;
    public Console(String str) {
        this.formatter = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
        this.str = str;
    }

    public static String getDateNow(){
        date = new Date();
        return formatter.format(date);
    }

    public void log (String message){
        System.out.println(getDateNow() + ' ' + '[' + str + ']' + ':' + message);
    }
}
