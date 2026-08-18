package view.panels;

import model.Category;
import service.AppService;
import utils.AppTheme;
import utils.ValidationUtils;
import view.components.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

public class CategoryPanel extends BasePanel {
    private final AppService service;private final JTextField id=UiFactory.field(),name=UiFactory.field(),description=UiFactory.field(),search=UiFactory.field();private final DefaultTableModel model=UiFactory.model("Mã","Tên danh mục","Mô tả");private final JTable table=new JTable(model);
    public CategoryPanel(AppService service){super("Quản lý danh mục");this.service=service;JPanel content=new JPanel(new BorderLayout(0,14));content.setOpaque(false);RoundedPanel form=card();form.setLayout(new BorderLayout(0,10));JPanel grid=formGrid();GridBagConstraints g=new GridBagConstraints();addField(grid,g,"Mã danh mục",id,0,0);addField(grid,g,"Tên danh mục",name,1,0);addField(grid,g,"Mô tả",description,0,1);form.add(grid);JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));buttons.setOpaque(false);buttons.add(btn("Thêm",AppTheme.SUCCESS,this::add));buttons.add(btn("Sửa",AppTheme.PRIMARY,this::update));buttons.add(btn("Xóa",AppTheme.DANGER,this::delete));buttons.add(btn("Làm mới",AppTheme.MUTED,this::clear));form.add(buttons,BorderLayout.SOUTH);content.add(form,BorderLayout.NORTH);RoundedPanel list=card();list.setLayout(new BorderLayout(0,10));search.putClientProperty("JTextField.placeholderText","Tìm kiếm danh mục...");list.add(search,BorderLayout.NORTH);list.add(UiFactory.scroll(table));content.add(list);add(content);UiFactory.onChange(search,this::loadTable);table.getSelectionModel().addListSelectionListener(e->select());refreshData();}
    private JButton btn(String t,Color c,Runnable r){RoundedButton b=new RoundedButton(t,c);b.addActionListener(e->{try{r.run();}catch(Exception x){UiFactory.error(this,x);}});return b;}
    private Category input(){return new Category(ValidationUtils.required(id.getText(),"Mã danh mục"),ValidationUtils.required(name.getText(),"Tên danh mục"),description.getText().trim());}
    private void add(){try{service.addCategory(input());clear();refreshData();UiFactory.success(this,"Đã thêm danh mục.");}catch(Exception e){throw new RuntimeException(e.getMessage(),e);}}
    private void update(){if(id.isEditable())throw new IllegalArgumentException("Hãy chọn danh mục cần sửa.");try{service.updateCategory(input());clear();refreshData();}catch(Exception e){throw new RuntimeException(e.getMessage(),e);}}
    private void delete(){int r=table.getSelectedRow();if(r<0)throw new IllegalArgumentException("Hãy chọn danh mục cần xóa.");if(JOptionPane.showConfirmDialog(this,"Bạn chắc chắn muốn xóa danh mục này?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try{service.deleteCategory(model.getValueAt(r,0).toString());clear();refreshData();}catch(Exception e){throw new RuntimeException(e.getMessage(),e);}}
    private void clear(){id.setText("");id.setEditable(true);name.setText("");description.setText("");table.clearSelection();}
    private void select(){int r=table.getSelectedRow();if(r<0)return;id.setText(model.getValueAt(r,0).toString());id.setEditable(false);name.setText(model.getValueAt(r,1).toString());description.setText(model.getValueAt(r,2).toString());}
    private void loadTable(){String q=search.getText().trim().toLowerCase(Locale.ROOT);model.setRowCount(0);for(Category c:service.categories())if(c.getId().toLowerCase().contains(q)||c.getName().toLowerCase().contains(q))model.addRow(new Object[]{c.getId(),c.getName(),c.getDescription()});}
    @Override public void refreshData(){loadTable();}
}
