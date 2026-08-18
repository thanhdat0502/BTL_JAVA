import model.Product;
import service.AppService;
import view.panels.ProductPanel;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;

public class ProductSortTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless","true");
        final AppService service=new AppService(Paths.get("build","sort-test-data-"+UUID.randomUUID()));
        final Throwable[] failure=new Throwable[1];

        SwingUtilities.invokeAndWait(()->{
            try{
                ProductPanel panel=new ProductPanel(service);
                JComboBox<?> sort=(JComboBox<?>)field(panel,"sort");
                JTable table=(JTable)field(panel,"table");
                check(sort.getParent()!=null,"ô sắp xếp chưa được gắn vào giao diện");
                check(sort.getParent().getComponentCount()==2,"thanh bộ lọc phải có hai combobox");

                sort.setSelectedItem("Giá tăng dần");
                Product cheapest=service.products().stream().min(Comparator.comparingLong(Product::getPrice)).get();
                check(cheapest.getId().equals(table.getValueAt(0,1)),"giá tăng dần");

                sort.setSelectedItem("Giá giảm dần");
                Product mostExpensive=service.products().stream().max(Comparator.comparingLong(Product::getPrice)).get();
                check(mostExpensive.getId().equals(table.getValueAt(0,1)),"giá giảm dần");

                sort.setSelectedItem("Tồn kho tăng dần");
                Product lowestStock=service.products().stream().min(Comparator.comparingInt(Product::getQuantity)).get();
                check(lowestStock.getId().equals(table.getValueAt(0,1)),"tồn kho tăng dần");

                sort.setSelectedItem("Tồn kho giảm dần");
                Product highestStock=service.products().stream().max(Comparator.comparingInt(Product::getQuantity)).get();
                check(highestStock.getId().equals(table.getValueAt(0,1)),"tồn kho giảm dần");
            }catch(Throwable error){failure[0]=error;}
        });

        if(failure[0]!=null)throw new RuntimeException(failure[0]);
        System.out.println("PRODUCT_SORT_TEST_OK");
    }

    private static Object field(Object target,String name) throws Exception {
        Field field=target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void check(boolean condition,String name){
        if(!condition)throw new AssertionError("Sắp xếp sai: "+name);
    }
}
