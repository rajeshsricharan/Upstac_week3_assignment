package org.upgrad.upstac.testrequests;

	import lombok.extern.slf4j.Slf4j;
	import org.junit.jupiter.api.Test;
	import org.mockito.Mock;
	import org.mockito.Mockito;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.core.annotation.Order;
	import org.springframework.http.HttpStatus;
	import org.springframework.security.test.context.support.WithUserDetails;
	import org.springframework.web.server.ResponseStatusException;
	import org.upgrad.upstac.config.security.UserLoggedInService;
	import org.upgrad.upstac.testrequests.lab.CreateLabResult;
	import org.upgrad.upstac.testrequests.lab.LabRequestController;
	import org.upgrad.upstac.testrequests.lab.TestStatus;
	import org.upgrad.upstac.testrequests.RequestStatus;
	import org.upgrad.upstac.users.User;
	import org.upgrad.upstac.users.UserService;
	import org.upgrad.upstac.users.models.Gender;

	import static org.hamcrest.MatcherAssert.assertThat;
	import static org.hamcrest.Matchers.containsString;
	import static org.hamcrest.Matchers.equalTo;
	import static org.hamcrest.Matchers.is;
	import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
	import static org.junit.jupiter.api.Assertions.*;

	import java.time.LocalDate;


	@SpringBootTest
	@Slf4j
	class LabRequestControllerTest {


	    @Autowired
	    LabRequestController labRequestController;

	    @Autowired
	    TestRequestQueryService testRequestQueryService;
	    
	    @Mock
	    UserLoggedInService userLoggedInService;
	    
	    @Autowired
	    TestRequestController testRequestController;
	    
	    
	    public TestRequest getTestRequestByStatus(RequestStatus status) {
	        return testRequestQueryService.findBy(status).stream().findFirst().get();
	     }
	   
	   

	     @Test
	     @Order(1)
	    @WithUserDetails(value = "tester")
	    public void calling_assignForLabTest_with_valid_test_request_id_should_update_the_request_status() {
	    	 
	    	 User user = getUser();

	         Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
	         
	         //Creating a new test to get the test request with status as INITIATED
	         CreateTestRequest createTest = new CreateTestRequest();
	         
	         TestRequest newTest = testRequestController.createRequest(createTest);
	    	 
	        TestRequest testRequest = getTestRequestByStatus(RequestStatus.INITIATED);
	        //Implement this method
	        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
	        // from labRequestController class. Pass the request id of testRequest object.

	        TestRequest result = labRequestController.assignForLabTest(testRequest.getRequestId());

	        //Use assertThat() methods to perform the following two comparisons
	        //  1. the request ids of both the objects created should be same
	        //  2. the status of the second object should be equal to 'INITIATED'
	        // make use of assertNotNull() method to make sure that the lab result of second object is not null
	        // use getLabResult() method to get the lab result

	        assertThat(result.getRequestId(), is(testRequest.getRequestId()));
	        assertThat(result.getStatus(),is(RequestStatus.LAB_TEST_IN_PROGRESS));
	        assertNotNull(result.getLabResult());
	       
	    }

	    

	    @Test
	    @Order(4)
	    @WithUserDetails(value = "tester")
	    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception() {

	        Long InvalidRequestId = -34L;

	        //Implement this method

	        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
	        // of labRequestController with InvalidRequestId as Id

	        //Use assertThat() method to perform the following comparison
	        //  the exception message should be contain the string "Invalid ID"

	        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> {
	            labRequestController.assignForLabTest(InvalidRequestId);
	        });

	        assertThat(result.getReason(),containsString("Invalid ID"));
	    }
	    
	    
	    @Test
	    @Order(3)
	    @WithUserDetails(value = "tester")
	    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details() {
	    	
	    //	User user = getUser();

	   //     Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
	      
	        
	        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

	        //Implement this method
	        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

	        //Create another object of the TestRequest method and explicitly update the status of this object
	        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)
	        CreateLabResult createLabResult = getCreateLabResult(testRequest);
	        
	        
	        TestRequest result = labRequestController.updateLabTest(testRequest.getRequestId(), createLabResult);
	        //Use assertThat() methods to perform the following three comparisons
	        //  1. the request ids of both the objects created should be same
	        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
	        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.

	        assertThat(result.getRequestId(),equalTo(testRequest.getRequestId()));
	        assertThat(result.getStatus(),equalTo(RequestStatus.LAB_TEST_COMPLETED));
	        assertThat(result.getLabResult().getResult(),equalTo(createLabResult.getResult()));        
	        
	    }



	    @Test
	    @Order(5)
	    @WithUserDetails(value = "tester")
	    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception() {

	       // TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
	    	Long InvalidRequestId = -34L;


	        //Implement this method
	        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

	        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
	        // of labRequestController with a negative long value as Id and the above created object as second parameter
	        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
	        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> {
	            labRequestController.assignForLabTest(InvalidRequestId);
	        });

	        //Use assertThat() method to perform the following comparison
	        //  the exception message should be contain the string "Invalid ID"

	        assertNotNull(result);
	        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
	        assertThat("Invalid ID", containsString(result.getReason()));

	    }

	    @Test
	    @Order(2)
	    @WithUserDetails(value = "tester")
	    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception() {
	    	
	        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

	        //Implement this method
	        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
	        // Set the result of the above created object to null.

	        CreateLabResult createLabResult = getCreateLabResult(testRequest);
	        createLabResult.setResult(null);
	        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
	        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
	        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
	        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> {
	            labRequestController.updateLabTest(testRequest.getRequestId(), createLabResult);
	        });

	        //Use assertThat() method to perform the following comparison
	        //  the exception message should be contain the string "ConstraintViolationException"
	        assertThat(result.getMessage(), containsString("ConstraintViolationException"));

	    }

	    public CreateLabResult getCreateLabResult(TestRequest testRequest) {

	        //Create an object of CreateLabResult and set all the values
	        // Return the object
	        CreateLabResult createLabResult = new CreateLabResult();
	        createLabResult.setComments("comments");
	        createLabResult.setBloodPressure("111");
	        createLabResult.setHeartBeat("43");
	        createLabResult.setOxygenLevel("22");
	        createLabResult.setTemperature("102");
	        createLabResult.setResult(TestStatus.NEGATIVE);
	        return createLabResult;
	    }
	    
	    public CreateTestRequest createTestRequest(User user) {
	        CreateTestRequest createTestRequest = new CreateTestRequest();
	        createTestRequest.setAddress("some Addres");
	        createTestRequest.setAge(98);
	        createTestRequest.setEmail("someone" + "98765412" + "@somedomain.com");
	        createTestRequest.setGender(Gender.MALE);
	        createTestRequest.setName("someuser");
	        createTestRequest.setPhoneNumber("98765412");
	        createTestRequest.setPinCode(716768);
	        return createTestRequest;
	    }
	    
	    public TestRequest updateTest(Long id) {
	    	TestRequest result = new TestRequest();
	    	result.setStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
			return result;
	    	
	    }
	    
	    
	    private User getUser() {
	        User user = new User();
	        user.setId(2L);
	        user.setUserName("doctor");
	        return user;
	    }
	    
	}

