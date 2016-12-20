package ru.universum.Printer;

import Server.Log;
import Server.LogCase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
    private static Date date = new Date();
    private static SimpleDateFormat formatter;
    private String from;
    public Console(String whois) {
        this.formatter = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
        this.from = whois;
    }

    public static String getDateNow(){
        date = new Date();
        return formatter.format(date);
    }

    public void log (String message, String type){
        switch (type){
            case "":// standard
                LogCase.putLog(new Log("\n("+getDateNow()+") "+ from +": " + message));
                break;

            case "w":
                LogCase.putLog(new Log("\n[WARNING] ("+getDateNow()+") "+ from +": "+message));
                break;

            case "m":
                LogCase.putLog(new Log("\n[MESSAGE] ("+getDateNow()+") "+ from +": "+message));
                break;

            case "err":
                LogCase.putLog(new Log("\n[ERROR] ("+getDateNow()+") "+ from +": "+message));
                break;

            case "exc":
                LogCase.putLog(new Log("\n[EXCEPTION] ("+getDateNow()+") "+ from +": "+message));
                break;

            default:
                System.err.println("["+type+"] ("+getDateNow()+") "+ from +": "+message);
                break;

        }
    }
}
