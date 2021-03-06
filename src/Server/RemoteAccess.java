package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteAccess extends Thread {
    private boolean isConnected = false;
    private Socket client;
    private ServerSocket remotingSocket;

    public RemoteAccess() throws IOException {
        remotingSocket = new ServerSocket(2904 , 0 , Server.ADDRESS);
        start();
    }

    public void setInputOutOnComReader() throws IOException {
        Server.comReader.inputStream = (DataInputStream) client.getInputStream();
        Server.comReader.outputStream = (DataOutputStream) client.getOutputStream();
        Server.comReader.setRemoting(true);
    }

    public void notConnected(){
        isConnected = false;
    }

    @Override
    public void run() {
        Server.console.log("Remote accss started" , "m");
        while(!isConnected){
            try {
                client = remotingSocket.accept();
                isConnected = true;
                Server.console.log("Remote access connected" , "m");
                setInputOutOnComReader();
            } catch (IOException ignored) {
            }
        }
    }
}
