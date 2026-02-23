public class DisciplineRule implements Rule {
    
    @Override
    public RuleResult checkIfElgiible(StudentProfile s) {
        if(s.disciplinaryFlag != LegacyFlags.NONE)return new RuleResult(false, "disciplinary flag present");
        return new RuleResult(true, null);
    }
}
