package designs.adapter.Sd;

import designs.adapter.ISellerSearch;

public class SellerSearchService implements ISellerSearch {
    @Override
    public String rank(String id) {
       return "Snapdeal-Merchant= 5";
    }
}
