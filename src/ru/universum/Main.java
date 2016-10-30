package ru.universum;

import ru.universum.Loader.Security;

import javax.swing.*;

public class Main{
    static JFrame frame = new JFrame("");
    static JPasswordField passwordField = new JPasswordField(20);

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(passwordField);
        passwordField.addActionListener(e -> {
            StrFromCharArr(passwordField.getPassword());
            System.out.println(Security.getMD5(passwordField.getPassword()));
            System.out.println(Security.getMD5(passwordField.getPassword()));
            System.out.println(Security.getMD5(passwordField.getPassword()));
        });
        frame.pack();
        frame.setVisible(true);
    }

    private static void StrFromCharArr(char[] pass){
        for (int i = 0; i < pass.length; i++) {
            System.out.print(pass[i]);
        }
        System.out.println();
    }
    private static String getStrFromChar(char[] pass){
        String str = null;
        for (char pas : pass) {
            str += pas;
        }
         return str;
    }
}
