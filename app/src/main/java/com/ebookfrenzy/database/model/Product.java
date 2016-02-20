package com.ebookfrenzy.database.model;

/**
 * Created by Patrick on 2/20/2016.
 */
public class Product {
    private int    _id;
    private String _name;
    private int    _quantity;

    public Product() {

    }

    public Product(int id, String name, int quantity) {
        this._id = id;
        this._name = name;
        this._quantity = quantity;
    }

    public Product(String name, int quantity) {
        this._name = name;
        this._quantity = quantity;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setProductName(String name) {
        this._name = name;
    }

    public String getProductName() {
        return this._name;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }
}
