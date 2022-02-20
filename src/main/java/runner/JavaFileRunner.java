package runner;

import java.io.*;

public class JavaFileRunner {

    public static String runCodeFile(File file) throws IOException, InterruptedException {
        System.out.println(file.getName());
        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", file.getName());
        Process compilationProcess = compilationProcessBuilder.start();
        compilationProcess.waitFor();

        System.out.println("Compiled "+ file.getName());

        File outputFile = new File("output.txt");
        System.out.println("Created out file");
        ProcessBuilder runProcessBuilder = new ProcessBuilder("java", file.getName().substring(0, file.getName().lastIndexOf(".")))
                .redirectOutput(outputFile);
        Process runProcess = runProcessBuilder.start();
        runProcess.waitFor();

        FileReader reader = new FileReader(outputFile);
        int i;
        StringBuilder builder = new StringBuilder();
        while((i=reader.read())!=-1)
            builder.append((char)i);
        reader.close();
        return builder.toString();
    }
}
