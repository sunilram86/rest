# Inventory SPA with Spring Boot

## Problems

Complete these exercises in order, using `git` to checkpoint your work as you go along.

As always, it is recommended that you read this entire problem set prior to beginning it so that you understand what you are building towards. Use React and a Spring Boot REST API to build a Single Page Application.

### Starting point

Use http://start.spring.io/ and select `Web`, `JPA`, and `Rest Repositories` to create a Gradle application in the directory that you cloned your forked repository to. You may call this application `products_and_categories` (or whatever you want that represents what it is). For this application you will not have to worry about style until stretch goals. Here are the features you are looking to build:

#### Feature 0

As a developer, I would like to have an API for creating and retrieving `categories`. A `category` record consists of the category `name` and its `_id`. 

1. Start by creating the Category class, with the appropriate private fields. 
1. Then use the `generate` capabilities of your IDE to create getters and setters, a constructor, and a `toString()` method.
1. Finally, create a [Controller](https://spring.io/guides/gs/serving-web-content/) which stores categories in a `HashMap<>`. (No persistence is required for this story)
 
Be sure you are operating in a TDD manner, and using [MockMVC](https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4) to test your API as you go.

If you get stuck, refer to the [Spring Boot Guides](https://spring.io/guides) for reference.

#### Feature 1

As a developer, I would like to have an API for creating, retrieving, and updating `products`. A `product` record consists of the `name`, `price`, and `description`. Like `categories`, `products` do not require persistence to complete this story. Storing them in an `HashMap<>` will do just fine.

#### Feature 2

As an end user, I would like to have my products and categories persist between server reboots.

1. Create a new database in MySQL.
1. Use mvnrepository.com to find the latest `mysql-connector-java` and import it into your build.gradle file.
1. Create one [Repository](https://spring.io/guides/gs/accessing-data-jpa/) for products, and one for categories.
1. Use [ddl-auto](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html) to generate your database schema automatically
1. Write tests to ensure you can save & retrieve products and categories
1. Hook your existing MVC controller up to your new database!

#### Feature 3

As an end user, I would like to be able to link products to categories so that each product can belong to many categories, and each category can have many products.

1. Review the [@ManyToMany](https://www.mkyong.com/hibernate/hibernate-many-to-many-relationship-example-annotation/) annotation
1. Ensure you have tests to verify products and categories can be linked and unlinked
1. Be wary of circular references, and understand the meaning of the JPA [Transient](http://stackoverflow.com/questions/2154622/why-does-jpa-have-a-transient-annotation) annotation.

#### Feature 4 (Stretch)

As an end user, I would like to have a single page application (SPA) flow for creating categories on the fly.

Use what [you've learned](https://learn.galvanize.com/content/gSchool/xp_curriculum/northland_1_revised/student_notes/react/introduction_to_react.md) about react.js to create the front end.   

#### Feature 5 (Stretch)

As an end user, I would like to have a single page application (SPA) flow that allows me to create products, providing them with an associated category in the same flow. In this SPA, I should be able to create products and associate them with one or more categories (_hint_: this means checkboxes), as well as creating new categories. This is a rough mockup of what this looks like:

<center>
  ![Wireframe of Products and Categories](https://galvanize.mybalsamiq.com/mockups/3831920.png?key=0a3a49896fe5fdecbd75cdc81da42a7e23eb14d6)
</center>

#### Feature 6 (Stretch)

As an end user, I would like to have a more detailed view appear when I click on one of the specific product links in the Product List. This is a rough mockup of what this looks like:

<center>
  ![Wireframe of Products and Categories with show view](https://galvanize.mybalsamiq.com/mockups/3831926.png?key=2b0cfeabae56792987a85f13fb790ad793b68525)
</center>

#### Additional Stretch Goals

1. Can you make this have a good UX? What changes would make a better user experience? One small idea for a starting point is something like placeholder text that tells the user what to enter in each form field.
1. How does a user update a product? What's a way to incorporate that workflow and still have the user live in one single page?
1. As a business owner, I would like for the Single Page Application (SPA) to be well-styled so that I feel warm and fuzzy about the hard work my developers do.
1. When I add a new category, does it appear as a checkbox on the product immediately, or does the user need to refresh the page? What if multiple users are using the site at once? Can you think of a way to make it so all users can see the same thing at once?

#### Setup

Fork and clone this repository so that you may make commits of your work along the way. To Fork a repository, click on the "Fork" button at the top right of this page. Next, from the Forked repository page, copy the clone URL and clone this locally using `git clone [URL copied from forked repository]`. When you have made commits, use `git push origin master` to push them up to the remote (Github).

## Reflection

1. Compare and contrast this version of the exercise with your previous implementation in node & express. Was this easier or harder? Simpler, or more complex?
1. Compare and contrast database migrations vs ddl-auto... what are the advantages of each?
1. What problems did you run into along the way? What was the best debugging technique? (logging, debugging, googling, etc?)
