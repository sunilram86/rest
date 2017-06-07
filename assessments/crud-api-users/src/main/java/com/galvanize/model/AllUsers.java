package com.galvanize.model;

import java.util.List;

public class AllUsers {
    private List<User> users;

    public AllUsers() {
    }

    public AllUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "AllUsers{" +
                "users=" + users +
                '}';
    }
}
