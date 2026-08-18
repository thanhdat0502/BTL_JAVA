package repository;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Đọc/ghi toàn bộ dữ liệu TXT. Các dòng hỏng bị bỏ qua và báo ra stderr. */
public class FileDataRepository {
    private final Path dataDir;
    private final List<User> users=new ArrayList<>();
    private final List<Product> products=new ArrayList<>();
    private final List<Category> categories=new ArrayList<>();
    private final List<Customer> customers=new ArrayList<>();
    private final List<Employee> employees=new ArrayList<>();
    private final List<Invoice> invoices=new ArrayList<>();
    private final List<InvoiceDetail> invoiceDetails=new ArrayList<>();

    public FileDataRepository(Path dataDir) throws IOException {this.dataDir=dataDir;Files.createDirectories(dataDir);load();initializeDemoData();}

    public void load() throws IOException {
        users.clear(); products.clear(); categories.clear(); customers.clear(); employees.clear(); invoices.clear(); invoiceDetails.clear();
        users.addAll(read("users.txt",p->new User(p[0],p[1],p[2]),3));
        products.addAll(read("products.txt",p->new Product(p[0],p[1],p[2],Long.parseLong(p[3]),Integer.parseInt(p[4]),p[5],p.length>=7?p[6]:""),-6));
        categories.addAll(read("categories.txt",p->new Category(p[0],p[1],p[2]),3));
        customers.addAll(read("customers.txt",p->new Customer(p[0],p[1],p[2],p[3],p[4]),5));
        employees.addAll(read("employees.txt",p->new Employee(p[0],p[1],p[2],p[3],p[4]),5));
        invoices.addAll(read("invoices.txt",p->new Invoice(p[0],LocalDateTime.parse(p[1]),p[2],p[3],Long.parseLong(p[4])),5));
        invoiceDetails.addAll(read("invoice_details.txt",p->new InvoiceDetail(p[0],p[1],Integer.parseInt(p[2]),Long.parseLong(p[3]),Long.parseLong(p[4])),5));
    }

    private <T> List<T> read(String file,Function<String[],T> parser,int fields) throws IOException {
        Path path=dataDir.resolve(file); if(Files.notExists(path))Files.createFile(path);
        List<T> result=new ArrayList<>(); int lineNo=0;
        for(String line:Files.readAllLines(path,StandardCharsets.UTF_8)){lineNo++;if(line.trim().isEmpty())continue;try{String[] p=line.split("\\|",-1);boolean wrong=fields>=0?p.length!=fields:p.length<Math.abs(fields);if(wrong)throw new IllegalArgumentException("Sai số cột");result.add(parser.apply(p));}catch(Exception e){System.err.println("Bỏ qua dòng lỗi "+file+":"+lineNo+" - "+e.getMessage());}}
        return result;
    }

