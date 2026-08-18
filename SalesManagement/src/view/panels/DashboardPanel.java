package view.panels;

import model.Invoice;
import service.AppService;
import utils.AppTheme;
import utils.CurrencyUtils;
import view.components.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class DashboardPanel extends BasePanel {
    private final AppService service;private final StatCard products=new StatCard("Tổng sản phẩm",AppTheme.PRIMARY),customers=new StatCard("Tổng khách hàng",AppTheme.SUCCESS),invoices=new StatCard("Tổng hóa đơn",new Color(0x8B5CF6)),revenue=new StatCard("Tổng doanh thu",new Color(0xF59E0B));private final DefaultTableModel model=UiFactory.model("Mã hóa đơn","Ngày tạo","Khách hàng","Nhân viên","Tổng tiền");private final RevenueChartPanel chart=new RevenueChartPanel();
    public DashboardPanel(AppService service){super("Tổng quan bán hàng");this.service=service;JPanel content=new JPanel(new BorderLayout(0,16));content.setOpaque(false);JPanel cards=new JPanel(new GridLayout(1,4,14,0));cards.setOpaque(false);cards.add(products);cards.add(customers);cards.add(invoices);cards.add(revenue);content.add(cards,BorderLayout.NORTH);JPanel lower=new JPanel(new GridLayout(1,2,14,0));lower.setOpaque(false);RoundedPanel recent=card();recent.setLayout(new BorderLayout(0,10));JLabel t=new JLabel("5 hóa đơn gần nhất");t.setFont(AppTheme.FONT.deriveFont(Font.BOLD,17));recent.add(t,BorderLayout.NORTH);recent.add(UiFactory.scroll(new JTable(model)));lower.add(recent);RoundedPanel graph=card();graph.setLayout(new BorderLayout());JLabel gt=new JLabel("Doanh thu 7 ngày có giao dịch gần nhất");gt.setFont(AppTheme.FONT.deriveFont(Font.BOLD,17));graph.add(gt,BorderLayout.NORTH);graph.add(chart);lower.add(graph);content.add(lower);add(content);refreshData();}
    @Override public void refreshData(){products.setValue(String.valueOf(service.products().size()));customers.setValue(String.valueOf(service.customers().size()));invoices.setValue(String.valueOf(service.invoices().size()));revenue.setValue(CurrencyUtils.format(service.totalRevenue()));model.setRowCount(0);service.invoices().stream().sorted(Comparator.comparing(Invoice::getCreatedAt).reversed()).limit(5).forEach(i->model.addRow(new Object[]{i.getId(),i.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),service.customerName(i.getCustomerId()),service.employeeName(i.getEmployeeId()),CurrencyUtils.format(i.getTotalAmount())}));chart.setData(service.dailyRevenue());}
}
