package designs.adapter;

import designs.adapter.Ex.MerchantData;
import designs.adapter.Ex.MerchantRepo;
import designs.adapter.Ex.MerchantSearchService;

public class Client {
    public static void main(String[] args) {
        MerchantRepo db = new MerchantRepo();
        db.addMerchant(new MerchantData("m-2", "3", "5", "7"));
        db.addMerchant(new MerchantData("m-1", "5", "6", "7"));
        db.addMerchant(new MerchantData("m-3", "1", "7", "7"));
        MerchantSearchService mss = new MerchantSearchService(db);
        ISellerSearch msa= new ExMerchantSearchAdapter(mss);


        SellerRankingService srs = new SellerRankingService(msa);
        System.out.println(srs.getRanking("m-3"));
    }
}
