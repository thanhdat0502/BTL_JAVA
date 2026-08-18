import service.AppService;
import view.panels.*;
import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class SwingPanelSmokeTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless","true");
        AppService service=new AppService(Paths.get("build","ui-test-data-"+UUID.randomUUID()));
        SwingUtilities.invokeAndWait(()->{
            BasePanel[] panels={new DashboardPanel(service),new ProductPanel(service),new CategoryPanel(service),new CustomerPanel(service),new EmployeePanel(service),new SalesPanel(service),new InvoicePanel(service),new StatisticPanel(service)};
            for(BasePanel panel:panels)panel.refreshData();
        });
        System.out.println("SWING_PANEL_SMOKE_TEST_OK");
    }
}
