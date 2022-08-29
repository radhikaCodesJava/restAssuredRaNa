package com.JobsApi.testcases;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.JobsApi.base.Jobs_Base;
import com.Utils.ExcelUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;

public class JobsApiDeleteRequest extends Jobs_Base{
	
	@Test(dataProvider = "getData", priority=1)
	public void deleteJob(String jobId) throws JsonMappingException, JsonProcessingException {
		
		Response response = given()
				             .queryParam("Job Id", jobId )
				
				           .when()
				             .delete(baseUrl + "/Jobs");
		
		//status code validation
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		String responseBody = response.getBody().asString();
		
		//schema validation
		assertThat(responseBody.replaceAll("NaN", "111"), matchesJsonSchemaInClasspath("jobsSchemaAll.json"));
		
		
		//validating if the job id is deleted
		ObjectMapper mapper = new ObjectMapper();
		
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>> hashmap = 
				mapper.readValue(responseBody.replaceAll("NaN","null"), new TypeReference<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>>>(){});
		
		LinkedHashMap<String, LinkedHashMap<String,String>> map = hashmap.get("data");
		
		
		LinkedHashMap<String, String> jobIdMap = map.get("Job Id");
		Assert.assertEquals(jobIdMap.containsValue(jobId),false);
		
	}
	
	
	
	@Test(dataProvider = "getNegativeData",priority=2)
	public void test_DeleteNonExistingJob(String jobId) {
		
		Response response = given()
				              .queryParam("Job Id", jobId)
				              
				           .when()
				               .delete(baseUrl +"/Jobs");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 404);
		
		String responseBody = response.getBody().asPrettyString();
		System.out.println("The response body is: "+responseBody);
		Assert.assertEquals(responseBody.contains("Job  not found"), true);
	}
	
	
	@DataProvider(name = "getData")
	public String[][] getTestData() throws IOException{
		
		int noOfRows = ExcelUtil.getRowCount(filePath, "Delete");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Delete", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		for(int i=1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				data[i-1][j] = ExcelUtil.getCellData(filePath, "Delete", i, j);
			}
		}
		
		return data;
	}
	
	
	@DataProvider(name = "getNegativeData")
	public String[][] getNegativeTestData() throws IOException{
		
		int noOfRows = ExcelUtil.getRowCount(filePath, "Delete_Negative");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Delete_Negative", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		for(int i=1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				data[i-1][j] = ExcelUtil.getCellData(filePath, "Delete_Negative", i, j);
			}
		}
		
		return data;

}
}
