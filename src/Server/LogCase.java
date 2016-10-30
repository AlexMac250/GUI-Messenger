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
        if(logs.size()!=0){
        int onWhichLog;
        for (int i = 0; i < howMuch-1 ; i++) {
            if (howMuch >= logs.size() - 1) {
                howMuch = logs.size() - 1;
            } else {
                howMuch = logs.size()- 1 - howMuch;
            }
            onWhichLog = logs.size()-1-howMuch;
            System.out.println(logs.get(onWhichLog).toString());
            }
        }
    }
}
