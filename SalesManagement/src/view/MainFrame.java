package view;

import model.User;
import service.AppService;
import utils.AppTheme;
import view.components.SidebarButton;
import view.panels.*;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private final CardLayout layout=new CardLayout();private final JPanel cards=new JPanel(layout);private final Map<String,BasePanel> panels=new LinkedHashMap<>();private final Map<String,SidebarButton> buttons=new LinkedHashMap<>();
    public MainFrame(AppService service,User user){super("Sales Management System - v1.6");setDefaultCloseOperation(EXIT_ON_CLOSE);setMinimumSize(new Dimension(1150,720));setSize(1380,820);setLocationRelativeTo(null);panels.put("Dashboard",new DashboardPanel(service));panels.put("Sản phẩm",new ProductPanel(service));panels.put("Danh mục",new CategoryPanel(service));panels.put("Khách hàng",new CustomerPanel(service));panels.put("Nhân viên",new EmployeePanel(service));panels.put("Bán hàng",new SalesPanel(service));panels.put("Hóa đơn",new InvoicePanel(service));panels.put("Thống kê",new StatisticPanel(service));for(Map.Entry<String,BasePanel> e:panels.entrySet())cards.add(e.getValue(),e.getKey());add(sidebar(service,user),BorderLayout.WEST);add(cards);showCard("Dashboard");}
    private JPanel sidebar(AppService service,User user){JPanel side=new JPanel();side.setBackground(AppTheme.SIDEBAR);side.setPreferredSize(new Dimension(220,0));side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));JLabel logo=new JLabel("  SALES PRO");logo.setForeground(Color.WHITE);logo.setFont(AppTheme.FONT.deriveFont(Font.BOLD,23));logo.setAlignmentX(LEFT_ALIGNMENT);logo.setBorder(BorderFactory.createEmptyBorder(25,18,8,10));side.add(logo);JLabel account=new JLabel("  "+user.getUsername()+" • "+user.getRole());account.setForeground(new Color(0x94A3B8));account.setBorder(BorderFactory.createEmptyBorder(0,18,20,8));account.setAlignmentX(LEFT_ALIGNMENT);side.add(account);for(String name:panels.keySet()){SidebarButton b=new SidebarButton(icon(name)+"  "+name);b.setAlignmentX(LEFT_ALIGNMENT);b.addActionListener(e->showCard(name));buttons.put(name,b);side.add(b);}side.add(Box.createVerticalGlue());SidebarButton logout=new SidebarButton("↪  Đăng xuất");logout.setForeground(new Color(0xFCA5A5));logout.setAlignmentX(LEFT_ALIGNMENT);logout.addActionListener(e->{if(JOptionPane.showConfirmDialog(this,"Bạn muốn đăng xuất?","Đăng xuất",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){dispose();new LoginFrame(service).setVisible(true);}});side.add(logout);side.add(Box.createVerticalStrut(18));return side;}
    private String icon(String name){if("Dashboard".equals(name))return "⌂";if("Sản phẩm".equals(name))return "▣";if("Danh mục".equals(name))return "☷";if("Khách hàng".equals(name))return "♙";if("Nhân viên".equals(name))return "♟";if("Bán hàng".equals(name))return "▤";if("Hóa đơn".equals(name))return "▧";return "▥";}
    private void showCard(String name){BasePanel p=panels.get(name);if(p!=null)p.refreshData();layout.show(cards,name);buttons.forEach((n,b)->b.setActive(n.equals(name)));}
}
