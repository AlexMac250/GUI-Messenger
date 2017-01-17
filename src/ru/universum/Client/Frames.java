package ru.universum.Client;

import ru.universum.Loader.Account;
import ru.universum.Loader.Friend;
import ru.universum.Printer.Console;
import ru.universum.Loader.Security;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
class Frames {
    @SuppressWarnings("ALL")
    private final String STYLE_heading = "heading";
    @SuppressWarnings("ALL")
    private final String STYLE_normal  = "normal";
    private final String FONT_style    = "Trebuchet MS";
    private final Color MAIN_COLOR = new Color(69, 151, 249);

    private Console console = new Console("Frames");
    private String typeILAF = "NLAF";

    private MainMenuFrame MainMenuFrame;
    LoginFrame LoginFrame;
    RegisterFrame RegisterFrame;
    private AboutFrame AboutFrame;
    MainFrame MainFrame;
    private SettingsFrame SettingsFrame;

    private class MainMenuFrame extends AbstractFrame {
        private JFrame frame;
        private JButton butLogin;
        private JButton butRegister;
        private JButton butSettings;
        private JButton butAbout;
        private JLabel label;
        private JLabel info;

        MainMenuFrame() {
            initial();
        }

        @Override
        public void initial() {
            switch (typeILAF){
                case "NLAF":
                    WindowUtilities.setNativeLookAndFeel();
                    break;
                case "JLAF":
                    WindowUtilities.setJavaLookAndFeel();
                    break;
                case "MLAF":
                    WindowUtilities.setMotifLookAndFeel();
                    break;
            }
            frame = new JFrame("NEOnline");
            butLogin = new JButton("Войти");
            butRegister = new JButton("Регистрация");
            butSettings = new JButton("Настройки");
            butAbout = new JButton("О программе");
            label = new JLabel("NEOnline");
            info = new JLabel("");

            butLogin.setFocusable(false);
            butRegister.setFocusable(false);
            butSettings.setFocusable(false);
            butAbout.setFocusable(false);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new GridBagLayout());

            frame.getContentPane().setBackground(Color.DARK_GRAY);
            label.setForeground(MAIN_COLOR);
            label.setFont(new Font(FONT_style, Font.BOLD, 20));

            GridBagLayoutManager(frame, label, GridBagConstraints.NORTH, 0, 0, 1);
            GridBagLayoutManager(frame, getEmptyLabel(2), GridBagConstraints.HORIZONTAL, 0, 1, 1);
            GridBagLayoutManager(frame, butLogin, GridBagConstraints.HORIZONTAL, 0, 2, 1);
            GridBagLayoutManager(frame, getEmptyLabel(2), GridBagConstraints.HORIZONTAL, 0, 3, 1);
            GridBagLayoutManager(frame, butRegister, GridBagConstraints.HORIZONTAL, 0, 4, 1);
            GridBagLayoutManager(frame, getEmptyLabel(2), GridBagConstraints.HORIZONTAL, 0, 5, 1);
            GridBagLayoutManager(frame, butSettings, GridBagConstraints.HORIZONTAL, 0, 6, 1);
            GridBagLayoutManager(frame, getEmptyLabel(2), GridBagConstraints.HORIZONTAL, 0, 7, 1);
            GridBagLayoutManager(frame, butAbout, GridBagConstraints.HORIZONTAL, 0, 8, 1);

            int w = 160;
            int h = Client.os_name.equals("Linux") ? 205 : 185;
            frame.setResizable(false);
            frame.setSize(w, h);
            frame.setLocationRelativeTo(null);

