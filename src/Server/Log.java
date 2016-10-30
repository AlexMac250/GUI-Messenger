package Server;

import java.util.Date;

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
