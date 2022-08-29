package com.JobsApi.testcases;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
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

public class JobsApiPutRequest extends Jobs_Base {
	String updatedValueKey;
	
	@Test(dataProvider = "getData", priority=1)
	public void test_UpdateJob(String jobId, String jobTitle, String jobLocation) throws JsonMappingException, JsonProcessingException {
		
		Response response = given()
				              .queryParam("Job Id", jobId)
				              .queryParam("Job Title", jobTitle)
				              .queryParam("Job Location", jobLocation)
				              
				           .when()
				              .put(baseUrl + "/Jobs");
		
		//Status code validation
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		String responseBody = response.getBody().asString();
		
		//schema validation
		assertThat(responseBody.replaceAll("NaN", "111"), matchesJsonSchemaInClasspath("jobsSchemaAll.json"));
		
		ObjectMapper mapper = new ObjectMapper();
		
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>> hashmap = 
				mapper.readValue(responseBody.replaceAll("NaN","null"), new TypeReference<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>>>(){});
		
		LinkedHashMap<String, LinkedHashMap<String,String>> map = hashmap.get("data");
		
		//Job Id Validation
		LinkedHashMap<String, String> jobIdMap = map.get("Job Id");
		Assert.assertTrue(jobIdMap.containsValue(jobId));
		
		//Job Title Validation
		LinkedHashMap<String, String> jobTitleMap = map.get("Job Title");
		Assert.assertTrue(jobTitleMap.containsValue(jobTitle));
		
		//Job Location Validation
		LinkedHashMap<String, String> jobLocationMap = map.get("Job Location");
		Assert.assertTrue(jobLocationMap.containsValue(jobLocation));
		
		
		}
	
	
	@Test(dataProvider = "getNegativeData", priority=2)
	public void test_UpdateNonExistingJob(String jobId, String jobTitle, String jobLocation) {
		
		Response response = given()
				              .queryParam("Job Id", jobId)
				              .queryParam("Job Title", jobTitle)
				              .queryParam("Job Location", jobLocation)
				              
				            .when()
				               .put(baseUrl+"/Jobs");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 404);
		
		String responseBody = response.getBody().asPrettyString();
		System.out.println("The response body is: "+responseBody);
		
		Assert.assertEquals(responseBody.contains("job not found"), true);
				              
	}
	
	
	@DataProvider(name="getData")
	public String[][] getTestData() throws IOException{
		
		int noOfRows = ExcelUtil.getRowCount(filePath, "Put");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Put", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		for(int i=1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				
				data[i-1][j] = ExcelUtil.getCellData(filePath, "Put", i, j);
			}
		}
		
		return data;
		
		
		
	}
	
	@DataProvider(name = "getNegativeData")
	public String[][] getNegativeTestData() throws IOException{
		
		int noOfRows = ExcelUtil.getRowCount(filePath, "Put_Negative");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Put_Negative", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		for(int i=1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				
				data[i-1][j] = ExcelUtil.getCellData(filePath, "Put_Negative", i, j);
			}
		}
		
		return data;
		
	}

}
