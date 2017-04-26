package com.alexwilkinson.studentvoting.model;

import java.util.List;

/**
 * Created by Alex on 26/04/2017.
 */
public interface CourseIdeaDAO {
    boolean add(CourseIdea idea);

    List<CourseIdea> findAll();
}
