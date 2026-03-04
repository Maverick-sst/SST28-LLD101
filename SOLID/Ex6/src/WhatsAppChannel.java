public class WhatsAppChannel implements NotificationSender{
    private final NotificationSender whatsAppSender;
    public WhatsAppChannel(NotificationSender whatsAppSender){
        this.whatsAppSender = whatsAppSender;
    }
    @Override
    public void send(Notification n) {
        if(n.phone == null || !n.phone.startsWith("+")){
            throw new IllegalArgumentException("phone must start with + and country code");
        }
        whatsAppSender.send(n);
    }
}
