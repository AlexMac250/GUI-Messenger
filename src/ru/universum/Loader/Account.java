package ru.universum.Loader;

import Server.WorkingServ;
import java.util.ArrayList;
import java.util.List;

public class Account {
    public boolean isOnline = false;
    public int id;
    public String login = "";
    public String password = "";
    public String name = "";
    public ArrayList<Friend> friends = new ArrayList<>();
    public static Integer idGL = 0;
    public List<Message> oflineMes = new ArrayList<>();
    private WorkingServ workingServ;

    public WorkingServ getWorkingServ() {
        return workingServ;
    }

    public void setWorkingServ(WorkingServ workingServ) {

        this.workingServ = workingServ;
    }

    public void writeOffline(Message message){
        oflineMes.add(message);
    }

    @Override
    public String toString() {
        return "Loader.Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", friends=" + friends +
                '}';
    }

    public Account(){

    }

    public Account(String login, boolean isOnline) {
        this.login = login;
        this.isOnline = isOnline;
    }

    public Account(int id, String login, String password, ArrayList<Friend> friends){
        this.id = id;
        this.login = login;
        this.password = password;
        this.friends = friends;
    }

}