import java.util.*;

public class EligibilityEngine {
    private final EligibilityStore store;
    private final List<Rule> rules;
    public EligibilityEngine(EligibilityStore store, List<Rule> rules) { this.store = store;
        this.rules=rules;
     }

    public void runAndPrint(StudentProfile s) {
        ReportPrinter p = new ReportPrinter();
        EligibilityEngineResult r = evaluate(s); // giant conditional inside
        p.print(s, r);
        store.save(s.rollNo, r.status);
    }

    public EligibilityEngineResult evaluate(StudentProfile s) {
        List<String> reasons = new ArrayList<>();
        String status = "ELIGIBLE";

        // OCP violation: long chain for each rule
        for(Rule r : rules){
            RuleResult res = r.checkIfElgiible(s);
            if(!res.isEligible){
                status = "NOT_ELIGIBLE";
                reasons.add(res.reason);
                break;
            }
        }

        return new EligibilityEngineResult(status, reasons);
    }
}

