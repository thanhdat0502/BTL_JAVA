package view.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private final Color color;
    public RoundedPanel(Color color){this.color=color;setOpaque(false);}
    @Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(0,0,0,14));g2.fillRoundRect(2,3,getWidth()-4,getHeight()-5,18,18);g2.setColor(color);g2.fillRoundRect(0,0,getWidth()-4,getHeight()-5,18,18);g2.dispose();super.paintComponent(g);}
}
