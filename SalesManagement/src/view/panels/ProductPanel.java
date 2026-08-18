package view.panels;

import model.Category;
import model.Product;
import service.AppService;
import utils.AppTheme;
import utils.CurrencyUtils;
import utils.ImageUtils;
import utils.ValidationUtils;
import view.components.RoundedButton;
import view.components.RoundedPanel;
import view.components.TableActionButtonRenderer;
import view.components.UiFactory;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ProductPanel extends BasePanel {
    private final AppService service;
    private final JTextField id=UiFactory.field();
    private final JTextField name=UiFactory.field();
    private final JTextField price=UiFactory.field();
    private final JTextField quantity=UiFactory.field();
    private final JTextField description=UiFactory.field();
    private final JTextField search=UiFactory.field();
    private final JComboBox<Category> category=new JComboBox<>();
    private final JComboBox<Object> filter=new JComboBox<>();
    private final JComboBox<String> sort=new JComboBox<>(new String[]{
        "Sắp xếp mặc định","Giá tăng dần","Giá giảm dần",
        "Tồn kho tăng dần","Tồn kho giảm dần"
    });
    private final DefaultTableModel model=UiFactory.imageModel(
        "Ảnh","Mã","Tên sản phẩm","Danh mục","Giá","Tồn kho","Mô tả","Cập nhật ảnh");
    private final JTable table=new JTable(model);
    private String selectedImagePath="";

    public ProductPanel(AppService service){
        super("Quản lý sản phẩm");
        this.service=service;

        JPanel content=new JPanel(new BorderLayout(0,14));
        content.setOpaque(false);
        content.add(createForm(),BorderLayout.NORTH);
        content.add(createProductList(),BorderLayout.CENTER);
        add(content,BorderLayout.CENTER);

        UiFactory.onChange(search,this::loadTable);
        filter.addActionListener(e->loadTable());
        sort.addActionListener(e->loadTable());
        table.getSelectionModel().addListSelectionListener(e->select());
        table.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseClicked(java.awt.event.MouseEvent event){
                int row=table.rowAtPoint(event.getPoint());
                int column=table.columnAtPoint(event.getPoint());
                if(row>=0&&column==7)chooseImageForProduct(row);
            }
        });
        refreshData();
    }

    private RoundedPanel createForm(){
        RoundedPanel form=card();
        form.setLayout(new BorderLayout(14,10));
        JPanel grid=formGrid();
        GridBagConstraints constraints=new GridBagConstraints();
        addField(grid,constraints,"Mã sản phẩm",id,0,0);
        addField(grid,constraints,"Tên sản phẩm",name,1,0);
        addField(grid,constraints,"Danh mục",category,0,1);
        addField(grid,constraints,"Giá bán",price,1,1);
        addField(grid,constraints,"Số lượng",quantity,0,2);
        addField(grid,constraints,"Mô tả",description,1,2);
        form.add(grid,BorderLayout.CENTER);

        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        actions.setOpaque(false);
        actions.add(button("Thêm",AppTheme.SUCCESS,this::add));
        actions.add(button("Sửa",AppTheme.PRIMARY,this::update));
        actions.add(button("Xóa",AppTheme.DANGER,this::delete));
        actions.add(button("Làm mới",AppTheme.MUTED,this::clear));
        form.add(actions,BorderLayout.SOUTH);
        return form;
    }

    private RoundedPanel createProductList(){
        RoundedPanel list=card();
        list.setLayout(new BorderLayout(0,10));
        JPanel filters=new JPanel(new BorderLayout(10,0));
        filters.setOpaque(false);
        search.putClientProperty("JTextField.placeholderText","Tìm kiếm sản phẩm...");
        filters.add(search,BorderLayout.CENTER);
        JPanel options=new JPanel(new GridLayout(1,2,10,0));
        options.setOpaque(false);
        filter.setPreferredSize(new Dimension(200,38));
        sort.setPreferredSize(new Dimension(190,38));
        options.add(filter);
        options.add(sort);
        filters.add(options,BorderLayout.EAST);
        list.add(filters,BorderLayout.NORTH);
        table.setRowHeight(64);
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(7).setPreferredWidth(105);
        table.getColumnModel().getColumn(7).setMaxWidth(115);
        table.getColumnModel().getColumn(7).setCellRenderer(new TableActionButtonRenderer());
        table.setToolTipText("Bấm Tải ảnh trên từng dòng để cập nhật ảnh sản phẩm");
        list.add(UiFactory.scroll(table),BorderLayout.CENTER);
        return list;
    }

    private JButton button(String text,Color color,Runnable action){
        RoundedButton button=new RoundedButton(text,color);
        button.addActionListener(e->{
            try{action.run();}
            catch(Exception exception){UiFactory.error(this,exception);}
        });
        return button;
    }

    private Product input(){
        Category selectedCategory=(Category)category.getSelectedItem();
        return new Product(
            ValidationUtils.required(id.getText(),"Mã sản phẩm"),
            ValidationUtils.required(name.getText(),"Tên sản phẩm"),
            selectedCategory==null?"":selectedCategory.getId(),
            ValidationUtils.positiveLong(price.getText(),"Giá bán"),
            ValidationUtils.nonNegativeInt(quantity.getText(),"Số lượng"),
            description.getText().trim(),selectedImagePath);
    }

    private void add(){
        try{
            service.addProduct(input());
            clear();
            refreshData();
            UiFactory.success(this,"Đã thêm sản phẩm.");
        }catch(Exception exception){throw new RuntimeException(exception.getMessage(),exception);}
    }

    private void update(){
        if(id.isEditable())throw new IllegalArgumentException("Hãy chọn sản phẩm cần sửa trong bảng.");
        try{
            service.updateProduct(input());
            clear();
            refreshData();
            UiFactory.success(this,"Đã cập nhật sản phẩm.");
        }catch(Exception exception){throw new RuntimeException(exception.getMessage(),exception);}
    }

    private void delete(){
        int row=table.getSelectedRow();
        if(row<0)throw new IllegalArgumentException("Hãy chọn sản phẩm cần xóa.");
        if(JOptionPane.showConfirmDialog(this,"Bạn chắc chắn muốn xóa sản phẩm này?",
            "Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        try{
            service.deleteProduct(model.getValueAt(row,1).toString());
            clear();
            refreshData();
        }catch(Exception exception){throw new RuntimeException(exception.getMessage(),exception);}
    }

    private void chooseImageForProduct(int row){
        try{
            String productId=model.getValueAt(row,1).toString();
            Product original=null;
            for(Product product:service.products()){
                if(product.getId().equals(productId)){original=product;break;}
            }
            if(original==null)throw new IllegalArgumentException("Không tìm thấy sản phẩm.");
            String imported=ImageUtils.chooseAndImport(this,productId);
            if(imported==null)return;
            Product updated=new Product(original.getId(),original.getName(),original.getCategoryId(),
                original.getPrice(),original.getQuantity(),original.getDescription(),imported);
            service.updateProduct(updated);
            selectedImagePath=imported;
            refreshData();
            UiFactory.success(this,"Đã cập nhật ảnh cho "+original.getName()+".");
        }catch(Exception exception){throw new RuntimeException("Không thể chọn ảnh: "+exception.getMessage(),exception);}
    }

    private void clear(){
        id.setText("");id.setEditable(true);name.setText("");price.setText("");
        quantity.setText("");description.setText("");selectedImagePath="";
        table.clearSelection();
        if(category.getItemCount()>0)category.setSelectedIndex(0);
    }

    private void select(){
        int row=table.getSelectedRow();
        if(row<0)return;
        String key=model.getValueAt(row,1).toString();
        for(Product product:service.products()){
            if(!product.getId().equals(key))continue;
            id.setText(product.getId());id.setEditable(false);name.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));
            quantity.setText(String.valueOf(product.getQuantity()));
            description.setText(product.getDescription());selectedImagePath=product.getImagePath();
            for(int index=0;index<category.getItemCount();index++){
                if(category.getItemAt(index).getId().equals(product.getCategoryId()))category.setSelectedIndex(index);
            }
            break;
        }
    }

    private void loadTable(){
        String query=search.getText().trim().toLowerCase(Locale.ROOT);
        Object selectedCategory=filter.getSelectedItem();
        List<Product> visibleProducts=new ArrayList<>();
        for(Product product:service.products()){
            boolean matchesSearch=product.getId().toLowerCase(Locale.ROOT).contains(query)
                ||product.getName().toLowerCase(Locale.ROOT).contains(query);
            boolean matchesCategory=!(selectedCategory instanceof Category)
                ||product.getCategoryId().equals(((Category)selectedCategory).getId());
            if(matchesSearch&&matchesCategory)visibleProducts.add(product);
        }
        sortProducts(visibleProducts);
        model.setRowCount(0);
        for(Product product:visibleProducts){
            model.addRow(new Object[]{ImageUtils.thumbnail(product.getImagePath(),54,48),product.getId(),
                product.getName(),service.categoryName(product.getCategoryId()),
                CurrencyUtils.format(product.getPrice()),product.getQuantity(),product.getDescription(),"Tải ảnh"});
        }
    }

    private void sortProducts(List<Product> products){
        String selectedSort=(String)sort.getSelectedItem();
        if("Giá tăng dần".equals(selectedSort))products.sort(Comparator.comparingLong(Product::getPrice));
        else if("Giá giảm dần".equals(selectedSort))products.sort(Comparator.comparingLong(Product::getPrice).reversed());
        else if("Tồn kho tăng dần".equals(selectedSort))products.sort(Comparator.comparingInt(Product::getQuantity));
        else if("Tồn kho giảm dần".equals(selectedSort))products.sort(Comparator.comparingInt(Product::getQuantity).reversed());
    }

    @Override
    public void refreshData(){
        Category selected=(Category)category.getSelectedItem();
        category.removeAllItems();
        filter.removeAllItems();
        filter.addItem("Tất cả danh mục");
        for(Category item:service.categories()){
            category.addItem(item);
            filter.addItem(item);
        }
        if(selected!=null){
            for(int index=0;index<category.getItemCount();index++){
                if(category.getItemAt(index).getId().equals(selected.getId()))category.setSelectedIndex(index);
            }
        }
        loadTable();
    }
}
