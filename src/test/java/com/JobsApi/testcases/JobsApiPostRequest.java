package com.JobsApi.testcases;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.JobsApi.base.Jobs_Base;
import com.Utils.ExcelUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertThat;


public class JobsApiPostRequest extends Jobs_Base {
	
	int i=0;
	
	@Test(dataProvider = "getData", priority=1)
	public void test_CreateNewJob(String jobId, String jobTitle, String jobLocation, String jobCompanyName, String jobType, String jobPostedTime, String jobDescription) throws IOException {
		
		Response response = given()
				            .queryParam("Job Id", jobId)
				            .queryParam("Job Title", jobTitle)
				            .queryParam("Job Location", jobLocation)
				            .queryParam("Job Company Name", jobCompanyName)
				            .queryParam("Job Type", jobType)
				            .queryParam("Job Posted time", jobPostedTime)
				            .queryParam("Job Description", jobDescription)
				            
				           .when()
				             .post(baseUrl+"/Jobs");
		
		//status code validation
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
		LinkedHashMap<String,String> jobIdMap = map.get("Job Id");
		
		String jobIdPos = Integer.toString(jobIdMap.size()-1);
		
		String actualJobId = jobIdMap.get(jobIdPos);
		
		Assert.assertEquals(actualJobId, jobId);
		
		//System.out.println("The new job id is: "+actualJobId);
		
		//Job Title Validation
        LinkedHashMap<String,String> jobTitleMap = map.get("Job Title");
		
		String jobTitlePos = Integer.toString(jobTitleMap.size()-1);
		
		String actualJobTitle = jobTitleMap.get(jobTitlePos);
		
		Assert.assertEquals(actualJobTitle, jobTitle);
		
		//System.out.println("The new job title is: "+actualJobTitle);
		
		//Job Location Validation
        LinkedHashMap<String,String> jobLocationMap = map.get("Job Location");
		
		String jobLocationPos = Integer.toString(jobLocationMap.size()-1);
		
		String actualJobLocation = jobLocationMap.get(jobLocationPos);
		
		Assert.assertEquals(actualJobLocation, jobLocation);
		
		//System.out.println("The new job location is: "+actualJobLocation);
		
		//Job Company Name Validation
		 LinkedHashMap<String,String> jobCompanyMap = map.get("Job Company Name");
			
		 String jobCompanyPos = Integer.toString(jobCompanyMap.size()-1);
			
		 String actualJobCompany = jobCompanyMap.get(jobCompanyPos);
			
		 Assert.assertEquals(actualJobCompany, jobCompanyName);
			
		 //System.out.println("The new job company name is: "+actualJobCompany);
		 
		 //Job Type Validation
		 LinkedHashMap<String,String> jobTypeMap = map.get("Job Type");
			
		 String jobTypePos = Integer.toString(jobTypeMap.size()-1);
			
		 String actualJobType = jobTypeMap.get(jobTypePos);
			
		 Assert.assertEquals(actualJobType, jobType);
		
		 //System.out.println("The new job type is: "+actualJobType);
		 
		 //Job Posted Time Validation
		 LinkedHashMap<String,String> jobPostedTimeMap = map.get("Job Posted time");
			
		 String jobPostedTimePos = Integer.toString(jobPostedTimeMap.size()-1);
			
		 String actualJobPostedTime = jobPostedTimeMap.get(jobPostedTimePos);
			
		 Assert.assertEquals(actualJobPostedTime, jobPostedTime);
		
		 //System.out.println("The new job posted time is: "+actualJobPostedTime);
		 
		 //Job Description Validation
		 LinkedHashMap<String,String> jobDescriptionMap = map.get("Job Description");
			
		 String jobDescriptionPos = Integer.toString(jobDescriptionMap.size()-1);
			
		 String actualJobDescription = jobDescriptionMap.get(jobDescriptionPos);
			
		 Assert.assertEquals(actualJobDescription, jobDescription);
		
		 //System.out.println("The new job description is: "+actualJobDescription);
		 
		 System.out.println("The new job details are ");
		 System.out.println("Job Id: "+actualJobId);
		 System.out.println("Job Title: "+actualJobTitle);
		 System.out.println("Job Location: "+actualJobLocation);
		 System.out.println("Job Company Name: "+actualJobCompany);
		 System.out.println("Job Type: "+actualJobType);
		 System.out.println("Job Posted Time: "+actualJobPostedTime);
		 System.out.println("Job description: "+actualJobDescription);
		 
		 Reporter.log("The new job details are ");
		 Reporter.log("Job Id: "+actualJobId);
		 Reporter.log("Job Title: "+actualJobTitle);
		 Reporter.log("Job Location: "+actualJobLocation);
		 Reporter.log("Job Company Name: "+actualJobCompany);
		 Reporter.log("Job Type: "+actualJobType);
		 Reporter.log("Job Posted Time: "+actualJobPostedTime);
		 Reporter.log("Job description: "+actualJobDescription);
		 
		 
		 
		i=i+1;
		ExcelUtil.setCellData(filePath, "Put", i, 0, jobId);
		ExcelUtil.setCellData(filePath, "Delete", i, 0, jobId);
		
		
		
		
		
	}
	
	
	@Test(dataProvider = "getNegativeData", priority=2)
	public void test_CreateJobExistingId(String jobId, String jobTitle, String jobLocation, String jobCompanyName,String jobType,String jobPostedTime,String jobDescription) {
		
		Response response = given()
				              .queryParam("Job Id", jobId)
	                          .queryParam("Job Title", jobTitle)
	                          .queryParam("Job Location", jobLocation)
	                          .queryParam("Job Company Name", jobCompanyName)
	                          .queryParam("Job Type", jobType)
	                          .queryParam("Job Posted time", jobPostedTime)
	                          .queryParam("Job Description", jobDescription)
	            
	                      .when()
	                          .post(baseUrl+"/Jobs");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 409);
		
		String responseBody = response.getBody().asString();
		System.out.println("The response body is: "+responseBody);
		
		Assert.assertEquals(responseBody.contains("already exists"),true);
		
		
	}
	
	@DataProvider(name="getData")
	public String[][] getTestData() throws IOException{
		
		int noOfRows = ExcelUtil.getRowCount(filePath, "Post");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Post", 1);
		String[][] data = new String[noOfRows][noOfColumns];
		for(int i=1; i<=noOfRows; i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				
				data[i-1][j] = ExcelUtil.getCellData(filePath, "Post", i, j);
			}
		}
		
		return data;
		
	}
	
	
	@DataProvider(name="getNegativeData")
	public String[][] getNegativeTestData() throws IOException{
		
		int noOfRows = ExcelUtil.getRowCount(filePath, "Post_Negative");
		int noOfColumns = ExcelUtil.getCellCount(filePath, "Post_Negative", 1);
		String data[][] = new String[noOfRows][noOfColumns];
		for(int i=1;i<=noOfRows;i++) {
			
			for(int j=0; j<noOfColumns; j++) {
				data[i-1][j]=ExcelUtil.getCellData(filePath, "Post_Negative", i, j);
			}
		}
		
		return data;
		
	}

}
