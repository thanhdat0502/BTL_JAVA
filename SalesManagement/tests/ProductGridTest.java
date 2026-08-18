import service.AppService;
import view.components.ProductCard;
import view.panels.SalesPanel;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.UUID;

public class ProductGridTest {
    public static void main(String[] args)throws Exception{
        System.setProperty("java.awt.headless","true");
        final AppService service=new AppService(Paths.get("build","grid-test-data-"+UUID.randomUUID()));
        final Throwable[] failure=new Throwable[1];
        SwingUtilities.invokeAndWait(()->{
            try{
                SalesPanel panel=new SalesPanel(service);
                JPanel grid=(JPanel)field(panel,"productGrid");
                check(grid.getComponentCount()>0,"lưới sản phẩm rỗng");
                JPanel firstRow=(JPanel)grid.getComponent(0);
                check(firstRow.getComponentCount()==2,"mỗi hàng phải có 2 cột để hiển thị đủ thông tin");
                check(firstRow.getComponent(0) instanceof ProductCard,"sản phẩm không hiển thị dạng card");
            }catch(Throwable error){failure[0]=error;}
        });
        if(failure[0]!=null)throw new RuntimeException(failure[0]);
        System.out.println("PRODUCT_GRID_TEST_OK");
    }

    private static Object field(Object target,String name)throws Exception{
        Field field=target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void check(boolean condition,String name){
        if(!condition)throw new AssertionError("Sai giao diện sản phẩm: "+name);
    }
}
