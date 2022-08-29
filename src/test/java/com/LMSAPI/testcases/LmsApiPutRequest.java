package com.LMSAPI.testcases;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.LMSAPI.base.Base;
import com.Utils.ExcelUtil;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LmsApiPutRequest extends Base {
	
	@Test(dataProvider = "getData")
	public void test_UpdateProgram(String progId, String progName, String progDescription, String online) {
		
		JSONObject requestParams = new JSONObject();
		
		requestParams.put("programId", progId);
		requestParams.put("programName", progName);
		requestParams.put("programDescription", progDescription);
		requestParams.put("online", online);
		
		
		Response response = given()
				              .auth()
				              .basic(username,password)
				              .contentType("application/json")
				              .body(requestParams)
				              
				           .when()
				              .put(baseUrl +"/programs/"  +progId)
				              
				           .then()
				              .assertThat()
				              .body(matchesJsonSchemaInClasspath("lmsSchema.json"))
				              .statusCode(200)
				              .extract()
				              .response();
		
		String responseBody = response.getBody().asString();
		
		Reporter.log("The response body is: "+responseBody);
		
		System.out.println("The response body is: "+responseBody);
		
		JsonPath jsonpath = response.jsonPath();
		
		String pId = jsonpath.getString("programId");
		String pName = jsonpath.getString("programName");
		String pDescription = jsonpath.getString("programDescription");
		String onlne = jsonpath.getString("online").toUpperCase();
		
		Assert.assertNotNull(pId);
		Assert.assertEquals(pId, progId);
		Assert.assertEquals(pName, progName);
		Assert.assertEquals(pDescription, progDescription);
		Assert.assertEquals(onlne, online);
				           
		
	}
	
	
	@DataProvider(name="getData")
	public String[][] getTestData() throws IOException{
		
		//String filePath = "C:\\Users\\R\\eclipse-workspace\\APIAutomation\\src\\test\\resources\\LMS_test_data\\TestData.xlsx";
		int noOfRows = ExcelUtil.getRowCount(filePath, "Put");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Put", 2);
		String[][] data = new String[noOfRows][noOfColumns];
		for(int i=1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				
				data[i-1][j]=ExcelUtil.getCellData(filePath, "Put", i, j);
			}
		}
		
		return data;
		
	}
	
	

}
