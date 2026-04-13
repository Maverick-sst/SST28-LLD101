package designs.adapter.Ex;

import java.util.HashMap;
import java.util.Map;

public class MerchantRepo {
    private final static Map<String, MerchantData> repo= new HashMap<>();
    public MerchantData addMerchant(MerchantData req){
        repo.put(req.getMerchantId(), req);
        return req;
    }
    public MerchantData getMerchantData(String merchantId){
        return repo.get(merchantId);
    }
}
