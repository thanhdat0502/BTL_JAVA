import service.AppService;
import utils.AppTheme;
import utils.ProjectPaths;
import view.LoginFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args){AppTheme.install();SwingUtilities.invokeLater(()->{try{AppService service=new AppService(ProjectPaths.dataDirectory());new LoginFrame(service).setVisible(true);}catch(Exception e){JOptionPane.showMessageDialog(null,"Không thể khởi động ứng dụng: "+e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);e.printStackTrace();}});}
}
