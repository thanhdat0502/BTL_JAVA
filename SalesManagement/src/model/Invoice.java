package model;

import java.time.LocalDateTime;

public class Invoice {
    private String id,customerId,employeeId;
    private LocalDateTime createdAt;
    private long totalAmount;
    public Invoice(String id,LocalDateTime createdAt,String customerId,String employeeId,long totalAmount){this.id=id;this.createdAt=createdAt;this.customerId=customerId;this.employeeId=employeeId;this.totalAmount=totalAmount;}
    public String getId(){return id;} public void setId(String v){id=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public String getCustomerId(){return customerId;} public void setCustomerId(String v){customerId=v;}
    public String getEmployeeId(){return employeeId;} public void setEmployeeId(String v){employeeId=v;}
    public long getTotalAmount(){return totalAmount;} public void setTotalAmount(long v){totalAmount=v;}
}
