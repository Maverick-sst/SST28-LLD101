public class CreditsRule implements Rule {
    private int minCredits;
    public CreditsRule(int minCredits){
        this.minCredits=minCredits;
    }

    @Override
    public RuleResult checkIfElgiible(StudentProfile s) {
        if(s.earnedCredits < minCredits)return new RuleResult(false, "credits below "+minCredits);
        return new RuleResult(true, null);
    }
}
