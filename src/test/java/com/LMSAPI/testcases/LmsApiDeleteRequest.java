package com.LMSAPI.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.LMSAPI.base.Base;
import com.Utils.ExcelUtil;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class LmsApiDeleteRequest extends Base {
	
	@Test(dataProvider="getData")
	public void test_DeleteProgram(String progId) {
		
		Response response = given()
				              .auth()
				              .basic(username,password)
				              
				           .when()
				              .delete(baseUrl + "/programs/"+ progId)
				              
				           .then()
				              .statusCode(200)
				              .extract()
				              .response();
		
		String responseBody = response.getBody().asString();
		Assert.assertEquals(responseBody, "");
	}
	
	
	@DataProvider(name="getData")
	public String[][] getTestData() throws IOException{
		
		//String filePath = "C:\\Users\\R\\eclipse-workspace\\APIAutomation\\src\\test\\resources\\LMS_test_data\\TestData.xlsx";
		int noOfRows = ExcelUtil.getRowCount(filePath, "delete");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "delete", 1);
		String[][] data = new String[noOfRows][noOfColumns];
		for(int i = 1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				
				data[i-1][0]=ExcelUtil.getCellData(filePath,"delete", i, j);
			}
		}
		
		return data;
				
	}

}
