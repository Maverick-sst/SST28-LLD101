public class EmailSender implements NotificationSender {
    private AuditLog audit ;
    public EmailSender(AuditLog audit) { 
        this.audit=audit;
     }
    @Override
    public void send(Notification n) {
       
        String body = n.body;
        System.out.println("EMAIL -> to=" + n.email + " subject=" + n.subject + " body=" + body);
        audit.add("email sent");
    }
}
