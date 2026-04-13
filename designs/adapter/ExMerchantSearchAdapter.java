package designs.adapter;

import designs.adapter.Ex.MerchantData;
import designs.adapter.Ex.MerchantSearchService;

public class ExMerchantSearchAdapter implements ISellerSearch {
    private MerchantSearchService mss ;
    public ExMerchantSearchAdapter(MerchantSearchService mss){
        this.mss = mss;
    }

    @Override
    public String rank(String id) {
        // get the full response
        MerchantData response= mss.fetchMerchantData(id);
        // send only whats needed----> kinda like DTO's 
        return "Ex-Merchant=" + response.getRating();
    }
}
