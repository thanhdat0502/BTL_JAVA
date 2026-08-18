package utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class AppTheme {
    public static final Color BACKGROUND=new Color(0xF5F7FB), SIDEBAR=new Color(0x1E293B), PRIMARY=new Color(0x3B82F6), SUCCESS=new Color(0x22C55E), DANGER=new Color(0xEF4444), TEXT=new Color(0x1F2937), MUTED=new Color(0x64748B), BORDER=new Color(0xE2E8F0);
    public static final String FONT_FAMILY=chooseVietnameseFont();
    public static final Font FONT=new FontUIResource(FONT_FAMILY,Font.PLAIN,14);
    private AppTheme(){}

    private static String chooseVietnameseFont(){
        String vietnamese="ăâđêôơưĂÂĐÊÔƠƯáàảãạếềểễệốồổỗộớờởỡợứừửữự";
        String[] candidates={"Segoe UI","Arial","Tahoma","Noto Sans","DejaVu Sans",Font.SANS_SERIF};
        for(String family:candidates){
            Font font=new Font(family,Font.PLAIN,14);
            if(font.canDisplayUpTo(vietnamese)==-1)return family;
        }
        return Font.SANS_SERIF;
    }

    public static void install(){
        FontUIResource plain=new FontUIResource(FONT_FAMILY,Font.PLAIN,14);
        FontUIResource bold=new FontUIResource(FONT_FAMILY,Font.BOLD,14);
        String[] plainKeys={
            "Button.font","CheckBox.font","ColorChooser.font","ComboBox.font",
            "DesktopIcon.font","EditorPane.font","FormattedTextField.font","Label.font",
            "List.font","Menu.font","MenuBar.font","MenuItem.font","OptionPane.font",
            "OptionPane.buttonFont","OptionPane.messageFont","Panel.font","PasswordField.font",
            "PopupMenu.font","ProgressBar.font","RadioButton.font","ScrollPane.font",
            "Spinner.font","TabbedPane.font","Table.font","TextArea.font","TextField.font",
            "TextPane.font","ToggleButton.font","ToolBar.font","ToolTip.font","Tree.font",
            "Viewport.font"
        };
        for(String key:plainKeys)UIManager.put(key,plain);
        UIManager.put("TableHeader.font",bold);
        UIManager.put("TitledBorder.font",bold);
        UIManager.put("InternalFrame.titleFont",bold);
    }
    public static void styleTable(JTable table){table.setRowHeight(34);table.setShowVerticalLines(false);table.setGridColor(BORDER);table.setSelectionBackground(new Color(0xDBEAFE));table.setSelectionForeground(TEXT);table.setFont(FONT);JTableHeader h=table.getTableHeader();h.setPreferredSize(new Dimension(0,38));h.setBackground(new Color(0xE2E8F0));h.setForeground(TEXT);h.setReorderingAllowed(false);}
}
