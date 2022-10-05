// Java implementation for a client
// Save file as Client.java

import com.google.gson.Gson;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class
public class Client {
    public GUIClientView getClientView() {
        return clientView;
    }

    private GUIClientView clientView;

    public FindNotesView getFindNotesView() {
        return findNotesView;
    }

    public CreateNoteView getCreateNoteView() {
        return createNoteView;
    }

    private FindNotesView findNotesView;
    private CreateNoteView createNoteView;

    public Client() {
        clientView = new GUIClientView(this);
        findNotesView  = new FindNotesView();
        createNoteView = new CreateNoteView();


    }

    public static void main(String[] args) {
        Client client = new Client();
        JFrame frame = new JFrame("GUIClientView");
        frame.setContentPane(client.getClientView().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*
    public static void main(String[] args) throws IOException
    {
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
            req.code = RequestModel.FIND_REQUEST;
            req.body = "date";

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

                        System.out.println("Receiving a NoteListModel object of size = " + list.list.size());
                    }
            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

     */
}
