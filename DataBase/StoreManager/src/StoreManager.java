public class StoreManager {

    private static StoreManager     instance = null;

    private SQLiteDataAdapter dao;


    private ProductView productView = null;

    public ProductView getProductView() {
        return productView;
    }
    // customer
    public CustomerView customerView = null;
    public CustomerView getCustomerView() {
        return customerView;
    }
    private CustomerController customerController = null;

    private ProductController productController = null;

    // order
    public OrderView orderView = null;
    public OrderView getOrderView() {return orderView; }
    private OrderController orderController = null;

    public static StoreManager getInstance() {
        if (instance == null)
            instance = new StoreManager("SQLite");
        return instance;
    }

    public SQLiteDataAdapter getDataAccess() {
        return dao;
    }

    private StoreManager(String db) {
        // do some initialization here!!!
        if (db.equals("SQLite"))
            dao = new SQLiteDataAdapter();

        dao.connect();
        productView = new ProductView();
        productController = new ProductController(productView, dao);
        customerView = new CustomerView();
        customerController = new CustomerController(customerView, dao);

        orderView = new OrderView();
        orderController = new OrderController(orderView, dao);

    }



}
