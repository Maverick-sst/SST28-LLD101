package designs.decorator;

public class EmailSender implements INotificationSender {
    private final EmailStructure email;
    public EmailSender(EmailStructure email){
        this.email = email;
    }

    @Override
    public String send() {
        String response = "from: "+email.getRecipient() + "\nsubject: "+ email.getSubject() + "\nbody: "+ email.getBody();
        return response;
    }    
}
