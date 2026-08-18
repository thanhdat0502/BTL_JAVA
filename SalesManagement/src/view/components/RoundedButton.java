package view.components;

import utils.AppTheme;
import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private Color normal,hover;
    public RoundedButton(String text,Color color){super(text);normal=color;hover=color.brighter();setForeground(Color.WHITE);setFont(AppTheme.FONT.deriveFont(Font.BOLD));setBorder(BorderFactory.createEmptyBorder(10,18,10,18));setContentAreaFilled(false);setFocusPainted(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));addMouseListener(new java.awt.event.MouseAdapter(){public void mouseEntered(java.awt.event.MouseEvent e){normal=hover;repaint();}public void mouseExited(java.awt.event.MouseEvent e){normal=color;repaint();}});}
    @Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(normal);g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);g2.dispose();super.paintComponent(g);}
}
