import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainMenuController implements ActionListener{
    mainMenu menuView;

    public mainMenuController(mainMenu view) {
        menuView = view;
        menuView.mainPro.addActionListener(this);
        menuView.mainCus.addActionListener(this);
        menuView.mainOrd.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuView.mainPro) {
            loadProdutctView();
        }
        if (e.getSource() == menuView.mainCus) {
            loadCustomerView();
        }
        if (e.getSource() == menuView.mainOrd) {
            loadOrderView();
        }
    }

    private void loadProdutctView() {
        StoreManager.getInstance().getProductView().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StoreManager.getInstance().getProductView().setVisible(true);
    }

    private void loadCustomerView() {
        StoreManager.getInstance().getCustomerView().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StoreManager.getInstance().getCustomerView().setVisible(true);
    }

    private void loadOrderView() {
        StoreManager.getInstance().getOrderView().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StoreManager.getInstance().getOrderView().setVisible(true);
    }


}
