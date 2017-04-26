package com.alexwilkinson.studentvoting.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 26/04/2017.
 */
public class SimpleCourseIdeaDAO implements CourseIdeaDAO {

    private List<CourseIdea> ideas;

    public SimpleCourseIdeaDAO() {
        ideas = new ArrayList<>();
    }

    @Override
    public boolean add(CourseIdea idea) {
        return ideas.add(idea);
    }

    @Override
    public List<CourseIdea> findAll() {
        return new ArrayList<>(ideas);
    }

    @Override
    public CourseIdea findBySlug(String slug) {
        //Using streams to check through all the data and if it finds the users slug then
        //the first value is returned
        //if no values are found then it throws an exception
        return ideas.stream()
                .filter(idea -> idea.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);

    }
}
