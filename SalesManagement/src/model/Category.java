package model;

public class Category {
    private String id, name, description;
    public Category(String id,String name,String description){this.id=id;this.name=name;this.description=description;}
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    @Override public String toString(){return name;}
}
