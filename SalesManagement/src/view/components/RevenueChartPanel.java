package view.components;

import utils.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RevenueChartPanel extends JPanel {
    private Map<LocalDate,Long> data=Collections.emptyMap();
    public RevenueChartPanel(){setOpaque(false);setPreferredSize(new Dimension(500,220));}
    public void setData(Map<LocalDate,Long> source){data=new TreeMap<>(source);repaint();}
    @Override protected void paintComponent(Graphics g){super.paintComponent(g);Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);int left=48,right=18,top=22,bottom=38,w=getWidth()-left-right,h=getHeight()-top-bottom;g2.setColor(AppTheme.BORDER);g2.drawLine(left,top,left,top+h);g2.drawLine(left,top+h,left+w,top+h);if(data.isEmpty()){g2.setColor(AppTheme.MUTED);g2.drawString("Chưa có dữ liệu doanh thu",left+20,top+h/2);g2.dispose();return;}java.util.List<Map.Entry<LocalDate,Long>> entries=new ArrayList<>(data.entrySet());if(entries.size()>7)entries=entries.subList(entries.size()-7,entries.size());long max=entries.stream().mapToLong(Map.Entry::getValue).max().orElse(1);int gap=Math.max(8,w/entries.size());int bar=Math.min(48,gap-10);DateTimeFormatter f=DateTimeFormatter.ofPattern("dd/MM");for(int i=0;i<entries.size();i++){Map.Entry<LocalDate,Long> e=entries.get(i);int bh=(int)(h*0.82*e.getValue()/max);int x=left+i*gap+(gap-bar)/2,y=top+h-bh;g2.setColor(AppTheme.PRIMARY);g2.fillRoundRect(x,y,bar,bh,8,8);g2.setColor(AppTheme.MUTED);g2.setFont(AppTheme.FONT.deriveFont(11f));g2.drawString(e.getKey().format(f),x-1,top+h+20);}g2.dispose();}
}
