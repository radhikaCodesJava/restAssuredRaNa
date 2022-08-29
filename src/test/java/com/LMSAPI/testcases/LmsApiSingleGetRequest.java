package com.LMSAPI.testcases;

import com.LMSAPI.base.Base;
import com.Utils.ExcelUtil;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LmsApiSingleGetRequest extends Base{
	
	@Test(dataProvider = "getData")
	public void test_GetSingleProgram(String programId){
		
		Response response = given()
		                      .auth()
		                      .basic(username,password)
		
		                    .when()
		                      .get(baseUrl+"/programs/"+ programId)
		                      
		                    .then()
		                      .assertThat()
		                      .body(matchesJsonSchemaInClasspath("lmsSchema.json"))
		                      .statusCode(200)
		                      .extract()
		                      .response();
		 
		
		String responseBody = response.getBody().asString();
		
		System.out.println("Response body is: "+responseBody);
		
	    JsonPath jsonpath = response.jsonPath();
	    
	    String progId = jsonpath.getString("programId");
	    
	    Assert.assertNotNull(progId);
	    
	    Assert.assertEquals(progId, programId);
		
		
	}
	
	@DataProvider(name="getData")
	
	public String[][] getTestData() throws IOException{
		
		String filePath = "C:\\Users\\balum\\Desktop\\restassured demos\\rahila project\\APIAutomation\\src\\test\\resources\\LMS_test_data\\TestData.xlsx";
		//String filePath = "C:\\Users\\R\\eclipse-workspace\\APIAutomation\\src\\test\\resources\\LMS_test_data\\TestData.xlsx";
		int noOfRows = ExcelUtil.getRowCount(filePath, "GetId");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "GetId", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		
		for(int i = 1; i<=noOfRows; i++) {
			
			for(int j=0;j<noOfColumns;j++) {
				
				data[i-1][j]=ExcelUtil.getCellData(filePath, "GetId", i, j);
			}
		}
		
		return data;
	}

}
