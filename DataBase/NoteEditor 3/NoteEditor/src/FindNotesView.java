import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class FindNotesView {
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JTextField queryTextField;
    private JButton searchButton;
    private JScrollPane scrollPanel;
    private JTable noteTable;
    private DefaultTableModel tableModel;

    public FindNotesView() {
        tableModel = new DefaultTableModel();
        noteTable.setModel(tableModel);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String query = queryTextField.getText();
                searchAndDisplayNotes(query);
            }
        });
    }

    private void searchAndDisplayNotes(String query) {
        try
        {

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            // -> connect to the server

            Gson gson = new Gson();

            // -> convert the RequestModel to json(text)

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.FIND_REQUEST; // id of the request
            req.body = query; // "date"

            String json = gson.toJson(req);
            dos.writeUTF(json); // send the request to the server
            // the program will pause here to wait for the response from the server
            String received = dis.readUTF();  //

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            if (res.code == ResponseModel.UNKNOWN_REQUEST) {
                System.out.println("The request is not recognized by the Server");
            }
            else

            if (req.code == RequestModel.LOAD_REQUEST) {
                NoteModel model = gson.fromJson(res.body, NoteModel.class);
                System.out.println("Receiving a NoteModel object");
                System.out.println("NoteID = " + model.id);
                System.out.println("Title = " + model.title);
                System.out.println("Body = " + model.body);
            }
            else
            if (req.code == RequestModel.FIND_REQUEST) {
                NoteListModel list = gson.fromJson(res.body, NoteListModel.class);


                String[] columnNames = new String[] {"ID", "Title", "Body"};
                tableModel.setColumnIdentifiers(columnNames);

                int row = list.getList().size();
                int col = 3;

                String[][] data = new String[row][col];

                for (int i=0; i<row; i++) {
                    data[i][0] = String.valueOf(list.getList().get(i).id);
                    data[i][1] = list.getList().get(i).title;
                    data[i][2] = list.getList().get(i).body;
                    tableModel.addRow(data[i]);
                }


//                noteTable = new JTable(data, columnNames);
//                scrollPanel.updateUI();
                // TODO -> update the ui


                System.out.println("Receiving a NoteListModel object of size = " + list.list.size());
            }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
