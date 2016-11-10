package ru.universum.Client;

class ClientMessage {

    private String from ;
    private String date ;
    private String text ;

    @Override
    public String toString() {
        return "Message from " + from + " at " + date + "\n" + text;
    }

    ClientMessage(String from, String date, String text) {
        this.from = from;
        this.date = date;
        this.text = text;
    }
}
