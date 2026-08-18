import model.Product;
import service.AppService;
import utils.ImageUtils;
import view.panels.ProductPanel;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.UUID;

public class ProductImageTest {
    public static void main(String[] args)throws Exception{
        System.setProperty("java.awt.headless","true");
        final java.nio.file.Path data=Paths.get("build","image-test-data-"+UUID.randomUUID());
        AppService service=new AppService(data);
        Product original=service.products().get(0);
        Product updated=new Product(original.getId(),original.getName(),original.getCategoryId(),
            original.getPrice(),original.getQuantity(),original.getDescription(),"assets/products/sample.png");
        service.updateProduct(updated);

        final AppService reloaded=new AppService(data);
        check("assets/products/sample.png".equals(reloaded.products().get(0).getImagePath()),"lưu đường dẫn ảnh");
        ImageIcon placeholder=ImageUtils.thumbnail("",54,48);
        check(placeholder.getIconWidth()==54&&placeholder.getIconHeight()==48,"ảnh mặc định");

        final Throwable[] failure=new Throwable[1];
        SwingUtilities.invokeAndWait(()->{
            try{
                ProductPanel panel=new ProductPanel(reloaded);
                JTable table=(JTable)field(panel,"table");
                check(table.getValueAt(0,0) instanceof ImageIcon,"thumbnail trong bảng");
                check(table.getColumnCount()==8,"cột cập nhật ảnh");
                check("Tải ảnh".equals(table.getValueAt(0,7)),"nút tải ảnh theo sản phẩm");
                try{
                    panel.getClass().getDeclaredField("imagePreview");
                    throw new AssertionError("khung ảnh phía trên vẫn còn");
                }catch(NoSuchFieldException expected){/* đúng: khung ảnh đã được bỏ */}
            }catch(Throwable error){failure[0]=error;}
        });
        if(failure[0]!=null)throw new RuntimeException(failure[0]);
        System.out.println("PRODUCT_IMAGE_TEST_OK");
    }

    private static Object field(Object target,String name)throws Exception{
        Field field=target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static void check(boolean condition,String name){
        if(!condition)throw new AssertionError("Sai chức năng ảnh: "+name);
    }
}
