package Server;

import ru.universum.Loader.Account;
import ru.universum.Loader.FileLoader;
import ru.universum.Loader.Friend;
import ru.universum.Printer.Console;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
     static ServerSocket mainSocket;
     static Integer connections = 40000;
     static ServerConsole console = new ServerConsole();
     private static List<WorkingServ> active = new ArrayList<>();
     static List<Account> accs = FileLoader.Import();
     private static List<Integer> freePorts = new ArrayList<>();
     static boolean isClosed = false;
     private static int portlocal = 0;
    static InetAddress ADDRESS;
     static ServerComReader reader = new ServerComReader();

    static List<WorkingServ> getActive() {
        return active;
    }

    private Server(Socket socket) throws IOException {
        portlocal = getPort();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeInt(portlocal);
        os.close();
        socket.close();
        active.add(new WorkingServ(portlocal));
    }

    static synchronized boolean addFriend(int id, Friend friend){
        boolean isInFriend = false;
        for(Friend fr : accs.get(id).friends){
            if(Objects.equals(fr.login, friend.login)){
                isInFriend = true;
                break;
            }
        }
        if(!isInFriend) {
            accs.get(id).friends.add(friend);
            isInFriend = true;
            boolean needToRewrite = false;
            for (Account acc: accs) {
                FileLoader.rewriteInBase(acc,needToRewrite);
                needToRewrite = true;
            }
            console.log("Base rewritten", "m");
        }else{
            isInFriend = false;
        }
        return isInFriend;
    }

    private static synchronized int getPort(){
        if(freePorts.size()!=0){
            portlocal = freePorts.get(0);
            freePorts.remove(0);
        }
        else{
            portlocal = connections;
        }
        return portlocal;
    }

    static synchronized void closeConnection(int idOF){
        freePorts.add(active.get(idOF).port);
        Server.active.remove(idOF);
    }

    static synchronized boolean register(String login, String password){
        boolean res = true;
        Account.idGL++;
        if(accs.size()!=0){
            for (Account acc : accs){
                if(!res){//если res = true , то проверка идет дальше до конца списка
                    break;
                }
                res = !acc.login.equals(login);
            }
        }else res = true;
        if(res){
            FileLoader.writeInBase(new Account((int)Account.idGL , login , password , new ArrayList<>()));
            accs.add(new Account((int)Account.idGL , login , password , new ArrayList<>()));
        }
        return res;
    }

    static synchronized Account logIn(String login, String password){
        Account a = null;
        for (Account acc: accs) {
            if(Objects.equals(acc.login, login) && Objects.equals(acc.password, password)){//FIXME PASSWORD TO MD5!
                if(!acc.isOnline) {
                    a = acc;
                    acc.isOnline = true;
                }
            }
        }
        return a;
    }

    static void CLOSE() {
        if (!isClosed) {
            for (WorkingServ w : active) {
                String[] close = new String[1];
                close[0] = "close";
                w.execute(close);
            }
            isClosed = true;
            try {
                if (mainSocket != null)
                    mainSocket.close();
            } catch (Exception ignored) {}
            console.log("Server stopped", "w");
        }
    }

    public static void setAddress() {
        try {
        List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        for(NetworkInterface intf : interfaces) {
            //System.out.println("("+intf.getName()+"):---");
            List<InetAddress> addrs = Collections.list(intf.getInetAddresses());

            for(InetAddress ipAddress : addrs){

                if(!ipAddress.isLoopbackAddress() & !ipAddress.isLinkLocalAddress()){
                    ADDRESS = ipAddress;
                    break;
                }
                //System.out.println("IP: "+ipAddress.getHostAddress());
            }
            //System.out.println("---");
        }
    }catch (Exception e) {console.log(""+e, "exc");
        }
    }


    static void startNew(){
        if(!isClosed){
            CLOSE();
        }
        reader.interrupt();
        connections = 40000;
        active = new ArrayList<>();
        accs = FileLoader.Import();
        freePorts = new ArrayList<>();
        isClosed = false;
        portlocal = 0;
        reader = new ServerComReader();
        reader.start();
        start();
    }

    static void start() {
        Account.idGL = accs.size()-1;
          try {
              mainSocket = new ServerSocket(2905,0,ADDRESS);
              console.log("started on " + mainSocket.getInetAddress().getHostAddress(), "m");
          } catch (Exception e1) {
              CLOSE();
              System.out.println("[MESSAGE] Restart server with another IP");
          }
              while (!isClosed) {
                  try {
                      new Server(mainSocket.accept());
                  }catch (Exception e){
                      CLOSE();
                      console.log("Connection lost", "w");
                      break;
                  }
              }

    }
}
