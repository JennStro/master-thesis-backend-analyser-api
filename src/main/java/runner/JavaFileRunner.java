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
        System.out.println("Created error file");

        ProcessBuilder compilationProcessBuilder = new ProcessBuilder("javac", file.getName())
                .redirectError(errorFile);
        Process compilationProcess = compilationProcessBuilder.start();
        compilationProcess.waitFor();

        System.out.println("Compiled exit code: " +compilationProcess.exitValue());

        if (compilationProcess.exitValue() != EXIT_STATUS_SUCCESS) {
            System.out.println("Failed to compile...");
            this.errorStream = getContents(errorFile);
            this.errorStream = cleanErrorStream(this.errorStream);


            System.out.println("Error" + errorStream);
        }

        if (compilationProcess.exitValue() == EXIT_STATUS_SUCCESS) {
            System.out.println("Compiled " + file.getName());

            ProcessBuilder runProcessBuilder = new ProcessBuilder("java", file.getName().substring(0, file.getName().lastIndexOf(".")))
                    .redirectOutput(outputFile)
                    .redirectError(errorFile);

            Process runProcess = runProcessBuilder.start();
            runProcess.waitFor();

            System.out.println("Runned exit code: " + runProcess.exitValue());

            if (runProcess.exitValue() == EXIT_STATUS_SUCCESS) {
                System.out.println("Runned " + file.getName());
            }

            this.outputStream = getContents(outputFile);
            this.errorStream = getContents(errorFile);
            this.errorStream = cleanErrorStream(this.errorStream);

            System.out.println("Out:" + outputStream);
            System.out.println("Error" + errorStream);
        }

    }

    /**
     *
     * @param errorStream
     * @return String cleaned for Java options output. If only non-characters are left, like "\n", return empty string.
     */
    private String cleanErrorStream(String errorStream) {
        String removeJavaOptions = errorStream.replace("Picked up JAVA_TOOL_OPTIONS: -XX:+UseContainerSupport -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8", "");

        if (stringHasLetter(removeJavaOptions)) {
            return removeJavaOptions;
        }
        return "";
    }

    private boolean stringHasLetter(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isAlphabetic(s.charAt(i))) {
                return true;
            }
        }
        return false;
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
