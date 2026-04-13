package designs.decorator;

public class WhatsappResponse {
    private final String msg;
    public WhatsappResponse(String msg){
        this.msg = msg;
    }
    public String getWhatsappMsg(){
        return "WhatsApp: " + this.msg;
    }
}
