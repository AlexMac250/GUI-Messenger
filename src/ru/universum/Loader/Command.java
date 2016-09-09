package ru.universum.Loader;

import java.util.List;

public class Command {
    String toDo;
    final String from = "server";
    List<String> args;
    String arg;

    public Command(String toDo, List<String> args) {
        this.toDo = toDo;
        this.args = args;
    }

    public Command(String toDo, String arg) {
        this.toDo = toDo;
        this.arg = arg;
    }

    @Override
    public String toString() {
        if(args!=null){
            return toDo + '$' + from + '$' + args.get(0) + '$' + args.get(1);
        }else {
        return toDo + '$' + from + '$' + arg;
        }
    }
}
