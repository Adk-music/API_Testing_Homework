package dto;

public class RegistrationSuccessData {

    private Integer id;
    private String token;

    public RegistrationSuccessData() {
        super();
    }

    public RegistrationSuccessData(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }
    public String getToken() {
        return token;
    }

}
