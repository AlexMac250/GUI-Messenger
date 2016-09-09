package ru.universum.Loader;

public class Friend {
    public int id;
    public String login;
    public boolean isOnline = false;

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
