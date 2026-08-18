package view.panels;

import service.AppService;
import utils.AppTheme;
import utils.CurrencyUtils;
import view.components.RevenueChartPanel;
import view.components.RoundedPanel;
import view.components.StatCard;
import view.components.UiFactory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StatisticPanel extends BasePanel {
    private final AppService service;
    private final StatCard revenue=new StatCard("Tổng doanh thu",AppTheme.SUCCESS);
    private final StatCard invoiceCount=new StatCard("Tổng số hóa đơn",AppTheme.PRIMARY);
    private final StatCard sold=new StatCard("Sản phẩm đã bán",new Color(0xF59E0B));
    private final StatCard best=new StatCard("Bán chạy nhất",new Color(0x8B5CF6));
    private final DefaultTableModel productModel=UiFactory.model(
        "Hạng","Mã sản phẩm","Tên sản phẩm","Đã bán","Doanh thu");
    private final DefaultTableModel customerModel=UiFactory.model(
        "Hạng","Mã khách hàng","Khách hàng","Số hóa đơn","Tổng chi tiêu");
    private final RevenueChartPanel chart=new RevenueChartPanel();

    public StatisticPanel(AppService service){
        super("Thống kê kinh doanh");
        this.service=service;

        JPanel root=new JPanel(new BorderLayout(0,14));
        root.setOpaque(false);

        JPanel cards=new JPanel(new GridLayout(1,4,14,0));
        cards.setOpaque(false);
        cards.add(revenue);
        cards.add(invoiceCount);
        cards.add(sold);
        cards.add(best);
        root.add(cards,BorderLayout.NORTH);

        JPanel body=new JPanel(new BorderLayout(0,14));
        body.setOpaque(false);
        body.add(createRankingTables(),BorderLayout.CENTER);
        body.add(createRevenueChart(),BorderLayout.SOUTH);
        root.add(body,BorderLayout.CENTER);

        add(root);
        refreshData();
    }

    private JPanel createRankingTables(){
        JPanel rankings=new JPanel(new GridLayout(1,2,14,0));
        rankings.setOpaque(false);
        rankings.add(createTableCard("Top 5 sản phẩm được mua nhiều nhất",productModel));
        rankings.add(createTableCard("Top 5 khách hàng theo tổng chi tiêu",customerModel));
        return rankings;
    }

    private RoundedPanel createTableCard(String title,DefaultTableModel tableModel){
        RoundedPanel panel=card();
        panel.setLayout(new BorderLayout(0,10));
        JLabel label=new JLabel(title);
        label.setFont(AppTheme.FONT.deriveFont(Font.BOLD,17));
        panel.add(label,BorderLayout.NORTH);
        panel.add(UiFactory.scroll(new JTable(tableModel)),BorderLayout.CENTER);
        return panel;
    }

    private RoundedPanel createRevenueChart(){
        RoundedPanel graph=card();
        graph.setLayout(new BorderLayout(0,8));
        graph.setPreferredSize(new Dimension(0,210));
        JLabel title=new JLabel("Biểu đồ doanh thu");
        title.setFont(AppTheme.FONT.deriveFont(Font.BOLD,17));
        graph.add(title,BorderLayout.NORTH);
        graph.add(chart,BorderLayout.CENTER);
        return graph;
    }

    @Override
    public void refreshData(){
        Map<String,Integer> productSales=service.productSales();
        Map<String,Long> productRevenue=service.productRevenue();
        Map<String,Long> customerSpending=service.customerSpending();
        Map<String,Integer> customerInvoiceCounts=service.customerInvoiceCounts();

        revenue.setValue(CurrencyUtils.format(service.totalRevenue()));
        invoiceCount.setValue(String.valueOf(service.invoices().size()));
        sold.setValue(String.valueOf(service.totalSold()));

        List<String> rankedProducts=rankProducts(productSales,productRevenue);
        best.setValue(rankedProducts.isEmpty()?"Chưa có":service.productName(rankedProducts.get(0)));
        loadProductRanking(rankedProducts,productSales,productRevenue);

        List<String> rankedCustomers=rankCustomers(customerSpending,customerInvoiceCounts);
        loadCustomerRanking(rankedCustomers,customerSpending,customerInvoiceCounts);
        chart.setData(service.dailyRevenue());
    }

    private List<String> rankProducts(Map<String,Integer> sales,Map<String,Long> revenueByProduct){
        List<String> ids=new ArrayList<>(sales.keySet());
        Collections.sort(ids,new Comparator<String>(){
            @Override public int compare(String first,String second){
                int byQuantity=Integer.compare(sales.get(second),sales.get(first));
                if(byQuantity!=0)return byQuantity;
                return Long.compare(revenueByProduct.get(second),revenueByProduct.get(first));
            }
        });
        return ids;
    }

    private List<String> rankCustomers(Map<String,Long> spending,Map<String,Integer> invoiceCounts){
        List<String> ids=new ArrayList<>(spending.keySet());
        Collections.sort(ids,new Comparator<String>(){
            @Override public int compare(String first,String second){
                int bySpending=Long.compare(spending.get(second),spending.get(first));
                if(bySpending!=0)return bySpending;
                return Integer.compare(invoiceCounts.get(second),invoiceCounts.get(first));
            }
        });
        return ids;
    }

    private void loadProductRanking(List<String> ids,Map<String,Integer> sales,
                                    Map<String,Long> revenueByProduct){
        productModel.setRowCount(0);
        for(int index=0;index<Math.min(5,ids.size());index++){
            String id=ids.get(index);
            productModel.addRow(new Object[]{index+1,id,service.productName(id),sales.get(id),
                CurrencyUtils.format(revenueByProduct.get(id))});
        }
    }

    private void loadCustomerRanking(List<String> ids,Map<String,Long> spending,
                                     Map<String,Integer> invoiceCounts){
        customerModel.setRowCount(0);
        for(int index=0;index<Math.min(5,ids.size());index++){
            String id=ids.get(index);
            customerModel.addRow(new Object[]{index+1,id,service.customerName(id),invoiceCounts.get(id),
                CurrencyUtils.format(spending.get(id))});
        }
    }
}
