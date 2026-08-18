package model;

public class Employee {
    private String id,name,phone,email,position;
    public Employee(String id,String name,String phone,String email,String position){this.id=id;this.name=name;this.phone=phone;this.email=email;this.position=position;}
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPosition(){return position;} public void setPosition(String v){position=v;}
    @Override public String toString(){return id+" - "+name;}
}
