package ru.universum.Client;


import ru.universum.Loader.Account;
import ru.universum.Loader.Friend;
import ru.universum.Loader.Message;
import ru.universum.Printer.Console;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
@SuppressWarnings("ALL")
public class Client {
    static List<ClientMessage> messages = new ArrayList<>();
    static Socket socket;
    static int port;
    static DataInputStream is;
    static DataOutputStream os;
    static Console console = new Console("Client");

    static final boolean NODATE = false;
    static final boolean DATED = true;
    static final String HOSTNAME = "localhost";

    static Account account = new Account();

    static boolean statusLogged = false;
    static boolean statusConnected = false;
    static boolean statusRegistered = false;

    static Frames Frames = new Frames();
    //есть только id и login
    static ArrayList<Account> usersInSearch = new ArrayList<>();//есть только id и login

    public static void main(String[] args) {
        Frames.startGUI();
        new BASH().run();
    }

    public static void connect(){
        try {
            Frames.LoginFrame.setInfo("Входим...", Color.ORANGE);
            socket = new Socket(InetAddress.getByName(HOSTNAME), 2905);
            is = new DataInputStream(socket.getInputStream());
            port = is.readInt();
            console.log("got port");
            socket.close();
            is.close();
            TimeUnit.MILLISECONDS.sleep(150);
            socket = new Socket(InetAddress.getByName(HOSTNAME), port);
            console.log("Connected to port " + port);
            statusConnected = true;
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            //читает входящие комманды
            InputReader reader = new InputReader(is);
            reader.start();
        } catch (Exception e) {
            Frames.LoginFrame.setInfo("Нет соединения", Color.RED);
            Frames.RegisterFrame.setInfo("Нет соединения", Color.RED);
            console.log("No connection");
            e.printStackTrace();
        }
    }

    public static void login(String login, String password){
        send(new Message("login",login,password,NODATE));}

