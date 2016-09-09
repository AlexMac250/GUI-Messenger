package Server;

import ru.universum.Loader.Account;
import ru.universum.Loader.Command;
import ru.universum.Loader.Friend;
import ru.universum.Loader.Message;
import ru.universum.Printer.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class WorkingServ extends Thread {
    Account acc;
    int port;
    Socket socketmain;
    InetAddress ip;
    ServerSocket serverSocket;
    DataOutputStream ds;
    Intellect intellect;
    DataInputStream is;
    Console console;

    public WorkingServ(int port) {
        this.port = port;
        console = new Console("workingserv#"+ (port-40000));
        start();
    }

    public void close() throws IOException {
        if(acc !=null) {
            send(new Command("connection", "closed"));
        }
        sendOffline();
        socketmain.close();
        serverSocket.close();
        console.log("Closed");
        Server.connections--;
        int local = 0;
        Iterator iterator = Server.getActive().iterator();
        while (iterator.hasNext()){
            iterator.next();
            if(Server.getActive().get(local) == this){
            }else{
                local++;
            }
        }
        Server.closeConnection(local);
        if(acc!=null){
            Server.accs.get(acc.id).isOnline = false;
            intellect.interrupt();
            interrupt();
        }
    }

    public void login(String login , String password){
        acc = Server.logIn(login , password);
        if(acc != null){
            acc.setWorkingServ(this);
            send(new Command("logged" , "true"));
            console.log("Acc " + acc.login + " logged");
            sendFriends();
            send(new Command("account" , Arrays.asList(acc.login,String.valueOf(acc.id))));
        }
        else{
            console.log("Fail login");
            send(new Command("logged","false"));
        }
    }
    //отправляет всех друзей оушена
    public void sendFriends(){
        if(acc.friends.size()!=0){
            for(Friend fr : acc.friends) {
                String[] friend = new String[2];
                friend[0] = String.valueOf(fr.id);
                friend[1] = fr.login;
                send(new Command("friend", Arrays.asList(friend)));
                if(Server.accs.get(fr.id).isOnline){
                    send(new Command("online",String.valueOf(fr.id)));
                }
            }
        }else{
            send(new Command("friend","null"));
        }
    }

    public void execute(String[] command){
        switch (command[0]) {
//останавливает работу сервера 
            case "stop":
                try {
                    close();
                } catch (IOException ignored) {
                }
                break;
//отправляет сообщение 
            case "send":
                send(new Message("message", command[1], command[2], command[3]));
                break;
//логинит входящее соединение 
            case "login":
                login(command[1], command[2]);
                if (acc != null) {
                    for (Message message : acc.oflineMes) {
                        send(message);
                    }
                    if (acc.oflineMes.size() != 0) {
                        acc.oflineMes = new ArrayList<>();
                    }
                    sendOnline();
                }
                break;
            case "register":
                Server.register(command[1], command[2]);
                send(new Command("registered", "true"));
                break;
            case "addFriend":
                askToFriend(Integer.parseInt(command[1]));
                break;
        }
    }

    public void newFriend(Friend friend){
        boolean res = Server.addFriend(acc.id, friend);
        if (res) {
            String[] friendarr = new String[2];
            friendarr[0] = String.valueOf(acc.friends.get(acc.friends.size() - 1).id);
            friendarr[1] = acc.friends.get(acc.friends.size() - 1).login;
            send(new Command("friend", Arrays.asList(friendarr)));
            if(Server.accs.get(friend.id).isOnline){
                send(new Command("online",String.valueOf(friend.id)));
            }
        } else
            send(new Command("friended", "false"));
    }

    public void askToFriend(int idOf){
        if(Server.accs.get(idOf).isOnline){
            Server.accs.get(idOf).getWorkingServ().send(new Command("askToFriend" , Arrays.asList(String.valueOf(acc.id),acc.login)));
        }
    }

    public void sendOnline() {
        int i = 0;
        if (acc.friends.size() != 0) {
            for (Friend friend : acc.friends) {
                if (Server.accs.get(friend.id).isOnline) {
                    Server.accs.get(friend.id).getWorkingServ().send(new Command("online", String.valueOf(acc.id)));
                }
            }
        }
    }

    public void sendOffline() {
        int i = 0;
        if (acc != null) {
            if (acc.friends.size() != 0) {
                for (Friend friend
                        : acc.friends) {
                    if (Server.accs.get(friend.id).isOnline) {
                        Server.accs.get(friend.id).getWorkingServ().send(new Command("offline", String.valueOf(acc.id)));
                    }
                }
            }
        }
    }

    //Отправляет команду с 1 или 2мя аргументами
    public void send(Command message){
        try {
            ds.writeUTF(message.toString());
            ds.flush();
        } catch (IOException e) {
            System.err.println("Error while sending");

        }
    }
    //Отправляет сообщение аккаунту клиента
    public void send(Message message){
        try {
            ds.writeUTF(message.toString());
            ds.flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void run() {
        try {
            console.log("Started");
            ip = InetAddress.getByName("localhost");
            serverSocket = new ServerSocket(port, 0, ip);
            socketmain = serverSocket.accept();
            Server.connections++;
            console.log("Socket accepted");
            ds = new DataOutputStream(socketmain.getOutputStream());
            is = new DataInputStream(socketmain.getInputStream());
            intellect = new Intellect(this);
            intellect.join();
            close();
        }catch (Exception e){
            e.printStackTrace();
            try {
                close();
            } catch (IOException ignored) {
                e.printStackTrace();
            }
            interrupt();
            e.printStackTrace();
        }
    }
}