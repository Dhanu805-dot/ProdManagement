package products;

import java.sql.Connection;
import java.util.Scanner;

public class ProdCrud {
    static Connection con=null;
    static String tableName="Product_Categories";
    static String tName="Orders";
    public static void viewProduct(Product_Categories pc){
        DbOperations.viewProducts(tableName);
    }
    public static void createProduct(Product_Categories pc){
        DbOperations.create(tableName,pc);
    }
    public static void updateProduct(Product_Categories pc,int pid){
        DbOperations.update(tableName,pc,pid);
    }
    public static void deleteProduct(int pid){
        DbOperations.delete(tableName,pid);
    }
    public static void placeOrder(int id){
        DbOperations.placeOrder(tName,id);
    }
    public static void generateInvoice(int id){
        DbOperations.generateInvoice(id);
    }
    public static void main(String[] args) throws Exception {
        con=ProdUtil.getConnection();
        Scanner scanner=new Scanner(System.in);
        Product_Categories pc=new Product_Categories();
        int mainChoice;
        do{
            System.out.println("1. Product Management");
            System.out.println("2. View Product");
            System.out.println("3. Place Order");
            System.out.println("4. Generate Invoice");
            System.out.println("5. Exit");
            System.out.println("enter your choice:");
            mainChoice= scanner.nextInt();

            switch(mainChoice){
                case 1: int subChoice;
                do{
                    System.out.println("welcome to Ecommerce World");
                    System.out.println("1. Create Product");
                    System.out.println("2. Update Product");
                    System.out.println("3. Delete Product");
                    System.out.println("4. Exit ");
                    System.out.println("Enter your choice");
                    subChoice=scanner.nextInt();
                    switch (subChoice){
                        case 1:
                            System.out.println("enter product ID");
                            pc.setCategory_id(scanner.nextInt());
                            scanner.nextLine();

                            System.out.println("enter product name");
                            pc.setCategory_name(scanner.nextLine());

                            System.out.println("enter status of product");
                            pc.setStatus(scanner.nextLine());

                            System.out.println("enter available stocks");
                            pc.setAvailable_Stock(scanner.nextInt());

                            System.out.println("enter the price of product");
                            pc.setPrice(scanner.nextDouble());
                            createProduct(pc);
                            break;

                        case 2:
                            System.out.println("enter product id to update");
                            int pid=scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("enter product name");
                            pc.setCategory_name(scanner.nextLine());

                            System.out.println("enter status of product");
                            pc.setStatus(scanner.nextLine());

                            System.out.println("enter available stocks");
                            pc.setAvailable_Stock(scanner.nextInt());

                            System.out.println("price of the product");
                            pc.setPrice(scanner.nextDouble());
                            updateProduct(pc,pid);
                            break;

                        case 3:
                            System.out.println("enter product id to delete");
                            int id=scanner.nextInt();
                            deleteProduct(id);
                            break;
                    }
                }while (subChoice!=4);
                break;

                case 2:
                    System.out.println("Displaying Products");
                    viewProduct(pc);
                    break;

                case 3:
                    System.out.println("select products to place order");
                    int pid=scanner.nextInt();
                    placeOrder(pid);
                    break;

                case 4:
                    System.out.println("enter product details to generate invoice");
                    System.out.println("Enter Product Id ");
                    int id=scanner.nextInt();
                    System.out.println("invoice is generating please wait...");
                    generateInvoice(id);
                    break;
                default:
                    System.out.println("😱 Op's Invalid Entry");
            }
        }while (mainChoice!=4);

    }
}
