package ru.universum.Client;

import ru.universum.Loader.Friend;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

class Frames {
    private final String STYLE_heading = "heading", STYLE_normal  = "normal" , FONT_style    = "Trebuchet MS";

    MainMenuFrame MainMenuFrame = new MainMenuFrame();
    LoginFrame LoginFrame = new LoginFrame();
    RegisterFrame RegisterFrame = new RegisterFrame();
    AboutFrame AboutFrame = new AboutFrame();
    MainFrame MainFrame = new MainFrame();

    private class MainMenuFrame extends AbsFrame {
        private JFrame frame;
        private JButton butLogin;
        private JButton butRegister;
        private JButton butAbout;
        private JLabel label;
        private JLabel info;


        MainMenuFrame() {
            initial();
        }

        @Override
        public void initial() {
            frame = new JFrame("NEOnline");
            butLogin = new JButton("Войти");
            butRegister = new JButton("Регистрация");
            butAbout = new JButton("О NEOnline");
            label = new JLabel("NEOnline");
            info = new JLabel("");

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new GridBagLayout());

            GridBagLayoutManager(frame, label, GridBagConstraints.NORTH, 0, 0, 1);
            GridBagLayoutManager(frame, butLogin, GridBagConstraints.HORIZONTAL, 0, 1 ,1);
            GridBagLayoutManager(frame, butRegister, GridBagConstraints.HORIZONTAL, 0, 2, 1);
            GridBagLayoutManager(frame, butAbout, GridBagConstraints.HORIZONTAL, 0, 3, 1);

            frame.setSize(150, 150);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            butLogin.addActionListener(e -> {
                LoginFrame.showFrame();

            });
            butRegister.addActionListener(e -> {
                if (!Client.statusConnected)Client.connect();
                RegisterFrame.showFrame();
            });
            butAbout.addActionListener(e -> AboutFrame.showFrame());
        }

        @Override
        public void showFrame() {
            frame.setVisible(true);
        }

