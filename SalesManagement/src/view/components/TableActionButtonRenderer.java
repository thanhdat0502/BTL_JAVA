package view.components;

import utils.AppTheme;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class TableActionButtonRenderer extends JButton implements TableCellRenderer {
    public TableActionButtonRenderer(){
        setOpaque(true);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(AppTheme.PRIMARY);
        setForeground(Color.WHITE);
        setFont(AppTheme.FONT.deriveFont(Font.BOLD,12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,boolean selected,
                                                   boolean focused,int row,int column){
        setText(value==null?"Tải ảnh":value.toString());
        return this;
    }
}
