import java.util.List;

public interface StudentRepo {
    void save(StudentRecord r);
    int count();
    List<StudentRecord> all();
}
