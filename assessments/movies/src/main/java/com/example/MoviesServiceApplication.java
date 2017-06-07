package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoviesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(MovieRepository repository) {
        return (args) -> {
            if (repository.count() == 0L) {
                repository.save(new Movie("When Harry Met Sally", 1989));
                repository.save(new Movie("The Princess Bride", 1987));
                repository.save(new Movie("The Goonies", 1985));
                repository.save(new Movie("Raiders of the Lost Ark", 1981));
                repository.save(new Movie("Gremlins", 1984));
            }
        };
    }

}