    private void initializeDemoData() throws IOException {
        Path marker=dataDir.resolve(".initialized");
        if(Files.exists(marker)){if(users.isEmpty()){users.add(new User("admin","123456","ADMIN"));saveUsers();}return;}
        if(users.isEmpty())users.add(new User("admin","123456","ADMIN"));
        if(categories.isEmpty()){categories.add(new Category("DM001","Thiết bị nhập","Bàn phím, chuột và phụ kiện"));categories.add(new Category("DM002","Âm thanh","Tai nghe và loa"));categories.add(new Category("DM003","Màn hình","Màn hình máy tính"));}
        if(products.isEmpty()){
            products.add(new Product("SP001","Bàn phím cơ Akko","DM001",1250000,20,"Switch tactile, LED RGB"));
            products.add(new Product("SP002","Chuột Logitech G304","DM001",750000,15,"Chuột không dây"));
            products.add(new Product("SP003","Bàn phím Logitech K120","DM001",220000,30,"Bàn phím văn phòng"));
            products.add(new Product("SP004","Chuột DareU EM908","DM001",390000,18,"Chuột gaming RGB"));
            products.add(new Product("SP005","Tai nghe HyperX Cloud II","DM002",1850000,10,"Tai nghe gaming 7.1"));
            products.add(new Product("SP006","Loa Bluetooth JBL Go 3","DM002",990000,12,"Loa bluetooth nhỏ gọn"));
            products.add(new Product("SP007","Tai nghe Sony WH-CH520","DM002",1290000,9,"Tai nghe không dây"));
            products.add(new Product("SP008","Màn hình LG 24 inch","DM003",3150000,8,"Full HD IPS 75Hz"));
            products.add(new Product("SP009","Màn hình Dell P2422H","DM003",4290000,6,"Full HD IPS"));
            products.add(new Product("SP010","Màn hình Asus 27 inch","DM003",5290000,5,"2K IPS 75Hz"));
        }
        if(customers.isEmpty()){customers.add(new Customer("KH001","Nguyễn Văn An","0901234567","an@gmail.com","Hà Nội"));customers.add(new Customer("KH002","Trần Thị Bình","0912345678","binh@gmail.com","Đà Nẵng"));customers.add(new Customer("KH003","Lê Minh Cường","0923456789","cuong@gmail.com","TP.HCM"));customers.add(new Customer("KH004","Phạm Thu Dung","0934567890","dung@gmail.com","Hải Phòng"));customers.add(new Customer("KH005","Hoàng Gia Huy","0945678901","huy@gmail.com","Cần Thơ"));}
        if(employees.isEmpty()){employees.add(new Employee("NV001","Đỗ Minh Khang","0961234567","khang@shop.vn","Quản lý"));employees.add(new Employee("NV002","Vũ Ngọc Lan","0972345678","lan@shop.vn","Thu ngân"));employees.add(new Employee("NV003","Bùi Quốc Nam","0983456789","nam@shop.vn","Bán hàng"));}
        saveAll(); Files.write(marker,Collections.singletonList("Sales Management initialized"),StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String clean(String s){return s==null?"":s.replace("|","/").replace("\r"," ").replace("\n"," ").trim();}
    private List<String> userLines(){return users.stream().map(x->clean(x.getUsername())+"|"+clean(x.getPassword())+"|"+clean(x.getRole())).collect(Collectors.toList());}
    private List<String> productLines(){return products.stream().map(x->clean(x.getId())+"|"+clean(x.getName())+"|"+clean(x.getCategoryId())+"|"+x.getPrice()+"|"+x.getQuantity()+"|"+clean(x.getDescription())+"|"+clean(x.getImagePath())).collect(Collectors.toList());}
    private List<String> categoryLines(){return categories.stream().map(x->clean(x.getId())+"|"+clean(x.getName())+"|"+clean(x.getDescription())).collect(Collectors.toList());}
    private List<String> customerLines(){return customers.stream().map(x->clean(x.getId())+"|"+clean(x.getName())+"|"+clean(x.getPhone())+"|"+clean(x.getEmail())+"|"+clean(x.getAddress())).collect(Collectors.toList());}
    private List<String> employeeLines(){return employees.stream().map(x->clean(x.getId())+"|"+clean(x.getName())+"|"+clean(x.getPhone())+"|"+clean(x.getEmail())+"|"+clean(x.getPosition())).collect(Collectors.toList());}
    private List<String> invoiceLines(){return invoices.stream().map(x->clean(x.getId())+"|"+x.getCreatedAt()+"|"+clean(x.getCustomerId())+"|"+clean(x.getEmployeeId())+"|"+x.getTotalAmount()).collect(Collectors.toList());}
    private List<String> detailLines(){return invoiceDetails.stream().map(x->clean(x.getInvoiceId())+"|"+clean(x.getProductId())+"|"+x.getQuantity()+"|"+x.getUnitPrice()+"|"+x.getTotalPrice()).collect(Collectors.toList());}

    private void write(String file,List<String> lines) throws IOException {Path target=dataDir.resolve(file),tmp=dataDir.resolve(file+".tmp");Files.write(tmp,lines,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);try{Files.move(tmp,target,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);}catch(AtomicMoveNotSupportedException e){Files.move(tmp,target,StandardCopyOption.REPLACE_EXISTING);}}
    public synchronized void saveUsers()throws IOException{write("users.txt",userLines());}
    public synchronized void saveProducts()throws IOException{write("products.txt",productLines());}
    public synchronized void saveCategories()throws IOException{write("categories.txt",categoryLines());}
    public synchronized void saveCustomers()throws IOException{write("customers.txt",customerLines());}
    public synchronized void saveEmployees()throws IOException{write("employees.txt",employeeLines());}
    public synchronized void saveInvoices()throws IOException{write("invoices.txt",invoiceLines());}
    public synchronized void saveInvoiceDetails()throws IOException{write("invoice_details.txt",detailLines());}
    public synchronized void saveAll()throws IOException{saveUsers();saveCategories();saveProducts();saveCustomers();saveEmployees();saveInvoices();saveInvoiceDetails();}

    /** Ghi ba file liên quan thanh toán. Nếu lỗi, khôi phục nội dung trước giao dịch. */
    public synchronized void saveCheckoutTransaction() throws IOException {
        Map<String,List<String>> newData=new LinkedHashMap<>();
        newData.put("products.txt",productLines());
        newData.put("invoices.txt",invoiceLines());
        newData.put("invoice_details.txt",detailLines());
        Map<String,byte[]> oldData=new HashMap<>(); List<Path> staged=new ArrayList<>();
        try{
            for(Map.Entry<String,List<String>> e:newData.entrySet()){Path target=dataDir.resolve(e.getKey());oldData.put(e.getKey(),Files.exists(target)?Files.readAllBytes(target):null);Path tmp=dataDir.resolve(e.getKey()+".txn");Files.write(tmp,e.getValue(),StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);staged.add(tmp);}
            for(String file:newData.keySet())Files.move(dataDir.resolve(file+".txn"),dataDir.resolve(file),StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException ex){for(Map.Entry<String,byte[]> e:oldData.entrySet()){try{Path p=dataDir.resolve(e.getKey());if(e.getValue()==null)Files.deleteIfExists(p);else Files.write(p,e.getValue());}catch(IOException ignored){}}throw ex;}
        finally{for(Path p:staged)Files.deleteIfExists(p);}
    }

    public List<User> getUsers(){return users;} public List<Product> getProducts(){return products;} public List<Category> getCategories(){return categories;}
    public List<Customer> getCustomers(){return customers;} public List<Employee> getEmployees(){return employees;} public List<Invoice> getInvoices(){return invoices;} public List<InvoiceDetail> getInvoiceDetails(){return invoiceDetails;}
}
