package Server;

import ru.universum.Client.Dialog;
import ru.universum.Loader.Account;
import ru.universum.Loader.Command;
import ru.universum.Loader.Friend;
import ru.universum.Loader.Message;
import ru.universum.Printer.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class WorkingServ extends Thread {
    Account acc;
    int port;
    private Socket socketMain;
    private ServerSocket serverSocket;
    DataOutputStream dataOutputStream;
    private Intellect intellect;
    DataInputStream is;
    Console console;
    private Message message;

    WorkingServ(int port) {
        this.port = port;
        console = new Console("workingserv#"+ (port-40000));
        start();
    }

    void close(){
        if(acc !=null) {
            send(new Command("connection", "closed"));
        }
        sendOffline();
        try {
            socketMain.close();
            serverSocket.close();
        } catch (IOException ignored) {}
        console.log("Closed", "m");
        Server.connections--;
        int local = 0;
        for (Object ignored : Server.getActive()) {
            if (Server.getActive().get(local) == this) {
                break;
            } else {
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
            console.log("Acc " + acc.login + " logged", "m");
            sendFriends();
            send(new Command("account" , new String[]{acc.login,String.valueOf(acc.id)}));
        }
        else{
            console.log("Fail login","m");
            send(new Command("logged","false"));
        }
    }
    //отправляет всех друзей оушена
    private void sendFriends(){
        if(acc.friends.size()!=0){
            for (Map.Entry<Integer , Friend> entry : acc.friends.entrySet()) {
                String[] friend = new String[2];
                friend[0] = String.valueOf(entry.getKey());
                friend[1] = entry.getValue().login;
                send(new Command("friend", friend));
            }
        }else{
            send(new Command("friend","null"));
        }
    }

    void newFriend(Friend friend){
        boolean res = Server.addFriend(acc.id, friend);
        if (res) {
            String[] friendArr = new String[2];
            friendArr[0] = String.valueOf(acc.friends.get(acc.friends.size() - 1).id);
            friendArr[1] = acc.friends.get(acc.friends.size() - 1).login;
            send(new Command("friend", friendArr));
            if (Server.accs.get(friend.id).isOnline) {
                send(new Command("online", String.valueOf(friend.id)));
            }
        }
    }

    private void askToFriend(int idOf){
        if(!acc.friends.containsValue(new Friend(Server.accs.get(idOf).id,Server.accs.get(idOf).login))) {
            if (Server.accs.get(idOf).isOnline) {
                Server.accs.get(idOf).getWorkingServ().send(new Command("askToFriend", new String[]{String.valueOf(acc.id), acc.login}));
            } else {
                Server.accs.get(idOf).offlineMes.add(new Message("askToFriend", "", String.valueOf(acc.id), acc.login));
            }
        }else{
            send(new Command("noFriended",Server.accs.get(idOf).login));
        }
    }

    private void sendOnline() {
        if (acc.friends.size() != 0) {
            for (Map.Entry<Integer, Friend> entry : acc.friends.entrySet()) {
                if (Server.accs.get(entry.getValue().id).isOnline) {
                    send(new Command("online", String.valueOf(entry.getKey())));
                    Server.accs.get(entry.getValue().id).getWorkingServ().send(new Command("online", String.valueOf(acc.id)));
                }
            }
        }
    }
    private void sendOffline() {
        if (acc != null) {
            if (acc.friends.size() != 0) {
                for (Map.Entry<Integer , Friend> entry : acc.friends.entrySet()) {
                if(Server.accs.get(entry.getKey()).isOnline){
                    Server.accs.get(entry.getValue().id).getWorkingServ().send(new Command("offline", String.valueOf(acc.id)));
                    }
                }
            }
        }
    }

    void execute(String[] command){
        switch (command[0]) {
            //останавливает работу сервера
            case "stop":
                    close();
                break;
            //отправляет сообщение
            case "send":
                message =  new Message("message", command[1], command[2], command[3]);
                send(message);
                acc.addToDialogWith(message);
                break;
            //логинит входящее соединение
            case "login":
                login(command[1], command[2]);
                if (acc != null) {
                    acc.offlineMes.forEach(this::send);
                    acc.offlineMes = new ArrayList<>();
                    sendHistory();
                    sendOnline();
                }
                break;
            case "register":
                if(Server.register(command[1], command[2])){
                    send(new Command("registered", "true"));
                }
                send(new Command("registered", "false"));
                break;
            case "addFriend":
                askToFriend(Integer.parseInt(command[1]));
                break;
            case "user" :
                send(new Command(command[0],new String[]{command[1],command[2]}));
                break;
        }
    }

    void sendHistory(){
        for (Map.Entry<Integer , Friend> entry : acc.friends.entrySet()) {
            for (Message message1 : entry.getValue().with.get()){
                send(message1);
            }
        }
    }

    //Отправляет команду с 1 или 2мя аргументами
    void send(Command message){
        try {
            dataOutputStream.writeUTF(message.toString());
            dataOutputStream.flush();
        } catch (IOException ignored) {
        }
    }
    //Отправляет сообщение аккаунту клиента
    private void send(Message message){
        try {
            dataOutputStream.writeUTF(message.toString());
            dataOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error while sending");
        }
    }

    @Override
    public void run() {
        try {
            console.log("Started", "m");
            serverSocket = new ServerSocket(port, 0, Server.ADDRESS);
            socketMain = serverSocket.accept();
            Server.connections++;
            console.log("Socket accepted", "m");
            dataOutputStream = new DataOutputStream(socketMain.getOutputStream());
            is = new DataInputStream(socketMain.getInputStream());
            intellect = new Intellect(this);
            intellect.join();
            close();
        }catch (Exception e){
                close();
        }
    }
}