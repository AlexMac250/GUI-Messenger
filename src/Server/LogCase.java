package Server;

import java.util.ArrayList;
import java.util.List;

public class LogCase {
    private static boolean isShowingLogs = false;
    static List<Log> logs = new ArrayList<>();

    public static void offLogs(){
        isShowingLogs = false;
    }

    public static void onLogs(){
        isShowingLogs = true;
    }

    public static void putLog(Log log){
        if(!isShowingLogs)
            logs.add(log);
            else System.out.println(log.toString());
    }

    public static void showLogs (){
        for (Log log : logs){
            System.out.println(log);
        }
    }

    public static void showLastOf(int howMuch){
        int onWhichLog =0;
        for (int i = 0; i < howMuch ; i++){
            //howMuch>logs.size()? howMuch = logs.size()-1 :  ;
            onWhichLog = logs.size()-1-howMuch+i;
            System.out.println(logs.get(onWhichLog).toString());
        }
    }
}
