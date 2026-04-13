package designs.decorator;

public class SmsResponse {
    private final String msg;
    public SmsResponse(String msg){
        this.msg = msg;
    }

    public String getSmsMsg(){
        return "Sms: " + msg;
    }
}
