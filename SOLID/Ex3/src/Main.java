import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Placement Eligibility ===");
        StudentProfile s = new StudentProfile("23BCS1001", "Ayaan", 8.10, 72, 18, LegacyFlags.NONE);

        RuleInput r= new RuleInput();

        List<Rule> rules = new ArrayList<>();
        Rule disciplineRule = new DisciplineRule();
        Rule cgrRule = new CgrRule(r.minCgr);
        Rule attendanceRule = new AttendanceRule(r.minAttendance);
        Rule creditsRule = new CreditsRule(r.minCredits);
        rules.add(disciplineRule);
        rules.add(cgrRule);
        rules.add(attendanceRule);
        rules.add(creditsRule);
        EligibilityEngine engine = new EligibilityEngine(new FakeEligibilityStore(), rules);

        engine.runAndPrint(s);
    }
}
