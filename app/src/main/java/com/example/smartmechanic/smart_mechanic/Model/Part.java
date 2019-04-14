package com.example.smartmechanic.smart_mechanic.Model;

public class Part {
    private String Name,Image,Description,Price,Model,Discount,MenuId;

    public Part() {
    }

    public Part(String name, String image, String description, String price, String model, String discount, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Model = model;
        Discount = discount;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
