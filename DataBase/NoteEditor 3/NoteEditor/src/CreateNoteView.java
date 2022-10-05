import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Statement;

public class CreateNoteView {
    private JTextField idTextField;
    private JTextField titleTextField;
    private JTextField bodyTextField;
    private JButton saveButton;
    private JButton loadButton;

    public void setIdTextField(JTextField idTextField) {
        this.idTextField = idTextField;
    }

    public void setTitleTextField(JTextField titleTextField) {
        this.titleTextField = titleTextField;
    }

    public void setBodyTextField(JTextField bodyTextField) {
        this.bodyTextField = bodyTextField;
    }

    private DataAccess dao;
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;

    public CreateNoteView() {
        dao = new SQLiteDataAdapter();

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idTextField.getText();
                loadAndDisplayNote(id);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NoteModel noteModel = new NoteModel();
                noteModel.id = Integer.parseInt(idTextField.getText());
                noteModel.title = titleTextField.getText();
                noteModel.body = bodyTextField.getText();
                saveNoteToServer(noteModel);
            }
        });
    }

    private void saveNoteToServer(NoteModel noteModel) {

        try
        {

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            Gson gson = new Gson();

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.SAVE_REQUEST;
            req.body = gson.toJson(noteModel);

            String json = gson.toJson(req);
            dos.writeUTF(json); // send the request to the server
            // the program will pause here to wait for the response from the server
            String received = dis.readUTF();  //

            NoteModel note = gson.fromJson(req.body, NoteModel.class);
            System.out.println("Server response:" + received);
            System.out.println("NoteID = " + note.id);
            System.out.println("Title = " + note.title);
            System.out.println("Body = " + note.body);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);


            NoteModel model = gson.fromJson(res.body, NoteModel.class);


            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void loadAndDisplayNote(String id) {
        try
        {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            Gson gson = new Gson();

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.LOAD_REQUEST;
            req.body = id;

            String json = gson.toJson(req);
            dos.writeUTF(json); // send the request to the server
            // the program will pause here to wait for the response from the server
            String received = dis.readUTF();  //

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);



            NoteModel model = gson.fromJson(res.body, NoteModel.class);
            System.out.println("Receiving a NoteModel object");
            System.out.println("NoteID = " + model.id);
            System.out.println("Title = " + model.title);
            System.out.println("Body = " + model.body);

            idTextField.setText(id);
            titleTextField.setText(model.title);
            bodyTextField.setText(model.body);


            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
