package Server;

public class Starter {
    public static void main(String[] args) {
        Server.setAddress("127.0.0.1"); // пиши в аргументы метода строку с адресом иначе адрес назначит сам
        Server.comReader.start();
        Server.start();
    }
}