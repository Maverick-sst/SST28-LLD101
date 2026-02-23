public class AttendanceRule implements Rule {
    private int minAttendance;
    public AttendanceRule(int minAttendance){
        this.minAttendance=minAttendance;
    }

    @Override
    public RuleResult checkIfElgiible(StudentProfile s) {
        if(s.attendancePct < minAttendance)return new RuleResult(false,"attendance below "+minAttendance );
        return new RuleResult(true, null);
    }
}
