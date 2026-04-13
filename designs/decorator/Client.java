package designs.decorator;

public class Client {
    public static void main(String[] args) {
        // // 1st test only email
        // EmailStructure req= new EmailStructure("mdrehan@gmail.com", "Urgent meeting", "Hey boss, please make sure to join our scheduled meet with our board members along with the ones from pied pipers to discuss about potential merger with Hooli!");
        // INotificationSender notifier = new EmailSender(req);
        // // System.out.println(notifier.send());

        // // 2nd email + whatsapp sequentially
        // WhatsappResponse msg = new WhatsappResponse("hello join the meet at 5pm sharp!");
        // notifier = new WhatsappDecorator(msg, notifier);
        // // System.out.println(notifier.send());

        // //3rd email + whatsapp + sms
        // SmsResponse sms = new SmsResponse("meeting starts in 5 mins!");
        // notifier = new SmsDecorator(sms, notifier);
        // System.out.println(notifier.send());



        // 2nd combo sms + wapp
        // SmsResponse sms = new SmsResponse("hey buddy call me once you reach home!");
        // INotificationSender notifier = new SmsSender(sms);
        // WhatsappResponse response = new WhatsappResponse("dude check your gallery once");
        // notifier = new WhatsappDecorator(response, notifier);  // sms ---->  whatsapp msg
        // System.out.println( notifier.send() );


        // 3rd combo wapp + email 
        // wapp ---> email
        WhatsappResponse  response = new WhatsappResponse("Open you gmail please!");
        INotificationSender notifier = new WhatsappSender(response);
        EmailStructure req= new EmailStructure("mdrehan@gmail.com", "Urgent meeting", "Hey boss, please make sure to join our scheduled meet with our board members along with the ones from pied pipers to discuss about potential merger with Hooli!");
        notifier = new EmailDecorator(req, notifier);
        
        // to send sms at the end simply use the sms decorator 
        notifier = new SmsDecorator(new SmsResponse("Done, Accepted!"), notifier);
        System.out.println(notifier.send());

    }
}
