package utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public final class ImageUtils {
    private static final Map<String,ImageIcon> CACHE=new HashMap<>();
    private ImageUtils(){}

    public static String chooseAndImport(Component parent,String productId)throws IOException{
        JFileChooser chooser=new JFileChooser();
        chooser.setDialogTitle("Chọn ảnh sản phẩm");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("Ảnh PNG, JPG, JPEG, GIF","png","jpg","jpeg","gif"));
        if(chooser.showOpenDialog(parent)!=JFileChooser.APPROVE_OPTION)return null;

        File source=chooser.getSelectedFile();
        String extension=extension(source.getName());
        String safeId=(productId==null||productId.trim().isEmpty()?"product":productId.trim()).replaceAll("[^A-Za-z0-9_-]","_");
        Path imageDirectory=ProjectPaths.projectDirectory().resolve("assets").resolve("products");
        Files.createDirectories(imageDirectory);
        String fileName=safeId+"_"+System.currentTimeMillis()+"."+extension;
        Path target=imageDirectory.resolve(fileName);
        Files.copy(source.toPath(),target,StandardCopyOption.REPLACE_EXISTING);
        CACHE.clear();
        return "assets/products/"+fileName;
    }

    public static ImageIcon thumbnail(String imagePath,int width,int height){
        String key=(imagePath==null?"":imagePath)+"@"+width+"x"+height;
        ImageIcon cached=CACHE.get(key);
        if(cached!=null)return cached;
        BufferedImage canvas=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=canvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0xEFF6FF));
        g.fillRoundRect(0,0,width,height,12,12);
        BufferedImage source=readImage(imagePath);
        if(source==null){
            g.setColor(AppTheme.PRIMARY);
            g.setFont(AppTheme.FONT.deriveFont(Font.BOLD,Math.max(11f,width/6f)));
            String text="ẢNH";
            int x=(width-g.getFontMetrics().stringWidth(text))/2;
            int y=(height+g.getFontMetrics().getAscent()-g.getFontMetrics().getDescent())/2;
            g.drawString(text,x,y);
        }else{
            double scale=Math.min((double)width/source.getWidth(),(double)height/source.getHeight());
            int scaledWidth=Math.max(1,(int)(source.getWidth()*scale));
            int scaledHeight=Math.max(1,(int)(source.getHeight()*scale));
            Image scaled=source.getScaledInstance(scaledWidth,scaledHeight,Image.SCALE_SMOOTH);
            g.drawImage(scaled,(width-scaledWidth)/2,(height-scaledHeight)/2,null);
        }
        g.dispose();
        ImageIcon icon=new ImageIcon(canvas);
        CACHE.put(key,icon);
        return icon;
    }

    private static BufferedImage readImage(String imagePath){
        if(imagePath==null||imagePath.trim().isEmpty())return null;
        try{
            Path path=new File(imagePath).isAbsolute()?new File(imagePath).toPath():ProjectPaths.projectDirectory().resolve(imagePath);
            if(Files.notExists(path))return null;
            return ImageIO.read(path.toFile());
        }catch(IOException e){return null;}
    }

    private static String extension(String name){
        int dot=name.lastIndexOf('.');
        if(dot<0||dot==name.length()-1)return "png";
        return name.substring(dot+1).toLowerCase();
    }
}
