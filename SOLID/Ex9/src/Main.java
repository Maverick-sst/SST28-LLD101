public class Main {
    public static void main(String[] args) {
        System.out.println("=== Evaluation Pipeline ===");
        Submission sub = new Submission("23BCS1007", "public class A{}", "A.java");
        
        new EvaluationPipeline(new CodeGrader(),new PlagiarismChecker(),new ReportWriter(), new Rubric()).evaluate(sub);
    }
}
