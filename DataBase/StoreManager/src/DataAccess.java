public interface DataAccess {

    // product information
    void connect();

    void saveProduct(ProductModel product);

    ProductModel loadProduct(int productID);

    // customers information
    void saveCustomer(CustomerModel customer);

    CustomerModel loadCustomer(int customerID);

    // Order information
    void saveOrder(OrderModel order);

    OrderModel loadOrder(int orderID);


}
