import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {

    public static RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseStatusOk(){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification responseStatusBadRequest(){
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }

    public static void initialSpec(RequestSpecification requestSpecification, ResponseSpecification responseStatus){
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseStatus;

    }


}
