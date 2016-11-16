package ru.universum.Client;

import java.io.DataInputStream;

class InputReader extends Thread {
    private DataInputStream in;
    @SuppressWarnings("WeakerAccess")
    String commands = "";
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
               // System.out.println(message);
                throwCommand();
            } catch (Exception e) {
                //e.printStackTrace();
                //Client.execute(new String[]{"connection"});
                interrupt();
            }

        }
    }
}
