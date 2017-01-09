package Server;

import ru.universum.Loader.Message;

import java.util.ArrayList;

public class Dialogue {
    private ArrayList<Message> messages = new ArrayList();

    public synchronized void addmes(Message message){
        messages.add(message);
    }

    public ArrayList<Message> get(){
        return messages;
    }
}
