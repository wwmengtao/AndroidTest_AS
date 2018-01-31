package com.example.rxjava2_android_sample.model;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class User {
    public long id;
    public String firstname;
    public String lastname;
    public boolean isFollowing;

    public User() {
    }

    public User(ApiUser apiUser) {
        this.id = apiUser.id;
        this.firstname = apiUser.firstname;
        this.lastname = apiUser.lastname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  firstname: ").append(firstname).append("\n");
        sb.append("  lastname: ").append(lastname).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
