package org.techArk.login;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.techArk.requestPOJO.LoginRequest;
import org.techArk.responsePOJO.StatusResponse;
import org.techArk.utils.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ValidateLoginAPI_Functionality extends BaseTest {
    APIHelper apiHelper;

    @BeforeClass
    public void beforeClass() {
        apiHelper = new APIHelper();
    }

    @Test(priority = 0, description = "validate login functionality with valid credentials")
    public void validateLoginWithValidCredentials() {
        Response login = apiHelper.login(EnvironmentDetails.getProperty("username"), EnvironmentDetails.getProperty("password"));
        //Response login = apiHelper.login("username","password");//
        Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_CREATED, "Login is  working for valid credentials.");
        //Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_OK, "Login is  working for valid credentials.");
        String actualResponse = login.jsonPath().prettyPrint();
        actualResponse = actualResponse.replace("[", "").replace("]", "");
        JsonUtils.validateSchema(actualResponse, "LoginResponseSchema.json");
        // Validate the data: using hamcrest matcher
        
    }

    @Test(priority = 1, description = "validate login functionality with invalid credentials")
    public void validateLoginWithInValidCredentials() {
        Response login = apiHelper.login("username", "password");
        Assert.assertEquals(login.getStatusCode(), HttpStatus.SC_UNAUTHORIZED, "Login is not returning proper status code with invalid credentials.");
        StatusResponse statusResponse = login.as(StatusResponse.class);
        Assert.assertEquals(statusResponse.getStatus(), TestDataUtils.getProperty("invalidCredentialsMessage"), "Status message is not returning as expected");
    }

}
