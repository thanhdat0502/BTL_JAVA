package model;

public class Product {
    private String id, name, categoryId, description, imagePath;
    private long price;
    private int quantity;

    public Product(String id, String name, String categoryId, long price, int quantity, String description) {
        this(id,name,categoryId,price,quantity,description,"");
    }
    public Product(String id, String name, String categoryId, long price, int quantity, String description, String imagePath) {
        this.id=id; this.name=name; this.categoryId=categoryId; this.price=price; this.quantity=quantity; this.description=description;
        this.imagePath=imagePath==null?"":imagePath;
    }
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getCategoryId(){return categoryId;} public void setCategoryId(String v){categoryId=v;}
    public long getPrice(){return price;} public void setPrice(long v){price=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getImagePath(){return imagePath;} public void setImagePath(String v){imagePath=v==null?"":v;}
    @Override public String toString(){return name;}
}
