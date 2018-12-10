package nsl.sam.example;

import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class JavaSamExampleApplicationTests {

	final String FILE_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY =
			"Well done demo-user! You are authorized basing on credentials stored in file. Your roles are [ROLE_USER]";

	final String  HARDCODED_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY =
			"Well done hardcoded-demo-user! You are authorized basing credentials hardcoded in annotation attribute. Your roles are [ROLE_USER]";

	final String  ENVIRONMENT_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY =
			"Well done environment-demo-user! You are authorized basing on credentials get from environment variable. Your roles are [ROLE_USER]";

	@Autowired
	private MockMvc mvc;

	@BeforeClass
	static public void setUpEnvironmentProvider() {
		System.setProperty(
				SteeredEnvironmentVariablesAccessor.SUPPLIER_PROPERTY_NAME,
				EnvironmentVariablesSupplier.class.getCanonicalName()
		);
	}

	@AfterClass
	static public void cleanUpEnvironmentProvider() {
		System.clearProperty(EnvironmentVariablesSupplier.class.getCanonicalName());
	}

    @Test
    public void unauthorizedWhenNoCredentialsAtAll() throws  Exception {
        MvcResult mvcResult =  mvc.perform(get("/file-user-info")).andReturn();

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.UNAUTHORIZED.value(), mvcResult.getResponse().getStatus()
        );
    }

    @Test
    public void authorizedWithHardcodedUserWhenHardcodedUserInfoEndpointCalled() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/hardcoded-user-info").with(httpBasic(
                        "hardcoded-demo-user", "hardcoded-demo-password"
                ))
        ).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.getResponse().getStatus()
        );

        assertEquals(
                "Response body doesn't match the expected value",
                HARDCODED_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    public void authorizedWithHardcodedTokenWhenHardcodedUserInfoEndpointCalled() throws Exception {
        MvcResult mvcResult  = mvc.perform(
                get("/hardcoded-user-info").header(
                        "Authorization",
                        "Bearer TOKEN_HARDCODED_IN_ANNOTATION"
                )
        ).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.getResponse().getStatus()
        );

        assertEquals(
                "Response body doesn't match the expected value",
                HARDCODED_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    public void authorizedWithTokenFromFileWhenFileUserInfoEndpointCalled() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/file-user-info")
                        .header(
                                "Authorization",
                                "Bearer TOKEN_STORED_IN_FILE"
                        )
        ).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.getResponse().getStatus()
        );

        assertEquals(
                "Response body doesn't match the expected value",
                FILE_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
    public void authorizedWithUserFromFileWhenFileUserInfoEndpointCalled() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/file-user-info").with(httpBasic(
                        "demo-user", "demo-password"
                ))
        ).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.getResponse().getStatus()
        );

        assertEquals(
                "Response body doesn't match the expected value",
                FILE_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.getResponse().getContentAsString()
        );
    }

    @Test
	public void authorizedWithEnvironmentUserWhenEnvironmentUserInfoEndpointCalled() throws Exception {
		MvcResult mvcResult = mvc.perform(
				get("/environment-user-info").with(httpBasic(
						"environment-demo-user", "environment-demo-password"
				))
		).andReturn();

		System.out.println(mvcResult.getResponse().getContentAsString());

		assertEquals(
				"Response status different from expected value.",
				HttpStatus.OK.value(), mvcResult.getResponse().getStatus()
		);

		assertEquals(
				"Response body doesn't match the expected value",
				ENVIRONMENT_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
				mvcResult.getResponse().getContentAsString()
		);
	}

    @Test
    public void authorizedWithEnvironmentTokenWhenEnvironmentUserInfoEndpointCalled() throws Exception {
        MvcResult mvcResult = mvc.perform(
                get("/environment-user-info").header(
                        "Authorization",
                        "Bearer TOKEN_READ_FROM_ENVIRONMENT"
                )
        ).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.getResponse().getStatus()
        );

        assertEquals(
                "Response body doesn't match the expected value",
                ENVIRONMENT_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.getResponse().getContentAsString()
        );
    }
}
