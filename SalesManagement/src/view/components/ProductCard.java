package view.components;

import model.Product;
import utils.AppTheme;
import utils.CurrencyUtils;
import utils.ImageUtils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

public class ProductCard extends RoundedPanel {
    public ProductCard(Product product,String categoryName,int soldQuantity,Consumer<Product> onAdd){
        super(Color.WHITE);
        setLayout(new BorderLayout(0,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,12,10));
        setPreferredSize(new Dimension(245,330));
        setMinimumSize(new Dimension(210,330));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText(product.getDescription());

        JLabel image=new JLabel(ImageUtils.thumbnail(product.getImagePath(),220,132));
        image.setPreferredSize(new Dimension(220,132));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setToolTipText(product.getName());
        add(image,BorderLayout.NORTH);

        JPanel information=new JPanel(new BorderLayout(0,4));
        information.setOpaque(false);
        information.setPreferredSize(new Dimension(220,105));

        JLabel name=new JLabel("<html><body style='width:205px'>"+escape(product.getName())+"</body></html>");
        name.setPreferredSize(new Dimension(210,46));
        name.setFont(AppTheme.FONT.deriveFont(Font.BOLD,15));
        name.setForeground(AppTheme.TEXT);
        name.setToolTipText(product.getName());
        information.add(name,BorderLayout.NORTH);

        JPanel metadata=new JPanel(new GridLayout(1,2,6,0));
        metadata.setOpaque(false);
        JLabel code=new JLabel(product.getId());
        code.setFont(AppTheme.FONT.deriveFont(12f));
        code.setForeground(AppTheme.MUTED);
        JLabel category=new JLabel(categoryName,SwingConstants.RIGHT);
        category.setFont(AppTheme.FONT.deriveFont(12f));
        category.setForeground(AppTheme.MUTED);
        category.setToolTipText(categoryName);
        metadata.add(code);
        metadata.add(category);
        information.add(metadata,BorderLayout.CENTER);

        JLabel price=new JLabel(CurrencyUtils.format(product.getPrice()));
        price.setPreferredSize(new Dimension(210,28));
        price.setFont(AppTheme.FONT.deriveFont(Font.BOLD,18));
        price.setForeground(AppTheme.DANGER);
        information.add(price,BorderLayout.SOUTH);
        add(information,BorderLayout.CENTER);

        JPanel footer=new JPanel(new BorderLayout(5,6));
        footer.setOpaque(false);
        JPanel numbers=new JPanel(new GridLayout(1,2,6,0));
        numbers.setOpaque(false);
        JLabel stock=new JLabel("Tồn kho: "+product.getQuantity());
        stock.setFont(AppTheme.FONT.deriveFont(12f));
        stock.setForeground(AppTheme.MUTED);
        JLabel sold=new JLabel("Đã bán: "+soldQuantity,SwingConstants.RIGHT);
        sold.setFont(AppTheme.FONT.deriveFont(12f));
        sold.setForeground(AppTheme.MUTED);
        numbers.add(stock);
        numbers.add(sold);
        footer.add(numbers,BorderLayout.NORTH);

        RoundedButton addButton=new RoundedButton(
            product.getQuantity()>0?"Thêm vào giỏ":"Hết hàng",
            product.getQuantity()>0?AppTheme.PRIMARY:AppTheme.MUTED);
        addButton.setEnabled(product.getQuantity()>0);
        addButton.addActionListener(event->onAdd.accept(product));
        footer.add(addButton,BorderLayout.CENTER);
        add(footer,BorderLayout.SOUTH);
    }

    private static String escape(String text){
        return text.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
