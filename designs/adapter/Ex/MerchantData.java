package designs.adapter.Ex;

public class MerchantData {
    private final String merchantId;
    private final  String rating;
    private final String totalOrders;
    private final String merchantLevel;   

    public MerchantData(String merchantId,String rating, String totalOrder,String merchantLevel){
        this.merchantId = merchantId;
        this.rating = rating;
        this.totalOrders = totalOrder;
        this.merchantLevel = merchantLevel;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getRating() {
        return rating;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public String getMerchantLevel() {
        return merchantLevel;
    }
    

} 
