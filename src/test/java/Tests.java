import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;

public class Tests {

    @Test
    public void getUsers() {
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusOk());
        Response response = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().body()
                .extract().response();

        List<String> referencesList = response.jsonPath().getList("data.avatar", String.class);
        Optional<String> expectedNameOptional = TestUtils.getAvatarFileNameStream(referencesList).findFirst();

        Assertions.assertThat(expectedNameOptional).isPresent();
        String expectedFileName = expectedNameOptional.get();

        Assertions.assertThat(TestUtils.getAvatarFileNameStream(referencesList)).contains(expectedFileName);
    }

    @Test
    public void registerSuccessful() {
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusOk());
        Map<String, String> data = new HashMap<>();
        data.put("email", "eve.holt@reqres.in");
        data.put("password", "pistol");
        data.put("id", "4");
        data.put("token", "QpwL5tke4Pnpja7X4");

        Response response = given()
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();

        Assertions.assertThat(jsonPath.getString("id")).isEqualTo(data.get("id"));
        Assertions.assertThat(jsonPath.getString("token")).isEqualTo(data.get("token"));
    }

    @Test
    public void registerUnsuccessful() {
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusBadRequest());
        Map<String, String> data = new HashMap<>();
        data.put("email", "sydney@fife");
        String message = "Missing password";
        Response response = given()
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assertions.assertThat(jsonPath.getString("error")).isEqualTo(message);
    }


    @Test
    public void getUsersList(){
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusOk());
        Response response = given()
                .when()
                .get("/api/unknown")
                .then()
                .log().body()
                .extract().response();

        List<String> referencesList = response.jsonPath().getList("data.year", String.class);
        Assertions.assertThat(referencesList).isSorted();
    }

}
