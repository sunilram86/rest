package com.galvanize;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class Seeds {

    @Bean
    public CommandLineRunner runSeeds(ProfessionalRepository repo, FollowRepository followRepository) {
        return (args) -> {
            followRepository.deleteAll();
            repo.deleteAll();

            Professional petra = new Professional();
            petra.setName("Public Petra");
            petra.setTitle("CEO");
            petra.setCompany("Widget Co");
            petra.setPublicProfile(true);
            petra.setEmail("peta@example.com");
            petra.setPassword("password1234");
            petra.setRole("PROFESSIONAL");
            repo.save(petra);

            Professional pete = new Professional();
            pete.setName("Private Pete");
            pete.setTitle("CEO");
            pete.setCompany("Acme Security");
            pete.setPublicProfile(false);
            pete.setEmail("pete@example.com");
            pete.setPassword("password");
            pete.setRole("PROFESSIONAL");
            repo.save(pete);

            Professional cait = new Professional();
            cait.setName("Connected Caitlin");
            cait.setTitle("CEO");
            cait.setCompany("Cars R Us");
            cait.setPublicProfile(false);
            cait.setEmail("cait@example.com");
            cait.setPassword("password");
            cait.setRole("PROFESSIONAL");
            repo.save(cait);

            Professional other = new Professional();
            other.setName("Other ProfessionalView");
            other.setTitle("CEO");
            other.setCompany("Not Your Business");
            other.setPublicProfile(false);
            other.setEmail("other@example.com");
            other.setPassword("password");
            other.setRole("PROFESSIONAL");
            repo.save(other);

            Follow follow = new Follow();
            follow.setProfessional(pete);
            follow.setFollower(cait);
            followRepository.save(follow);


            Professional admin = new Professional();
            admin.setName("Admin User");
            admin.setTitle("CEO");
            admin.setCompany("Adminify");
            admin.setPublicProfile(false);
            admin.setEmail("admin@example.com");
            admin.setPassword("password");
            admin.setRole("ADMIN");
            repo.save(admin);
        };
    }

}