    public static void register(String login, String password){
        send(new Message("register",login,password,NODATE));
        try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException ignored) {}
        if (statusRegistered){
            login(login, password);
            Frames.RegisterFrame.dispose();
        } else {
            Frames.RegisterFrame.setInfo("Ошибка регистрации", Color.RED);
        }
    }

    public static ArrayList<Account> getUsersInSearch(){
        return usersInSearch;
    }

    public static void resOfFriend(String ans, int id){
        execute(descript("resOfFriend$"+id+"$"+ans));
    }

    public static void writeMessage(String from, String date, String message){
        Frames.MainFrame MFrame = Frames.MainFrame;
        Friend friend = null;
        for (Friend fr : account.friends) {
            if (fr.id == Integer.parseInt(from)){
                friend = fr;
                break;
            }
        }
        MFrame.currentFriend = friend;
        MFrame.insertText(MFrame.MessageBox, "\n"+friend.login+" ["+date+"]----------------------\n", MFrame.heading);
        MFrame.insertText(MFrame.MessageBox, message+"\n", MFrame.normal);
    }

    public static void execute(String[] command){
        switch (command[0]){
            // второй аргумент - логин третий пароль
            case "login" :
                send(new Message(command[0],command[1],command[2] , NODATE));
                break;

            //так же как и в логине
            case "register" :
                send(new Message(command[0], command[1], command[2] , NODATE));
                break;

            case "send" :
                //первый аргумент - send , второй - idTO , третий сам текст , на сеерв отправляется 4 аргумента где третий это дата а четвертый это текст.
                send(new Message(command[0], command[1], command[2] , DATED));
                break;

            case "message" :
                //принял входящее сообщеине
                messages.add(new ClientMessage(command[1] , command[2], command[3]));
                writeMessage(command[1], command[2], command[3]);
                System.out.println(messages.get(messages.size()-1));
                break;
                //заполняет френдов с сервера.
            case "friend" :
                addFriend(command);
                break;

            case "account" :
                account.login = command[2];
                account.id = Integer.parseInt(command[3]);
                Frames.MainFrame.showFrame();
                // отображаешь майн фрейм
                break;

            case "logged" :
                if(command[2].equals("true")){
                    statusLogged = true;
                    Frames.LoginFrame.dispose();
                    //залогинился
                }else{
                    Frames.LoginFrame.setInfo("Неверные логин или пароль", Color.RED);
                }
                break;

            //ну тут понятно
            case "connection" :
                Frames.LoginFrame.setInfo("Соединение потеряно!", Color.RED);
                Frames.RegisterFrame.setInfo("Соединение потеряно!", Color.RED);
                Frames.MainFrame.setInfo("Соединение потеряно!", Color.RED);
                statusLogged = false;
                statusConnected = true;
                console.log("Connection refused");
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                System.exit(1);
                break;

            case "addFriend" :
                send(new Message(command[0],command[1], command[2], NODATE));
                break;

            //если ошибка при добавлении в друзья
            case "noFriended" :
                Frames.MainFrame.setInfo("Ваше предложение в друзья "+command[1]+", отменено.", Color.BLACK);
                //ЗДЕСЬ СДЕЛАЙ ВЫВОД ТОГО ЧТО ЕГО ПРЕДЛОЖЕНИЕ В ДРУЗЬЯ ПРОИГНОРИЛ ПОЛЬЗОВАТЕЛЬ С НИКОМ command[1]
                break;

            case "online" :
                for (Friend fr : account.friends){
                    if(fr.id == Integer.parseInt(command[2])){
                        fr.isOnline = true;
                        Frames.MainFrame.panFriends.removeAll();
                        Frames.MainFrame.loadFriends();
                        Frames.MainFrame.getFrame().repaint();
                        break;
                    }
                }
                break;

            case "offline" :
                for (Friend fr : account.friends){
                    if(fr.id == Integer.parseInt(command[2])) {
                        fr.isOnline = false;
                        Frames.MainFrame.panFriends.removeAll();
                        Frames.MainFrame.loadFriends();
                        Frames.MainFrame.getFrame().repaint();
                        break;
                    }
                }
                break;

            //доделай сам
            case "askToFriend" :
                Frames.new AddFriend(new Friend(Integer.parseInt(command[2]), command[3]));
                //console.log("Accept friend :" +command[3] + " ?");
                break;

            case "registered" :
                if(command[2].equals("true")){
                    statusRegistered = true;
                }
                break;

            case "resOfFriend" :
                //resOfFriend id answer
                send(new Message(command[0],command[1],command[2],NODATE));
                break;

            //при первом запросе юзеров
            case "getUsers" :
                send(new Message(command[0],"","",NODATE));
                break;

            //при 2м+ запросе
            case "get20More" :
                send(new Message(command[0],"","",NODATE));
                break;

            case "user" :
                usersInSearch.add(new Account(command[1]/*+(command[1].equals(account.login) ? " (Вы)" : "")*/,Integer.parseInt(command[2]),Boolean.parseBoolean(command[3])));
                System.out.println(usersInSearch.get(usersInSearch.size()-1).login + " " + usersInSearch.get(usersInSearch.size()-1).id);
                break;

            case "findByNick" :
                send(new Message(command[0],command[1],"",NODATE));
                break;
        }
    }

    public static void addFriend(String[] args){
        if(!Objects.equals(args[2], "null")){
            account.friends.add(new Friend(Integer.parseInt(args[2]),args[3]));
        }else{
            console.log("No friends");
        }
    }

    //работает , не трогать
    public static String[] descript(String message){
        String[] s = new String[4];
        char[] c = message.toCharArray();
        int i = 0;
        s[0] = "";
        for (char ch : c){
            if(s[0].equals("send")& i ==2){
                s[i]+=(ch);
            }else{
                if((ch == '$' | ch == '[' | ch == ']')& i!=3){
                    i++;
                    s[i] = "";
                }else{
                    s[i]+=(ch);
                }
            }
        }
        return s;
    }

    //работает , не трогать
    public static void send(Message message){
        try {
            os = new DataOutputStream(socket.getOutputStream());
            os.writeUTF(message.toString());
            os.flush();
        } catch (IOException e) {
            console.log("Lost connection");
            statusConnected = false;
            statusLogged = true;
        }
    }
}