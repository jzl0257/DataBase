import javax.swing.*;
import java.util.ArrayList;

public class NoteListModel {
    public ArrayList<NoteModel> getList() {
        return list;
    }

    public ArrayList<NoteModel> list = new ArrayList<>();
}
