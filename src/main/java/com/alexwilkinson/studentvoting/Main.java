package com.alexwilkinson.studentvoting;
import com.alexwilkinson.studentvoting.model.CourseIdea;
import com.alexwilkinson.studentvoting.model.CourseIdeaDAO;
import com.alexwilkinson.studentvoting.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
/**
 * Created by Alex on 25/04/2017.
 */
public class Main {

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
                response.redirect("/");
                halt();
            }
        }));

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));
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
            idea.addVoter(request.attribute("username"));
            response.redirect("/ideas");
            return null;
        });


    }

}
