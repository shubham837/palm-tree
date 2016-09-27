package crossover.controller;

import crossover.config.UnitTestContext;
import crossover.dao.UserDao;
import crossover.models.User;
import crossover.responses.UserDetailResponse;
import crossover.responses.UserListResponse;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by shubham.singhal on 27/09/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class UserControllerTest {
    private static final UUID USER_ID = UUID.fromString("8230a400-9745-41e4-90c0-2a0c1fbcdb3c");

    private UserController controller;

    private RedisCacheManager redisCacheManager;

    private UserDao userDao;

    private Cache cache;

    @Before
    public void setUp() {
        controller = new UserController();
        userDao = mock(UserDao.class);
        redisCacheManager = mock(RedisCacheManager.class);
        cache = mock(RedisCache.class);

        ReflectionTestUtils.setField(controller, "userDao", userDao);
        ReflectionTestUtils.setField(controller, "redisCacheManager", redisCacheManager);
    }

    @Test
    public void testPostUserSuccess() {
        User user = getUser();
        UUID userId = UUID.randomUUID();

        when(userDao.save(user)).thenReturn(user);
        when(redisCacheManager.getCache("Authorization")).thenReturn(cache);
        when(cache.putIfAbsent(userId + ":" + userId, userId)).thenReturn(null);

        ResponseEntity<UserDetailResponse> actual = controller.postUser(userId, user);
        verify(userDao, times(1)).save(user);
        verify(redisCacheManager, times(1)).getCache("Authorization");
        verify(cache, times(1)).putIfAbsent(userId + ":" + userId, userId);
        verifyNoMoreInteractions(userDao);

        assertEquals(actual.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testGetUserListSuccess() {
        UUID userId = UUID.randomUUID();

        User user = getUser();

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userDao.findAll()).thenReturn(users);

        ResponseEntity<UserListResponse> actual = controller.getUserList(userId);
        verify(userDao, times(1)).findAll();
        verifyNoMoreInteractions(userDao);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody().getUsers().size(), 1);
    }


    @Test
    public void testGetUserDetailSuccess() throws Exception{
        User user = getUser();
        UUID userId = UUID.randomUUID();

        user.setId(USER_ID);

        when(userDao.getDetail(USER_ID)).thenReturn(user);

        ResponseEntity<UserDetailResponse> actual = controller.getUserDetail(userId, USER_ID.toString());
        verify(userDao, times(1)).getDetail(USER_ID);
        verifyNoMoreInteractions(userDao);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody().getUser(), user);
    }


    @Test
    public void testPatchUserSuccess() {

        User user = getUser();
        UUID userId = UUID.randomUUID();
        when(userDao.getDetail(USER_ID)).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);


        ResponseEntity<UserDetailResponse> actual = controller.patchUserDetail(userId, USER_ID.toString(), user);
        verify(userDao, times(1)).getDetail(USER_ID);
        verify(userDao, times(1)).save(user);
        verifyNoMoreInteractions(userDao);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);

    }


    private User getUser() {
        User user = new User();
        user.setEmail("TestEmail@example.org");
        user.setName("Test Name");
        return user;
    }


}
