package Server;

public class Log {
    private String message;

    public Log(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
