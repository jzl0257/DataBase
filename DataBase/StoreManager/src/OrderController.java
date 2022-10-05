import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class OrderController implements ActionListener{
    OrderView myView;
    DataAccess myDAO;

    public OrderController(OrderView view, DataAccess dao) {
        myView = view;
        myDAO = dao;
        myView.btnSave.addActionListener(this);
        myView.btnLoad.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myView.btnLoad) {
            loadOrderAndDisplay();
        }
        if (e.getSource() == myView.btnSave) {
            saveOrder();
        }
    }

    private void loadOrderAndDisplay() {

        try {
            int orderID = Integer.parseInt(myView.txtOrderID.getText());
            OrderModel orderModel = myDAO.loadOrder(orderID);
            myView.txtOrderDate.setText(String.valueOf(orderModel.orderDate));
            myView.txtCustomer.setText(String.valueOf(orderModel.customer));
            myView.txtTotalCost.setText(String.valueOf(orderModel.totalCost));
            myView.txtTotalTax.setText(String.valueOf(orderModel.totalTax));

        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for orderID");
            ex.printStackTrace();
        }
    }

    private void saveOrder() {
        OrderModel orderModel = new OrderModel();

        try {
            int orderID = Integer.parseInt(myView.txtOrderID.getText());
            orderModel.orderID = orderID;
            orderModel.orderDate = myView.txtOrderDate.getText();
            orderModel.customer = myView.txtCustomer.getText();
            int totalCost = Integer.parseInt(myView.txtTotalCost.getText());
            orderModel.totalCost = totalCost;
            int totalTax = Integer.parseInt(myView.txtTotalTax.getText());
            orderModel.totalTax = totalTax;

            myDAO.saveOrder(orderModel);
            JOptionPane.showMessageDialog(null, "Order saved successfully!");
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for orderID");
            ex.printStackTrace();
        }
    }

}