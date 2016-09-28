package ru.universum.Client;

import ru.universum.Client.ClientMessage;
import ru.universum.Loader.Friend;

import java.util.ArrayList;
import java.util.List;

public class Dialog {

    public Dialog(Friend with) {
        this.with = with;
    }

    public void addMes(ClientMessage clientMessage){
        messages.add(clientMessage);
    }

    public ClientMessage getLast(){
        return messages.get(messages.size()-1);
    }

    Friend with;
    List<ClientMessage> messages = new ArrayList<>();
}
