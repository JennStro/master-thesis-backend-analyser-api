package runner;

import java.io.*;

public class JavaFileRunner {

    private static final int EXIT_STATUS_SUCCESS = 0;

    public String getErrorStream() {
        return errorStream;
    }

    public String getOutputStream() {
        return outputStream;
    }

    private String errorStream = "";
    private String outputStream = "";

    public void runCodeFile(File file) throws IOException, InterruptedException {
        File outputFile = new File("output.txt");
        System.out.println("Created out file");
        File errorFile = new File("errorfile.txt");

        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", file.getName())
                .redirectError(errorFile);
        Process compilationProcess = compilationProcessBuilder.start();
        compilationProcess.waitFor();

        System.out.println("Compiled exit code: " +compilationProcess.exitValue());

        if (compilationProcess.exitValue() == EXIT_STATUS_SUCCESS) {
            System.out.println("Compiled " + file.getName());

            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", file.getName().substring(0, file.getName().lastIndexOf(".")))
                    .redirectOutput(outputFile)
                    .redirectError(errorFile);

            Process runProcess = runProcessBuilder.start();
            runProcess.waitFor();

            if (runProcess.exitValue() == EXIT_STATUS_SUCCESS) {
                System.out.println("Runned " + file.getName());
            }
        }

        this.outputStream = getContents(outputFile);
        this.errorStream = getContents(errorFile);

        System.out.println("Out:" + outputStream);
        System.out.println("Error" + errorStream);

    }

    private String getContents(File file) throws IOException {
        FileReader reader = new FileReader(file);
        int i;
        StringBuilder output = new StringBuilder();
        while((i=reader.read())!=-1)
            output.append((char)i);
        reader.close();
        return output.toString();
    }
}
