import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {

    public static RequestSpecification requestSpecification(){

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/")
                .build();
        return requestSpecification;
    }

    public static ResponseSpecification responseStatusOk(){
        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        return responseSpecification;
    }

    public static ResponseSpecification responseStatusBadRequest(){
        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
        return responseSpecification;
    }

    public static void initialSpec(RequestSpecification requestSpecification, ResponseSpecification responseStatus){
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseStatus;

    }


}
