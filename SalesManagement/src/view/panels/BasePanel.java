package view.panels;

import utils.AppTheme;
import view.components.RoundedPanel;
import view.components.UiFactory;
import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected BasePanel(String title){setLayout(new BorderLayout(0,18));setBackground(AppTheme.BACKGROUND);setBorder(BorderFactory.createEmptyBorder(24,26,24,26));add(UiFactory.title(title),BorderLayout.NORTH);}
    protected JPanel formGrid(){JPanel p=new JPanel(new GridBagLayout());p.setOpaque(false);return p;}
    protected void addField(JPanel p,GridBagConstraints g,String label,JComponent component,int col,int row){g.gridx=col*2;g.gridy=row;g.weightx=0;g.fill=GridBagConstraints.NONE;g.insets=new Insets(5,5,5,8);p.add(new JLabel(label),g);g.gridx=col*2+1;g.weightx=1;g.fill=GridBagConstraints.HORIZONTAL;p.add(component,g);}
    protected RoundedPanel card(){RoundedPanel p=new RoundedPanel(Color.WHITE);p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));return p;}
    public abstract void refreshData();
}
