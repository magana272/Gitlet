package utils.DataStruct;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class Blob implements Serializable {
    private String fileName;
    private String fileConents;
    private Date date;
    public Blob(){
        date  = new Date(System.currentTimeMillis());
        fileConents = null;}
    public Blob(String filename) {
        fileName = filename;
        date  = new Date(System.currentTimeMillis());
        fileConents = null;
        try {
            fileConents = String.join(System.lineSeparator(),Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //essentially the contents of a tree
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
