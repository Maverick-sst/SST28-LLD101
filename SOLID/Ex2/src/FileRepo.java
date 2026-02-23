public interface FileRepo {
    void save(String name,String content);
    int countLines(String name);
    
}
