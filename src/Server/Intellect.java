package Server;

import ru.universum.Loader.Account;
import ru.universum.Loader.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Intellect extends Thread{
    private int GettingUsers = 0;
    private DataOutputStream os;
    private DataInputStream is;
    private Account acc;
    private String message;
    WorkingServ serv ;
    List<Answer> answers = new ArrayList<>();

    public Intellect(WorkingServ workingServ) {
        this.os = workingServ.ds;
        this.serv = workingServ;
        this.is = workingServ.is;
        this.acc = workingServ.acc;
        start();
    }

    public String[] descript(String message){
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

    public void sendOffline(Message message , int id){
        Server.accs.get(id).oflineMes.add(message);
    }

    public void execute(String[] command){
        int i = 0;
        switch (command[0]){
            case "send":
                if(Server.accs.get(Integer.parseInt(command[1])).isOnline){
                    command[1] = acc.login;
                    Server.accs.get(Integer.parseInt(command[1])).getWorkingServ().execute(command);
                }
                else{
                    sendOffline(new Message("message", acc.login , command[2], command[3]) , Integer.parseInt(command[1]));
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
                for(Account acc : Server.accs){
                    if(GettingUsers!=20){
                        serv.execute(new String[]{"send","user",String.valueOf(acc.id),acc.login});
                        i++;
                    }
                    else{
                        break;
                    }
                }
                break;
            case "get20More" :
                for(i = GettingUsers;i<=GettingUsers+20;i++){
                    Account account = Server.accs.get(i);
                    serv.execute(new String[]{"send","user",String.valueOf(account.id),account.login});
                }
                GettingUsers+=20;
                break;
        }
    }
    @Override
    public void run() {
        while (!interrupted()){
            try {
                message = is.readUTF();
                serv.console.log("Got command " + message);
                execute(descript(message));
            } catch (IOException e) {
                System.err.println("Error in server " + "# " + (serv.port-40000));
                interrupt();
            }
        }
    }
}