        @Override
        public void hideFrame() {
            frame.setVisible(false);
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

    class LoginFrame extends AbsFrame {

        private JFrame frame;
        private JPanel panLogin;
        private JPanel panBut;
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
            frame = new JFrame("Войти");
            panLogin = new JPanel();
            panBut = new JPanel();
            loginField = new JTextField(15);
            passwordField = new JPasswordField(15);
            butLogin = new JButton("Войти");
            checkBox = new JCheckBox("Запомнить");
            info = new JLabel();
            label = new JLabel("Введите логин и пароль");

            loginField.setToolTipText("ЛОГИН");
            passwordField.setToolTipText("ПАРОЛЬ");

//            frame.add(panLogin, BorderLayout.CENTER);
//            frame.add(panBut, BorderLayout.SOUTH);
            frame.setLayout(new GridBagLayout());
            GridBagLayoutManager(frame, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(frame, loginField, GridBagConstraints.HORIZONTAL, 0, 1, 2);
            GridBagLayoutManager(frame, passwordField, GridBagConstraints.HORIZONTAL, 0, 2, 2);
            GridBagLayoutManager(frame, info, GridBagConstraints.CENTER, 0, 3, 2);
            GridBagLayoutManager(frame, checkBox, GridBagConstraints.CENTER, 0, 4, 1);
            GridBagLayoutManager(frame, butLogin, GridBagConstraints.CENTER, 1, 4, 1);
//            panLogin.add(label);
//            panLogin.add(loginField);
//            panLogin.add(passwordField);
//            panLogin.add(info);
//            panBut.add(checkBox);
//            panBut.add(butLogin);

            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            frame.setSize(200, 149);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            butLogin.addActionListener(e -> {
                setInfo("Входим...", Color.ORANGE);
                if (!Client.statusConnected) Client.connect();
                Client.login(loginField.getText(), passwordField.getText());
            });
        }

        @Override
        public void showFrame() {
            frame.setVisible(true);
        }

        @Override
        public void hideFrame() {
            frame.setVisible(false);
        }

        @Override
        public void dispose() {
            frame.dispose();
            MainMenuFrame.dispose();
        }

        @Override
        public synchronized void setInfo(String message, Color color) {
            info.setForeground(color);
            info.setText(message);
        }
    }

    class MainFrame extends AbsFrame{

        private Style heading = null; // стиль заголовка
        private Style normal  = null; // стиль текста
        private  final  String[][]  TEXT = {
                {"                                                                                              ", "heading"},
                {"\r\n                                               "                                           , "normal" },
                {"\nВыберете диалог"                                                                             , "heading" },
                {"\r\n                                               "                                           , "normal" },
                {"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" },{"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" },{"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" }, {"\n", "normal" }};
                // FIXME: 30.08.16 ПОФИКСИТЬ КОСТЫЛЬ
        private JFrame frame;
        private Container contentPain;
        public JPanel panFriends;
        private JPanel panMainContent;
        private JPanel panMessages;
        private JPanel panSendMessage;
        private JButton butSendMessage;
        private JTextPane MessageBox;
        private JTextField textField;
        private JScrollPane scrollFriends;
        private JScrollPane scrollMessage;

        public MainFrame() {
        }

        @Override
        public void initial() {
            frame = new JFrame("NEOnline - Сообщения (v0.1 beta 1)");
            frame.setSize(701, 406);
            frame.setResizable(false);
            contentPain = frame.getContentPane();
            panFriends = new JPanel();
            panMainContent = new JPanel();
            panMessages = new JPanel();
            panSendMessage = new JPanel();
            butSendMessage = new JButton("Отправить");
            MessageBox = new JTextPane(){
                @Override
                public void setSize(Dimension d) {
                    if(d.width <= getParent().getSize().width){
                        d.width = getParent().getSize().width;
                    }
                    super.setSize(d);
                }
                public boolean getScrollableTracksViewportWidth() {
                    return false;
                }
            };
            textField = new JTextField(30);
            scrollFriends = new JScrollPane(panFriends);
            scrollMessage = new JScrollPane();


            scrollMessage.createVerticalScrollBar();


            contentPain.add(panMainContent,BorderLayout.CENTER);
            panMainContent.add(scrollMessage);

            scrollMessage.getViewport().add(MessageBox);
            scrollMessage.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            panMainContent.add(panSendMessage, BorderLayout.SOUTH);

            MessageBox.setMinimumSize(new Dimension(300, 336));
            MessageBox.setSize(new Dimension(445, 336));

            panSendMessage.setLayout(new FlowLayout());
            panSendMessage.add(textField);
            panSendMessage.add(butSendMessage);
            contentPain.add(scrollFriends, BorderLayout.WEST);

            panSendMessage.setBackground(Color.GRAY);
            panMainContent.setBackground(Color.GRAY);
            scrollMessage.setBackground(Color.GRAY);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            createStyles(MessageBox);
            loadText(MessageBox);
            loadFriends();


            frame.setLocationRelativeTo(null);

            butSendMessage.addActionListener(e -> {
            });
        }

        @Override
        public void showFrame() {
            initial();
            frame.setVisible(true);
        }

        @Override
        public void hideFrame() {
            frame.setVisible(false);
        }

        @Override
        public void dispose() {
            frame.dispose();
        }

        @Override
        public void setInfo(String message, Color color) {
            final JFrame frMess = new JFrame("СООБЩЕНИЕ!");
            final JPanel panel = new JPanel();
            final JPanel panel1 = new JPanel();
            final JLabel text = new JLabel(message);
            final JButton butOK = new JButton("Закрыть");
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

        private void createStyles(JTextPane editor) {
            // Создание стилей
            normal = editor.addStyle(STYLE_normal, null);
            StyleConstants.setFontFamily(normal, FONT_style);
            StyleConstants.setFontSize(normal, 16);
            // Наследуем свойстdо FontFamily
            heading = editor.addStyle(STYLE_heading, normal);
            StyleConstants.setFontSize(heading, 18);
            StyleConstants.setBold(heading, true);
        }

        private void changeDocumentStyle(JTextPane editor) {
            // Изменение стиля части текста
            SimpleAttributeSet blue = new SimpleAttributeSet();
            StyleConstants.setForeground(blue, Color.blue);
            StyledDocument doc = editor.getStyledDocument();
            doc.setCharacterAttributes(10, 9, blue, false);
        }

        /**
         * Процедура добавления в редактор строки определенного стиля
         * @param editor редактор
         * @param string строка
         * @param style стиль
         */
        private void insertText(JTextPane editor, String string, Style style) {
            try {
                Document doc = editor.getDocument();
                doc.insertString(doc.getLength(), string, style);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void loadText(JTextPane editor) {
            // Загружаем в документ содержимое
            for (int i = 0; i < TEXT.length; i++) {
                Style style = (TEXT[i][1].equals(STYLE_heading)) ? heading : normal;
                insertText(editor, TEXT[i][0], style);
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
            editor.insertComponent(radio);*/

        }

        public void loadFriends(){
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            panFriends.setLayout(new GridBagLayout());
            panFriends.setSize(panFriends.getWidth()+50,panFriends.getHeight());
            scrollFriends.createVerticalScrollBar();
            scrollFriends.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollFriends.setMaximumSize(new Dimension(136, 391));
            panFriends.setBackground(Color.DARK_GRAY);

            final int countFriends = Client.account.friends.size();
            GridBagConstraints c = new GridBagConstraints();

            {
                JLabel label = new JLabel("Друзья");
                label.setForeground(Color.WHITE);
                c.fill = GridBagConstraints.CENTER;
                c.gridx = 1;
                c.gridy = 0;
                panFriends.add(label, c);
            }

            for (int i = 0; i < countFriends; i++) {
                String login = Client.account.friends.get(i).login;
                int max = 12;
                JButton button = new JButton();
                if (login.length() > max) {
                    button.setToolTipText(login);
                    login = login.substring(0, max - 1) + "...";
                }
                button.setText(login);
                JCheckBox checkBoxOnline = new JCheckBox("", Client.account.friends.get(i).isOnline);
                checkBoxOnline.setEnabled(false);

                if (checkBoxOnline.isSelected()) checkBoxOnline.setToolTipText("Online");
                else checkBoxOnline.setToolTipText("Offline");

                c.fill = GridBagConstraints.CENTER;
                c.gridx = 0;
                c.gridy = i+1;
                panFriends.add(checkBoxOnline, c);

                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = i+1;
                panFriends.add(button, c);
                if (countFriends > 13) {
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 2;
                    c.gridy = i+1;
                    panFriends.add(new JLabel("    "), c);
                }
                button.addActionListener(e -> System.out.println(frame.getSize().toString()));
            }
            //panFriends.setSize(600, panFriends.getHeight());
        }
    }

    class RegisterFrame extends AbsFrame{

        public RegisterFrame() {
            initial();
        }

        JFrame frame;
        JLabel label;
        JLabel labLogin;
        JLabel labPassword;
        JLabel labRPassword;
        JLabel info;
        JTextField loginField;
        JPasswordField passwordField;
        JPasswordField passwordField2;
        JButton registerBut;

        @Override
        public void initial() {
            frame = new JFrame("NEOnline - Регистрация");
            label = new JLabel("Регистрация");
            labLogin = new JLabel("Логин:");
            labPassword = new JLabel("Пароль:");
            labRPassword = new JLabel("Повторите пароль:");
            loginField = new JTextField(15);
            passwordField = new JPasswordField(15);
            passwordField2 = new JPasswordField(15);
            info = new JLabel("");
            registerBut = new JButton("Зарегистрироваться!");

            frame.setLayout(new GridBagLayout());
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            buildFrame();

            frame.pack();
            frame.setSize(frame.getWidth()+50,frame.getHeight()+20);
            frame.setLocationRelativeTo(null);

            registerBut.addActionListener(e -> {        // FIXME: 30.08.16 ДОБАВИТЬ MD5 ШИФРОВАНИЕ
                    if (!passwordField.getText().contains("!") && !passwordField.getText().contains("@") && !passwordField.getText().contains("\"") && !passwordField.getText().contains("?")){
                        Client.register(loginField.getText(), passwordField.getText());
                    }
                }
            );
        }

        @Override
        public void showFrame() {
            frame.setVisible(true);
        }

        @Override
        public void hideFrame() {
            frame.setVisible(false);
        }

        @Override
        public void dispose() {
            frame.dispose();
        }

        @Override
        public void setInfo(String message, Color color) {

        }

        public void buildFrame(){
            GridBagLayoutManager(frame, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(frame, labLogin, GridBagConstraints.EAST, 0, 1, 1);
            GridBagLayoutManager(frame, loginField, GridBagConstraints.HORIZONTAL, 1, 1, 1);
            GridBagLayoutManager(frame, labPassword, GridBagConstraints.EAST, 0, 2, 1);
            GridBagLayoutManager(frame, passwordField,GridBagConstraints.HORIZONTAL, 1, 2, 1);
            GridBagLayoutManager(frame, labRPassword, GridBagConstraints.EAST, 0, 3, 1);
            GridBagLayoutManager(frame, passwordField2, GridBagConstraints.HORIZONTAL, 1, 3, 1);
            GridBagLayoutManager(frame, info, GridBagConstraints.CENTER, 0, 4, 2);
            GridBagLayoutManager(frame, registerBut, GridBagConstraints.HORIZONTAL, 0, 5, 2);
        }


    }

    //-------------------------------------------//

    class AboutFrame{

        public void showFrame() {
            JFrame frame = new JFrame("О NEOnline");
            JLabel label = new JLabel("NEOnline");
            JLabel empty= new JLabel(" ");
            JLabel text = new JLabel("Лучший бесплатный мессенджер!");
            JLabel creatorsLab = new JLabel("Создатели:");
            JLabel labZver = new JLabel("ZVER - Иван Кокорев                 (https://vk.com/vanian98)");
            JLabel labAlex = new JLabel("ALEX - Александр Василенко    (https://vk.com/aleksandr_vasilenko)");
            JButton butCl = new JButton("Закрыть");

            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.setLayout(new GridBagLayout());

            label.setFont(new Font(FONT_style, Font.BOLD, 20));
            label.setForeground(Color.DARK_GRAY);
            creatorsLab.setForeground(new Color(106, 135, 89));
            labAlex.setForeground(new Color(255, 100, 25));
            labZver.setForeground(new Color(255, 100, 25));

            GridBagLayoutManager(frame, label, GridBagConstraints.CENTER, 0, 0, 2);
            GridBagLayoutManager(frame, empty, GridBagConstraints.HORIZONTAL, 0, 1, 2);
            GridBagLayoutManager(frame, text, GridBagConstraints.CENTER, 0, 2, 2);
            GridBagLayoutManager(frame, empty, GridBagConstraints.HORIZONTAL, 0, 3, 2);
            GridBagLayoutManager(frame, creatorsLab, GridBagConstraints.CENTER, 0, 4, 2);
            GridBagLayoutManager(frame, labZver, GridBagConstraints.HORIZONTAL, 0, 6, 2);
            GridBagLayoutManager(frame, labAlex, GridBagConstraints.HORIZONTAL, 0, 7, 2);
            GridBagLayoutManager(frame, empty, GridBagConstraints.HORIZONTAL, 0, 8, 2);
            GridBagLayoutManager(frame, butCl, GridBagConstraints.PAGE_END, 1, 9, 1);

            frame.setSize(522, 199);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            butCl.addActionListener(e -> frame.dispose());
            frame.setVisible(true);
        }
    }

    class AddFriend{
        Friend friend;

        public AddFriend(Friend friend) {
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

            butRefuse.addActionListener(e -> frame.dispose());
            frame.pack();
            frame.setAlwaysOnTop(true);
            frame.setResizable(false);
            frame.getContentPane().setSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight()+20);
            frame.setSize(frame.getWidth()+25, frame.getHeight()+25);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    public void GridBagLayoutManager(JFrame frame, JComponent component, int fill, int gridX, int gridY, int gridWidth){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = fill;
        c.gridwidth = gridWidth;
        c.gridx = gridX;
        c.gridy = gridY;
        frame.add(component, c);
    }

    public void startGUI(){
        MainMenuFrame.showFrame();
    }
}
