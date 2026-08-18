package view.components;

import utils.AppTheme;
import javax.swing.*;
import java.awt.*;

public class SidebarButton extends JButton {
    private boolean active;
    public SidebarButton(String text){super(text);setHorizontalAlignment(SwingConstants.LEFT);setForeground(new Color(0xCBD5E1));setFont(AppTheme.FONT.deriveFont(Font.BOLD));setBorder(BorderFactory.createEmptyBorder(12,22,12,12));setMaximumSize(new Dimension(Integer.MAX_VALUE,48));setContentAreaFilled(false);setFocusPainted(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));addMouseListener(new java.awt.event.MouseAdapter(){public void mouseEntered(java.awt.event.MouseEvent e){if(!active)setForeground(Color.WHITE);}public void mouseExited(java.awt.event.MouseEvent e){if(!active)setForeground(new Color(0xCBD5E1));}});}
    public void setActive(boolean active){this.active=active;setForeground(active?Color.WHITE:new Color(0xCBD5E1));repaint();}
    @Override protected void paintComponent(Graphics g){if(active){Graphics2D g2=(Graphics2D)g.create();g2.setColor(AppTheme.PRIMARY);g2.fillRoundRect(8,3,getWidth()-16,getHeight()-6,12,12);g2.dispose();}super.paintComponent(g);}
}
