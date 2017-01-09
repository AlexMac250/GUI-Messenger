package ru.universum.Loader;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Integer ,Friend> friends = new HashMap<>();
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
                            friends.put(Integer.parseInt(inf.get(i)),new Friend(Integer.parseInt(inf.get(i)), inf.get(i + 1)));
                            i += 2;
                        }
                        base.add(new Account(id, login, password, friends));
                        inf = new ArrayList<>();
                        friends = new HashMap<>();
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
                    friends.put(Integer.parseInt(inf.get(i)) , new Friend(Integer.parseInt(inf.get(i)), inf.get(i + 1)));
                    i += 2;
                }
                base.add(new Account(id, login, password, friends));
                inf = new ArrayList<>();
                friends = new HashMap<>();
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
                for (Map.Entry<Integer , Friend> entry : acc.friends.entrySet()) {
                    info += entry.getKey();
                    info += '/';
                    info += entry.getValue().login;
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
            for (Map.Entry<Integer , Friend> entry : acc.friends.entrySet()) {
                info += entry.getKey();
                info += '/';
                info += entry.getValue().login;
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