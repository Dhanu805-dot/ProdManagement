package products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Scanner;

public class DbOperations {
    static Connection con=null;
    static Product_Categories pc=new Product_Categories();
    static Orders orders=new Orders();

    static void create(String tableName,Product_Categories pc){
        try{
            con=ProdUtil.getConnection();
            String query="INSERT INTO "+tableName+"(CATEGORY_ID,CATEGORY_NAME,STATUS,AVAILABLE_STOCK,PRICE)"
                    +"VALUES(?,?,?,?,?)";
            PreparedStatement pst=con.prepareStatement(query);
            pst.setInt(1,pc.getCategory_id());
            pst.setString(2, pc.getCategory_name());
            pst.setString(3, pc.getStatus());
            pst.setInt(4,pc.getAvailable_Stock());
            pst.setDouble(5,pc.getPrice());
            int rpws=pst.executeUpdate();
            if(rpws>0){
                System.out.println("Product Added Successfully");
            }
            else{
                System.out.println("Failed to Add Product...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void update(String tableName, Product_Categories pc, int id) {
        try {
            con = ProdUtil.getConnection();

            String query = "UPDATE " + tableName +
                    " SET CATEGORY_NAME = ?, STATUS = ?, AVAILABLE_STOCK = ?, PRICE = ?" +
                    " WHERE CATEGORY_ID = ?";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, pc.getCategory_name());
            pst.setString(2, pc.getStatus());
            pst.setInt(3, pc.getAvailable_Stock());
            pst.setDouble(4, pc.getPrice());
            pst.setInt(5, id); // using `id` in WHERE clause

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Product Updated Successfully");
            } else {
                System.out.println("❌ Failed to Update Product");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void delete(String tableName, int id) {
        try {
            Connection con = ProdUtil.getConnection();

            String deleteOrders = "DELETE FROM Orders WHERE CATEGORY_ID = ?";
            PreparedStatement pst1 = con.prepareStatement(deleteOrders);
            pst1.setInt(1, id);
            pst1.executeUpdate();

            String deleteCategory = "DELETE FROM Product_Categories WHERE CATEGORY_ID = ?";
            PreparedStatement pst2 = con.prepareStatement(deleteCategory);
            pst2.setInt(1, id);
            pst2.executeUpdate();

            PreparedStatement pst = con.prepareStatement(deleteCategory);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product Deleted Successfully 👍.");
            } else {
                System.out.println("Failed to Delete Product.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void viewProducts(String tableName){
      try{
          Connection con=ProdUtil.getConnection();
          String query="SELECT * FROM " + tableName;
          PreparedStatement pst= con.prepareStatement(query);
          ResultSet rs=pst.executeQuery();
          while(rs.next()){
              System.out.println("--------------------------------------");
              System.out.println("Product_ID:"+rs.getInt("Category_ID"));
              System.out.println("Product Name:"+rs.getString("Category_Name"));
              System.out.println("Status: "+rs.getString("Status"));
              System.out.println("Available Stocks"+rs.getInt("Available_Stock"));
              System.out.println("Price"+rs.getDouble("Price"));
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
    }
    static void placeOrder(String tName, int id) {
        Scanner sc = new Scanner(System.in);
        Connection con = null;

        try {
            con = ProdUtil.getConnection();
            con.setAutoCommit(false);


            String checkQuery = "SELECT CATEGORY_NAME, AVAILABLE_STOCK, PRICE FROM Product_Categories WHERE CATEGORY_ID = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Product not found.");
                return;
            }

            String categoryName = rs.getString("CATEGORY_NAME");
            int availableStock = rs.getInt("AVAILABLE_STOCK");
            double price = rs.getDouble("PRICE");


            // Step 2: Ask user for quantity
            System.out.print("Enter quantity to order: ");
            int quantity = sc.nextInt();

            if (quantity <= 0) {
                System.out.println("❌ Quantity must be greater than 0.");
                return;
            }

            if (quantity > availableStock) {
                throw new OutOfStockException();
//                System.out.println("❌ Not enough stock available. Only " + availableStock + " left.");
//                return;
            }

            double totalAmount = quantity * price;

            // Step 3: Insert into Orders
            String insertQuery = "INSERT INTO Orders (ORDER_ID, CATEGORY_ID, CATEGORY_NAME, ORDERED_QUANTITY, PRICE, TOTAL_AMOUNT, ORDERED_DATE) VALUES (100,?, ?, ?, ?,?,?)";
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            insertStmt.setInt(1, id);
//            insertStmt.setInt(2,5);
            insertStmt.setString(2, categoryName);
            insertStmt.setInt(3, quantity);
            insertStmt.setDouble(4,price);
            insertStmt.setDouble(5, totalAmount);
            insertStmt.setDate(6,java.sql.Date.valueOf(LocalDate.now()));
            int inserted = insertStmt.executeUpdate();

            // Step 4: Update stock
            String updateStockQuery = "UPDATE Product_Categories SET AVAILABLE_STOCK = AVAILABLE_STOCK - ? WHERE CATEGORY_ID = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateStockQuery);
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, id);
            int updated = updateStmt.executeUpdate();

            String orderQuery="SELECT * FROM ORDERS " + tName;
            PreparedStatement pst= con.prepareStatement(orderQuery);
            ResultSet resultSet=pst.executeQuery(orderQuery);
            while(resultSet.next()) {
                System.out.println("View your Orders Here");
                System.out.println("------------------------------------------------------------");
                System.out.println("Order_ID:" + resultSet.getInt("Order_ID"));
                System.out.println("Category_ID:" + resultSet.getInt("Category_ID"));
                System.out.println("Product Name:" + resultSet.getString("Category_Name"));
                System.out.println("Quantity:" + resultSet.getInt("Ordered_Quantity"));
                System.out.println("Price per Unit:" + resultSet.getDouble("Price"));
                System.out.println("Total Amount:" + resultSet.getDouble("Total_Amount"));
                System.out.println("Ordered Date:" + resultSet.getDate("Ordered_Date"));
                System.out.println("---------------------------------------------------------------");
            }

            if (inserted > 0 && updated > 0) {
                con.commit(); // Save both actions
                System.out.println("✅ Order placed successfully. Total Amount: ₹" + totalAmount);

                // Show remaining stock
                int remainingStock = availableStock - quantity;
                System.out.println("📦 Remaining stock: " + remainingStock);
            } else {
                con.rollback(); // Revert any changes
                System.out.println("❌ Failed to place order.");
            }

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    public static void generateInvoice(int id) {
        final double CGST_RATE = 0.09;  // 9%
        final double SGST_RATE = 0.09;  // 9%

        Connection con = null;

        try {
            con = ProdUtil.getConnection();

            String query = "SELECT CATEGORY_NAME, PRICE,ORDERED_QUANTITY FROM ORDERS  WHERE CATEGORY_ID = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Product with ID " + id + " not found.");
                return;
            }

            String categoryName = rs.getString("CATEGORY_NAME");
            double price = rs.getDouble("PRICE");

            int quant=rs.getInt("ORDERED_QUANTITY");
            double subtotal = price *quant ;
            double cgst = subtotal * CGST_RATE;
            double sgst = subtotal * SGST_RATE;
            double total = subtotal + cgst + sgst;

            // Print the invoice
            System.out.println("\n🧾 -------- INVOICE --------");
            System.out.println("Category ID   : " + id);
            System.out.println("Category Name : " + categoryName);
            System.out.println("Quantity      : " + quant);
            System.out.printf("Price/Unit    : %.2f\n", price);
            System.out.println("----------------------------");
            System.out.printf("Subtotal      : %.2f\n", subtotal);
            System.out.printf("CGST (9%%)     : %.2f\n", cgst);
            System.out.printf("SGST (9%%)     : %.2f\n", sgst);
            System.out.printf("Total Amount  : %.2f\n", total);
            System.out.println("----------------------------\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
