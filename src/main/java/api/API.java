package api;

import master.thesis.backend.analyser.Analyser;
import master.thesis.backend.errors.BaseError;
import master.thesis.backend.errors.BugReport;
import org.json.JSONObject;

import static spark.Spark.*;

public class API {

    public static void main(String[] args) {
        port(Integer.parseInt(new ProcessBuilder().environment().get("PORT")));
        get("/health", (req, res) -> {
            return "Health ok";
        });

        get("/", (req, res) -> {
            return "Hello world";
        });

        post("/analyse", (request, response) -> {

            BugReport report = new Analyser().analyse(request.body());
            JSONObject res = new JSONObject();
            if(report.getException().isPresent()) {
                res.put("hasException", truncateMessage(report.getException().get().getMessage()));
                return res;
            }
            if (!report.getBugs().isEmpty()) {
                BaseError error = report.getBugs().get(0);
                res.put("status", "errors");
                res.put("containingClass", error.getContainingClass());
                res.put("lineNumber", error.getLineNumber());
                res.put("explanation", error.getWhat());
                if (error.getSuggestion().isPresent()) {
                    res.put("suggestion", error.getSuggestion().get());
                }
                if (error.getLink().isPresent()) {
                    res.put("moreInfoLink", error.getLink().get());
                }
                if (error.getTip().isPresent()) {
                    res.put("tip", error.getTip().get());
                }
                return res;
            }
            res.put("status", "noerrors");
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
            return message.substring(0, message.indexOf("Problem stacktrace")- "Problem stacktrace".length());

        }
        return message;
    }
}
