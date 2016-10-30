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
        if(!isShowingLogs) logs.add(log);
            else {
            System.out.println(log.toString());
            System.out.print("[ENTER COMMAND]");
        }
    }

    public static void showLogs (){
        for (Log log : logs){
            System.out.println(log);
        }
    }

    public static void showLastOf(int howMuch){
        int size = logs.size();
        if(size!=0){
        int onWhichLog;
        for (int i = 0; i < howMuch-1 ; i++) {
            if (howMuch >= size - 1) {
                howMuch = size - 1;
            } else {
                howMuch = size- 1 - howMuch;
            }
            onWhichLog = size-1-howMuch;
            System.out.println(logs.get(onWhichLog).toString());
            }
        }
    }
}
