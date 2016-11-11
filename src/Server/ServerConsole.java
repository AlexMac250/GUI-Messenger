package Server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerConsole {
    static Date date = new Date();
    static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy hh:mm:ss");;
    String str = "SERVER";

    public static String getDateNow(){
        date = new Date();
        return formatter.format(date);
    }

    public void log (String message){
        System.out.print(new Log("\n("+getDateNow()+") "+str+": " + message));
    }
    public void log (String message, String type){
        switch (type){

            case "w":
                System.out.print(new Log("\n[WARNING] ("+getDateNow()+") "+str+": "+message));
                break;

            case "m":
                System.out.print(new Log("\n[MESSAGE] ("+getDateNow()+") "+str+": "+message));
                break;

            case "err":
                System.out.print(new Log("\n[ERROR] ("+getDateNow()+") "+str+": "+message));
                break;

            case "exc":
                System.out.print(new Log("\n[EXCEPTION] ("+getDateNow()+") "+str+": "+message));
                break;
        }
    }
}