import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
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
        Optional<String> expectedNameOptional = referencesList.stream()
                .map(reference -> reference.split("/"))
                .map(stringArray -> stringArray[stringArray.length - 1]).findFirst();

        Assert.assertTrue(expectedNameOptional.isPresent());
        String expectedFileName = expectedNameOptional.get();


        referencesList.stream()
                .map(reference -> reference.split("/"))
                .map(stringArray -> stringArray[stringArray.length - 1])
                .forEach(resultFileName -> Assert.assertEquals(
                        resultFileName,
                        expectedFileName,
                        "Имя файла аватара отличается от ожидаемого"));

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
                .contentType("application/json")
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getString("id"), data.get("id"));
        Assert.assertEquals(jsonPath.getString("token"), data.get("token"));

    }

    @Test
    public void registerUnsuccessful() {
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusBadRequest());
        String email = "sydney@fife";
        String message = "Missing email or username";
        Response response = given()
                .body(email)
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.getString("error"), message);
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
        List<String> myList = new ArrayList<>(referencesList);
        Collections.sort(myList);

        Assert.assertEquals(myList,referencesList);
    }

}
