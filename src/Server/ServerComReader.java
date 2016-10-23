package Server;

import ru.universum.Loader.Account;
import ru.universum.Printer.Console;

import java.sql.Time;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ServerComReader extends Thread{
    String message = "";
    Scanner scanner = new Scanner(System.in);
    private boolean isAdminLogged = false;
    Console console = new Console("Server-Console");
    boolean isExecuting = false;
    public void execute(String[] command){
        switch (command[0]){

            case "stop" :
                isExecuting = true;
                while (isAdminLogged){
                    if (!Server.isClosed) {
                        Server.console.log("Are u sure?(y/n)");
                        message = scanner.next();
                        message = message.toLowerCase();
                        if (message.equals("y") & !Server.isClosed) {
                            Server.CLOSE();
                            break;
                        } else {
                            if (message.equals("n")) {
                                isExecuting = false;
                                break;
                            } else {
                                System.out.println("Error symbol");
                            }
                        }
                    }else{
                        System.err.println("Server already stopped >>> change ip and restart it");
                        break;
                    }
                }
                isExecuting = false;
                break;

            case "login" :
                isExecuting = true;
                if(command[1].equals("admin")){
                    Account acc = Server.logIn("admin" , command[2]);
                    if(acc != null& !isAdminLogged){
                        isAdminLogged = true;
                        console.log("Admin logged in");
                    }else{
                        console.log("Error logging in console");
                    }
                }
                isExecuting = false;
                break;

            case "out" :
                isExecuting = true;
                if(isAdminLogged) {
                    isAdminLogged = false;
                }else {
                    System.err.println("Not enough permissions >>> login as Admin");
                }
                isExecuting = false;
                break;

            case "showinfo" :
                if(isAdminLogged) {
                    console.log("Meta-inf about server :" + '\n'
                            + "Connections : " + (Server.connections-40000) + '\n'
                            + "Users in base : " + Account.idGL+1 + '\n'
                            + "IP-ADDRESS OF SERVER - "+ Server.ip );
                }else {
                    System.err.println("Not enough permissions >>> login as Admin");
                }
                isExecuting = false;
                break;

            case "restart" :
                isExecuting = true;
                Server.startNew();
                isExecuting = false;
                break;

            case "ip" :
                isExecuting = true;
                if(isAdminLogged) {
                    Server.ip = command[1];
                    for(int i = 0 ; i<3 ; i++){
                        try {
                            System.out.println("Server restarts in " + (3-i));
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {}
                    }
                try {
                    if (System.getProperty("os.name").equals("Windows")) new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    if (System.getProperty("os.name").equals("Linux")) new ProcessBuilder("terminal", "clear").inheritIO().start().waitFor();
                } catch (Exception e) {}
                Server.startNew();
                }
                break;

            case "help" :
                isExecuting = true;
                if(!isAdminLogged) {
                    System.out.println("Enter \"login\" to log in as Admin");
                }else {
                    System.out.println(
                            "--------------------------------- \n" +
                            "Command \"stop\" - stops server if this one is working \n" +
                            "Command \"out\" - logging out \n" +
                            "Command \"showinfo\" - shows meta-inf about server \n" +
                            "Command \"restart\" - restarting server with default fields (excepting Base) \n" +
                            "Command \"ip\" [0.0.0.0] - changes ip of Server and then restarts it on new address \n" +
                            "Command \"exit\" - shutdown program"+
                            "--------------------------------- \n");
                }
                isExecuting = false;
                break;

            case "exit" :
                if(isAdminLogged){
                    if (Server.isClosed){
                        message = scanner.next();
                        message = message.toLowerCase();
                        if (message.equals("y") & !Server.isClosed) {
                            System.exit(0);
                            break;
                        } else {
                            if (message.equals("n")) {
                                break;
                            } else {
                                System.out.println("Error symbol");
                            }
                        }
                    } else {
                        System.err.println(" >> Server is running! << ");
                    }

                } else {
                    System.err.println("Not enough permissions >>> login as Admin");
                }
                break;

            default :
                console.log("Enter \"help\" for more information");

        }
    }

    public String[] descript(String message){
        String[] s = new String[4];
        char[] c = message.toCharArray();
        int i = 0;
        s[0] = "";
        for (char ch : c){
            if(ch == ' '& !Objects.equals(s[i], "")){
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
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                interrupt();
            }
        while (!isExecuting) {
            System.out.print("Command >>>");
            message = scanner.nextLine();
            message = message.toLowerCase();
            execute(descript(message));
                try {
                TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                interrupt();
                }
            }
        }
    }
}
