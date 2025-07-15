package products;

public class Orders {
    private int category_Id;
    private String category_name;
    private int ordered_Quantity;
    private double Price;

    public int getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(int category_Id) {
        this.category_Id = category_Id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getOrdered_Quantity() {
        return ordered_Quantity;
    }

    public void setOrdered_Quantity(int ordered_Quantity) {
        this.ordered_Quantity = ordered_Quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
