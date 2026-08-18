package model;

public class Customer {
    private String id,name,phone,email,address;
    public Customer(String id,String name,String phone,String email,String address){this.id=id;this.name=name;this.phone=phone;this.email=email;this.address=address;}
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    @Override public String toString(){return id+" - "+name;}
}
