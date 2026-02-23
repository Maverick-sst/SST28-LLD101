import java.util.*;

public class OnboardingService {

    private StudentRepo db;
    private IdGenerator idGenerator;
    public OnboardingService(StudentRepo db, IdGenerator idGenerator) { 
        this.db =db;
        this.idGenerator=idGenerator;
     }

    // Intentionally violates SRP: parses + validates + creates ID + saves + prints.
    public void registerFromRawInput(String raw) {
        System.out.println("INPUT: " + raw);

        Parser p= new Parser();
        Map<String,String> kv =p.parse(raw);
        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        
        Validator valid = new Validator(name, email, phone, program);
        if(!valid.isValid()){
            ErrorHandler.handleError(valid.getErrors());
            return;
        }

        String id = idGenerator.next();
        StudentRecord rec = new StudentRecord(id, name, email, phone, program);

        db.save(rec);
        RegistrationResult res = new RegistrationResult(rec, db.count());
        res.printDetails();

    }
}
