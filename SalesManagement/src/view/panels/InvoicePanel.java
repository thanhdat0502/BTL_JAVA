package view.panels;

import model.Invoice;
import service.AppService;
import utils.CurrencyUtils;
import view.components.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Locale;

public class InvoicePanel extends BasePanel {
    private final AppService service;private final JTextField search=UiFactory.field(),date=UiFactory.field();private final DefaultTableModel model=UiFactory.model("Mã hóa đơn","Ngày","Khách hàng","Nhân viên","Tổng tiền");private final JTable table=new JTable(model);
    public InvoicePanel(AppService service){super("Quản lý hóa đơn");this.service=service;RoundedPanel list=card();list.setLayout(new BorderLayout(0,10));JPanel filters=new JPanel(new GridLayout(1,2,10,0));filters.setOpaque(false);search.putClientProperty("JTextField.placeholderText","Tìm theo mã hoặc khách hàng...");date.putClientProperty("JTextField.placeholderText","Lọc ngày: yyyy-MM-dd");filters.add(search);filters.add(date);list.add(filters,BorderLayout.NORTH);list.add(UiFactory.scroll(table));add(list);UiFactory.onChange(search,this::loadTable);UiFactory.onChange(date,this::loadTable);table.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)showDetail();}});refreshData();}
    private void loadTable(){String q=search.getText().trim().toLowerCase(Locale.ROOT),ds=date.getText().trim();LocalDate selected=null;if(!ds.isEmpty())try{selected=LocalDate.parse(ds);}catch(DateTimeParseException ignored){}final LocalDate filterDate=selected;model.setRowCount(0);service.invoices().stream().sorted(Comparator.comparing(Invoice::getCreatedAt).reversed()).filter(i->(i.getId().toLowerCase().contains(q)||service.customerName(i.getCustomerId()).toLowerCase().contains(q))&&(ds.isEmpty()||(filterDate!=null&&i.getCreatedAt().toLocalDate().equals(filterDate)))).forEach(i->model.addRow(new Object[]{i.getId(),i.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),service.customerName(i.getCustomerId()),service.employeeName(i.getEmployeeId()),CurrencyUtils.format(i.getTotalAmount())}));}
    private void showDetail(){int r=table.getSelectedRow();if(r<0)return;String id=model.getValueAt(r,0).toString();service.invoices().stream().filter(i->i.getId().equals(id)).findFirst().ifPresent(i->new InvoiceDetailDialog(SwingUtilities.getWindowAncestor(this),service,i).setVisible(true));}
    @Override public void refreshData(){loadTable();}
}
