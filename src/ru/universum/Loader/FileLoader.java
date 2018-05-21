package ru.universum.Loader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("ALL")
public class FileLoader {
    static File file = new File(System.getProperty("user.home")+"/"+"Accounts(NEOnline).txt");

    public static ArrayList<Account> Import() {
        try {
            file.createNewFile();
        } catch (IOException e) {}
        ArrayList<Account> base = new ArrayList<>();
        List<String> inf = new ArrayList<>();
        String info = "";
        int i;
        ArrayList<Friend> friends = new ArrayList<>();
        try (FileInputStream fin = new FileInputStream(file)) {
            while ((i = fin.read()) != -1) {
                if ((char) i != '\n' & (char)i != '\r') {
                    if ((char) i == '/') {
                        inf.add(info);
                        info = "";
                    } else
                        if((char) i != '\n' & (char) i != '\r')
                            info += (char) i;
                } else {
                    if(info!="") {
                        Integer id = 0;
                        String login = "";
                        String password = "";
                        inf.add(info);
                        id = Integer.parseInt(inf.get(0));
                        inf.remove(0);
                        login = inf.get(0);
                        inf.remove(0);
                        password = inf.get(0);
                        inf.remove(0);
                        info = "";
                        i = 0;
                        for (; i < inf.size(); ) {
                            friends.add(new Friend(Integer.parseInt(inf.get(i)), inf.get(i + 1)));
                            i += 2;
                        }
                        base.add(new Account(id, login, password, friends));
                        inf = new ArrayList<>();
                        friends = new ArrayList<>();
                    }
                }
            }
            if(info!="") {
                Integer id = 0;
                String login = "";
                String password = "";
                inf.add(info);
                id = Integer.parseInt(inf.get(0));
                inf.remove(0);
                login = inf.get(0);
                inf.remove(0);
                password = inf.get(0);
                inf.remove(0);
                info = "";
                i = 0;
                for (; i < inf.size(); ) {
                    friends.add(new Friend(Integer.parseInt(inf.get(i)), inf.get(i + 1)));
                    i += 2;
                }
                base.add(new Account(id, login, password, friends));
                inf = new ArrayList<>();
                friends = new ArrayList<>();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return base;
    }

    public static void writeInBase(Account acc) {
        try (FileWriter fon = new FileWriter(file, true)) {
            Integer i = 0;
            String info = "";
            info += "\r\n";
            info += acc.id;
            info += '/';
            info += acc.login;
            info += '/';
            info += acc.password;
            if (0 != acc.friends.size()) {
                info += '/';
                i = 1;
                for (Friend f : acc.friends) {
                    info += f.id;
                    info += '/';
                    info += f.login;
                    if (i != acc.friends.size()) {
                        info += '/';
                    }
                    i++;
                }
            }
            fon.write(info);
            fon.flush();
            System.out.println("Added to base");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static void rewriteInBase(Account acc, boolean isFirst){
        try (FileWriter fon = new FileWriter(file, isFirst)) {
        Integer i = 0;
        String info = "";
        info += "\r\n";
        info += acc.id;
        info += '/';
        info += acc.login;
        info += '/';
        info += acc.password;
        if (0 != acc.friends.size()) {
            info += '/';
            i = 1;
            for (Friend f : acc.friends) {
                info += f.id;
                info += '/';
                info += f.login;
                if (i != acc.friends.size()) {
                    info += '/';
                }
                i++;
            }
        }
        fon.write(info);
        fon.flush();
 //       System.out.println("Added to base");
    } catch (IOException e) {
        e.getMessage();
    }
}
}