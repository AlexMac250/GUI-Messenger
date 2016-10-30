package Server;

public class Main {
    public static void main(String[] args) {
        Server.setAddress(); // пиши в аргументы метода строку с адресом иначе адрес назначит сам
        Server.reader.start();
        Server.start();
    }
}