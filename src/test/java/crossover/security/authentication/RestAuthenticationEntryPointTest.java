package crossover.security.authentication;

import crossover.security.authentication.RestAuthenticationEntryPoint;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;


public class RestAuthenticationEntryPointTest {
    private static final Logger log = LoggerFactory.getLogger(RestAuthenticationEntryPointTest.class);

    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Before
    public void setUp() {
        authenticationEntryPoint = new RestAuthenticationEntryPoint();
    }

    @Test
    public void test_commence_ShouldSetResponseStatusToUnauthorized() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException ex = new AuthenticationCredentialsNotFoundException("");

        authenticationEntryPoint.commence(request, response, ex);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }
}
