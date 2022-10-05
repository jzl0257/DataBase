import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIClientView {
    private Client client;
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getLoadSaveNotesButton() {
        return loadSaveNotesButton;
    }

    public JButton getFindNotesButton() {
        return findNotesButton;
    }

    private JPanel mainPanel;
    private JButton loadSaveNotesButton;
    private JButton findNotesButton;



    public GUIClientView(Client client) {
        this.client = client;
        findNotesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Find Notes");
                frame.setContentPane(client.getFindNotesView().getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        loadSaveNotesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Load/Save Notes");
                frame.setContentPane(client.getCreateNoteView().getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });


    }
}
