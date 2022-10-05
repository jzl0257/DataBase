// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

import com.google.gson.Gson;


// Server class
public class Server
{
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);



        // running infinite loop for getting
        // client request

        System.out.println("Starting server program!!!");

        int nClients = 0;


        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                nClients++;
                System.out.println("A new client is connected : " + s + " client number: " + nClients);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                processRequest(s, dis, dos);

                dis.close();
                dos.close();
                s.close();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

    private static void processRequest(Socket s, DataInputStream dis, DataOutputStream dos) throws Exception {
        Gson gson = new Gson();
        DataAccess dao = new SQLiteDataAdapter();
        ResponseModel res = new ResponseModel();

        dao.connect();

        String received = dis.readUTF();

        System.out.println("Message from client " + received);

        RequestModel req = gson.fromJson(received, RequestModel.class);

        if (req.code == RequestModel.EXIT_REQUEST) {
            System.out.println("Client " + s + " sends exit...");
            System.out.println("Closing this connection.");
            s.close();
            System.out.println("Connection closed");
        }
        else
        if (req.code == RequestModel.LOAD_REQUEST) { // load a note from database
            int id = Integer.parseInt(req.body);

            NoteModel note = dao.loadNote(id);

            if (note == null) {
                res.code = ResponseModel.DATA_NOT_FOUND;
                res.body = "";
            }
            else {
                res.code = ResponseModel.OK;
                res.body = gson.toJson(note);
            }
        }
        else
        if (req.code == RequestModel.SAVE_REQUEST) {

            System.out.println(req.body);
            NoteModel note = gson.fromJson(req.body, NoteModel.class);
            dao.saveNote(note);
            res.code = ResponseModel.OK;
            res.body = "";

        }
        else
        if (req.code == RequestModel.FIND_REQUEST) {

            String keyword = req.body;

            System.out.println("The Client wants to search with keyword = " + keyword);

            NoteListModel list = dao.searchNote(keyword);

            if (list.list.size() > 0) {
                res.code = ResponseModel.OK;
                res.body = gson.toJson(list);
            }
            else {
                res.code = ResponseModel.DATA_NOT_FOUND;
                res.body = "";
            }
        }
        else {
            res.code = ResponseModel.UNKNOWN_REQUEST;
            res.body = "";
        }

        String json = gson.toJson(res);
        System.out.println("JSON object of ResponseModel: " + json);

        dos.writeUTF(json);
        dos.flush();

        dao.disconnect();

    }
}





