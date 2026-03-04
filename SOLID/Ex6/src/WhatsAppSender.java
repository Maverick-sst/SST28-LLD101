public class WhatsAppSender implements NotificationSender {
    private AuditLog audit ;
    public WhatsAppSender(AuditLog audit) { this.audit=audit; }

    @Override
    public void send(Notification n) {
    
        System.out.println("WA -> to=" + n.phone + " body=" + n.body);
        audit.add("wa sent");
    }
}
