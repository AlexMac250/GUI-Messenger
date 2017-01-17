package Server;

import ru.universum.Loader.Account;
import ru.universum.Loader.FileLoader;
import ru.universum.Loader.Friend;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    private static ServerSocket mainSocket;
    static Integer connections = 40000;
    static ServerConsole console = new ServerConsole();
    private static List<WorkingServ> active = new ArrayList<>();
    static List<Account> accs = FileLoader.Import();
    private static List<Integer> freePorts = new ArrayList<>();
    static boolean isClosed = false;
    private static int localport = 0;
    static InetAddress ADDRESS;
    static String ifases = null;
    static ServerComReader comReader = new ServerComReader();
    static RemoteAccess remoteAccess = null;

    static List<WorkingServ> getActive() {
        return active;
    }

    private Server(Socket socket) throws IOException {
        localport = getPort();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeInt(localport);
        os.close();
        socket.close();
        active.add(new WorkingServ(localport));
    }

    static synchronized boolean addFriend(int id, Friend friend){
        boolean isInFriend = false;
        for (Map.Entry<Integer , Friend> entry : accs.get(id).friends.entrySet()) {
            if(Objects.equals(entry.getValue().login, friend.login)){
                isInFriend = true;
                break;
            }
        }
        if(!isInFriend) {
            accs.get(id).friends.put(friend.id,friend);
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
            localport = freePorts.get(0);
            freePorts.remove(0);
        }
        else{
            localport = connections;
        }
        return localport;
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
            FileLoader.writeInBase(new Account((int)Account.idGL , login , password , new HashMap<>()));
            accs.add(new Account((int)Account.idGL , login , password , new HashMap<>()));
        }
        return res;
    }

    static synchronized Account logIn(String login, String password){
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

    static void setAddress() {
            ifases = "\nINTERFACES:";
              try {
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for(NetworkInterface intf : interfaces) {
                    ifases += "\n     "+intf.getName()+": ";
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for(int i = 0; i < addrs.size(); i++){
                        InetAddress ipAddress =addrs.get(i);
                        ifases += ipAddress.getHostAddress()+(i == addrs.size()-1 ? "": ", ");
                        if(!ipAddress.isLoopbackAddress() & !ipAddress.isLinkLocalAddress() & !ipAddress.getHostAddress().contains(":")){
                            ADDRESS = ipAddress;
                            break;
                        }
                    }
                }
                ifases += "\n----end of interfaces----";
            }catch (Exception e) {
                console.log(""+e, "exc");
        }
    }

    @SuppressWarnings("ALL")
    public static void setAddress(String address) {
        try {
            ADDRESS = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static void startNew(){
        if(!isClosed){
            CLOSE();
        }
        comReader.interrupt();
        connections = 40000;
        active = new ArrayList<>();
        accs = FileLoader.Import();
        freePorts = new ArrayList<>();
        isClosed = false;
        localport = 0;
        comReader = new ServerComReader();
        comReader.start();
        start();
    }

    static void start() {
        Account.idGL = accs.size() - 1;
        try {
            mainSocket = new ServerSocket(2905, 0, ADDRESS);
            remoteAccess = new RemoteAccess();
            console.log("Started on " + mainSocket.getInetAddress().getHostAddress(), "m");
        } catch (Exception e) {
            CLOSE();
            console.log("" + e, "exc");
            System.out.println("[MESSAGE] Restart server with another IP");
        }
        while (!isClosed) {
            try {
                new Server(mainSocket.accept());
            } catch (Exception e) {
                CLOSE();
                console.log("Connection lost", "w");
                console.log("" + e, "exc");
                break;
            }
        }

    }
}
