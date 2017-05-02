package com.alexwilkinson.studentvoting;
import com.alexwilkinson.studentvoting.model.CourseIdea;
import com.alexwilkinson.studentvoting.model.CourseIdeaDAO;
import com.alexwilkinson.studentvoting.model.NotFoundException;
import com.alexwilkinson.studentvoting.model.SimpleCourseIdeaDAO;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;
/**
 * Created by Alex on 25/04/2017.
 */
public class Main {

    private static final java.lang.String FLASH_MESSAGE_KEY = "flash_message";

    public static void main(String[] args) {

        staticFileLocation("/public");

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        before(((request, response) -> {
            if(request.cookie("username") != null){
                request.attribute("username", request.cookie("username"));
            }
        }));

        //filters all who are not logged in to the ideas page
        before("/ideas", ((request, response) -> {
            if(request.attribute("username") == null){
                setFlashMessage(request, "Please sign in first");
                response.redirect("/");
                halt();
            }
        }));

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));
            model.put("flashMessage", captureFlashMessage(req));
            return new ModelAndView(model, "index.hbs");
        },new HandlebarsTemplateEngine());

        post("/", (request, response) -> {
            Map<String,String> model = new HashMap<>();

            String username = request.queryParams("username");
            response.cookie("username", username);

            model.put("username", username);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas", (request, response) -> {

            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            model.put("flashMessage", captureFlashMessage(request));

            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (request, response) -> {
            String title = request.queryParams("title");
            CourseIdea courseIdea = new CourseIdea(title, request.attribute("username"));
            dao.add(courseIdea);
            response.redirect("/ideas");
            return null;

        });

        post("/ideas/:slug/vote", (request, response) -> {
            CourseIdea idea = dao.findBySlug(request.params("slug"));
            boolean added = idea.addVoter(request.attribute("username"));
            if(added){
                setFlashMessage(request, "You voted!");
            }
            else{
                setFlashMessage(request, "You already voted");
            }
            response.redirect("/ideas");
            return null;
        });

        get("/ideas/:slug", (request, response) -> {
            Map<String, Object> model  = new HashMap<>();
            model.put("idea", dao.findBySlug(request.params("slug")));
            return new ModelAndView(model,"ideas-detail.hbs");
        }, new HandlebarsTemplateEngine() );

        //used to handle the exception thrown by directing to a 404
        exception(NotFoundException.class, (exception, request, response) -> {
            response.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            response.body(html);
        });


    }

    //flash message setup that uses teh session to send messages to the user good for UX
    private static void setFlashMessage(Request request, String message) {
        request.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage(Request request){
        if(request.session(false) == null){
            return null;
        }
        if(!request.session().attributes().contains(FLASH_MESSAGE_KEY)){
            return null;
        }
        return (String)request.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage(Request request){
        String message = getFlashMessage(request);
        if(message !=null){
            request.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }

}
