package crossover.security.authentication;

import crossover.security.authentication.RestAuthenticationFailureHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;


public class RestAuthenticationFailureHandlerTest {

    private RestAuthenticationFailureHandler failureHandler;

    @Before
    public void setUp() {
        failureHandler = new RestAuthenticationFailureHandler();
    }

    @Test
    public void test_onAuthenticationFailure_ShouldSetResponseStatusToUnauthorized() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException ex = new BadCredentialsException("");

        failureHandler.onAuthenticationFailure(request, response, ex);

        assertEquals(MockHttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(RestAuthenticationFailureHandler.STATUS_MESSAGE_AUTHENTICATION_FAILED, response.getErrorMessage());
    }

}
