package com.example.aakash.cartmobile;

/**
 * Created by Aakash on 23-03-2018.
 */

public class ListItems {



    private String ProductName;
    private String ProductPrice;
    private String ProductQuantity;

    public ListItems() {
        // This is default constructor.
    }
    public ListItems(String name, String price, String quantity) {

        this.ProductName = name;
        this.ProductPrice = price;
        this.ProductQuantity = quantity;

        // This is default constructor.
    }

    public String getProductName() {

        return ProductName;
    }

    public void setProductName(String name) {

        this.ProductName = name;
    }

    public String getProductPrice() {

        return ProductPrice;
    }

    public void setProductPrice(String price) {

        this.ProductPrice = price;
    }

    public String getProductQuantity() {

        return ProductQuantity;
    }

    public void setProductQuantity(String quantity) {

        this.ProductQuantity = quantity;
    }

}