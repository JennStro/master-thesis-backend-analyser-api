package api;

import master.thesis.backend.analyser.Analyser;
import master.thesis.backend.errors.BaseError;
import master.thesis.backend.errors.BugReport;

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
            if (!report.getBugs().isEmpty()) {
                BaseError error = report.getBugs().get(0);
                if (error.getLineNumber() == -1) {
                    if (error.getSuggestion().isPresent()) {
                        return "In class " + error.getContainingClass() + " " + error.getWhat() + "\n" + error.getSuggestion().get();
                    }
                    return "In class " + error.getContainingClass() + " " + error.getWhat();
                } else {
                    if (error.getSuggestion().isPresent()) {
                        return "In class " + error.getContainingClass() + ", on line " + error.getLineNumber() + " " + error.getWhat() + "\n" + error.getSuggestion().get();
                    }
                    return "In class " + error.getContainingClass() + ", on line " + error.getLineNumber() + " " + error.getWhat();
                }

            }
            return "Found no errors!";
        });
    }
}
