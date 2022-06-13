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
        this.date  = new Date(System.currentTimeMillis());
        this.fileConents = null;}
    public Blob(String filename) {
        this.fileName = filename;
        this.date  = new Date(System.currentTimeMillis());
        try {
            this.fileConents = String.join(System.lineSeparator(),Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            this.fileConents = null;
        }
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileConents() {
        return fileConents;
    }
    public void setFileConents(String fileConents) {
        this.fileConents = fileConents;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    //essentially the contents of a tree
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
