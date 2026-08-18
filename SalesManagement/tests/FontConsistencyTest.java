import utils.AppTheme;
import javax.swing.UIManager;
import java.awt.Font;

public class FontConsistencyTest {
    public static void main(String[] args){
        AppTheme.install();
        String[] keys={"Label.font","Button.font","TextField.font","PasswordField.font","ComboBox.font","CheckBox.font","Table.font","TableHeader.font","OptionPane.messageFont"};
        for(String key:keys){
            Font font=UIManager.getFont(key);
            if(font==null||!font.getFamily().equals(AppTheme.FONT.getFamily()))throw new AssertionError("Font không đồng nhất: "+key);
        }
        String vietnamese="Tiếng Việt: Quản lý sản phẩm, hóa đơn, khách hàng";
        if(AppTheme.FONT.canDisplayUpTo(vietnamese)!=-1)throw new AssertionError("Font không hỗ trợ đủ tiếng Việt");
        System.out.println("FONT_CONSISTENCY_TEST_OK: "+AppTheme.FONT.getFamily());
    }
}
