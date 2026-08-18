package view.panels;

import model.Customer;
import model.Employee;
import service.AppService;
import utils.AppTheme;
import utils.ValidationUtils;
import view.components.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

abstract class PersonCrudPanel extends BasePanel {
    protected final AppService service;private final boolean employee;private final JTextField id=UiFactory.field(),name=UiFactory.field(),phone=UiFactory.field(),email=UiFactory.field(),extra=UiFactory.field(),search=UiFactory.field();private final DefaultTableModel model;private final JTable table;
    protected PersonCrudPanel(AppService service,boolean employee){super(employee?"Quản lý nhân viên":"Quản lý khách hàng");this.service=service;this.employee=employee;String prefix=employee?"nhân viên":"khách hàng";model=UiFactory.model("Mã","Họ tên","Số điện thoại","Email",employee?"Chức vụ":"Địa chỉ");table=new JTable(model);JPanel content=new JPanel(new BorderLayout(0,14));content.setOpaque(false);RoundedPanel form=card();form.setLayout(new BorderLayout(0,10));JPanel grid=formGrid();GridBagConstraints g=new GridBagConstraints();addField(grid,g,"Mã "+prefix,id,0,0);addField(grid,g,"Họ tên",name,1,0);addField(grid,g,"Số điện thoại",phone,0,1);addField(grid,g,"Email",email,1,1);addField(grid,g,employee?"Chức vụ":"Địa chỉ",extra,0,2);form.add(grid);JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));buttons.setOpaque(false);buttons.add(btn("Thêm",AppTheme.SUCCESS,this::add));buttons.add(btn("Sửa",AppTheme.PRIMARY,this::update));buttons.add(btn("Xóa",AppTheme.DANGER,this::delete));buttons.add(btn("Làm mới",AppTheme.MUTED,this::clear));form.add(buttons,BorderLayout.SOUTH);content.add(form,BorderLayout.NORTH);RoundedPanel list=card();list.setLayout(new BorderLayout(0,10));search.putClientProperty("JTextField.placeholderText","Tìm theo mã, tên hoặc số điện thoại...");list.add(search,BorderLayout.NORTH);list.add(UiFactory.scroll(table));content.add(list);add(content);UiFactory.onChange(search,this::loadTable);table.getSelectionModel().addListSelectionListener(e->select());refreshData();}
    private JButton btn(String t,Color c,Runnable r){RoundedButton b=new RoundedButton(t,c);b.addActionListener(e->{try{r.run();}catch(Exception x){UiFactory.error(this,x);}});return b;}
    private void validateInput(){ValidationUtils.required(id.getText(),"Mã");ValidationUtils.required(name.getText(),"Họ tên");ValidationUtils.phone(phone.getText());ValidationUtils.email(email.getText());ValidationUtils.required(extra.getText(),employee?"Chức vụ":"Địa chỉ");}
    private void add(){validateInput();try{if(employee)service.addEmployee(new Employee(id.getText().trim(),name.getText().trim(),phone.getText().trim(),email.getText().trim(),extra.getText().trim()));else service.addCustomer(new Customer(id.getText().trim(),name.getText().trim(),phone.getText().trim(),email.getText().trim(),extra.getText().trim()));clear();refreshData();UiFactory.success(this,"Đã thêm dữ liệu.");}catch(Exception e){throw new RuntimeException(e.getMessage(),e);}}
    private void update(){if(id.isEditable())throw new IllegalArgumentException("Hãy chọn một dòng cần sửa.");validateInput();try{if(employee)service.updateEmployee(new Employee(id.getText(),name.getText().trim(),phone.getText().trim(),email.getText().trim(),extra.getText().trim()));else service.updateCustomer(new Customer(id.getText(),name.getText().trim(),phone.getText().trim(),email.getText().trim(),extra.getText().trim()));clear();refreshData();}catch(Exception e){throw new RuntimeException(e.getMessage(),e);}}
    private void delete(){int r=table.getSelectedRow();if(r<0)throw new IllegalArgumentException("Hãy chọn một dòng cần xóa.");if(JOptionPane.showConfirmDialog(this,"Bạn chắc chắn muốn xóa dữ liệu này?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try{if(employee)service.deleteEmployee(model.getValueAt(r,0).toString());else service.deleteCustomer(model.getValueAt(r,0).toString());clear();refreshData();}catch(Exception e){throw new RuntimeException(e.getMessage(),e);}}
    private void clear(){id.setEditable(true);for(JTextField f:new JTextField[]{id,name,phone,email,extra})f.setText("");table.clearSelection();}
    private void select(){int r=table.getSelectedRow();if(r<0)return;id.setEditable(false);id.setText(model.getValueAt(r,0).toString());name.setText(model.getValueAt(r,1).toString());phone.setText(model.getValueAt(r,2).toString());email.setText(model.getValueAt(r,3).toString());extra.setText(model.getValueAt(r,4).toString());}
    private void loadTable(){String q=search.getText().trim().toLowerCase(Locale.ROOT);model.setRowCount(0);if(employee)for(Employee x:service.employees()){if(match(x.getId(),x.getName(),x.getPhone(),q))model.addRow(new Object[]{x.getId(),x.getName(),x.getPhone(),x.getEmail(),x.getPosition()});}else for(Customer x:service.customers()){if(match(x.getId(),x.getName(),x.getPhone(),q))model.addRow(new Object[]{x.getId(),x.getName(),x.getPhone(),x.getEmail(),x.getAddress()});}}
    private boolean match(String id,String name,String phone,String q){return id.toLowerCase().contains(q)||name.toLowerCase().contains(q)||phone.contains(q);}
    @Override public void refreshData(){loadTable();}
}
