package Server;

public class Main {
    public static void main(String[] args) {
        ServerComReader reader = new ServerComReader();
        reader.start();
        Server.getIp();
        Server.start();
    }
}
