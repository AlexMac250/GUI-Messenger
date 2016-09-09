package ru.universum.Loader;

import ru.universum.Printer.Console;

public class Message {
    String command;
    String from;
    String message;
    boolean withDate = false;
    String date ;

    public Message(String command, String from, String message , boolean withDate) {
        this.command = command;
        this.from = from;
        this.message = message;
        this.withDate = withDate;
    }

    public Message(String command , String from , String date , String message) {
        this.command = command;
        this.from = from;
        this.message = message;
        this.date = date;
    }

    public String toString() {
            if(date!=null) {
                return command + '$' + from + '[' + date + ']' + message;
            }else {
                if (withDate) {
                    return command + '$' + from + '[' + Console.getDateNow() + ']' + message;
                }else return command + '$' + from + '$' + message;
            }
        }
    }
