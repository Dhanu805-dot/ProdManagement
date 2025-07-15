package products;

import java.sql.Date;

public class Product_Categories {
     private int category_id;
     private String category_name;
    private String status;
    private int available_Stock;
    private double Price;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAvailable_Stock() {
        return available_Stock;
    }

    public void setAvailable_Stock(int available_Stock) {
        this.available_Stock = available_Stock;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setDate(int i, Date date) {
    }
}
