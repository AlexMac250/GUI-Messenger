package Server;

import ru.universum.Loader.Account;
import ru.universum.Printer.Console;

import java.util.Objects;
import java.util.Scanner;

public class ServerComReader extends Thread{
    String message = "";
    Scanner scanner = new Scanner(System.in);
    private boolean isAdminLogged = false;
    Console console = new Console("Server console");

    public void execute(String[] command){
        switch (command[0]){

            case "stop" :
                while(isAdminLogged) {
                    Server.console.log("Are u sure y/n");
                    message = scanner.next();
                    if (message.equals("y")) {
                        Server.CLOSE();
                        break;
                    }else{
                        if(!message.equals("n"))
                        System.out.println("Error symbol");
                    }
                }
                break;

            case "login" :
                if(command[1].equals("admin")){
                    if(!Server.logIn("admin",command[2]).equals(null)& !isAdminLogged){
                        isAdminLogged = true;
                        console.log("Admin logged in");
                    }else{
                        console.log("Error logging in console");
                    }
                }
                break;

            case "out" :
                if(isAdminLogged) {
                    isAdminLogged = false;
                }
                break;

            case "showInfo" :
                if(isAdminLogged) {
                    console.log("Meta-inf about server :" + '\n'
                            + "Connections : " + (Server.connections-40000) + '\n'
                            + "Users in base : " + Account.idGL);
                }
                break;

            case "startNew" :
                Server.startNew();
                break;


            case "ip" :
                Server.ip = command[1];
                console.log("Restart server?(y/n)");
                message = scanner.next();
                if(Objects.equals(message, "y")) {
                    Server.startNew();
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
            message = scanner.nextLine();
            message = message.toLowerCase();
            execute(descript(message));
        }
    }
}
