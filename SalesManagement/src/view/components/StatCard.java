package view.components;

import utils.AppTheme;
import javax.swing.*;
import java.awt.*;

public class StatCard extends RoundedPanel {
    private final JLabel value=new JLabel();
    public StatCard(String title,Color accent){super(Color.WHITE);setLayout(new BorderLayout());setBorder(BorderFactory.createEmptyBorder(18,20,18,20));JLabel t=new JLabel(title);t.setForeground(AppTheme.MUTED);t.setFont(AppTheme.FONT);value.setForeground(accent);value.setFont(AppTheme.FONT.deriveFont(Font.BOLD,24));add(t,BorderLayout.NORTH);add(value,BorderLayout.CENTER);}
    public void setValue(String text){value.setText(text);}
}
