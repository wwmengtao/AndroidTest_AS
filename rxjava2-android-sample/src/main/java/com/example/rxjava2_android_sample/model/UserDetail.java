package com.example.rxjava2_android_sample.model;

/**
 * Created by amitshekhar on 04/02/17.
 */

public class UserDetail {

    public long id;
    public String firstname;
    public String lastname;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDetail {\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  firstname: ").append(firstname).append("\n");
        sb.append("  lastname: ").append(lastname).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
