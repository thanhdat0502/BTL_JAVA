package view.panels;

import model.CartItem;
import model.Category;
import model.Customer;
import model.Employee;
import model.Invoice;
import model.Product;
import service.AppService;
import utils.AppTheme;
import utils.CurrencyUtils;
import view.components.RoundedButton;
import view.components.RoundedPanel;
import view.components.ProductCard;
import view.components.UiFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SalesPanel extends BasePanel {
    private final AppService service;
    private final JTextField search=UiFactory.field();
    private final JComboBox<Object> category=new JComboBox<>();
    private final JComboBox<Customer> customer=new JComboBox<>();
    private final JComboBox<Employee> employee=new JComboBox<>();
    private final DefaultTableModel cartModel=UiFactory.model("Sản phẩm","Số lượng","Đơn giá","Thành tiền");
    private final JPanel productGrid=new JPanel();
    private final JTable cartTable=new JTable(cartModel);
    private final List<CartItem> cart=new ArrayList<>();
    private final JLabel total=new JLabel("0 ₫",SwingConstants.RIGHT);

    public SalesPanel(AppService service){
        super("Bán hàng");
        this.service=service;
        JPanel halves=new JPanel(new GridLayout(1,2,14,0));
        halves.setOpaque(false);
        halves.add(createProductArea());
        halves.add(createCartArea());
        add(halves);

        UiFactory.onChange(search,this::loadProducts);
        category.addActionListener(e->loadProducts());
        refreshData();
    }

    private RoundedPanel createProductArea(){
        RoundedPanel left=card();
        left.setLayout(new BorderLayout(0,10));
        JPanel filters=new JPanel(new GridLayout(1,2,8,0));
        filters.setOpaque(false);
        search.putClientProperty("JTextField.placeholderText","Tìm sản phẩm...");
        filters.add(search);
        filters.add(category);
        left.add(filters,BorderLayout.NORTH);
        productGrid.setOpaque(false);
        productGrid.setLayout(new BoxLayout(productGrid,BoxLayout.Y_AXIS));
        JScrollPane scroll=new JScrollPane(productGrid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.BACKGROUND);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        left.add(scroll,BorderLayout.CENTER);
        return left;
    }

    private RoundedPanel createCartArea(){
        RoundedPanel right=card();
        right.setLayout(new BorderLayout(0,10));
        JPanel selectors=new JPanel(new GridLayout(2,2,8,8));
        selectors.setOpaque(false);
        selectors.add(new JLabel("Khách hàng"));selectors.add(customer);
        selectors.add(new JLabel("Nhân viên"));selectors.add(employee);
        right.add(selectors,BorderLayout.NORTH);
        right.add(UiFactory.scroll(cartTable),BorderLayout.CENTER);

        JPanel bottom=new JPanel(new BorderLayout(8,8));
        bottom.setOpaque(false);
        JPanel edit=new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        edit.setOpaque(false);
        edit.add(button("+",AppTheme.SUCCESS,()->changeQuantity(1)));
        edit.add(button("−",AppTheme.MUTED,()->changeQuantity(-1)));
        edit.add(button("Xóa",AppTheme.DANGER,this::removeCart));
        bottom.add(edit,BorderLayout.WEST);

        JPanel pay=new JPanel(new BorderLayout(6,6));
        pay.setOpaque(false);
        total.setFont(AppTheme.FONT.deriveFont(Font.BOLD,22));
        total.setForeground(AppTheme.DANGER);
        pay.add(total,BorderLayout.NORTH);
        RoundedButton checkoutButton=new RoundedButton("THANH TOÁN",AppTheme.SUCCESS);
        checkoutButton.addActionListener(e->checkout());
        pay.add(checkoutButton,BorderLayout.CENTER);
        bottom.add(pay,BorderLayout.EAST);
        right.add(bottom,BorderLayout.SOUTH);
        return right;
    }

    private JButton button(String text,Color color,Runnable action){
        RoundedButton button=new RoundedButton(text,color);
        button.addActionListener(e->action.run());
        return button;
    }

    private void addToCart(Product product){
        if(product==null||product.getQuantity()==0){UiFactory.error(this,new IllegalArgumentException("Sản phẩm đã hết hàng."));return;}
        CartItem found=null;
        for(CartItem item:cart)if(item.getProduct().getId().equals(product.getId())){found=item;break;}
        if(found==null)cart.add(new CartItem(product,1));
        else if(found.getQuantity()<product.getQuantity())found.setQuantity(found.getQuantity()+1);
        else{UiFactory.error(this,new IllegalArgumentException("Số lượng đã đạt mức tồn kho."));return;}
        loadCart();
    }

    private void changeQuantity(int delta){
        int row=cartTable.getSelectedRow();
        if(row<0)return;
        CartItem item=cart.get(row);
        int newQuantity=item.getQuantity()+delta;
        if(newQuantity<=0)cart.remove(row);
        else if(newQuantity>item.getProduct().getQuantity()){
            UiFactory.error(this,new IllegalArgumentException("Số lượng không được lớn hơn tồn kho."));return;
        }else item.setQuantity(newQuantity);
        loadCart();
    }

    private void removeCart(){
        int row=cartTable.getSelectedRow();
        if(row>=0){cart.remove(row);loadCart();}
    }

    private void checkout(){
        try{
            Customer selectedCustomer=(Customer)customer.getSelectedItem();
            Employee selectedEmployee=(Employee)employee.getSelectedItem();
            Invoice invoice=service.checkout(new ArrayList<>(cart),
                selectedCustomer==null?"":selectedCustomer.getId(),
                selectedEmployee==null?"":selectedEmployee.getId());
            cart.clear();refreshData();
            UiFactory.success(this,"Thanh toán thành công. Mã hóa đơn: "+invoice.getId());
            new InvoiceDetailDialog(SwingUtilities.getWindowAncestor(this),service,invoice).setVisible(true);
        }catch(Exception exception){UiFactory.error(this,exception);}
    }

    private void loadProducts(){
        String query=search.getText().trim().toLowerCase(Locale.ROOT);
        Object selectedCategory=category.getSelectedItem();
        List<Product> visibleProducts=new ArrayList<>();
        for(Product product:service.products()){
            boolean matchesSearch=product.getId().toLowerCase(Locale.ROOT).contains(query)
                ||product.getName().toLowerCase(Locale.ROOT).contains(query);
            boolean matchesCategory=!(selectedCategory instanceof Category)
                ||((Category)selectedCategory).getId().equals(product.getCategoryId());
            if(matchesSearch&&matchesCategory)visibleProducts.add(product);
        }
        productGrid.removeAll();
        if(visibleProducts.isEmpty()){
            JLabel empty=new JLabel("Không tìm thấy sản phẩm phù hợp",SwingConstants.CENTER);
            empty.setForeground(AppTheme.MUTED);
            empty.setPreferredSize(new java.awt.Dimension(0,100));
            productGrid.add(empty);
        }else{
            Map<String,Integer> soldQuantities=service.productSales();
            for(int start=0;start<visibleProducts.size();start+=2){
                JPanel row=new JPanel(new GridLayout(1,2,12,0));
                row.setOpaque(false);
                row.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE,330));
                row.setPreferredSize(new java.awt.Dimension(0,330));
                for(int offset=0;offset<2;offset++){
                    int index=start+offset;
                    if(index<visibleProducts.size()){
                        Product product=visibleProducts.get(index);
                        int sold=soldQuantities.containsKey(product.getId())?soldQuantities.get(product.getId()):0;
                        row.add(new ProductCard(product,service.categoryName(product.getCategoryId()),sold,this::addToCart));
                    }else{
                        JPanel filler=new JPanel();filler.setOpaque(false);row.add(filler);
                    }
                }
                productGrid.add(row);
                productGrid.add(Box.createVerticalStrut(10));
            }
        }
        productGrid.revalidate();
        productGrid.repaint();
    }

    private void loadCart(){
        cartModel.setRowCount(0);
        long sum=0;
        for(CartItem item:cart){
            cartModel.addRow(new Object[]{item.getProduct().getName(),item.getQuantity(),
                CurrencyUtils.format(item.getProduct().getPrice()),CurrencyUtils.format(item.getTotal())});
            sum+=item.getTotal();
        }
        total.setText("Tổng tiền: "+CurrencyUtils.format(sum));
    }

    @Override
    public void refreshData(){
        Object selectedCategory=category.getSelectedItem();
        category.removeAllItems();
        category.addItem("Tất cả danh mục");
        for(Category item:service.categories())category.addItem(item);
        if(selectedCategory instanceof Category){
            for(int index=1;index<category.getItemCount();index++){
                Category item=(Category)category.getItemAt(index);
                if(item.getId().equals(((Category)selectedCategory).getId()))category.setSelectedIndex(index);
            }
        }
        customer.removeAllItems();
        for(Customer item:service.customers())customer.addItem(item);
        employee.removeAllItems();
        for(Employee item:service.employees())employee.addItem(item);
        loadProducts();loadCart();
    }
}
