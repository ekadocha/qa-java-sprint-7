package ru.praktikum_services.qa_scooter.model;

public class Courier {

    private String login;
    private String password;
    private String firstName;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }

    public Courier setPassword(String password) {
        this.password = password;
        return this;
    }

    public Courier setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
}
