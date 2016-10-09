package Server;

import ru.universum.Loader.Account;
import ru.universum.Printer.Console;

import java.util.Scanner;

public class ServerComReader extends Thread{
    String message = "";
    Scanner scanner = new Scanner(System.in);
    private boolean isAdminLogged = false;
    Console console = new Console("Server console");

    public void execute(String[] command){
        switch (command[0]){

            case "stop" :
                if(isAdminLogged) {
                    Server.console.log("Are u sure y/n");
                    message = scanner.next();
                    if (message.equals("y") | message.equals("Y")) {
                        Server.CLOSE();
                    }
                }
                break;

            case "login" :
                if(command[1].equals("admin")){
                    if(!Server.logIn("admin",command[2]).equals(null)){
                        Server.accs.get(0).isOnline = true;
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
                    Server.accs.get(0).isOnline = false;
                }
                break;

            case "showInfo" :
                if(isAdminLogged) {
                    console.log("Meta-inf about server :" + '\n'
                            + "Connections : " + (Server.connections-40000) + '\n'
                            + "Users in base : " + Account.idGL);
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
        System.out.println("Do you want start server automatically or enter IP?(1/2)");
        if(scanner.nextLine().equals("2")){
            System.out.println("Enter ip(Be careful about correctness");
            Server.ip = scanner.nextLine();
            interrupt();
        }else{
          //  Server.ip = Server.getIp();
            interrupt();
        }
        while (!interrupted()){
            //ТУТ БУДЕШЬ ВМЕСТО СКАННЕРА ВСТАВЛЯТЬ СВОЮ ШАЛУПОНЬ
            message = scanner.nextLine();
            execute(descript(message));
        }
    }
}
