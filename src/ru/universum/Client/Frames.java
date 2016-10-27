package ru.universum.Client;

import ru.universum.Loader.Account;
import ru.universum.Loader.Friend;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.text.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Frames {
    private final String STYLE_heading = "heading";
    @SuppressWarnings("ALL")
    private final String STYLE_normal  = "normal";
    private final String FONT_style    = "Trebuchet MS";
    private final Color MAIN_COLOR = new Color(69, 151, 249);

    private MainMenuFrame MainMenuFrame = new MainMenuFrame();
    LoginFrame LoginFrame = new LoginFrame();
    RegisterFrame RegisterFrame = new RegisterFrame();
    private AboutFrame AboutFrame = new AboutFrame();
    MainFrame MainFrame = new MainFrame();
    SettingsFrame SettingsFrame = new SettingsFrame();

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
            WindowUtilities.setNativeLookAndFeel();
            frame = new JFrame("NEOnline");
            butLogin = new JButton("Войти");
            butRegister = new JButton("Регистрация");
            butSettings = new JButton("Настройки");
            butAbout = new JButton("О программе");
            label = new JLabel("NEOnline");
            info = new JLabel("");

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

            frame.setSize(160, 185);
            frame.setResizable(true);
            frame.setLocationRelativeTo(null);

            butLogin.addActionListener(e -> LoginFrame.showFrame());
            butRegister.addActionListener(e -> RegisterFrame.showFrame());
            butSettings.addActionListener(e -> SettingsFrame.showFrame()); // FIXME: 25.09.16 CODE SETTINGS
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
    @SuppressWarnings("ALL")
    class LoginFrame extends AbstractFrame {
        JDialog dialog;
        private JTextField loginField;
        private JPasswordField passwordField;
        private JButton butLogin;
        private JCheckBox checkBox;
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
            checkBox = new JCheckBox("Запомнить");
            info = new JLabel();
            label = new JLabel("Введите логин и пароль");

            label.setForeground(Color.WHITE);
            checkBox.setForeground(Color.WHITE);
            checkBox.setFocusable(false);
            checkBox.setBackground(Color.DARK_GRAY);
            loginField.setBackground(Color.GRAY);
            passwordField.setBackground(Color.GRAY);
            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            butLogin.setFocusable(false);

            loginField.setToolTipText("ЛОГИН");
            passwordField.setToolTipText("ПАРОЛЬ");

            dialog.setLayout(new GridBagLayout());
            GridBagLayoutManager(dialog, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(dialog, loginField, GridBagConstraints.HORIZONTAL, 0, 1, 2);
            GridBagLayoutManager(dialog, getEmptyLabel(3), GridBagConstraints.HORIZONTAL, 0, 2, 2);
            GridBagLayoutManager(dialog, passwordField, GridBagConstraints.HORIZONTAL, 0, 3, 2);
            GridBagLayoutManager(dialog, getEmptyLabel(3), GridBagConstraints.HORIZONTAL, 0, 4, 2);
            GridBagLayoutManager(dialog, info, GridBagConstraints.CENTER, 0, 5, 2);
            GridBagLayoutManager(dialog, checkBox, GridBagConstraints.CENTER, 0, 6, 1);
            GridBagLayoutManager(dialog, butLogin, GridBagConstraints.CENTER, 1, 6, 1);

            dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            dialog.setAlwaysOnTop(true);
            dialog.setSize(200, 149);
            dialog.setResizable(false);

            butLogin.addActionListener(e -> login());
            loginField.addActionListener(e -> login());
            passwordField.addActionListener(e -> login());
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

        private void login(){
            setInfo("Входим...", Color.ORANGE);
            if (loginField.getText().length() > 1 & passwordField.getPassword().length > 1) {
                if (!Client.statusConnected) Client.connect();
                Client.login(loginField.getText(), passwordField.getText());
            } else {
                setInfo("Введите данные!", Color.RED);
            }
        }
    }

    //-------------------------------------------//

    class MainFrame extends AbstractFrame {
        Style heading = null; // стиль заголовка
        Style normal = null; // стиль текста
        JTabbedPane tabbedPane;
        ArrayList<Tab> tabs = new ArrayList<>();
//            private final String[][] TEXT = {
//                {"                                                                                                                    ", "heading"},
//                {"\r\n                                               ", "normal"},
//                {"\nВыберете друга", "heading"},
//                {"\r\n                                               ", "normal"},
//                {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}, {"\n", "normal"}};
//        // FIXME: 30.08.16 ПОФИКСИТЬ КОСТЫЛЬ
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
                frame = new JFrame("NEOnline - Сообщения ("+Client.version+")");
                frame.setSize(755, 500);
                frame.setResizable(true);                                // FIXME: 20.09.16 resizable
                contentPain = frame.getContentPane();
                panFriends = new JPanel();
                panMainContent = new JPanel();
                scrollFriends = new JScrollPane(panFriends);
                tabbedPane = new JTabbedPane();
                tabbedPane.setBackground(Color.DARK_GRAY);
                //createTab(null);

                panMainContent.setBackground(Color.DARK_GRAY);
                contentPain.setBackground(Color.DARK_GRAY);

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLayout(new GridBagLayout());
                scrollFriends.setMaximumSize(new Dimension(scrollFriends.getWidth(), 317));

                GridBagLayoutManager(frame, scrollFriends, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, 0, 0, 1);
                panMainContent.add(tabbedPane);
                GridBagLayoutManager(frame, panMainContent, GridBagConstraints.CENTER, 1, 0, 1);

                frame.setLocationRelativeTo(null);

            } catch (Exception e) {
                setInfo(e.toString(), Color.RED);
            }
        }

        @Override
        public void showFrame() {
            initial();
            loadFriends();
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
            frMess.pack();
            frMess.setSize(frMess.getWidth() + 30, frMess.getHeight() + 10);
            frMess.setLocationRelativeTo(null);

            frMess.setVisible(true);
        }

        //-----//

        void createTab(Friend friend){
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            JTextPane MessageBox = new JTextPane();
            JPanel panMessages = new JPanel();
            JPanel panSendMessage = new JPanel();
            JButton butSendMessage = new JButton("Отправить");
            JTextField textField = new JTextField(30);
            JScrollPane scrollMessage = new JScrollPane();

            panMessages.setLayout(null);
            MessageBox.setBounds(0, 0, 490, 280);
            panMessages.add(MessageBox);
            panMessages.setPreferredSize(new Dimension(468, 272));
            createStyles(MessageBox);
            MessageBox.setBackground(Color.GRAY);
            MessageBox.setForeground(Color.WHITE);
            scrollMessage.setBackground(Color.DARK_GRAY);
            panSendMessage.setBackground(Color.DARK_GRAY);

            scrollMessage.createVerticalScrollBar();
            scrollMessage.getViewport().add(panMessages);
            scrollMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollMessage.setAutoscrolls(true);

            panel.setLayout(new GridBagLayout());
            GridBagLayoutManager(panel, scrollMessage, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(panel, textField, GridBagConstraints.HORIZONTAL, 0, 1, 1);
            GridBagLayoutManager(panel, butSendMessage, GridBagConstraints.CENTER, 1, 1, 1);


            textField.addActionListener(e -> {
                System.out.println(MessageBox.getSize());
                if (textField.getText().length() > 0) {
                    insertText(MessageBox, "\n--- " + Client.account.login + " [" + new SimpleDateFormat("dd/MM/yyyy | hh:mm").format(new Date()) + "] ----------------------\n", heading);
                    insertText(MessageBox, textField.getText() + "\n", normal);
                    Client.execute(new String[]{"send", currentFriend.id + "", textField.getText()});
                    textField.setText("");
                }
            });
            butSendMessage.addActionListener(e -> send());
            Dialog FDialog = null;
//            for (int i = 0; i < Client.dialogs.size(); i++) {
//                if (Client.dialogs.get(i).with.login.equals(friend.login)){
//                    FDialog = Client.dialogs.get(i);
//                }
//            }

            tabs.add(new Tab(scrollMessage, textField, butSendMessage, friend.login, FDialog));
            tabbedPane.addTab(friend.login, panel);
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

//        private void loadText(JTextPane editor) {
//            // Загружаем в документ содержимое
//            for (String[] aTEXT : TEXT) {
//                Style style = (aTEXT[1].equals(STYLE_heading)) ? heading : normal;
//                insertText(editor, aTEXT[0], style);
//            }
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

        }*/

        private void buildPanMessages() {}

        void loadFriends() {
            panFriends.setLayout(new GridBagLayout());

            scrollFriends.createVerticalScrollBar();
            scrollFriends.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            scrollFriends.setSize(scrollFriends.getWidth(), contentPain.getHeight() - 2);
            //scrollFriends.setMaximumSize(new Dimension(136, 391));
            panFriends.setBackground(Color.GRAY);

            final int COUNTFRIENDS = Client.account.friends.size();
            GridBagConstraints c = new GridBagConstraints();

            JLabel label = new JLabel();
            if (COUNTFRIENDS != 0) label.setText("Друзья:");
            else label.setText("У вас ещё нет друзей :(");
            label.setForeground(Color.WHITE);
            c.fill = GridBagConstraints.CENTER;
            c.gridx = 1;
            c.gridy = 0;
            panFriends.add(label, c);

            for (int i = 0; i < COUNTFRIENDS; i++) {
                String login = Client.account.friends.get(i).login;
                int max = 12;
                JButton button = new JButton();
                if (login.length() > max) {
                    button.setToolTipText(login);
                    login = login.substring(0, max - 1) + "...";
                }
                button.setText(login);
                currentFriend = Client.account.friends.get(i);
                JCheckBox checkBoxOnline = new JCheckBox("", currentFriend.isOnline);
                checkBoxOnline.setBackground(Color.GRAY);
                checkBoxOnline.setEnabled(false);

                if (checkBoxOnline.isSelected()) checkBoxOnline.setToolTipText("Online");
                else checkBoxOnline.setToolTipText("Offline");

                GridBagLayoutManager(panFriends, checkBoxOnline, GridBagConstraints.CENTER, 0, i + 1, 1);
                GridBagLayoutManager(panFriends, button, GridBagConstraints.HORIZONTAL, 1, i + 1, 1);
                if (COUNTFRIENDS > 13)
                    GridBagLayoutManager(panFriends, new JLabel("    "), GridBagConstraints.HORIZONTAL, 2, i + 1, 1);

                int finalI = i;
                button.addActionListener(e -> {
                    setDialog(Client.account.friends.get(finalI));
                    createTab(currentFriend);
                });
            }
            JButton button = new JButton("+");
            button.setForeground(MAIN_COLOR);
            button.setFont(new Font(FONT_style, Font.BOLD, button.getFont().getSize() + 6));
            GridBagLayoutManager(panFriends, button, GridBagConstraints.HORIZONTAL, 0, COUNTFRIENDS + 1, 2);

            button.addActionListener(e -> new FindFriend().showFrame());

        }

        void setDialog(Friend friend) {
            currentFriend = friend;
        }

        private void send() {

        }

        @SuppressWarnings("ALL")   // FIXME: 24.09.16 delete WarningBloker
        private void buildMenuBar() {}

        JFrame getFrame() {
            return frame;
        }

        class Tab{
            JScrollPane MessageBox;
            JTextField textField;
            JButton button;
            int count;
            String tabName;
            Dialog dialog;

            Tab(JScrollPane messageBox, JTextField textField, JButton button, String tabName, Dialog dialog) {
                MessageBox = messageBox;
                this.textField = textField;
                this.button = button;
                this.count = tabs.size()+1;
                this.tabName = tabName;
                this.dialog = dialog;
            }
        }
    }

    //-------------------------------------------//

    @SuppressWarnings("ALL")
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

        public RegisterFrame() {
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

            dialog.setLayout(new GridBagLayout());
            dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            dialog.setResizable(false);

            buildFrame();

            dialog.pack();
            dialog.setSize(dialog.getWidth()+50,dialog.getHeight()+20);

            loginField.addActionListener(e -> {
                setInfo("Проврьте данные и нажмите кнопку \"Зарегистрироваться!\"", Color.ORANGE);
                dialog.pack();
                dialog.setSize(dialog.getWidth()+50,dialog.getHeight()+20);
            });

            passwordField.addActionListener(e -> {
                setInfo("Проврьте данные и нажмите кнопку \"Зарегистрироваться!\"", Color.ORANGE);
                dialog.pack();
                dialog.setSize(dialog.getWidth()+50,dialog.getHeight()+20);
            });

            passwordField2.addActionListener(e -> {
                setInfo("Проврьте данные и нажмите кнопку \"Зарегистрироваться!\"", Color.ORANGE);
                dialog.pack();
                dialog.setSize(dialog.getWidth()+50,dialog.getHeight()+20);
            });


            registerBut.addActionListener(e -> {        // FIXME: 30.08.16 ДОБАВИТЬ MD5 ШИФРОВАНИЕ
                        if (!passwordField.getText().contains("!") && !passwordField.getText().contains("@") && !passwordField.getText().contains("\"") && !passwordField.getText().contains("?")){
                            if (!Client.statusConnected)Client.connect();
                            Client.register(loginField.getText(), passwordField.getText());
                        }
                    }
            );
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

        public void buildFrame(){
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
            fieldSend.setForeground(Color.WHITE);
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

            dialog.pack();
            dialog.setSize(dialog.getWidth()+30,dialog.getHeight()+10);

            table.setSize(new Dimension(dialog.getWidth()-10, table.getHeight()));

            dialog.setLocationRelativeTo(null);

            butSearch.addActionListener(e -> findUsers(fieldSearch.getText()));

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
            frMess.setSize(frMess.getWidth()+30, frMess.getHeight()+10);
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
        @Override
        public void initial() {
            dialog = new JDialog(MainMenuFrame.frame, "Настройки", true);
            dialog.setLayout(new GridBagLayout());
            dialog.setResizable(false);
            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            JLabel info = new JLabel("Стили оформления окна");
            info.setFont(new Font(FONT_style, Font.BOLD, 20));
            info.setForeground(MAIN_COLOR);
            JButton butClose = new JButton("Закрыть");
            butNativeJLeF = new JButton("Системный стиль");
            butStandartJLeF = new JButton("Java стиль");
            butMotifJLeF = new JButton("Motif стиль");
            GridBagLayoutManager(dialog, info, GridBagConstraints.CENTER, 0, 0, 1);
            GridBagLayoutManager(dialog, butNativeJLeF, GridBagConstraints.HORIZONTAL, 0, 1, 1);
            GridBagLayoutManager(dialog, butStandartJLeF, GridBagConstraints.HORIZONTAL, 0, 2, 1);
            GridBagLayoutManager(dialog, butMotifJLeF, GridBagConstraints.HORIZONTAL, 0, 3, 1);
            GridBagLayoutManager(dialog, butClose, GridBagConstraints.CENTER, 0, 4, 1);
            butNativeJLeF.addActionListener(e -> {
                dialog.dispose();
                WindowUtilities.setNativeLookAndFeel();
                showFrame();
            });
            butStandartJLeF.addActionListener(e -> {
                dialog.dispose();
                WindowUtilities.setJavaLookAndFeel();
                showFrame();
            });
            butMotifJLeF.addActionListener(e -> {
                dialog.dispose();
                WindowUtilities.setMotifLookAndFeel();
                showFrame();
            });
            butClose.addActionListener(e -> {
                dialog.dispose();
            });

            dialog.pack();
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
            JLabel labZver = new JLabel("ZVER - Иван Кокорев                 (https://vk.com/vanian98)");
            JLabel labAlex = new JLabel("ALEX - Александр Василенко    (https://vk.com/aleksandr_vasilenko)");
            JLabel labVersion = new JLabel(Client.version);
            JButton butCl = new JButton("Закрыть");

            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setLayout(new GridBagLayout());

            labZver.setFocusable(true);

            dialog.getContentPane().setBackground(Color.DARK_GRAY);
            text.setForeground(Color.WHITE);
            label.setFont(new Font(FONT_style, Font.BOLD, 20));
            label.setForeground(MAIN_COLOR);
            creatorsLab.setForeground(new Color(106, 135, 89));
            labAlex.setForeground(new Color(255, 100, 25));
            labZver.setForeground(new Color(255, 100, 25));
            labVersion.setForeground(new Color(37, 37, 37));

            GridBagLayoutManager(dialog, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(dialog, empty, GridBagConstraints.HORIZONTAL, 0, 1, 2);
            GridBagLayoutManager(dialog, text, GridBagConstraints.CENTER, 0, 2, 2);
            GridBagLayoutManager(dialog, empty, GridBagConstraints.HORIZONTAL, 0, 3, 2);
            GridBagLayoutManager(dialog, creatorsLab, GridBagConstraints.CENTER, 0, 4, 2);
            GridBagLayoutManager(dialog, labZver, GridBagConstraints.HORIZONTAL, 0, 6, 2);
            GridBagLayoutManager(dialog, labAlex, GridBagConstraints.HORIZONTAL, 0, 7, 2);
            GridBagLayoutManager(dialog, empty, GridBagConstraints.HORIZONTAL, 0, 8, 2);
            GridBagLayoutManager(dialog, butCl, GridBagConstraints.PAGE_END, 1, 9, 1);
            GridBagLayoutManager(dialog, labVersion,GridBagConstraints.CENTER, 1, 10, 1);
            labVersion.setBounds(0, 0, labVersion.getWidth(), labVersion.getHeight());

            dialog.setSize(522, 199);
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

            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.setLayout(new GridBagLayout());

            GridBagLayoutManager(frame, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(frame, friendInf, GridBagConstraints.CENTER, 0, 1, 2);
            GridBagLayoutManager(frame, butRefuse, GridBagConstraints.HORIZONTAL, 0, 2, 1);
            GridBagLayoutManager(frame, butAccept, GridBagConstraints.HORIZONTAL, 1, 2, 1);

            butAccept.addActionListener(e -> {
                Client.resOfFriend("yes", friend.id);
                frame.dispose();
            });
            butRefuse.addActionListener(e -> {
                Client.resOfFriend("no", friend.id);
                frame.dispose();
            });

            frame.pack();
            frame.setAlwaysOnTop(true);
            frame.setResizable(false);
            frame.getContentPane().setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight()+20);
            frame.setSize(frame.getWidth()+25, frame.getHeight()+25);
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
        MainMenuFrame.showFrame();
    }

    private static class WindowUtilities {

        /** Tell system to use native look and feel, as in previous
         *  releases. Metal (Java) LAF is the default otherwise.
         */


        //is used in current application
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
                System.err.println("Error setting Motif LAF: " + e);
            }
        }
    }
}
