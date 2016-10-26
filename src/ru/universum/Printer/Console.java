package ru.universum.Printer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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

    public void log (String message, String type){
        switch (type){
            case "":// standard
                System.out.println("("+getDateNow()+") "+str+": " + message);
                break;

            case "w":
                System.err.println("[WARNING] ("+getDateNow()+") "+str+": "+message);
                break;

            case "m":
                System.out.println("[MESSAGE] ("+getDateNow()+") "+str+": "+message);
                break;

            case "err":
                System.err.println("[ERROR] ("+getDateNow()+") "+str+": "+message);
                break;

            case "exc":
                System.err.println("[EXCEPTION] ("+getDateNow()+") "+str+": "+message);
                break;

        }
    }
}
