
import dto.RegistrationData;
import dto.RegistrationSuccessData;
import dto.ResourceData;
import dto.SingleUserData;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import specifications.Specifications;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class TestsWithDto {

    @Test
    public void getUsers(){
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusOk());
        List<SingleUserData> singleUserDataList = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().body()
                .extract().body().jsonPath().getList("data", SingleUserData.class);

        List<String> referencesList = singleUserDataList.stream().map(SingleUserData::getAvatar).collect(Collectors.toList());
        Optional<String> expectedNameOptional = TestUtils.getAvatarFileNameStream(referencesList).findFirst();

        Assertions.assertThat(expectedNameOptional).isPresent();
        String expectedFileName = expectedNameOptional.get();

        Assertions.assertThat(TestUtils.getAvatarFileNameStream(referencesList)).contains(expectedFileName);

    }

    @Test
    public void registerSuccessful() {
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusOk());
        RegistrationData registrationData = new RegistrationData("eve.holt@reqres.in","pistol");
        RegistrationSuccessData registrationSuccessData = given()
                .contentType("application/json")
                .body(registrationData)
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .extract().as(RegistrationSuccessData.class);

        Assertions.assertThat(registrationSuccessData.getId()).isNotNull();
        Assertions.assertThat(registrationSuccessData.getToken()).isNotEmpty();

    }

    @Test
    public void registerUnsuccessful() {
        Specifications.initialSpec(Specifications.requestSpecification(),Specifications.responseStatusBadRequest());
        RegistrationData registrationData = new RegistrationData("sydney@fife","");
        String message = "Missing password";
        Response response = given()
                .contentType("application/json")
                .body(registrationData)
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
        List<ResourceData> resourceDataList = given()
                .when()
                .get("/api/unknown")
                .then()
                .log().body()
                .extract().body().jsonPath().getList("data", ResourceData.class);

        List<Integer> referencesList = resourceDataList.stream().map(ResourceData::getYear).collect(Collectors.toList());
        Assertions.assertThat(referencesList).isSorted();

    }


}
