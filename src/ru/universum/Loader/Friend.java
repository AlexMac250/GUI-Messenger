package ru.universum.Loader;

import Server.Dialogue;

public class Friend {
    public int id;
    public String login;
    public boolean isOnline = false;
    public Dialogue with = new Dialogue();

    @Override
    public String toString() {
        return "Loader.Friend{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }

    public Friend(int id, String login){
        this.id = id;
        this.login = login;
    }

}
