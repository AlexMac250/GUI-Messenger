package ru.universum.Client;

import ru.universum.Loader.Friend;

import java.util.Scanner;

public class BASH extends Thread{

    Frames Frames = new Frames();
    Scanner scan = new Scanner(System.in);
    @Override
    public void run() {
        System.out.println("BASH v1.0");
        System.out.print(">> ");
        while (!interrupted()) command(scan.nextLine());
    }

    private void command(String command){
        String[] line = decrypt(command);
        switch (line[0]){
            case "showFrame":
                switch (line[1]){
                    case "MainFrame":
                        Frames.new MainFrame().showFrame();
                        break;
                    case "AddFriend":
                        Frames.new AddFriend(new Friend(-1, "$user.login"));
                        break;
                    default:
                        System.err.print("Frame \""+line[1]+"\" not found!");
                        break;
                }
                break;

            case "startMethod":
                switch (line[1]){
                    case "interrupt();" :
                        interrupt();
                        break;
                }
                break;

            case "connect":
                Client.connect();

            case "login":
                Client.login(line[1], line[2]);
                break;
            case "addFriend":
                Client.addFriend(new String[]{line[1], line[2]});
                break;

            case "exit":
                System.exit(2);
                break;

            default:
                System.err.println("Command \""+ line[0]+"\" not found!");
                break;
        }
        System.out.print(">> ");
    }

    public static String[] decrypt(String message){
        String[] s = new String[4];
        char[] c = message.toCharArray();
        int i = 0;
        s[0] = "";
        for (char ch : c){
            if((ch == ' ')){
                i++;
                s[i] = "";
            }else{
                s[i]+=(ch);
            }
        }
        return s;
    }
}
