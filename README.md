# SparkTest
Some practice with the Spark Microframework
---
#### Development - Complete
This project is a simple voting web application that allows users to sign in with a simple cookie and either add or vote on courses.
This application has largely allowed me to practice many elements of Spark a really neat microframework for Java.

The project does not link to a database but stores the data in a collection on the server meaning the data will be wiped on restarts,
however this has proven fine for development and if I were to deploy this idea I would add a database backend to link the app into a
more persistent datastore.

The application serves the pages, has a custom 404 page and displays messages to ensure the user knows what is going on as they click
buttons. Also due to the polling collection being set as a set it does not allow multiply votes per person.

Project Breakdown
---
1. The web application is written in Spark.
2. Serving data from a List and Set creating semi-permanent data for testing.
3. The templating engine I used is Handlebars which has proven to be quite the interesting engine to use.
4. The sessions are tracked by cookies and sessions.

### The Code

Using Spark I ended up using the Gradle compiling method to collect the resources from around the web and hold them in the project.
Gradle seems to be to have been a great choice as it seems to simplify the Maven process greatly, whilst not comprimising on the
packages available.

Spark is a great platform for getting everything up and running quickly, a lot of the controller logic is just placed in the Main.java
class, this is good for small projects but it quickly becomes unmanageable as a project swells which is why Spark seems to be best
for smaller projects.

I am using the MVC design phillosophy to sperate out all of the components, it has become somewhat standard practice in web development
so it is no surprise.

Most of the logic can be found on the Main.java page for the controling of the models which are implemented in the model directory.
CourseIdea is the base model which sets up all the fields with the DAO interface and DAO sitting on top to allow data to be passed
back and fourth to the model and view. 

The views are all partials of the base.hbs which imports all of the key parts and allows the other pages to be built on top. This
is good design practice and prevents needless code reuse and also allows for much easier styling.

## UX

In this project the views have been implemented in a rather basic manner with a small amount of CSS being used to make them more 
readable. This is an area that can be greatly improved for a complete project, however this was never the focus of this project
so it has been left rather basic, although reasonable.

##Retrospective

This was a rather eye opening project that highlighted a new framework I have never seen before and allowed me to play around with 
most of the basic ideas that would be needed within the context of the framework. I feel I learnt a lot and coming from Spring Boot
I can see the benefits of Spark but also the limitations.

This was also the first time I have worked with Handlebars but do quite like it as a templating engine and is one I think I would look
to use again in the future.

##Improvements

The obvious improvement here would be a database to server the data, this would not be so hard to bring in due to the way the classes
have been setup, although just for practice and testing it is probably not that important for me.

Along side this there are lots of little tweeks that can be added such as making the platform more secure and of course styling the
application much more to create a much more rounded experience. 
