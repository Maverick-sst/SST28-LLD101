package designs.decorator;

public class SmsDecorator extends SmsSender implements INotificationSender{
    private final INotificationSender sender;
    public SmsDecorator(SmsResponse response , INotificationSender sender){
        super(response);
        this.sender = sender;
    }

    @Override
    public String send() {
        String res1 = sender.send();
        String res2 = super.send();

        return res1 + "\n" + res2;
    }
    
}
