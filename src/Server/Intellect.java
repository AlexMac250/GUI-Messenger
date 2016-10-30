package Server;

import ru.universum.Loader.Account;
import ru.universum.Loader.Command;
import ru.universum.Loader.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class Intellect extends Thread{
    private int GettingUsers = 0;
    private DataOutputStream os;
    private DataInputStream is;
    private Account acc;
    private WorkingServ serv ;
    private List<Answer> answers = new ArrayList<>();

    Intellect(WorkingServ workingServ) {
        this.os = workingServ.dataOutputStream;
        this.serv = workingServ;
        this.is = workingServ.is;
        this.acc = workingServ.acc;
        start();
    }

    private String[] descript(String message){
        List<StringBuilder> sl = new ArrayList<>();
        char[] c = message.toCharArray();
        int i = 0;
        sl.add(new StringBuilder());
        for (char ch : c){
            if(ch == '$' & i!=3 | ch == '[' | ch == ']'){
                i++;
                sl.add(new StringBuilder());
            }else{
                sl.get(i).append(ch);
            }
        }
        String s[] = new String[4];
        i = 0;
        for(StringBuilder builder : sl){
            s[i] = builder.toString();
            i++;
        }
        return s;
    }

    private void sendOffline(Message message, int id){
        Server.accs.get(id).oflineMes.add(message);
    }

    private void execute(String[] command){
        int i = 0;

        switch (command[0]){
            case "send":
                if(Server.accs.get(Integer.parseInt(command[1])).isOnline){
                    int to = Integer.parseInt(command[1]);
                    command[1] = String.valueOf(acc.id);
                    Server.accs.get(to).getWorkingServ().execute(command);
                }
                else{
                    int to = Integer.parseInt(command[1]);
                    command[1] = String.valueOf(acc.id);
                    sendOffline(new Message("message", command[1] , command[2], command[3]) , to);
                }
                break;

            case "login":
                serv.execute(command);
                acc = serv.acc;
                break;

            case "register" :
                serv.execute(command);
                break;

            case "addFriend" :
                serv.execute(command);
                break;

            case "resOfFriend" :
                answers.add(new Answer(Integer.parseInt(command[1]),command[2],serv));
                break;

            case "getUsers":
                GettingUsers = 0;
                for(Account account : Server.accs){
                    if(i<=Server.accs.size()-1) {
                        if (GettingUsers != 20) {
                            serv.send(new Command("user", new String[]{account.login, String.valueOf(account.id) , String.valueOf(account.isOnline)}));
                            i++;
                        }else{
                            break;
                        }
                    }
                    else{
                        break;
                    }
                }
                break;

            case "get20More" :
                for(i = GettingUsers;i<=GettingUsers+20;i++){
                    if(i<=Server.accs.size()-1) {
                        Account account = Server.accs.get(i);
                        serv.send(new Command("user", new String[]{account.login, String.valueOf(account.id) , String.valueOf(account.isOnline)}));
                    }else{
                        break;
                    }
                }
                GettingUsers+=20;
                break;

            case "findByNick":
                for(Account account :Server.accs){
                    if(account.login.equals(command[1])){
                        serv.send(new Command("user", new String[]{account.login, String.valueOf(account.id) , String.valueOf(account.isOnline)}));
                    }else{
                        serv.send(new Command("user","null"));
                    }
                }
                break;

            case "close" :
                    serv.close();
                break;
        }
    }

    @Override
    public void run() {
        while (!interrupted()){
            try {
                String message = is.readUTF();
                serv.console.log("Got command " + message, "m");
                execute(descript(message));
            } catch (IOException e) {
                System.err.println("\nUser disconnected " + "# " + (serv.port-40000));
                interrupt();
            }
        }
    }
}