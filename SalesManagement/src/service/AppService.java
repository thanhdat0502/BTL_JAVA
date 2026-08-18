package service;

import model.*;
import repository.FileDataRepository;
import utils.ValidationUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class AppService {
    private final FileDataRepository repo;
    public AppService(Path dataDir)throws IOException{repo=new FileDataRepository(dataDir);}
    public List<User> users(){return Collections.unmodifiableList(repo.getUsers());}
    public List<Product> products(){return Collections.unmodifiableList(repo.getProducts());}
    public List<Category> categories(){return Collections.unmodifiableList(repo.getCategories());}
    public List<Customer> customers(){return Collections.unmodifiableList(repo.getCustomers());}
    public List<Employee> employees(){return Collections.unmodifiableList(repo.getEmployees());}
    public List<Invoice> invoices(){return Collections.unmodifiableList(repo.getInvoices());}
    public List<InvoiceDetail> invoiceDetails(){return Collections.unmodifiableList(repo.getInvoiceDetails());}

    public Optional<User> login(String username,String password){return repo.getUsers().stream().filter(x->x.getUsername().equals(username)&&x.getPassword().equals(password)).findFirst();}
    private boolean existsId(Collection<?> list,String id){return list.stream().anyMatch(x->{if(x instanceof Product)return ((Product)x).getId().equalsIgnoreCase(id);if(x instanceof Category)return ((Category)x).getId().equalsIgnoreCase(id);if(x instanceof Customer)return ((Customer)x).getId().equalsIgnoreCase(id);if(x instanceof Employee)return ((Employee)x).getId().equalsIgnoreCase(id);return false;});}
    private <T> T find(List<T> list,Predicate<T> p,String message){return list.stream().filter(p).findFirst().orElseThrow(()->new IllegalArgumentException(message));}

    public void addProduct(Product x)throws IOException{validateProduct(x);if(existsId(repo.getProducts(),x.getId()))throw new IllegalArgumentException("Mã sản phẩm đã tồn tại.");repo.getProducts().add(x);repo.saveProducts();}
    public void updateProduct(Product x)throws IOException{validateProduct(x);Product old=find(repo.getProducts(),p->p.getId().equalsIgnoreCase(x.getId()),"Không tìm thấy sản phẩm.");int i=repo.getProducts().indexOf(old);repo.getProducts().set(i,x);try{repo.saveProducts();}catch(IOException e){repo.getProducts().set(i,old);throw e;}}
    public void deleteProduct(String id)throws IOException{Product x=find(repo.getProducts(),p->p.getId().equals(id),"Không tìm thấy sản phẩm.");repo.getProducts().remove(x);try{repo.saveProducts();}catch(IOException e){repo.getProducts().add(x);throw e;}}
    private void validateProduct(Product x){ValidationUtils.required(x.getId(),"Mã sản phẩm");ValidationUtils.required(x.getName(),"Tên sản phẩm");if(x.getPrice()<=0)throw new IllegalArgumentException("Giá bán phải lớn hơn 0.");if(x.getQuantity()<0)throw new IllegalArgumentException("Số lượng không được âm.");if(repo.getCategories().stream().noneMatch(c->c.getId().equals(x.getCategoryId())))throw new IllegalArgumentException("Phải chọn danh mục hợp lệ.");}

    public void addCategory(Category x)throws IOException{ValidationUtils.required(x.getId(),"Mã danh mục");ValidationUtils.required(x.getName(),"Tên danh mục");if(existsId(repo.getCategories(),x.getId()))throw new IllegalArgumentException("Mã danh mục đã tồn tại.");repo.getCategories().add(x);repo.saveCategories();}
    public void updateCategory(Category x)throws IOException{ValidationUtils.required(x.getName(),"Tên danh mục");Category old=find(repo.getCategories(),c->c.getId().equals(x.getId()),"Không tìm thấy danh mục.");int i=repo.getCategories().indexOf(old);repo.getCategories().set(i,x);try{repo.saveCategories();}catch(IOException e){repo.getCategories().set(i,old);throw e;}}
    public void deleteCategory(String id)throws IOException{if(repo.getProducts().stream().anyMatch(p->p.getCategoryId().equals(id)))throw new IllegalArgumentException("Không thể xóa: danh mục đang có sản phẩm sử dụng.");Category x=find(repo.getCategories(),c->c.getId().equals(id),"Không tìm thấy danh mục.");repo.getCategories().remove(x);try{repo.saveCategories();}catch(IOException e){repo.getCategories().add(x);throw e;}}

    private void validatePerson(String id,String name,String phone,String email){ValidationUtils.required(id,"Mã");ValidationUtils.required(name,"Họ tên");ValidationUtils.phone(phone);ValidationUtils.email(email);}
    public void addCustomer(Customer x)throws IOException{validatePerson(x.getId(),x.getName(),x.getPhone(),x.getEmail());if(existsId(repo.getCustomers(),x.getId()))throw new IllegalArgumentException("Mã khách hàng đã tồn tại.");repo.getCustomers().add(x);repo.saveCustomers();}
    public void updateCustomer(Customer x)throws IOException{validatePerson(x.getId(),x.getName(),x.getPhone(),x.getEmail());Customer old=find(repo.getCustomers(),v->v.getId().equals(x.getId()),"Không tìm thấy khách hàng.");int i=repo.getCustomers().indexOf(old);repo.getCustomers().set(i,x);try{repo.saveCustomers();}catch(IOException e){repo.getCustomers().set(i,old);throw e;}}
    public void deleteCustomer(String id)throws IOException{if(repo.getInvoices().stream().anyMatch(v->v.getCustomerId().equals(id)))throw new IllegalArgumentException("Không thể xóa khách hàng đã có hóa đơn.");Customer x=find(repo.getCustomers(),v->v.getId().equals(id),"Không tìm thấy khách hàng.");repo.getCustomers().remove(x);try{repo.saveCustomers();}catch(IOException e){repo.getCustomers().add(x);throw e;}}
    public void addEmployee(Employee x)throws IOException{validatePerson(x.getId(),x.getName(),x.getPhone(),x.getEmail());if(existsId(repo.getEmployees(),x.getId()))throw new IllegalArgumentException("Mã nhân viên đã tồn tại.");repo.getEmployees().add(x);repo.saveEmployees();}
    public void updateEmployee(Employee x)throws IOException{validatePerson(x.getId(),x.getName(),x.getPhone(),x.getEmail());Employee old=find(repo.getEmployees(),v->v.getId().equals(x.getId()),"Không tìm thấy nhân viên.");int i=repo.getEmployees().indexOf(old);repo.getEmployees().set(i,x);try{repo.saveEmployees();}catch(IOException e){repo.getEmployees().set(i,old);throw e;}}
    public void deleteEmployee(String id)throws IOException{if(repo.getInvoices().stream().anyMatch(v->v.getEmployeeId().equals(id)))throw new IllegalArgumentException("Không thể xóa nhân viên đã lập hóa đơn.");Employee x=find(repo.getEmployees(),v->v.getId().equals(id),"Không tìm thấy nhân viên.");repo.getEmployees().remove(x);try{repo.saveEmployees();}catch(IOException e){repo.getEmployees().add(x);throw e;}}

    public synchronized Invoice checkout(List<CartItem> cart,String customerId,String employeeId)throws IOException{
        if(cart==null||cart.isEmpty())throw new IllegalArgumentException("Giỏ hàng đang trống.");
        for(CartItem item:cart){Product current=find(repo.getProducts(),p->p.getId().equals(item.getProduct().getId()),"Sản phẩm không còn tồn tại.");if(item.getQuantity()<=0||item.getQuantity()>current.getQuantity())throw new IllegalArgumentException("Tồn kho không đủ cho sản phẩm: "+current.getName());}
        if(repo.getCustomers().stream().noneMatch(c->c.getId().equals(customerId)))throw new IllegalArgumentException("Vui lòng chọn khách hàng.");
        if(repo.getEmployees().stream().noneMatch(e->e.getId().equals(employeeId)))throw new IllegalArgumentException("Nhân viên không hợp lệ.");
        String id=nextInvoiceId();long total=cart.stream().mapToLong(CartItem::getTotal).sum();Invoice invoice=new Invoice(id,LocalDateTime.now(),customerId,employeeId,total);
        Map<Product,Integer> oldStocks=new HashMap<>();List<InvoiceDetail> details=new ArrayList<>();
        for(CartItem item:cart){Product p=find(repo.getProducts(),x->x.getId().equals(item.getProduct().getId()),"");oldStocks.put(p,p.getQuantity());p.setQuantity(p.getQuantity()-item.getQuantity());details.add(new InvoiceDetail(id,p.getId(),item.getQuantity(),p.getPrice(),p.getPrice()*item.getQuantity()));}
        repo.getInvoices().add(invoice);repo.getInvoiceDetails().addAll(details);
        try{repo.saveCheckoutTransaction();return invoice;}catch(IOException e){oldStocks.forEach(Product::setQuantity);repo.getInvoices().remove(invoice);repo.getInvoiceDetails().removeAll(details);throw e;}
    }
    private String nextInvoiceId(){int max=repo.getInvoices().stream().map(Invoice::getId).filter(s->s.matches("HD\\d+")).mapToInt(s->Integer.parseInt(s.substring(2))).max().orElse(0);return String.format("HD%05d",max+1);}
    public long totalRevenue(){return repo.getInvoices().stream().mapToLong(Invoice::getTotalAmount).sum();}
    public int totalSold(){return repo.getInvoiceDetails().stream().mapToInt(InvoiceDetail::getQuantity).sum();}
    public Map<String,Integer> productSales(){Map<String,Integer> m=new HashMap<>();for(InvoiceDetail d:repo.getInvoiceDetails())m.merge(d.getProductId(),d.getQuantity(),Integer::sum);return m;}
    public Map<String,Long> productRevenue(){Map<String,Long> m=new HashMap<>();for(InvoiceDetail d:repo.getInvoiceDetails())m.merge(d.getProductId(),d.getTotalPrice(),Long::sum);return m;}
    public Map<String,Long> customerSpending(){Map<String,Long> m=new HashMap<>();for(Invoice i:repo.getInvoices())m.merge(i.getCustomerId(),i.getTotalAmount(),Long::sum);return m;}
    public Map<String,Integer> customerInvoiceCounts(){Map<String,Integer> m=new HashMap<>();for(Invoice i:repo.getInvoices())m.merge(i.getCustomerId(),1,Integer::sum);return m;}
    public Map<LocalDate,Long> dailyRevenue(){Map<LocalDate,Long> m=new TreeMap<>();for(Invoice i:repo.getInvoices())m.merge(i.getCreatedAt().toLocalDate(),i.getTotalAmount(),Long::sum);return m;}
    public String categoryName(String id){return repo.getCategories().stream().filter(c->c.getId().equals(id)).map(Category::getName).findFirst().orElse(id);}
    public String customerName(String id){return repo.getCustomers().stream().filter(c->c.getId().equals(id)).map(Customer::getName).findFirst().orElse(id);}
    public String employeeName(String id){return repo.getEmployees().stream().filter(c->c.getId().equals(id)).map(Employee::getName).findFirst().orElse(id);}
    public String productName(String id){return repo.getProducts().stream().filter(c->c.getId().equals(id)).map(Product::getName).findFirst().orElse(id);}
}
