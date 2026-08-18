package view.components;

import utils.AppTheme;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public final class UiFactory {
    private UiFactory(){}
    public static JLabel title(String text){JLabel l=new JLabel(text);l.setFont(AppTheme.FONT.deriveFont(Font.BOLD,26));l.setForeground(AppTheme.TEXT);return l;}
    public static JTextField field(){JTextField f=new JTextField();f.setPreferredSize(new Dimension(160,38));f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER),BorderFactory.createEmptyBorder(6,10,6,10)));return f;}
    public static DefaultTableModel model(String... columns){return new DefaultTableModel(columns,0){@Override public boolean isCellEditable(int r,int c){return false;}};}
    public static DefaultTableModel imageModel(String... columns){return new DefaultTableModel(columns,0){@Override public boolean isCellEditable(int r,int c){return false;}@Override public Class<?> getColumnClass(int column){return column==0?ImageIcon.class:Object.class;}};}
    public static JScrollPane scroll(JTable table){AppTheme.styleTable(table);JScrollPane s=new JScrollPane(table);s.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));s.getViewport().setBackground(Color.WHITE);return s;}
    public static void onChange(JTextField field,Runnable action){field.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){action.run();}public void removeUpdate(DocumentEvent e){action.run();}public void changedUpdate(DocumentEvent e){action.run();}});}
    public static void error(Component parent,Exception e){JOptionPane.showMessageDialog(parent,e.getMessage()==null?"Đã xảy ra lỗi. Vui lòng thử lại.":e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}
    public static void success(Component parent,String text){JOptionPane.showMessageDialog(parent,text,"Thành công",JOptionPane.INFORMATION_MESSAGE);}
}
