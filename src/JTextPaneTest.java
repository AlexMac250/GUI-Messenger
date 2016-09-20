import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.text.*;

public class JTextPaneTest extends JFrame
{
    // Текстовый редактор
    private  JTextPane textEditor = null;
    // Стили редактора
    private  Style     heading    = null; // стиль заголовка
    private  Style     normal     = null; // стиль текста

    private  final  String      STYLE_heading = "heading",
            STYLE_normal  = "normal" ,
            FONT_style    = "Times New Roman";
    private  final  String[][]  TEXT = {
            {"Компонент JTextPane \r\n"                           , "heading"},
            {"\r\n"                                               , "normal" },
            {"JTextPane незаменим при создании в приложении  \r\n", "normal" },
            {"многофункционального текстового редактора.\r\n"     , "normal" },
            {"\r\n"                                               , "normal" },
            {"Он позволяет вставлять в документ визуальные \r\n"  , "normal" },
            {"компоненты типа JCheckBox и JRadioButton.\r\n "     , "normal" }};
    public JTextPaneTest()
    {
        super("Пример редактора JTextPane");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Создание редактора
        textEditor = new JTextPane();
        // Определение стилей редактора
        createStyles(textEditor);
        // Загрузка документа
        loadText(textEditor);
        changeDocumentStyle(textEditor);
        // Размещение редактора в панели содержимого
        getContentPane().add(new JScrollPane(textEditor));
        // Открытие окна
        setSize(380, 240);
        setVisible(true);
    }
    /**
     * Процедура формирования стилей редактора
     * @param editor редактор
     */
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
    /**
     * Процедура загрузки текста в редактор
     * @param editor редактор
     */
    private void loadText(JTextPane editor)
    {
        // Загружаем в документ содержимое
        for (int i = 0; i < TEXT.length; i++) {
            Style style = (TEXT[i][1].equals(STYLE_heading)) ? heading : normal;
            insertText(editor, TEXT[i][0], style);
        }
        // Размещение компонента в конце текста
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
    }
    /**
     * Процедура изменения стиля документа
     * @param editor редактор
     */
    private void changeDocumentStyle(JTextPane editor)
    {
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
    private void insertText(JTextPane editor, String string, Style style)
    {
        try {
            Document doc = editor.getDocument();
            doc.insertString(doc.getLength(), string, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        new JTextPaneTest();
    }
}