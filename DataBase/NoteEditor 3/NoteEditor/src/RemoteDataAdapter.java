import com.google.gson.Gson;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;

public class RemoteDataAdapter implements DataAccess {
    Connection conn = null;
    Gson gson = new Gson();

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void saveNote(NoteModel note) {
        /*try
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
            req.body = gson.toJson(note);

            String json = gson.toJson(req);
            dos.writeUTF(json); // send the request to the server
            // the program will pause here to wait for the response from the server
            String received = dis.readUTF();  //

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);

            NoteModel model = gson.fromJson(res.body, NoteModel.class);


            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }*/

    }

    @Override
    public NoteModel loadNote(int id) {
       /* try
        {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            Gson gson = new Gson();

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            String ID = Integer.toString(id);
            RequestModel req = new RequestModel();
            req.code = RequestModel.LOAD_REQUEST;
            req.body = ID;

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

            dis.close();
            dos.close();
            s.close();
        } catch (Exception e){
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public NoteListModel searchNote(String keyword) {
        Socket s = null;
        NoteListModel list = null;
        try {
            s = new Socket("localhost", 5056);
            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            RequestModel req = new RequestModel();
            req.code = RequestModel.FIND_REQUEST;
            req.body = keyword;

            String json = gson.toJson(req);
            dos.writeUTF(json);

            String received = dis.readUTF();

            System.out.println("Server response:" + received);

            ResponseModel res = gson.fromJson(received, ResponseModel.class);
            list = gson.fromJson(res.body, NoteListModel.class);

            System.out.println("Receiving a NoteListModel object of size = " + list.list.size());


            dis.close();
            dos.close();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}
