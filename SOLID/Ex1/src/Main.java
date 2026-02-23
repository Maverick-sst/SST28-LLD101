public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        
        StudentRepo db=new FakeDb();
        IdGenerator idGenerator=new IdGenerator(db);
        OnboardingService svc = new OnboardingService(db,idGenerator);

        String raw = "name=maverick;email=maverick@sst.edu;phone=9876543210;program=SWE";
        svc.registerFromRawInput(raw);

        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(db));
    }
}
