package designs.adapter.Ex;

// legacy code---->

public class MerchantSearchService {
    private MerchantRepo db ;
    public MerchantSearchService(MerchantRepo db ){
        this.db = db;
    }
    public MerchantData fetchMerchantData(String merchantId){
         return this.db.getMerchantData(merchantId);
    }
}
