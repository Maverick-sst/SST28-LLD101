public class CgrRule implements Rule {
    private double minCgr;
    public CgrRule(double minCgr){
        this.minCgr=minCgr;
    }

    @Override
    public RuleResult checkIfElgiible(StudentProfile s) {
        if(s.cgr < minCgr)return new RuleResult(false, "CGR below "+minCgr);
        return new RuleResult(true, null);
    }
}
