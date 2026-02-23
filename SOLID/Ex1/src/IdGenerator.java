public class IdGenerator {
    private StudentRepo db;
    public IdGenerator(StudentRepo db){
        this.db=db;
    }    
    public String next(){
         return IdUtil.nextStudentId(db.count());
    }
}
