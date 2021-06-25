package com.example.qiupys.wateroder.supplement;
public class RateItem {
    private int image;
    private String name;
    private String price;
    private String surplus;
    private String supplier;
    private String warehouse;
    private String waterid;

    public RateItem(int image, String name, String price) {
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public RateItem(int image, String name, String price,String surplus,String supplier,String warehouse) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.surplus=surplus;
        this.supplier=supplier;
        this.warehouse=warehouse;
    }

    public RateItem(){

    }

    public RateItem(String[] values){
        this.image = 0;
        this.name=values[1];
        this.price=values[2];
        this.surplus=values[3];
        this.supplier=values[4];
        this.warehouse=values[5];
        this.waterid=values[6];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
}
