package designs.decorator;

public class SmsSender implements INotificationSender {
    private final SmsResponse smsResponse;
    public SmsSender(SmsResponse smsResponse){
        this.smsResponse = smsResponse;
    }

    @Override
    public String send() {
        return smsResponse.getSmsMsg();
    }
}
