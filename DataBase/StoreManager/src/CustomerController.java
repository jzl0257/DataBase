import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class CustomerController implements ActionListener{
    CustomerView myView;
    DataAccess myDAO;

    public CustomerController(CustomerView view, DataAccess dao) {
        myView = view;
        myDAO = dao;
        myView.btnSave.addActionListener(this);
        myView.btnLoad.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myView.btnLoad) {
            loadCustomerAndDisplay();
        }
        if (e.getSource() == myView.btnSave) {
            saveCustomer();
        }
    }

    private void loadCustomerAndDisplay() {

        try {
            int customerID = Integer.parseInt(myView.txtCustomerID.getText());
            CustomerModel customerModel = myDAO.loadCustomer(customerID);
            myView.txtCustomerName.setText(String.valueOf(customerModel.name));
            myView.txtCustomerPhone.setText(String.valueOf(customerModel.phone));
            myView.txtCustomerAddress.setText(String.valueOf(customerModel.address));
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for customerID");
            ex.printStackTrace();
        }
    }

    private void saveCustomer() {
        CustomerModel customerModel = new CustomerModel();

        try {
            int customerID = Integer.parseInt(myView.txtCustomerID.getText());
            customerModel.customerID = customerID;
            customerModel.name = myView.txtCustomerName.getText();
            customerModel.phone = myView.txtCustomerPhone.getText();
            customerModel.address = myView.txtCustomerAddress.getText();

            myDAO.saveCustomer(customerModel);
            JOptionPane.showMessageDialog(null, "Product saved successfully!");
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid format for customerID");
            ex.printStackTrace();
        }
    }

}
