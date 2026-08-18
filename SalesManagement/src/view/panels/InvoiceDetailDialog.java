package view.panels;

import model.Invoice;
import model.InvoiceDetail;
import service.AppService;
import utils.AppTheme;
import utils.CurrencyUtils;
import view.components.UiFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class InvoiceDetailDialog extends JDialog {
    public InvoiceDetailDialog(Window owner,AppService service,Invoice invoice){super(owner,"Chi tiết hóa đơn "+invoice.getId(),ModalityType.APPLICATION_MODAL);setSize(700,500);setLocationRelativeTo(owner);JPanel root=new JPanel(new BorderLayout(0,14));root.setBackground(AppTheme.BACKGROUND);root.setBorder(BorderFactory.createEmptyBorder(20,22,20,22));JPanel info=new JPanel(new GridLayout(2,2,8,8));info.setOpaque(false);info.add(new JLabel("Mã hóa đơn: "+invoice.getId()));info.add(new JLabel("Ngày tạo: "+invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));info.add(new JLabel("Khách hàng: "+service.customerName(invoice.getCustomerId())));info.add(new JLabel("Nhân viên: "+service.employeeName(invoice.getEmployeeId())));root.add(info,BorderLayout.NORTH);DefaultTableModel m=UiFactory.model("Sản phẩm","Số lượng","Đơn giá","Thành tiền");for(InvoiceDetail d:service.invoiceDetails())if(d.getInvoiceId().equals(invoice.getId()))m.addRow(new Object[]{service.productName(d.getProductId()),d.getQuantity(),CurrencyUtils.format(d.getUnitPrice()),CurrencyUtils.format(d.getTotalPrice())});root.add(UiFactory.scroll(new JTable(m)));JLabel total=new JLabel("TỔNG CỘNG: "+CurrencyUtils.format(invoice.getTotalAmount()),SwingConstants.RIGHT);total.setForeground(AppTheme.DANGER);total.setFont(AppTheme.FONT.deriveFont(Font.BOLD,22));root.add(total,BorderLayout.SOUTH);setContentPane(root);}
}
