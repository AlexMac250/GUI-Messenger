package ru.universum.Client;

import ru.universum.Loader.Friend;

import java.util.ArrayList;
import java.util.List;

public class Dialog{

    public Dialog(Friend dialogWith) {
        this.dialogWith = dialogWith;
    }

    void addMes(ClientMessage clientMessage){
        messages.add(clientMessage);
    }

    public ClientMessage getLast(){
        return messages.get(messages.size()-1);
    }

    Friend dialogWith;
    List<ClientMessage> messages = new ArrayList<>();
}
