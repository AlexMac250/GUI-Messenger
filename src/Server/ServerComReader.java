package Server;

import java.util.Scanner;

public class ServerComReader extends Thread{
    String message = "";
    Scanner scanner = new Scanner(System.in);

    public void execute(String[] command){
        switch (command[0]){
            case "stop" :
                Server.console.log("Are u sure y/n");
                message = scanner.next();
                if(message.equals("y")|message.equals("Y")){
                    Server.CLOSE();
                }
                break;
        }
    }

    public String[] descript(String message){
        String[] s = new String[4];
        char[] c = message.toCharArray();
        int i = 0;
        s[0] = "";
        for (char ch : c){
            if(ch == ' '){
                i++;
                s[i] = "";
            }else{
                s[i]+=(ch);
            }
        }
        return s;
    }

    @Override
    public void run() {
        while (!interrupted()){
            //ТУТ БУДЕШЬ ВМЕСТО СКАННЕРА ВСТАВЛЯТЬ СВОЮ ШАЛУПОНЬ
            message = scanner.next();
            execute(descript(message));
        }
    }
}
