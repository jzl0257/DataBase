import javax.swing.*;
import java.awt.*;
public class mainMenu extends JFrame{
    public JButton mainPro = new JButton("Product data");
    public JButton mainCus = new JButton("Customer data");
    public JButton mainOrd = new JButton("Order data");

    public mainMenu() {
        this.setTitle("Store Manager Main Menu");
        this.setSize(new Dimension(600,300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(mainPro);
        buttonPanel.add(mainCus);
        buttonPanel.add(mainOrd);
        this.getContentPane().add(buttonPanel);
    }

}
