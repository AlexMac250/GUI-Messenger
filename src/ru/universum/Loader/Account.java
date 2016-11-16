package ru.universum.Loader;

import Server.WorkingServ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.universum.Client.Dialog;

public class Account {
    public boolean isOnline = false;
    public int id;
    public String login = "";
    public String password = "";
    public String name = "";
    public ArrayList<Friend> friends = new ArrayList<>();
    public static long idGL = 0;
    public List<Message> offlineMes = new ArrayList<>();
    public Map<Integer, Dialog> dialogs = new HashMap<>();
    private WorkingServ workingServ;

    public WorkingServ getWorkingServ() {
        return workingServ;
    }

    public void setWorkingServ(WorkingServ workingServ) {

        this.workingServ = workingServ;
    }

    public void writeOffline(Message message){
        offlineMes.add(message);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", friends=" + friends +
                "}\n";
    }

    public Account(){

    }

    public Account(String login, int id , boolean isOnline) {
        this.login = login;
        this.id = id;
        this.isOnline = isOnline;
    }

    public Account(int id, String login, String password, ArrayList<Friend> friends){
        this.id = id;
        this.login = login;
        this.password = password;
        this.friends = friends;
    }
}