package Server;

import java.util.ArrayList;
import java.util.List;

public class LogCase {
    private static boolean isShowingLogs = true;
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
        for (int i = 0; i < howMuch-1 ; i++) {
            if(size-1-howMuch>0){
                System.out.println(logs.get(logs.size()-1-howMuch));
                }else{
                break;
            }
            }
        }
    }
}
