package Server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerConsole {
    static Date date = new Date();
    static SimpleDateFormat formatter;
    String str;
    public static String getDateNow(){
        date = new Date();
        return formatter.format(date);
    }
    public void log (String message, String type){
        switch (type){

            case "":// standard
                LogCase.putLog(new Log("("+getDateNow()+") "+str+": " + message));
                break;

            case "w":
                LogCase.putLog(new Log("[WARNING] ("+getDateNow()+") "+str+": "+message));
                break;

            case "m":
                LogCase.putLog(new Log("[MESSAGE] ("+getDateNow()+") "+str+": "+message));
                break;

            case "err":
                LogCase.putLog(new Log("[ERROR] ("+getDateNow()+") "+str+": "+message));
                break;

            case "exc":
                LogCase.putLog(new Log("[EXCEPTION] ("+getDateNow()+") "+str+": "+message));
                break;
        }
    }
}
