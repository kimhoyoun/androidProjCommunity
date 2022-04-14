package com.example.andprojcommunity.model;

public class UserAccount {

    private String name;
    private String emailId;
    private String age;
    private String gender;
    private String idToken;
    private String password;

    public UserAccount(){
        this("","","","","","");
    }
    public UserAccount(String name, String emailId, String age, String gender, String idToken, String password){
        this.name = name;
        this.emailId = emailId;
        this.age = age;
        this.gender = gender;
        this.idToken = idToken;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "name='" + name + '\'' +
                ", emailId='" + emailId + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", idToken='" + idToken + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
