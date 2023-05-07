package com.example.shop;

public class DateIdea {
    private String id;
    private String name;
    private String price;
    private Integer cartedCount;

    public DateIdea(String name, String price, Integer cartedCount) {
        this.name = name;
        this.price = price;
        this.cartedCount = cartedCount;
    }

    public DateIdea() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public String _getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
