package model;

public class InvoiceDetail {
    private String invoiceId,productId;
    private int quantity;
    private long unitPrice,totalPrice;
    public InvoiceDetail(String invoiceId,String productId,int quantity,long unitPrice,long totalPrice){this.invoiceId=invoiceId;this.productId=productId;this.quantity=quantity;this.unitPrice=unitPrice;this.totalPrice=totalPrice;}
    public String getInvoiceId(){return invoiceId;} public void setInvoiceId(String v){invoiceId=v;}
    public String getProductId(){return productId;} public void setProductId(String v){productId=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
    public long getUnitPrice(){return unitPrice;} public void setUnitPrice(long v){unitPrice=v;}
    public long getTotalPrice(){return totalPrice;} public void setTotalPrice(long v){totalPrice=v;}
}
