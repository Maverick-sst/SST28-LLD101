package designs.decorator;

public class WhatsappSender  implements INotificationSender {
    private final WhatsappResponse whatsappResponse;
    public WhatsappSender(WhatsappResponse whatsappResponse){
        this.whatsappResponse = whatsappResponse;
    }

    @Override
    public String send() {
        return whatsappResponse.getWhatsappMsg();
    }
    
}
