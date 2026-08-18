import model.CartItem;
import model.Customer;
import model.Employee;
import model.Product;
import service.AppService;
import view.panels.StatisticPanel;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;

public class RankingStatisticsTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless","true");
        final AppService service=new AppService(Paths.get("build","ranking-test-data-"+UUID.randomUUID()));
        Customer firstCustomer=service.customers().get(0);
        Customer secondCustomer=service.customers().get(1);
        Employee employee=service.employees().get(0);
        Product expensive=service.products().get(9);
        Product popular=service.products().get(0);

        service.checkout(Collections.singletonList(new CartItem(expensive,2)),firstCustomer.getId(),employee.getId());
        service.checkout(Collections.singletonList(new CartItem(popular,3)),secondCustomer.getId(),employee.getId());

        check(service.customerSpending().get(firstCustomer.getId())>service.customerSpending().get(secondCustomer.getId()),"tổng chi tiêu khách hàng");
        check(service.productSales().get(popular.getId())>service.productSales().get(expensive.getId()),"số lượng sản phẩm đã bán");

        final Throwable[] failure=new Throwable[1];
        SwingUtilities.invokeAndWait(()->{
            try{
                StatisticPanel panel=new StatisticPanel(service);
                DefaultTableModel customerModel=(DefaultTableModel)field(panel,"customerModel");
                DefaultTableModel productModel=(DefaultTableModel)field(panel,"productModel");
                check(firstCustomer.getId().equals(customerModel.getValueAt(0,1)),"xếp hạng khách hàng");
                check(popular.getId().equals(productModel.getValueAt(0,1)),"xếp hạng sản phẩm");
            }catch(Throwable error){failure[0]=error;}
        });
        if(failure[0]!=null)throw new RuntimeException(failure[0]);
        System.out.println("RANKING_STATISTICS_TEST_OK");
    }

    private static Object field(Object target,String name)throws Exception{
        Field field=target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void check(boolean condition,String name){
        if(!condition)throw new AssertionError("Sai thống kê: "+name);
    }
}
