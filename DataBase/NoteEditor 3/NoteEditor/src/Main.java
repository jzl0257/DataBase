public class Main {

    public static void main(String[] args) {
        DataAccess dao = new RemoteDataAdapter();
        dao.connect();
        //NoteModel note = dao.loadNote(1);

        //System.out.println("Note 1 title = " + note.title);

        NoteListModel listModel = dao.searchNote("date");

        System.out.println("Number of notes found: " + listModel.list.size());

        dao.disconnect();

    }
}
