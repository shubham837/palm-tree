package crossover.security.authorization;


import crossover.models.RentalInfo;
import crossover.models.SecurityRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RentalInfoPermissionEvaluatorTest {

    private static final String DOMAIN_OBJECT_TODO = "Todo";

    private static final String ANONYMOUS = "anonymous";

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    private static final String PERMISSION_ADD = "add";

    private static final String ROLE_UNKNOWN = "ROLE_UNKNOWN";

    private static final String TARGET_ID = "1";

    private RentalInfoPermissionEvaluator permissionEvaluator;

    @Before
    public void setUp() {
        permissionEvaluator = new RentalInfoPermissionEvaluator();
    }

    @Test
    public void test_hasPermission_AnonymousUser_ShouldReturnFalse() {
        Authentication anonymous = createAuthenticationForAnonymousUser();
        boolean hasPermission = permissionEvaluator.hasPermission(anonymous, DOMAIN_OBJECT_TODO, PERMISSION_ADD);
        assertFalse(hasPermission);
    }

    @Test
    public void test_hasPermission_UserIsLoggedInAndTargetDomainObjectIsUnknown_ShouldReturnFalse() {
        Authentication loggedInUser = createAuthenticationForLoggedInUser(SecurityRole.ROLE_USER.name());
        boolean hasPermission = permissionEvaluator.hasPermission(loggedInUser, new RentalInfo(), PERMISSION_ADD);
        assertFalse(hasPermission);
    }

    @Test
    public void test_hasPermission_UserIsLoggedInButHasUnknownRole_ShouldReturnFalse() {
        Authentication loggedInUser = createAuthenticationForLoggedInUser(ROLE_UNKNOWN);
        boolean hasPermission = permissionEvaluator.hasPermission(loggedInUser, DOMAIN_OBJECT_TODO, PERMISSION_ADD);
        assertFalse(hasPermission);
    }

    @Test
    public void test_hasPermission_UserIsLoggedIn_ShouldReturnTrue() {
        Authentication loggedInUser = createAuthenticationForLoggedInUser(SecurityRole.ROLE_USER.name());
        boolean hasPermission = permissionEvaluator.hasPermission(loggedInUser, DOMAIN_OBJECT_TODO, PERMISSION_ADD);
        assertTrue(hasPermission);
    }

    @Test
    public void test_hasPermission_MethodNotImplemented_ShouldReturnFalse() {
        Authentication loggedInUser = createAuthenticationForLoggedInUser(SecurityRole.ROLE_USER.name());
        boolean hasPermission = permissionEvaluator.hasPermission(loggedInUser, TARGET_ID, DOMAIN_OBJECT_TODO, PERMISSION_ADD);
        assertFalse(hasPermission);
    }

    private Authentication createAuthenticationForAnonymousUser() {
        List<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
        return new AnonymousAuthenticationToken(ANONYMOUS, ANONYMOUS, authorities);
    }

    private Authentication createAuthenticationForLoggedInUser(String role) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
        UserDetails principal = createPrincipal(authorities);
        return createAuthentication(principal, authorities);
    }

    private UserDetails createPrincipal(List<GrantedAuthority> authorities ) {
        return new User(USERNAME, PASSWORD, authorities);
    }

    private Authentication createAuthentication(UserDetails principal, List<GrantedAuthority> authorities) {
        return new TestingAuthenticationToken(principal, USERNAME, authorities);
    }
}
