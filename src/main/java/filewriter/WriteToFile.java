package filewriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class WriteToFile {

    public static void write(String code, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(code);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Uh oh could not read file");
            e.printStackTrace();
        }

    }

}