            try {
                frame.setIconImage(ImageIO.read(MainMenuFrame.class.getResource("favicon.png")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            butLogin.addActionListener(e -> LoginFrame.showFrame());
            butRegister.addActionListener(e -> RegisterFrame.showFrame());
            butSettings.addActionListener(e -> SettingsFrame.showFrame());
            butAbout.addActionListener(e -> AboutFrame.showFrame());
        }

        @Override
        public void showFrame() {
            frame.setVisible(true);
        }

        @Override
        public void dispose() {
            frame.dispose();
        }

        @Override
        public void setInfo(String s, Color color) {
            info.setForeground(color);
            info.setText(s);
        }
    }

    //-------------------------------------------//
    class LoginFrame extends AbstractFrame {
        JDialog dialog;
        private JTextField loginField;
        private JPasswordField passwordField;
        private JButton butLogin;
        private JCheckBox remAccData;
        private JLabel info;
        private JLabel label;

        LoginFrame() {
            initial();
        }

        @Override
        public void initial() {

            dialog = new JDialog(MainMenuFrame.frame, "Войти", true);
            loginField = new JTextField(15);
            passwordField = new JPasswordField(15);
            butLogin = new JButton("Войти");
            remAccData = new JCheckBox("Не запоминать");
            info = new JLabel();
            label = new JLabel("Введите логин и пароль");

            label.setForeground(Color.WHITE);
            remAccData.setForeground(Color.WHITE);
            remAccData.setFocusable(false);
            remAccData.setBackground(Color.DARK_GRAY);
            //remAccData.setSelected(Security.getRemData());
            loginField.setBackground(Color.GRAY);
            passwordField.setBackground(Color.GRAY);
            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            butLogin.setFocusable(false);

            loginField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            passwordField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            remAccData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            loginField.setToolTipText("ЛОГИН");
            passwordField.setToolTipText("ПАРОЛЬ");

            dialog.setLayout(new GridBagLayout());
            GridBagLayoutManager(dialog, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(dialog, loginField, GridBagConstraints.HORIZONTAL, 0, 1, 2);
            GridBagLayoutManager(dialog, getEmptyLabel(3), GridBagConstraints.HORIZONTAL, 0, 2, 2);
            GridBagLayoutManager(dialog, passwordField, GridBagConstraints.HORIZONTAL, 0, 3, 2);
            GridBagLayoutManager(dialog, getEmptyLabel(3), GridBagConstraints.HORIZONTAL, 0, 4, 2);
            GridBagLayoutManager(dialog, info, GridBagConstraints.CENTER, 0, 5, 2);
            GridBagLayoutManager(dialog, remAccData, GridBagConstraints.CENTER, 0, 6, 1);
            GridBagLayoutManager(dialog, butLogin, GridBagConstraints.CENTER, 1, 6, 1);

            dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            dialog.setAlwaysOnTop(true);
            dialog.setSize(200, 149);
            dialog.setResizable(false);

            butLogin.addActionListener(e -> login());
            loginField.addActionListener(e -> login());
            passwordField.addActionListener(e -> login());
            loginField.setText(Security.getUserLogin());
            passwordField.setText(Security.getUserPass());
            //remAccData.setSelected(Security.getRemData());
        }

        @Override
        public void showFrame() {
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        @Override
        public void dispose() {
            dialog.dispose();
            MainMenuFrame.dispose();
        }

        @Override
        public void setInfo(String message, Color color) {
            info.setForeground(color);
            info.setText(message);
        }

        private String getPass(){
            String s = "";
            for (char ch : passwordField.getPassword()) {
                s += ch;
            }
            return s;
        }

        private void login(){
            if (!remAccData.isSelected()){
//                Security.setAboutUser(loginField.getText(), getPass());
//                Security.setRemData(remAccData.isSelected());
            }
            setInfo("Входим...", Color.ORANGE);
            if (loginField.getText().length() > 1 & passwordField.getPassword().length > 1) {
                if (!Client.statusConnected) Client.connect();
                Client.login(loginField.getText(), Security.getMD5(passwordField.getPassword()));
            } else {
                setInfo("Введите данные!", Color.RED);
            }
        }
    }

    //-------------------------------------------//

    class MainFrame extends AbstractFrame {
        boolean isInit = false;
        Style heading = null; // стиль заголовка
        Style normal = null; // стиль текста
        JTabbedPane tabbedPane;
        Map<Integer, PanFriend> panFriendList = new HashMap<>();
        Map<Integer, Tab> tabs = new HashMap<>();
        private JFrame frame;
        private Container contentPain;
        JPanel panFriends;
        private JPanel panMainContent;
        private JScrollPane scrollFriends;
        Friend currentFriend = null;

        MainFrame() {}

        @Override
        public void initial() {
            try {
                currentFriend = null;
                frame = new JFrame("NEOnline - Сообщения ("+Client.client_version+") | "+Client.account.login);
                contentPain = frame.getContentPane();
                panFriends = new JPanel();
                panMainContent = new JPanel();
                scrollFriends = new JScrollPane(panFriends);
                tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(Color.DARK_GRAY);
                createTab(null);

                try {
                    frame.setIconImage(ImageIO.read(MainMenuFrame.class.getResource("favicon.png")));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                panMainContent.setBackground(Color.DARK_GRAY);
                contentPain.setBackground(Color.DARK_GRAY);

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLayout(new GridBagLayout());
                scrollFriends.setMaximumSize(new Dimension(scrollFriends.getWidth(), 317));

                GridBagLayoutManager(frame, scrollFriends, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, 0, 0, 1);
                panMainContent.add(tabbedPane);
                GridBagLayoutManager(frame, panMainContent, GridBagConstraints.CENTER, 1, 0, 1);
                loadFriends();
                buildMenuBar();

                frame.setSize(Pack(frame, 15, 15));

                frame.setLocationRelativeTo(null);
                isInit = true;
                setInfo("Внимание! Это beta версия программы! Возможны глюки и лаги. Обо всех происходящих ошибках просим сообщить программистам!", Color.red);
            } catch (Exception e) {
                setInfo(e.toString(), Color.RED);
            }
        }

        @Override
        public void showFrame() {
            initial();
            frame.setVisible(true);
            frame.setVisible(false);
            frame.setVisible(true);
        }

        @Override
        public void dispose() {
            frame.dispose();
        }

        @Override
        public void setInfo(String message, Color color) {
            final JDialog frMess = new JDialog(frame, "СООБЩЕНИЕ!", true);
            final JPanel panel = new JPanel();
            final JPanel panel1 = new JPanel();
            final JLabel text = new JLabel(message);
            final JButton butOK = new JButton("Закрыть");
            frMess.setResizable(false);
            text.setForeground(color);
            frMess.add(panel, BorderLayout.NORTH);
            frMess.add(panel1, BorderLayout.SOUTH);
            panel.add(text, BorderLayout.WEST);
            panel1.add(butOK, BorderLayout.EAST);
            frMess.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            butOK.addActionListener(e -> frMess.dispose());
            frMess.setSize(Pack(frMess, 30, 10));
            frMess.setLocationRelativeTo(null);
            frMess.setVisible(true);
        }
        //-----//

        void createTab(Friend friend) {
            if (!tabs.containsKey(friend == null ? -2 : friend.id)) {
                try {
                    JPanel panel = new JPanel();
                    panel.setLayout(new GridBagLayout());
                    JTextPane MessageBox = new JTextPane();
                    JPanel panMessages = new JPanel();
                    JPanel panSendMessage = new JPanel();
                    JButton butSendMessage = new JButton("Отправить");
                    JButton butCloseTab = new JButton("╳");
                    butCloseTab.setToolTipText("Закрыть вкладку");
                    JTextField textField = new JTextField(Client.os_name.equals("Linux") ? 25 : 45);
                    MessageBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    JScrollPane scrollMessage = new JScrollPane();
                    if (friend == null) {
                        JLabel lab = new JLabel(Client.account.friends.size() >= 1 ? "Выберите друга" : "У вас нет друзей :(");
                        lab.setFont(new Font(FONT_style, Font.BOLD, 40));
                        lab.setForeground(MAIN_COLOR);
                        GridBagLayoutManager(panel, lab, GridBagConstraints.CENTER, 0, 0, 1);
                        panel.setPreferredSize(new Dimension(500, 300));
                        panel.setBackground(Color.DARK_GRAY);
                        tabs.put(-1, new Tab(scrollMessage, MessageBox, textField, butSendMessage, panel, "Привет!", null));
                        //tabbedPane.getComponentAt(tabs.get(currentFriend.id).count).setForeground(MAIN_COLOR);
                    } else {
                        if (tabs.containsKey(-1)){
                            tabbedPane.remove(tabs.get(-1).count);
                            tabs.remove(-1);
                        }
                        MessageBox.setLayout(null);
                        MessageBox.setEditable(false);
                        MessageBox.setPreferredSize(new Dimension(500, 272));
                        MessageBox.setBounds(0, 0, 498, 320);
                        createStyles(MessageBox);

                        MessageBox.setBackground(Color.GRAY);
                        MessageBox.setForeground(Color.WHITE);
                        //scrollMessage.setBackground(Color.DARK_GRAY);
                        panSendMessage.setBackground(Color.DARK_GRAY);
                        butSendMessage.setForeground(MAIN_COLOR);
                        butCloseTab.setForeground(Color.RED);

                        scrollMessage.getViewport().add(MessageBox);
                        scrollMessage.createVerticalScrollBar();
                        scrollMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                        panel.setLayout(new GridBagLayout());

                        GridBagLayoutManager(panel, scrollMessage, GridBagConstraints.CENTER, 0, 0, 3);
                        GridBagLayoutManager(panel, butCloseTab, GridBagConstraints.HORIZONTAL, 0, 1, 1);
                        GridBagLayoutManager(panel, textField, GridBagConstraints.HORIZONTAL, 1, 1, 1);
                        GridBagLayoutManager(panel, butSendMessage, GridBagConstraints.HORIZONTAL, 2, 1, 1);

                        textField.addActionListener(e -> sendMessage(textField, MessageBox));
                        butSendMessage.addActionListener(e -> sendMessage(textField, MessageBox));
                        butCloseTab.addActionListener(e -> {
                            tabbedPane.remove(tabs.get(currentFriend.id).count);
                            tabs.remove(currentFriend.id);
                            if (tabs.size() == 0) createTab(null);
                        });

                        Dialog FDialog = null; //FIXME!!!
                        try {
                            if (Client.account.friends.size() > 0) {
                                FDialog = Client.dialogs.get(friend.id);
                                tabs.put(friend.id, new Tab(scrollMessage, MessageBox, textField, butSendMessage, panel, friend.login, FDialog));
                                tabbedPane.getComponentAt(tabs.get(currentFriend.id).count).setForeground(MAIN_COLOR);
                                if(Client.dialogs.size()!=0){
                                    for (ClientMessage message : FDialog.messages) {
                                        insertText(MessageBox, "\n" + currentFriend.login + " [" + message.date + "]\n", heading);
                                        insertText(MessageBox, message.text + "\n", normal);
                                    }
                                }
                            } else {
                                System.out.println("Нет диалогов");
                            }
                        } catch (Exception e) {
                            console.log("" + e, "exc");
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                tabbedPane.setSelectedIndex(tabs.get(friend.id).count);
            }
        }

        private void createStyles(JTextPane editor) {
            // Создание стилей
            normal = editor.addStyle(STYLE_normal, null);
            StyleConstants.setFontFamily(normal, FONT_style);
            StyleConstants.setFontSize(normal, 16);
            // Наследуем свойство dо FontFamily
            heading = editor.addStyle(STYLE_heading, normal);
            StyleConstants.setFontSize(heading, 18);
            StyleConstants.setBold(heading, true);
        }

        void insertText(JTextPane editor, String string, Style style) {
            try {
                Document doc = editor.getDocument();
                doc.insertString(doc.getLength(), string, style);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void loadFriends() {
            panFriends.removeAll();
            panFriends.setLayout(new GridBagLayout());
            scrollFriends.createVerticalScrollBar();
            scrollFriends.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollFriends.setSize(scrollFriends.getWidth(), contentPain.getHeight() - 2);
            panFriends.setBackground(Color.GRAY);

            JLabel label = new JLabel();

            final int FRIENDS = Client.account.friends.size();

            if (FRIENDS != 0) label.setText("Ваши друзья:");
            else {
                label.setText("Давайте дружить!");
                label.setToolTipText("Нажмите кнопку \"+\" для показа списка пользователей!");
            }
            label.setForeground(Color.WHITE);
            GridBagLayoutManager(panFriends, label, GridBagConstraints.CENTER, 1, 0, 1);

            int i = 0;

            for (Map.Entry<Integer , Friend> entry : Client.account.friends.entrySet()) {//for (Map.Entry<Integer , Friend> entry : acc.friends.entrySet()) {
                int maxLength = 12;
                String login = entry.getValue().login;
                JButton button = new JButton();
                JCheckBox checkBoxOnline = new JCheckBox("", entry.getValue().isOnline);

                if (login.length() > maxLength) {
                    button.setToolTipText(login);
                    login = login.substring(0, maxLength - 1) + "...";
                }

                button.setText(login);
                checkBoxOnline.setBackground(Color.GRAY);
                checkBoxOnline.setEnabled(false);

                if (checkBoxOnline.isSelected()) checkBoxOnline.setToolTipText("Online");
                else checkBoxOnline.setToolTipText("Offline");

                GridBagLayoutManager(panFriends, checkBoxOnline, GridBagConstraints.CENTER, 0, i + 1, 1);
                GridBagLayoutManager(panFriends, button, GridBagConstraints.HORIZONTAL, 1, i + 1, 1);
                if (FRIENDS > 13) GridBagLayoutManager(panFriends, new JLabel("    "), GridBagConstraints.HORIZONTAL, 2, i + 1, 1);

                button.addActionListener(e -> {
                    currentFriend = entry.getValue();
                    System.out.println("friend selected");
                    System.out.println(currentFriend.toString());
                    setDialog(entry.getValue());
                    createTab(entry.getValue());
                    tabbedPane.setSelectedIndex(tabs.get(entry.getValue().id).count);
                });
                panFriendList.put(entry.getValue().id, new PanFriend(button, checkBoxOnline));
                System.out.println(entry.getValue().toString());
                i++;
            }

            JButton button = new JButton("+");
            button.setForeground(MAIN_COLOR);
            button.setFont(new Font(FONT_style, Font.BOLD, button.getFont().getSize() + 6));
            GridBagLayoutManager(panFriends, button, GridBagConstraints.HORIZONTAL, 0, FRIENDS + 1, 2);

            button.addActionListener(e -> {
                System.out.println(panFriends.getWidth());
                new FindFriend().showFrame();
            });
            frame.setVisible(false);
            frame.setVisible(true);
        }

        void sendMessage(JTextField textField, JTextPane MessageBox){
            if (textField.getText().length() > 0) {
                insertText(MessageBox, "\n" + Client.account.login + " [" + new SimpleDateFormat("dd/MM/yyyy | hh:mm").format(new Date()) + "]\n", heading);
                insertText(MessageBox, textField.getText() + "\n", normal);
                Client.execute(new String[]{"send", currentFriend.id + "", textField.getText()});
                textField.setText("");
            }
        }

        void setDialog(Friend friend) {
            currentFriend = friend;
        }
        private void buildMenuBar() {
            JMenuBar menuBar = new JMenuBar();
            JMenu butMenu = new JMenu("Меню");
            JMenuItem butAbout = new JMenuItem("О приложении");
            JMenu exitMenu = new JMenu("Выход");
            JMenuItem butToMineMenu = new JMenuItem("Разлогиниться и выйти в главное меню");
            JMenuItem shutdownProgram = new JMenuItem("Разлогиниться и закрыть приложение");
            butAbout.addActionListener(e -> {
                AboutFrame.showFrame();
            });

            butToMineMenu.addActionListener(e -> {
                dispose();
                Client.disconnect();
            });

            shutdownProgram.addActionListener(e -> {
                Client.disconnect();
                System.exit(0);
            });
            menuBar.add(butMenu);
            butMenu.add(exitMenu);
            butMenu.add(butAbout);
            exitMenu.add(butToMineMenu);
            exitMenu.add(shutdownProgram);
            frame.setJMenuBar(menuBar);
        }



        class PanFriend{
            JButton button;
            JCheckBox isOnline;

            public PanFriend(JButton button, JCheckBox isOnline) {
                this.button = button;
                this.isOnline = isOnline;
            }
        }

        public class Tab{
            JScrollPane jScrollPane;
            JTextPane MessageBox;
            JTextField textField;
            JButton button;
            int count;
            String tabName;
            Dialog dialog;

            Tab(JScrollPane jScrollPane, JTextPane MessageBox, JTextField textField, JButton button, JPanel panel, String tabName, Dialog dialog) {
                this.count = tabbedPane.getTabCount();
                this.jScrollPane = jScrollPane;
                this.MessageBox = MessageBox;
                this.textField = textField;
                this.button = button;
                this.tabName = tabName;
                this.dialog = dialog;
                tabbedPane.addTab(tabName, panel);
            }
        }
        /* // Размещение компонента в конце текста
            StyledDocument doc = editor.getStyledDocument();
            editor.setCaretPosition(doc.getLength());
            JCheckBox check = new JCheckBox("JCheckBox");
            check.setFont(new Font(FONT_style, Font.ITALIC, 16));
            check.setOpaque(false);
            editor.insertComponent(check);

            JRadioButton radio = new JRadioButton("JRadioButton");
            radio.setFont(new Font(FONT_style, Font.ITALIC, 16));
            radio.setOpaque(false);
            radio.setSelected(true);
            editor.insertComponent(radio);

        private void loadText(JTextPane editor) {
            // Загружаем в документ содержимое
            for (String[] aTEXT : TEXT) {
                Style style = (aTEXT[1].equals(STYLE_heading)) ? heading : normal;
                insertText(editor, aTEXT[0], style);
        }*/
        JFrame getFrame() {
            return frame;
        }
    }

    //-------------------------------------------//

    @SuppressWarnings("WeakerAccess")
    class RegisterFrame extends AbstractFrame {

        JDialog dialog;
        JLabel label;
        JLabel labLogin;
        JLabel labPassword;
        JLabel labRPassword;
        JLabel info;
        JTextField loginField;
        JPasswordField passwordField;
        JPasswordField passwordField2;
        JButton registerBut;

        RegisterFrame() {
            initial();
        }

        @Override
        public void initial() {
            dialog = new JDialog(MainMenuFrame.frame, "NEOnline - Регистрация", true);
            label = new JLabel("Регистрация");
            labLogin = new JLabel("Логин:");
            labPassword = new JLabel("Пароль:");
            labRPassword = new JLabel("Повторите пароль:");
            loginField = new JTextField(15);
            passwordField = new JPasswordField(15);
            passwordField2 = new JPasswordField(15);
            info = new JLabel("");
            registerBut = new JButton("Зарегистрироваться!");

            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            label.setForeground(MAIN_COLOR);
            label.setFont(new Font(FONT_style, Font.BOLD, 18));
            labLogin.setForeground(Color.WHITE);
            labPassword.setForeground(Color.WHITE);
            labRPassword.setForeground(Color.WHITE);
            loginField.setBackground(Color.GRAY);
            passwordField.setBackground(Color.GRAY);
            passwordField2.setBackground(Color.GRAY);

            loginField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            passwordField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            passwordField2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            dialog.setLayout(new GridBagLayout());
            dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            dialog.setResizable(false);

            buildFrame();

            dialog.pack();
            dialog.setSize(dialog.getWidth()+50,dialog.getHeight()+20);

            loginField.addActionListener(e -> reviewData());

            passwordField.addActionListener(e -> reviewData());

            passwordField2.addActionListener(e -> reviewData());

            registerBut.addActionListener(e -> {
                        if (Security.getMD5(passwordField.getPassword()).equals(Security.getMD5(passwordField2.getPassword()))){
                            if (!Client.statusConnected)Client.connect();
                            Client.register(loginField.getText(), Security.getMD5(passwordField.getPassword()));
                        }
                    }
            );
        }

        private void reviewData(){
            setInfo("Проврьте данные и нажмите кнопку \"Зарегистрироваться!\"", Color.ORANGE);
            dialog.setSize(Pack(dialog, 50, 20));
        }

        @Override
        public void showFrame() {
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        @Override
        public void dispose() {
            dialog.dispose();
        }

        @Override
        public void setInfo(String message, Color color) {
            info.setText(message);
            info.setForeground(color);
        }

        void buildFrame(){
            GridBagLayoutManager(dialog, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(dialog, labLogin, GridBagConstraints.EAST, 0, 1, 1);
            GridBagLayoutManager(dialog, loginField, GridBagConstraints.HORIZONTAL, 1, 1, 1);
            GridBagLayoutManager(dialog, getEmptyLabel(2), GridBagConstraints.HORIZONTAL, 0, 2, 2);
            GridBagLayoutManager(dialog, labPassword, GridBagConstraints.EAST, 0, 3, 1);
            GridBagLayoutManager(dialog, passwordField, GridBagConstraints.HORIZONTAL, 1, 3, 1);
            GridBagLayoutManager(dialog, getEmptyLabel(2), GridBagConstraints.HORIZONTAL, 0, 4, 2);
            GridBagLayoutManager(dialog, labRPassword, GridBagConstraints.EAST, 0, 5, 1);
            GridBagLayoutManager(dialog, passwordField2, GridBagConstraints.HORIZONTAL, 1, 5, 1);
            GridBagLayoutManager(dialog, getEmptyLabel(3), GridBagConstraints.HORIZONTAL, 0, 6, 2);
            GridBagLayoutManager(dialog, info, GridBagConstraints.CENTER, 0, 7, 2);
            GridBagLayoutManager(dialog, registerBut, GridBagConstraints.HORIZONTAL, 0, 8, 2);
        }


    }

    //-------------------------------------------//

    private class FindFriend extends AbstractFrame {
        JDialog dialog;
        JLabel labSearch;
        JLabel labSend;
        JPanel panSearch;
        JPanel panSend;
        JTextField fieldSearch;
        JTextField fieldSend;
        JButton butSearch;
        JButton butSend;
        JButton butUpdate;
        JScrollPane scrollPane;
        JTable table;
        TableModel model;

        @Override
        public void initial() {
            Client.usersInSearch = new ArrayList<>();
            Client.execute(new String[]{"getUsers","",""});
            dialog = new JDialog(MainFrame.frame, "Добавить друга", true);
            labSearch = new JLabel("Поиск по логину. Введите логин:");
            labSend = new JLabel("Введите логин человека из таблицы, которому хотите предложить дружбу:");
            panSearch = new JPanel();
            panSend = new JPanel();
            fieldSearch = new JTextField(50);
            fieldSend = new JTextField(20);
            butSearch = new JButton("Поиск");
            butSend = new JButton("Предложить дружбу");
            butUpdate = new JButton("Обновить");

            ArrayList<Account> users = Client.usersInSearch;
            model = new SearchUsersTabel(users);
            table = new JTable(model);
            scrollPane = new JScrollPane(table);

            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            fieldSearch.setForeground(Color.WHITE);
            fieldSearch.setBackground(Color.GRAY);
            fieldSend.setBackground(Color.GRAY);
            fieldSend.setForeground(Color.DARK_GRAY);
            panSearch.setBackground(Color.DARK_GRAY);
            panSend.setBackground(Color.DARK_GRAY);
            labSearch.setForeground(Color.DARK_GRAY);
            labSend.setForeground(Color.WHITE);
            butUpdate.setForeground(MAIN_COLOR);
            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            table.setBackground(Color.DARK_GRAY);
            table.setGridColor(Color.GRAY);
            table.setForeground(Color.WHITE);

            dialog.setLayout(new GridBagLayout());
            panSearch.setLayout(new GridBagLayout());
            panSend.setLayout(new GridBagLayout());

            GridBagLayoutManager(dialog, panSearch, GridBagConstraints.HORIZONTAL, 0, 0, 2);
            GridBagLayoutManager(dialog, scrollPane, GridBagConstraints.CENTER, 1, 1, 1);
            GridBagLayoutManager(dialog, panSend, GridBagConstraints.HORIZONTAL, 0, 2, 2);

            GridBagLayoutManager(panSearch, labSearch, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(panSearch, fieldSearch, GridBagConstraints.HORIZONTAL, 0, 1, 1);
            GridBagLayoutManager(panSearch, butSearch, GridBagConstraints.CENTER, 1, 1, 1);

            GridBagLayoutManager(panSend, labSend, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(panSend, butUpdate, GridBagConstraints.FIRST_LINE_START, 0, 1, 1);
            GridBagLayoutManager(panSend, fieldSend, GridBagConstraints.HORIZONTAL, 2, 0, 1);
            GridBagLayoutManager(panSend, butSend, GridBagConstraints.HORIZONTAL, 2, 1, 1);

            dialog.setSize(Pack(dialog, 30, 10));

            table.setSize(new Dimension(dialog.getWidth()-10, table.getHeight()));

            dialog.setLocationRelativeTo(null);

            butSearch.addActionListener(e -> {
                setInfo("К сожалению, данная функция в этой версии недоступна :-( Возможно, она появится в следующей версии. Приносим свои извенения", Color.BLACK);
                //findUsers(fieldSearch.getText());
            });

            butSend.addActionListener(e -> sendAddFriend());

            butUpdate.addActionListener(e -> {
                Client.usersInSearch = new ArrayList<>();
                Client.execute(new String[]{"getUsers","",""});
                dispose();
                showFrame();
            });
        }

        @Override
        public void showFrame() {
            initial();
            dialog.setVisible(true);
        }

        @Override
        public void dispose() {
            dialog.dispose();
        }

        @Override
        public void setInfo(String message, Color color) {
            final JDialog frMess = new JDialog(dialog, "СООБЩЕНИЕ!", true);
            final JPanel panel = new JPanel();
            final JPanel panel1 = new JPanel();
            final JLabel text = new JLabel(message);
            final JButton butOK = new JButton("Закрыть");
            frMess.setResizable(false);
            text.setForeground(color);
            frMess.add(panel, BorderLayout.NORTH);
            frMess.add(panel1, BorderLayout.SOUTH);
            panel.add(text, BorderLayout.WEST);
            panel1.add(butOK, BorderLayout.EAST);
            frMess.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            butOK.addActionListener(e -> frMess.dispose());
            frMess.pack();
            frMess.setSize(Pack(frMess, 30, 10));
            frMess.setLocationRelativeTo(null);

            frMess.setVisible(true);
        }

        //------------//

        private void sendAddFriend() {
            ArrayList<Account> accounts = new ArrayList<>();
            accounts.addAll(Client.usersInSearch);
            for (Account acc : accounts) {
                if (acc.login.equals(fieldSend.getText()))
                    if (!Client.account.login.equals(fieldSend.getText())) {
                        Client.execute(new String[]{"addFriend", String.valueOf(acc.id), fieldSend.getText()});
                        setInfo("Заявка в друзья отправлена!", Color.BLACK);
                        break;
                    } else {
                        setInfo("Нельзя добавить в друзья самого себя!", Color.RED);
                        break;
                    }
            }
        }

        private void findUsers(String login){
            dialog.remove(table);
            dialog.remove(scrollPane);
            Client.usersInSearch = new ArrayList<>();
            Client.execute(new String[]{"getUsers","",""});
            ArrayList<Account> accounts = new ArrayList<>();
            for (Account acc : Client.usersInSearch) {
                if (acc.login.equals(login)) {
                    accounts.add(acc);
                    break;
                }
            }
            accounts.addAll(Client.usersInSearch.stream().filter(acc -> acc.login.contains(login.substring(0, Math.round(login.length() / 2))) & !acc.login.equals(login)).collect(Collectors.toList()));
            model = new SearchUsersTabel(accounts);
            table = new JTable(model);
            scrollPane = new JScrollPane(table);
            GridBagLayoutManager(dialog, scrollPane, GridBagConstraints.CENTER, 1, 1, 1);
        }

        class SearchUsersTabel implements TableModel {
            @SuppressWarnings("ALL")
            private Set<TableModelListener> listeners = new HashSet<>();
            ArrayList<Account> users = null;

            SearchUsersTabel(ArrayList<Account> users){
                this.users = users;
            }

            @Override
            public int getRowCount() {
                return users.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return "Логин";
                    case 1:
                        return "Статус";
                }
                return "";
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex){
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                }
                return null;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return users.get(rowIndex).login;
                    case 1:
                        return (users.get(rowIndex).isOnline) ? "Онлаин" : "Оффлаин";
                    case 2:
                        JButton but = new JButton("Отправить запрос");
                        but.setForeground(MAIN_COLOR);
                        but.addActionListener(e -> {
                            ArrayList<Account> accounts = new ArrayList<>();
                            accounts.addAll(Client.usersInSearch);
                            for (Account acc : accounts) {
                                if (acc.login.equals(fieldSend.getText()))
                                    if (!Client.account.login.equals(fieldSend.getText())) {
                                        Client.execute(new String[]{"addFriend", String.valueOf(acc.id), fieldSend.getText()});
                                        break;
                                    } else {
                                        setInfo("Нельзя добавить в друзья самого себя!", Color.RED);
                                        break;
                                    }
                            }
                        });
                        return but;

                }
                return "";
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            }

            @Override
            public void addTableModelListener(TableModelListener l) {
                listeners.add(l);
            }

            @Override
            public void removeTableModelListener(TableModelListener l) {
                listeners.remove(l);
            }
        }

    }

    //-------------------------------------------//

    private class SettingsFrame extends AbstractFrame {
        JDialog dialog;
        JButton butNativeJLeF;
        JButton butStandartJLeF;
        JButton butMotifJLeF;
        JButton butDelAccData;
        JToolBar toolBar;

        @Override
        public void initial() {
            dialog = new JDialog(MainMenuFrame.frame, "Настройки", true);
            dialog.setLayout(new GridBagLayout());
            dialog.setResizable(true);
            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            toolBar = new JToolBar("Стиль оформления");
            toolBar.setLayout(new GridBagLayout());
            toolBar.setBackground(Color.DARK_GRAY);
            JLabel info = new JLabel("Стили оформления окна");
            info.setFont(new Font(FONT_style, Font.BOLD, 20));
            info.setForeground(MAIN_COLOR);
            JButton butClose = new JButton("Применить");
            butNativeJLeF = new JButton("Системный стиль");
            butStandartJLeF = new JButton("Java стиль");
            butMotifJLeF = new JButton("Motif стиль");
            butDelAccData = new JButton("Удалить мои данные о аккаунте");

            butNativeJLeF.setFocusable(false);
            butStandartJLeF.setFocusable(false);
            butMotifJLeF.setFocusable(false);

            GridBagLayoutManager(dialog, info, GridBagConstraints.CENTER, 0, 0, 1);
            GridBagLayoutManager(dialog, getEmptyLabel(5), GridBagConstraints.HORIZONTAL, 0, 1, 1);
            GridBagLayoutManager(dialog, toolBar, GridBagConstraints.CENTER, 0, 1, 1);
            GridBagLayoutManager(toolBar, butNativeJLeF, GridBagConstraints.HORIZONTAL, 0, 0, 1);
            GridBagLayoutManager(toolBar, butStandartJLeF, GridBagConstraints.HORIZONTAL, 0, 1, 1);
            GridBagLayoutManager(toolBar, butMotifJLeF, GridBagConstraints.HORIZONTAL, 0, 2, 1);
            GridBagLayoutManager(dialog, getEmptyLabel(15), GridBagConstraints.HORIZONTAL, 0, 3, 1);
            GridBagLayoutManager(dialog, butDelAccData, GridBagConstraints.CENTER, 0, 4, 1);
            GridBagLayoutManager(dialog, getEmptyLabel(3), GridBagConstraints.CENTER, 0, 5, 1);
            GridBagLayoutManager(dialog, butClose, GridBagConstraints.CENTER, 0, 6, 1);

            butNativeJLeF.addActionListener(e -> {
                info.setText("Нажмите применить");
                typeILAF = "NLAF";
            });

            butStandartJLeF.addActionListener(e -> {
                info.setText("Нажмите применить");
                typeILAF = "JLAF";
            });

            butMotifJLeF.addActionListener(e -> {
                info.setText("Нажмите применить");
                typeILAF = "MLAF";
            });

            butDelAccData.addActionListener(e -> {
                //Security.setAboutUser("", "");
                LoginFrame.loginField.setText("");
                LoginFrame.passwordField.setText("");
                info.setText("Успешно!");
            });

            butClose.addActionListener(e -> {
                LoginFrame.dispose();
                RegisterFrame.dispose();
                SettingsFrame.dispose();
                MainMenuFrame.dispose();
                MainMenuFrame = new MainMenuFrame();
                LoginFrame = new LoginFrame();
                RegisterFrame = new RegisterFrame();
                startGUI();
            });

            dialog.setSize(Pack(dialog, 40, 30));
            dialog.setLocationRelativeTo(null);
        }

        @Override
        public void showFrame() {
            initial();
            dialog.setVisible(true);
        }

        @Override
        public void dispose() {

        }

        @Override
        public void setInfo(String message, Color color) {

        }
    }

    //-------------------------------------------//

    private class AboutFrame{

        void showFrame() {
            JDialog dialog = new JDialog(MainMenuFrame.frame, "О NEOnline", true);
            JLabel label = new JLabel("NEOnline");
            JLabel empty= new JLabel(" ");
            JLabel text = new JLabel("Лучший бесплатный мессенджер!");
            JLabel creatorsLab = new JLabel("Создатели:");
            JLabel labZver = new JLabel("ZVER - Иван Кокорев                 ");
            JLabel labAlex = new JLabel("ALEX - Александр Василенко    ");
            JLabel labURIZver = new JLabel("https://vk.com/vanian98");
            JLabel labURIAlex = new JLabel("https://vk.com/aleksandr_vasilenko");
            JLabel labClientVersion = new JLabel(Client.client_version);
            JLabel labJavaVersion = new JLabel("Java version: "+Client.java_version);
            JButton butCl = new JButton("Закрыть");

            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setLayout(new GridBagLayout());

            labZver.setFocusable(true);

            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            text.setForeground(Color.WHITE);
            label.setFont(new Font(FONT_style, Font.BOLD, 40));
            label.setForeground(MAIN_COLOR);
            creatorsLab.setForeground(new Color(106, 135, 89));
            labAlex.setForeground(new Color(255, 100, 25));
            labZver.setForeground(new Color(255, 100, 25));
            labURIAlex.setForeground(MAIN_COLOR);
            labURIZver.setForeground(MAIN_COLOR);
            labURIAlex.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            labURIZver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            labClientVersion.setForeground(new Color(37, 37, 37));
            labJavaVersion.setForeground(new Color(37, 37, 37));

            GridBagLayoutManager(dialog, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(dialog, empty, GridBagConstraints.HORIZONTAL, 0, 1, 2);
            GridBagLayoutManager(dialog, text, GridBagConstraints.CENTER, 0, 2, 2);
            GridBagLayoutManager(dialog, empty, GridBagConstraints.HORIZONTAL, 0, 3, 2);
            GridBagLayoutManager(dialog, creatorsLab, GridBagConstraints.CENTER, 0, 4, 2);
            GridBagLayoutManager(dialog, labZver, GridBagConstraints.HORIZONTAL, 0, 6, 1);
            GridBagLayoutManager(dialog, labURIZver, GridBagConstraints.HORIZONTAL, 1, 6, 1);
            GridBagLayoutManager(dialog, labAlex, GridBagConstraints.HORIZONTAL, 0, 7, 1);
            GridBagLayoutManager(dialog, labURIAlex, GridBagConstraints.HORIZONTAL, 1, 7, 1);
            GridBagLayoutManager(dialog, empty, GridBagConstraints.HORIZONTAL, 0, 8, 2);
            GridBagLayoutManager(dialog, butCl, GridBagConstraints.PAGE_END, 0, 9, 2);
            GridBagLayoutManager(dialog, labClientVersion,GridBagConstraints.CENTER, 0, 10, 2);
            GridBagLayoutManager(dialog, labJavaVersion, GridBagConstraints.LINE_END, 0, 11, 2);
            labClientVersion.setBounds(0, 0, labClientVersion.getWidth(), labClientVersion.getHeight());

            labURIAlex.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Util.openLink(new URI(labURIAlex.getText()));
                    } catch (URISyntaxException se) {
                        System.out.println("Не верный URL: "+se.getMessage());
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    labURIAlex.setForeground(new Color(140, 190, 250));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    labURIAlex.setForeground(MAIN_COLOR);
                }
            });

            labURIZver.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Util.openLink(new URI(labURIZver.getText()));
                    } catch (URISyntaxException se) {
                        System.out.println("Не верный URL: "+se.getMessage());
                    }

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    labURIZver.setForeground(new Color(140, 190, 250));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    labURIZver.setForeground(MAIN_COLOR);
                }
            });

            final boolean[] isRand = {false};

            label.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isRand[0] = true;
                    new Thread(() -> {
                        int red = 69;
                        int green = 151;
                        boolean colR = true;
                        boolean colG = true;
                        boolean color = false;
                        while (isRand[0]) {
                            if (red <= 1) {
                                colR = true;
                                color = true;
                            }
                            if (red >= 130) colR = false;
                            if (green >= 255){
                                colR = true;
                                color = false;
                            }
                            if (color & colR) red += 2;
                            if (!color & colR) red -= 2;
                            if (color) green += 2;
                            if (!color) green -= 2;
                            try {
                                TimeUnit.MILLISECONDS.sleep(20);
                            } catch (InterruptedException runnable) {
                                runnable.printStackTrace();
                            }
                            label.setForeground(new Color(red, green, 249));
                        }
                        while (true) {
                            if (red < 69) red += 1;
                            if (red > 69) red -= 1;
                            if (green < 151) green += 1;
                            if (green > 151) green -= 1;
                            try {
                                TimeUnit.MILLISECONDS.sleep(10);
                            } catch (InterruptedException runnable) {
                                runnable.printStackTrace();
                            }
                            label.setForeground(new Color(red, green, 249));
                            if (red == 69 & green == 151) break;
                        }
                        //label.setForeground(MAIN_COLOR);
                    }).start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isRand[0] = false;
                }
            });
            dialog.setSize(522, 205);
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(null);
            butCl.addActionListener(e -> dialog.dispose());
            dialog.setVisible(true);
        }
    }

    //-------------------------------------------//

    class AddFriend{
        Friend friend;

        AddFriend(Friend friend) {
            this.friend = friend;
            initial();
        }

        private void initial(){
            JFrame frame = new JFrame("Входящая заявка в друзья");
            JButton butAccept = new JButton("Одобрить");
            JButton butRefuse = new JButton("Отказать");
            JLabel label = new JLabel("Вас хотят добавить в друзья");
            JLabel friendInf = new JLabel("Ник: "+friend.login+", Статус: "+((friend.isOnline) ? "Онлайн" : "Оффлайн"));

            frame.getContentPane().setBackground(Color.DARK_GRAY);
            butAccept.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            butAccept.setFocusable(false);
            butRefuse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            butRefuse.setFocusable(false);
            label.setForeground(Color.WHITE);
            friendInf.setForeground(Color.WHITE);

            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.setLayout(new GridBagLayout());

            GridBagLayoutManager(frame, label, GridBagConstraints.CENTER, 0, 0, 3);
            GridBagLayoutManager(frame, friendInf, GridBagConstraints.CENTER, 0, 1, 3);
            GridBagLayoutManager(frame, butRefuse, GridBagConstraints.HORIZONTAL, 0, 2, 1);
            GridBagLayoutManager(frame, getEmptyLabel(20), GridBagConstraints.CENTER, 1, 2, 1);
            GridBagLayoutManager(frame, butAccept, GridBagConstraints.HORIZONTAL, 2, 2, 1);

            butAccept.addActionListener(e -> {
                Client.resOfFriend("yes", friend.id);
                frame.dispose();
            });
            butRefuse.addActionListener(e -> {
                Client.resOfFriend("no", friend.id);
                frame.dispose();
            });
            frame.setAlwaysOnTop(true);
            frame.setResizable(false);
            frame.getContentPane().setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight()+20);
            frame.setSize(Pack(frame, 50, 25));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    //-----------------------------------------------------------------------------------------------------//

    private static void GridBagLayoutManager(JFrame frame, JComponent component, int fill, int gridX, int gridY, int gridWidth){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridwidth = gridWidth;
        c.gridx = gridX;
        c.gridy = gridY;
        frame.add(component, c);

    }
    private static void GridBagLayoutManager(JComponent parent, JComponent component, int fill, int gridX, int gridY, int gridWidth){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridwidth = gridWidth;
        c.gridx = gridX;
        c.gridy = gridY;
        parent.add(component, c);
    }
    private static void GridBagLayoutManager(JFrame frame, JComponent component, int anchor, int fill, int gridX, int gridY, int gridWidth){
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = anchor;
        c.fill = fill;
        c.gridwidth = gridWidth;
        c.gridx = gridX;
        c.gridy = gridY;
        frame.add(component, c);
    }
    private static void GridBagLayoutManager(Container parent, JComponent component, int fill, int gridX, int gridY, int gridWidth){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridwidth = gridWidth;
        c.gridx = gridX;
        c.gridy = gridY;
        parent.add(component, c);
    }

    private static JLabel getEmptyLabel(int size) {
        JLabel emptyLabel = new JLabel(" ");
        emptyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size));
        return emptyLabel;
    }

    void startGUI(){
        MainMenuFrame = new MainMenuFrame();
        SettingsFrame = new SettingsFrame();
        LoginFrame = new LoginFrame();
        RegisterFrame = new RegisterFrame();
        AboutFrame = new AboutFrame();
        MainFrame = new MainFrame();
        MainMenuFrame.showFrame();
    }

    private static Dimension Pack(JFrame frame, int w, int h){
        frame.pack();
        return new Dimension(frame.getWidth()+w, frame.getHeight()+h);
    }
    private static Dimension Pack(JDialog dialog, int w, int h){
        dialog.pack();
        return new Dimension(dialog.getWidth()+w, dialog.getHeight()+h);
    }

    private static class WindowUtilities {
        static void setNativeLookAndFeel() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception e) {
                System.err.println("Error setting native LAF: " + e);
            }
        }

        static void setJavaLookAndFeel() {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch(Exception e) {
                System.err.println("Error setting Java LAF: " + e);
            }
        }

        static void setMotifLookAndFeel() {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            } catch(Exception e) {
                System.out.println("Error setting Motif LAF: " + e);
            }
        }
    }
}
