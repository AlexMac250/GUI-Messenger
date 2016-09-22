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
    private Socket socketmain;
    private ServerSocket serverSocket;
    DataOutputStream dataOutputStream;
    private Intellect intellect;
    DataInputStream is;
    Console console;

    WorkingServ(int port) {
        this.port = port;
        console = new Console("workingserv#"+ (port-40000));
        start();
    }

    private void close() throws IOException {
        if(acc !=null) {
            send(new Command("connection", "closed"));
        }
        sendOffline();
        socketmain.close();
        serverSocket.close();
        console.log("Closed");
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
    private void sendFriends(){
        if(acc.friends.size()!=0){
            for(Friend fr : acc.friends) {
                String[] friend = new String[2];
                friend[0] = String.valueOf(fr.id);
                friend[1] = fr.login;
                send(new Command("friend", Arrays.asList(friend)));
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
            send(new Command("friend", Arrays.asList(friendArr)));
            if(Server.accs.get(friend.id).isOnline){
                send(new Command("online",String.valueOf(friend.id)));
            }
        } else
            send(new Command("friended", "false"));
    }

    private void askToFriend(int idOf){
        if(Server.accs.get(idOf).isOnline){
            Server.accs.get(idOf).getWorkingServ().send(new Command("askToFriend" , Arrays.asList(String.valueOf(acc.id),acc.login)));
        }
    }

    private void sendOnline() {
        if (acc.friends.size() != 0) {
            acc.friends.stream().filter(friend -> Server.accs.get(friend.id).isOnline).forEachOrdered(friend -> {
                send(new Command("online", String.valueOf(friend.id)));
                Server.accs.get(friend.id).getWorkingServ().send(new Command("online", String.valueOf(acc.id)));
            });
        }
    }

    private void sendOffline() {
        if (acc != null) {
            if (acc.friends.size() != 0) {
                acc.friends.stream().filter(friend -> Server.accs.get(friend.id).isOnline).forEachOrdered(friend ->
                    Server.accs.get(friend.id).getWorkingServ().send(new Command("offline", String.valueOf(acc.id))));
            }
        }
    }

    void execute(String[] command){
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
                    acc.oflineMes.forEach(this::send);
                    acc.oflineMes = new ArrayList<>();
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
            console.log("Started");
            InetAddress ip = InetAddress.getByName("localhost");
            serverSocket = new ServerSocket(port, 0, ip);
            socketmain = serverSocket.accept();
            Server.connections++;
            console.log("Socket accepted");
            dataOutputStream = new DataOutputStream(socketmain.getOutputStream());
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