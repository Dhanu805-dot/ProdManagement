package products;

public class OutOfStockException extends Exception{
    @Override
    public String getMessage() {
        return "❌ Sorry Not enough stock available. Enter a Valid Quantity";
    }
}
