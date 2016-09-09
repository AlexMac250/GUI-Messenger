package ru.universum.Client;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Waiter extends Thread {
    boolean res;
    @Override
    public void run() {
        setDaemon(false);
        while (!res) {
            try {
                Client.socket = new Socket("localhost", 2905);
                TimeUnit.SECONDS.sleep(10);
                Client.main(null);
                res = true;
            } catch (Exception e) {
                Client.console.log("No connection");
                Client.console.log("Waiting for connection");
            }
        }
    }
}
