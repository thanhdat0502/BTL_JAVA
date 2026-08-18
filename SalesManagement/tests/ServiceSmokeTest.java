import model.*;
import service.AppService;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ServiceSmokeTest {
    public static void main(String[] args) throws Exception {
        Path data=Paths.get("build","test-data-"+UUID.randomUUID());
        AppService service=new AppService(data);
        check(service.categories().size()==3,"demo categories");
        check(service.products().size()==10,"demo products");
        check(service.customers().size()==5,"demo customers");
        check(service.login("admin","123456").isPresent(),"login");
        Product p=service.products().get(0);int stock=p.getQuantity();
        Invoice invoice=service.checkout(Collections.singletonList(new CartItem(p,2)),service.customers().get(0).getId(),service.employees().get(0).getId());
        check(p.getQuantity()==stock-2,"stock subtraction");
        check(service.invoices().size()==1&&service.invoiceDetails().size()==1,"invoice saved");
        AppService reloaded=new AppService(data);
        check(reloaded.invoices().stream().anyMatch(x->x.getId().equals(invoice.getId())),"invoice reload");
        check(reloaded.products().get(0).getQuantity()==stock-2,"stock reload");
        boolean categoryRejected=false;
        try{reloaded.deleteCategory(reloaded.products().get(0).getCategoryId());}
        catch(IllegalArgumentException e){categoryRejected=true;}
        check(categoryRejected,"category in use validation");
        boolean rejected=false;try{reloaded.checkout(Collections.singletonList(new CartItem(reloaded.products().get(0),9999)),reloaded.customers().get(0).getId(),reloaded.employees().get(0).getId());}catch(IllegalArgumentException e){rejected=true;}
        check(rejected,"overselling validation");
        Files.write(data.resolve("products.txt"),Collections.singletonList("DONG|BI|LOI"),StandardCharsets.UTF_8,StandardOpenOption.APPEND);
        AppService tolerant=new AppService(data);
        check(tolerant.products().size()==10,"malformed line is ignored");
        System.out.println("SERVICE_SMOKE_TEST_OK");
    }
    private static void check(boolean ok,String name){if(!ok)throw new AssertionError("Failed: "+name);}
}
