import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.*;

public class SQLiteDataAdapter implements DataAccess {
    Connection conn = null;

    @Override
    public void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:store.db";
            // create a connection to the database
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);

            if (conn == null)
                System.out.println("Cannot make the connection!!!");
            else
                System.out.println("The connection object is " + conn);

            System.out.println("Connection to SQLite has been established.");

            /* Test data!!!
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            while (rs.next())
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
            */

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();

            if (loadProduct(product.productID) == null) {           // this is a new product!
                stmt.execute("INSERT INTO Product(productID, name, price, quantity) VALUES ("
                        + product.productID + ","
                        + '\'' + product.name + '\'' + ","
                        + product.price + ","
                        + product.quantity + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Product SET "
                        + "productID = " + product.productID + ","
                        + "name = " + '\'' + product.name + '\'' + ","
                        + "price = " + product.price + ","
                        + "quantity = " + product.quantity +
                        " WHERE productID = " + product.productID
                );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ProductModel loadProduct(int productID) {
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE ProductID = " + productID);
            if (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.name = rs.getString(2);
                product.price = rs.getDouble(3);
                product.quantity = rs.getDouble(4);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return product;
    }
    @Override
    public void saveCustomer(CustomerModel customer) {
        try {
            Statement stmt = conn.createStatement();

            if (loadCustomer(customer.customerID) == null) {           // this is a new customer!
                stmt.execute("INSERT INTO Customer(customerID, name, phone, address) VALUES ("
                        + customer.customerID + ","
                        + '\'' + customer.name + '\'' + ","
                        + '\'' + customer.phone + '\'' + ","
                        + '\'' + customer.address + '\'' + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Customer SET "
                        + "customerID = " + customer.customerID + ","
                        + "name = " + '\'' + customer.name + '\'' + ","
                        + "phone = " + '\'' + customer.phone + '\'' + ","
                        + "address = " + '\'' + customer.address + '\'' +
                        " WHERE customerID = " + customer.customerID
                );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CustomerModel loadCustomer(int customerID) {
        CustomerModel customer = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer WHERE CustomerID = " + customerID);
            if (rs.next()) {
                customer = new CustomerModel();
                customer.customerID = rs.getInt(1);
                customer.name = rs.getString(2);
                customer.phone = rs.getString(3);
                customer.address = rs.getString(4);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    @Override
    public void saveOrder(OrderModel order) {
        try {
            Statement stmt = conn.createStatement();

            if (loadOrder(order.orderID) == null) {           // this is a new order!
                stmt.execute("INSERT INTO Orders(OrderID, OrderDate, Customer, TotalCost, TotalTax) VALUES ("
                        + order.orderID + ","
                        + '\'' + order.orderDate + '\'' + ","
                        + '\'' + order.customer + '\'' + ","
                        + order.totalCost + ","
                        + order.totalTax + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Orders SET "
                        + "orderID = " + order.orderID + ","
                        + "orderDate = " + '\'' + order.orderDate + '\'' + ","
                        + "customer = " + '\'' + order.customer + '\'' + ","
                        + "totalCost = " + order.totalCost + ","
                        + "totalTax = " + order.totalTax +
                        " WHERE orderID = " + order.orderID
                );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public OrderModel loadOrder(int orderID) {
        OrderModel order = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders WHERE OrderID = " + orderID);
            if (rs.next()) {
                order = new OrderModel();
                order.orderID = rs.getInt(1);
                order.orderDate = rs.getString(2);
                order.customer = rs.getString(3);
                order.totalCost = rs.getDouble(4);
                order.totalTax = rs.getDouble(5);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }



}
