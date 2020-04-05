package com.test.chat.ws.objects;

public class Msg {

    public Msg() {
        super();
    }

    public Msg(String user, String message) {
        super();
        this.user = user;
        this.message = message;
    }

    private String user;
    private String message;

    public String getUser() {
        return user;
    }

    public void setUser(
            String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message) {
        this.message = message;
    }

}
