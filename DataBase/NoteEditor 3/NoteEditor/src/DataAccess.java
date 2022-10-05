public interface DataAccess {
    void connect();

    void disconnect();

    void saveNote(NoteModel note);

    NoteModel loadNote(int id);

    NoteListModel searchNote(String keyword);
}
