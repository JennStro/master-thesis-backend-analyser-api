package runner;

import java.io.*;

public class JavaFileRunner {

    public static String runCodeFile(File file) throws IOException, InterruptedException {
        System.out.println(file.getName());
        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", file.getName())
                .directory(new File("src/main/java"));
        Process compilationProcess = compilationProcessBuilder.start();
        compilationProcess.waitFor();

        System.out.println("Compiled "+ file.getName());

        File outputFile = new File("src/main/java/output.txt");
        System.out.println("Created out file");
        ProcessBuilder runProcessBuilder = new ProcessBuilder("java", file.getName().substring(0, file.getName().lastIndexOf(".")))
                .directory(new File("src/main/java"))
                .redirectOutput(outputFile);
        Process runProcess = runProcessBuilder.start();
        runProcess.waitFor();

        FileReader reader = new FileReader(outputFile);
        int i;
        while((i=reader.read())!=-1)
            System.out.print((char)i);
        reader.close();
        return "";
    }
}
