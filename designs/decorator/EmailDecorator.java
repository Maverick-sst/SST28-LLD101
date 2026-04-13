package designs.decorator;

public class EmailDecorator extends EmailSender implements INotificationSender {
    private final INotificationSender sender;
    public EmailDecorator(EmailStructure email,INotificationSender sender) {
        super(email);
        this.sender = sender;
    }

    @Override
    public String send() {
        String res1= sender.send();
        String res2 = super.send();
        return res1 + "\n" + res2;
    }
}
