package designs.adapter;

public class SellerRankingService {
    private ISellerSearch sss; // abstraction
    public SellerRankingService(ISellerSearch sss){
        this.sss = sss;
    }
    public String getRanking(String id){
        return sss.rank(id);
    }
} 