package Server;

import ru.universum.Loader.Account;
import ru.universum.Loader.FileLoader;
import ru.universum.Loader.Friend;
import ru.universum.Printer.Console;

import java.io.*;
import java.net.*;
import java.util.*;

@SuppressWarnings("ALL")
public class Server{
    static ServerSocket mainSocket;
    static Integer connections = 40000;
    static Console console = new Console("server");
    private static List<WorkingServ> active = new ArrayList<>();
    static List<Account> accs = FileLoader.Import();
    static List<Integer> freePorts = new ArrayList<>();
    static boolean isClosed = false;
    static int portlocal = 0;
    static DataOutputStream os;
    static String ip = null;

    public static List<WorkingServ> getActive() {
        return active;
    }

    public Server(Socket socket) throws IOException {
        portlocal = getPort();
        os = new DataOutputStream(socket.getOutputStream());
        os.writeInt(portlocal);
        os.close();
        socket.close();
        active.add(new WorkingServ(portlocal));
    }

    public static synchronized boolean addFriend(int id , Friend friend){
        boolean res = false;
        for(Friend fr : accs.get(id).friends){
            if(fr.login == friend.login){
                res = false;
                break;
            }
        }
        if(!res) {
            accs.get(id).friends.add(friend);
            res = true;
            boolean needToRewrite = false;
            for (Account acc: accs) {
                FileLoader.rewriteInBase(acc,needToRewrite);
                needToRewrite = true;
            }
            console.log("Base rewritten");
        }
        return res;
    }

    public static synchronized int getPort(){
        if(freePorts.size()!=0){
            portlocal = freePorts.get(0);
            freePorts.remove(0);
        }
        else{
            portlocal = connections;
        }
        return portlocal;
    }

    public static synchronized void closeConnection(int idOF){
        freePorts.add(active.get(idOF).port);
        Server.active.remove(idOF);
    }

    public static synchronized boolean register(String login , String password){
        boolean res = true;
        Account.idGL++;
        if(accs.size()!=0){
            for (Account acc : accs){
                if(!res){//если res = true , то проверка идет дальше до конца списка
                    break;
                }
                if (acc.login.equals(login)){
                    res = false;//если логин совпадает с существующим
                }else{
                    res = true;//если нет
                }
            }
        }else res = true;
        if(res==true){
            FileLoader.writeInBase(new Account(Account.idGL , login , password , new ArrayList<>()));
            accs.add(new Account(Account.idGL , login , password , new ArrayList<>()));
        }
        return res;
    }

    public static synchronized Account logIn(String login , String password){
        Account a = null;
        for (Account acc: accs) {
            if(Objects.equals(acc.login, login) && Objects.equals(acc.password, password)){
                if(!acc.isOnline) {
                    a = acc;
                    acc.isOnline = true;
                }
            }
        }
        return a;
    }

    public static void CLOSE(){
        for(WorkingServ w : active){
            String[] close = new String[1];
            close[0] = "close";
            w.execute(close);
        }
        isClosed = true ;
    }

    public static String getIp(){
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();
            System.out.println(ip);
        }catch (Exception e) {
        }
        return ip;
    }

    public static void main(String[] args) {
        System.out.println(accs.toString());
        Account.idGL = accs.size()-1;
            //Читает команды для сервера , внутри переделай
            ServerComReader reader = new ServerComReader();
            reader.start();
            try {
                reader.join();
            } catch (InterruptedException e) {
            }
          try {
            mainSocket = new ServerSocket(2905);
            console.log("started on " + InetAddress.getByName(getIp()));
            while (!isClosed) {
                try {
                    new Server(mainSocket.accept());
                }catch (Exception e){
                    console.log("Connection lost");
                }
            }
        } catch (Exception e) {
            CLOSE();
            e.printStackTrace();
        }
    }
}
