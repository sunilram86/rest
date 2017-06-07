package com.galvanize.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleUser {
    @JsonView(Views.DetailView.class)

    private boolean authenticated;
    @JsonView(Views.DetailView.class)
    private User user;

    public SimpleUser() {
    }

    public SimpleUser(boolean authenticated, User user) {
        this.authenticated = authenticated;
        this.user = user;
    }

    public boolean isAuthenticated(boolean b) {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleUser that = (SimpleUser) o;

        if (authenticated != that.authenticated) return false;
        return user != null ? user.equals(that.user) : that.user == null;
    }

    @Override
    public int hashCode() {
        int result = (authenticated ? 1 : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleUser{" +
                "authenticated=" + authenticated +
                ", user=" + user +
                '}';
    }
}
