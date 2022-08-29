package com.LMSAPI.testcases;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.LMSAPI.base.Base;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class LmsApiGetRequest extends Base {
	
	@Test
	public void test_GetAllPrograms() {
		
		Response response = given()
		                     .auth()
		                       .basic(username, password)
		
		                    .when()
		                       .get(baseUrl + "/programs")
		                       
		                    .then()
		                       .assertThat()
		                       .body(matchesJsonSchemaInClasspath("lmsSchemaAll.json"))
		                       .body("[0]", hasKey("programId"))
		                       .body("[0]", hasKey("programName"))
		                       .body("[0]", hasKey("programDescription"))
		                       .body("[0]", hasKey("online"))
		                       .statusCode(200)
		                       .extract()
		                       .response();
		

		JsonPath jsonpath = response.jsonPath();
		
		String firstProgId = jsonpath.getString("[0].programId");
		
		Reporter.log("The first program Id is: "+firstProgId);
		
		System.out.println("The first program Id is: "+firstProgId);
		
		Assert.assertNotNull(firstProgId);
		
		
		
		
    }
	
}
	
