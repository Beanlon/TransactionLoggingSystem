package utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NoSpaceKeyListener extends KeyAdapter {
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == ' ') {
            e.consume(); // Prevent space input
        }
    }
}