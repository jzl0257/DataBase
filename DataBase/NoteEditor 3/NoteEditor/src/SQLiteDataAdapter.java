import java.sql.*;

public class SQLiteDataAdapter implements DataAccess {
    Connection conn = null;

    @Override
    public void disconnect() {
        try {
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:notes.db";
            // create a connection to the database
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);

            System.out.println(" conn = " + conn);

            if (conn == null)
                System.out.println("Cannot make the connection!!!");
            else
                System.out.println("The connection object is " + conn);

            System.out.println("Connection to SQLite has been established.");

            /* Test data
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Notes");

            while (rs.next())
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
*/

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveNote(NoteModel note) {
        try {
            Statement stmt = conn.createStatement();

            if (loadNote(note.id) == null) {// this is a new product!
                stmt.execute("INSERT INTO Notes(id, title, body) VALUES ("
                        + note.id + ", "
                        + "\'" + note.title  + '\'' + ","
                        + "\'" + note.body + '\'' + ")"

                );
            }
            else {
                stmt.executeUpdate("UPDATE Notes SET "
                        + "id = " + note.id + ", "
                        + "title = '" + note.title + "', "
                        + "body = '" + note.body + "'"
                        + " WHERE id = " + note.id
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public NoteModel loadNote(int id) {
        NoteModel note = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Notes WHERE id = " + id);
            if (rs.next()) {
                note = new NoteModel();
                note.id = rs.getInt(1);
                note.title = rs.getString(2);
                note.body = rs.getString(3);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return note;
    }

    @Override
    public NoteListModel searchNote(String keyword) {
        NoteModel note = null;
        NoteListModel listModel = new NoteListModel();
        try {
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM Notes WHERE title LIKE \'%" +  keyword + "%\'";
            System.out.println("Search SQL statement " + sql);

            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                note = new NoteModel();
                note.id = rs.getInt(1);
                note.title = rs.getString(2);
                note.body = rs.getString(3);

                listModel.list.add(note); // make a new NoteModel and add
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listModel;
    }


}
