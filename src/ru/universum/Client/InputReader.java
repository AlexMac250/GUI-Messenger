package ru.universum.Client;

import java.io.DataInputStream;
import java.io.IOException;

public class InputReader extends Thread {
    private DataInputStream in;
    public String commands = "";
    private String message = "";

    InputReader(DataInputStream in) {
        this.in = in;
    }

    private void throwCommand(){
        Client.execute(Client.descript(message));
    }

    @Override
    public void run() {
        while (!interrupted()& !Client.socket.isClosed()) {
            try {
                message = in.readUTF();
                commands += "\n" + message;
                throwCommand();
            } catch (Exception e) {
                //e.printStackTrace();
                //Client.execute(new String[]{"connection"});
                interrupt();
            }

        }
    }
}
