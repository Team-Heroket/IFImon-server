package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserLoginDTO {



    private String token;

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String input) {
        this.token = input;
    }
}
