public class RegistrationResult {
    private final StudentRecord studentRecord;
    private final int totalStudents;
    

    public RegistrationResult(StudentRecord studentRecord, int totalStudents){
        this.studentRecord=studentRecord;
        this.totalStudents=totalStudents;
    }

    public  void printDetails(){
        System.out.println("OK: created student " + studentRecord.id);
        System.out.println("Saved. Total students: " + totalStudents);
        System.out.println("CONFIRMATION:");
        System.out.println(studentRecord);
    }
}
