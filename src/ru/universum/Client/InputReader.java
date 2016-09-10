package ru.universum.Client;

import java.io.DataInputStream;

public class InputReader extends Thread {
    DataInputStream in;
    String message = "";

    public InputReader(DataInputStream in) {
        this.in = in;
    }

    public void throwCommand(){
        Client.execute(Client.descript(message));
    }

    @Override
    public void run() {
        while (!interrupted()){
            try {
                message = in.readUTF();
                Client.console.log("Got command " + message);
                throwCommand();
            }catch (Exception e){
                Client.execute(new String[]{"connection"});
                interrupt();
            }

        }
    }
}
