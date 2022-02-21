package runner;

import java.io.*;

public class JavaFileRunner {

    private static final int EXIT_STATUS_SUCCESS = 0;

    public static String runCodeFile(File file) throws IOException, InterruptedException {
        File outputFile = new File("output.txt");
        System.out.println("Created out file");

        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", file.getName())
                .redirectError(outputFile);
        Process compilationProcess = compilationProcessBuilder.start();
        compilationProcess.waitFor();

        System.out.println("Compiled exit code: " +compilationProcess.exitValue());

        if (compilationProcess.exitValue() == EXIT_STATUS_SUCCESS) {
            System.out.println("Compiled " + file.getName());

            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", file.getName().substring(0, file.getName().lastIndexOf(".")))
                    .redirectOutput(outputFile);

            Process runProcess = runProcessBuilder.start();
            runProcess.waitFor();

            if (runProcess.exitValue() == EXIT_STATUS_SUCCESS) {
                System.out.println("Runned " + file.getName());
            }
        }

        FileReader reader = new FileReader(outputFile);
        int i;
        StringBuilder builder = new StringBuilder();
        while((i=reader.read())!=-1)
            builder.append((char)i);
        reader.close();
        return builder.toString();

    }
}
