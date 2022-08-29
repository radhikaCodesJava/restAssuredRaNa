package com.JobsApi.testcases;

import com.JobsApi.base.Jobs_Base;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class JobsApiGetRequest extends Jobs_Base{
	
	@Test
	public void test_GetAllJobDetails() throws JsonMappingException, JsonProcessingException {
		
		Response response = given()
				            
				           .when()
				             .get(baseUrl + "/Jobs");
		                  
		
		//status code validation
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		String responseBody = response.getBody().asString();
		
		//schema validation
		assertThat(responseBody.replaceAll("NaN", "111"), matchesJsonSchemaInClasspath("jobsSchemaAll.json"));
	   
	   ObjectMapper mapper = new ObjectMapper();
	   //mapper.enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature());
	   
	  // LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> map =
		        //(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>) mapper.readValue(responseBody.replaceAll("NaN", "null"), Map.class);
	   
	   LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>> hashmap = 
		mapper.readValue(responseBody.replaceAll("NaN","null"), new TypeReference<LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>>>(){});
	   
	   LinkedHashMap<String, LinkedHashMap<String,String>> dataMap = hashmap.get("data");
	   
	   //validating if nodes are present
	   Assert.assertEquals(dataMap.containsKey("Job Id"), true);
	   Assert.assertEquals(dataMap.containsKey("Job Title"), true);
	   Assert.assertEquals(dataMap.containsKey("Job Location"), true);
	   Assert.assertEquals(dataMap.containsKey("Job Type"), true);
	   Assert.assertEquals(dataMap.containsKey("Job Posted time"), true);
	   Assert.assertEquals(dataMap.containsKey("Job Company Name"), true);
	   Assert.assertEquals(dataMap.containsKey("Job Description"), true);
	   
	  
	   }

}
