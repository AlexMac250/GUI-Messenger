package Server;

public class Log {
    String message;

    public Log(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
