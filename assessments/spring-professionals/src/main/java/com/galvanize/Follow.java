package com.galvanize;

import javax.persistence.*;

@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Professional professional;

    @ManyToOne
    private Professional follower;

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public Professional getFollower() {
        return follower;
    }

    public void setFollower(Professional follower) {
        this.follower = follower;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
