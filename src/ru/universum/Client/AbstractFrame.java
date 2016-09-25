package ru.universum.Client;

import java.awt.*;

abstract class AbstractFrame {
    abstract public void initial();
    abstract public void showFrame();
    abstract public void hideFrame();
    abstract public void dispose();
    abstract public void setInfo(String message, Color color);
}
