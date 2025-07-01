package org.app.quizeappculture.entites;

public class User {
    private String name;
    private String email;

    // Constructeur vide
    public User() {}

    // Constructeur avec paramètres
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
