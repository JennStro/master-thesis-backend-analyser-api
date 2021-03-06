package api;

import filewriter.WriteToFile;
import master.thesis.backend.analyser.Analyser;
import master.thesis.backend.errors.BaseError;
import master.thesis.backend.errors.BugReport;
import org.json.JSONArray;
import org.json.JSONObject;
import runner.JavaFileRunner;

import java.io.File;
import java.util.ArrayList;

import static spark.Spark.*;

public class API {

    public static void main(String[] args) {
        // Default is 4567
        port(Integer.parseInt(new ProcessBuilder().environment().get("PORT")));

        get("/health", (req, res) -> {
            return "Health ok";
        });

        get("/", (req, res) -> {
            return "Hello world";
        });

        post("/runjava", (request, response) -> {
            BugReport report = new Analyser().analyse(request.body());
            JSONObject res = new JSONObject();

            if (report.getException().isEmpty()) {
                String className = report.getClassName();
                File file = new File(className + ".java");
                WriteToFile.write(request.body(), file);
                System.out.println("Created new file. Code is written to file.");
                JavaFileRunner runner = new JavaFileRunner();
                runner.runCodeFile(file);
                res.put("out", runner.getOutputStream());
                res.put("error", runner.getErrorStream());

                System.out.println("File deleted: " + file.getName() + " "+ file.delete());
                System.out.println("File deleted: output.txt " +new File("output.txt").delete());
                System.out.println("File deleted: errorfile.txt " +new File("errorfile.txt").delete());
                System.out.println("File deleted: Classfile " +new File(file.getName().substring(0, file.getName().lastIndexOf(".")) + ".class").delete());
            } else {
                res.put("out", "");
                res.put("error", truncateMessage(report.getException().get().getMessage()));
            }
            return res;
        });

        post("/analyse", (request, response) -> {
            BugReport report = new Analyser().analyse(request.body());
            JSONObject res = new JSONObject();
            if(report.getException().isPresent()) {
                res.put("hasException", truncateMessage(report.getException().get().getMessage()));
                return res;
            }

            ArrayList<JSONObject> JSONerrors = new ArrayList<>();

            for (BaseError error : report.getBugs()) {
                JSONObject JSONerror = new JSONObject();
                JSONerror.put("type", error.getClass().getName());
                JSONerror.put("containingClass", error.getContainingClass());
                JSONerror.put("explanation", error.getCauseOfError());
                if (error.getLineNumber() > -1) {
                    JSONerror.put("lineNumber", error.getLineNumber());
                }
                if (error.getSuggestion().isPresent()) {
                    JSONerror.put("suggestion", error.getSuggestion().get());
                }
                if (error.getMoreInfoLink().isPresent()) {
                    JSONerror.put("moreInfoLink", error.getMoreInfoLink().get());
                }
                if (error.getTip().isPresent()) {
                    JSONerror.put("tip", error.getTip().get());
                }

                JSONerrors.add(JSONerror);

                res.put("errors", JSONerrors);

            }
            return res;
        });

        options("/*",
                (request, response) -> {
                    response.header("Access-Control-Allow-Methods", "GET, POST");
                    response.header("Access-Control-Allow-Headers", "Content-Type, cache-control, pragma");
                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

    /**
     *
     * @param message
     * @return message without stacktrace
     */
    private static String truncateMessage(String message) {
        if (message.contains("Problem stacktrace")) {
            return message.substring(0, message.indexOf("Problem stacktrace"));

        }
        return message;
    }
}
