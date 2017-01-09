package Server;

import ru.universum.Loader.Security;
import ru.universum.Loader.Account;
import ru.universum.Printer.Console;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class ServerComReader extends Thread{
    private String message = "";
    private Scanner scanner = new Scanner(System.in);
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    public boolean isRemoting;
    private String pass = "";

    public void setRemoting(boolean remoting) {
        isRemoting = remoting;
    }

    private boolean isAdminLogged = false;
    private Console console = new Console("Server-Console");
    private boolean isExecuting = false;

    private void execute(String[] command) {
        try {
            switch (command[0]) {
                case "stop":
                    isExecuting = true;
                    while (isAdminLogged) {
                        if (!Server.isClosed) {
                            Server.console.log("Type password one more time", "m");
                            read();
                            if(Security.getMD5(message.toCharArray()).equals(pass)){
                            Server.console.log("Are u sure? (y/n)", "m");
                            read();
                            if (message.equals("y") & !Server.isClosed) {
                                Server.CLOSE();
                                break;
                            } else
                                {
                                if (message.equals("n"))
                                    {
                                    isExecuting = false;
                                    break;
                                    } else
                                        {
                                    System.out.println("Error symbol");
                                    }
                                }
                            }
                        } else {
                            Server.console.log("Server already stopped >>> change ip and restart it", "err");
                            break;
                        }
                    }
                    break;

                case "login":
                    isExecuting = true;
                    if (command[1].equals("admin")) {
                        Account acc = null;
                        for (Account a: Server.accs) {
                            if (Objects.equals(a.password, Security.getMD5(command[2].toCharArray())) & a.login.equals("admin"))
                                acc = a;
                            assert acc != null;
                            pass = acc.password;
                        }
                        if (acc != null & !isAdminLogged) {
                            isAdminLogged = true;
                            System.out.println("[MESSAGE]" + '(' +Console.getDateNow() + ')' + "Server-Console : Admin logged");
                        } else {
                            System.err.println("[ERROR]" + '(' +Console.getDateNow() + ')' + "Server-Console : Error logging");
                        }
                    }
                    break;

                case "out":
                    isExecuting = true;
                    if (isAdminLogged) {
                        isAdminLogged = false;
                    } else {
                        System.err.println("Not enough permissions >>> login as Admin");
                    }
                    break;

                case "showinfo":
                    if (isAdminLogged) {
                        System.out.println("\nMeta-inf about server :" + '\n'
                                + "Connections : " + (Server.connections - 40000) + '\n'
                                + "Users in base : " + (Account.idGL + 1) + '\n'
                                + "IP-ADDRESS OF SERVER - " + Server.ADDRESS.getHostAddress() + '\n');
                    } else {
                        System.err.println("Not enough permissions >>> login as Admin");
                    }
                    break;

                case "restart":
                    isExecuting = true;
                    try {
                        if (System.getProperty("os.name").equals("Windows"))
                            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } catch (Exception e) {
                        interrupt();
                    }
                    Server.startNew();
                    break;

                case "ip":
                    isExecuting = true;
                    if (isAdminLogged) {
                        try {
                            Server.ADDRESS = InetAddress.getByName(command[1]);
                        } catch (UnknownHostException e) {
                            console.log("" + e, "exc");
                        }
                        for (int i = 0; i < 3; i++) {
                            try {
                                System.out.println("Server restarts in " + (3 - i));
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                interrupt();
                            }
                        }
                        try {
                            if (System.getProperty("os.name").equals("Windows"))
                                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        } catch (Exception e) {
                            interrupt();
                        }
                        Server.startNew();
                    }
                    break;

                case "help":
                    isExecuting = true;
                    if (!isAdminLogged) {
                        System.out.println("Enter \"login\" to log in as Admin");
                    } else {
                        System.out.println(
                                        "--------------------------------- \n" +
                                        "Command \"stop\" - stops server if this one is working \n" +
                                        "Command \"out\" - logging out \n" +
                                        "Command \"showinfo\" - shows meta-inf about server \n" +
                                        "Command \"restart\" - restarting server with default fields (excepting Base) \n" +
                                        "Command \"ip\" [0.0.0.0] - changes ip of Server and then restarts it on new address \n" +
                                        "Command \"exit\" - shutdown program" +
                                        "\n--------------------------------- \n");
                    }
                    break;

                case "exit":
                    if (isAdminLogged) {
                        if (Server.isClosed) {
                            Server.console.log("Are u sure?(y/n)", "");
                            read();
                            if (message.equals("y")) {
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

                case "logs":
                    switch (command[1]) {

                        case "off":
                            LogCase.offLogs();
                            break;
                        case "on":
                            LogCase.onLogs();
                            break;
                        case "showlast":
                            LogCase.showLastOf(Integer.parseInt(command[2]));
                            break;

                    }
                    break;

                case "ifaces":
                    System.out.println(Server.ifases);
                    break;

                case "egg":
                    System.out.println("\n||\\  || ||̄ ̄ ̄ ̄  //̄ ̄ ̄ ̄ \\\\\n||\\\\ || ||____ ||    ||\n|| \\\\|| ||̄ ̄ ̄ ̄  ||    ||\n||  \\|| ||____ \\\\____//nline Server\n\nHave a nice day, bro! ;)\n");
                    break;
            }
        } catch (Exception ignored) {

        }
    }


    private String[] descript(String message){
        if(!message.equals("")) {
            List<StringBuilder> sl = new ArrayList<>();
            char[] c = message.toCharArray();
            int i = 0;
            sl.add(new StringBuilder());
            for (char ch : c) {
                if (ch == ' ' & !sl.get(sl.size() - 1).toString().equals("")) {
                    i++;
                    sl.add(new StringBuilder());
                } else {
                    sl.get(i).append(ch);
                }
            }
            String s[] = new String[4];
            i = 0;
            for (StringBuilder builder : sl) {
                s[i] = builder.toString();
                i++;
            }
            return s;
        }
        return null;
    }

    public void read(){
        if(!isRemoting) {
            message = scanner.nextLine();
            message = message.toLowerCase();
        }else {
            try {
                message = inputStream.readUTF();
                message = message.toLowerCase();
            } catch (IOException e) {
                isRemoting = false ;
                Server.remoteAccess.notConnected();
            }
        }
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException e) {
            interrupt();
        }
        System.out.println("\nEnter \"help\" for more info");
        while (!interrupted()){
            while (!isExecuting) {
                //System.out.print("[ENTER COMMAND]: ");
                read();
                execute(descript(message));
                isExecuting = false;
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }
}
