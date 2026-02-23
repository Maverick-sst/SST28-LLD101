
import java.util.ArrayList;
import java.util.List;

public class Validator {
    private String name;
    private String email;
    private String phone;
    private String program;
    List<String> errors= new ArrayList<>();
    
    public Validator(String name, String email, String phone,String program){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.program=program;
    }

    public boolean isValid(){
        
        if(name.isBlank())errors.add("name is required");
        if(email.isBlank() || !email.contains("@"))errors.add("email is invalid");
        if(phone.isBlank() || !phone.chars().allMatch(Character :: isDigit)) errors.add("phone is invalid");
        if (!(program.equals("CSE") || program.equals("AI") || program.equals("SWE"))) errors.add("program is invalid");

        if(!errors.isEmpty()){
            ErrorHandler err = new ErrorHandler();
            err.handleError(errors);
            return false;
        }

        return true;
    }

    public List<String> getErrors(){
        return errors;
    }
}
