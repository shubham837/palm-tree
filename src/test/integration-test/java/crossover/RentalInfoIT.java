package crossover;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import crossover.Application;
import crossover.dao.RentalInfoDao;
import crossover.dao.RentalInfoSolrDao;
import crossover.dao.UserDao;
import crossover.models.RentalInfo;
import crossover.models.User;
import crossover.responses.RentalInfoDetailResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.*;

/**
 * Created by shubham.singhal on 27/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class RentalInfoIT {

    private static final Logger log = LoggerFactory.getLogger(RentalInfoIT.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RentalInfoDao rentalInfoDao;

    @Autowired
    private RentalInfoSolrDao rentalInfoSolrDao;

    @Value("${local.server.port}")
    private int serverPort;

    @Autowired
    @Qualifier(value = "RedisCacheManager")
    CacheManager redisCacheManager;

    private final static UUID USER1_ID = UUID.randomUUID();
    private final static UUID USER2_ID = UUID.randomUUID();
    private final static UUID USER3_ID = UUID.randomUUID();
    private final static UUID RENTAL_INFO1_ID = UUID.randomUUID();
    private final static UUID RENTAL_INFO2_ID = UUID.randomUUID();
    private final static String RENTAL_INFO_RESOURCE = "/rental-info";
    private final static String RENTAL_INFO_DETAIL_RESOURCE = "/rental-info/{rental_info_id}";
    private final static String AUTH_KEY1 = "TEST_AUTH_KEY1";
    private final static String ACCESS_TOKEN1 = "TEST_ACCESS_TOKEN1";
    private final static String AUTH_KEY2 = "TEST_AUTH_KEY2";
    private final static String ACCESS_TOKEN2 = "TEST_ACCESS_TOKEN2";
    private final static String ADMIN_AUTH_KEY = "TEST_ADMIN_AUTH_KEY";
    private final static String ADMIN_ACCESS_TOKEN = "TEST_ADMIN_ACCESS_TOKEN";
    private final static UUID ADMIN_UUID = UUID.fromString("c40bfc4e-377c-47f7-861e-3338b217105f");

    private User getUser1() {
        User user = new User();
        user.setId(USER1_ID);
        user.setName("TestName");
        user.setEmail("testemail1@example.com");
        return user;
    }

    private User getUser2() {
        User user = new User();
        user.setId(USER2_ID);
        user.setName("Test Name2");
        user.setEmail("testemail2@example.com");
        return user;
    }

    private User getUser3() {
        User user = new User();
        user.setId(USER3_ID);
        user.setName("TestName3");
        user.setEmail("testemail3@example.com");
        return user;
    }

    private RentalInfo getRentalInfo1() {

        RentalInfo rentalInfo = new RentalInfo();
        rentalInfo.setId(RENTAL_INFO1_ID.toString());
        rentalInfo.setType("Villa");
        rentalInfo.setCity("TestCity");
        rentalInfo.setDailyPrice(26.30);
        rentalInfo.setHasAirCondition(true);
        return rentalInfo;
    }

    private RentalInfo getRentalInfo2() {
        RentalInfo rentalInfo = new RentalInfo();
        rentalInfo.setId(RENTAL_INFO2_ID.toString());
        rentalInfo.setType("Bunglow");
        rentalInfo.setCity("TestCity2");
        rentalInfo.setDailyPrice(24.20);
        rentalInfo.setHasAirCondition(false);
        return rentalInfo;
    }

    private void saveToCassandra() {
        userDao.save(getUser1());
        userDao.save(getUser2());
        userDao.save(getUser3());
        rentalInfoDao.save(getRentalInfo1());
        rentalInfoDao.save(getRentalInfo2());
    }

    private void saveToRedis() {
        RedisCache cache = (RedisCache) redisCacheManager.getCache("Authorization");
        cache.putIfAbsent(AUTH_KEY1 + ':' + ACCESS_TOKEN1, USER1_ID);
        cache.putIfAbsent(AUTH_KEY2 + ':' + ACCESS_TOKEN2, USER2_ID);
        cache.putIfAbsent(ADMIN_AUTH_KEY + ':' + ADMIN_ACCESS_TOKEN, ADMIN_UUID);
    }

    @Before
    public void setUp() {
        saveToCassandra();
        saveToRedis();
        RestAssured.port = serverPort;
    }

    @After
    public void tearDown() {
        userDao.deleteAll();
        rentalInfoDao.deleteAll();
        Cache cache = redisCacheManager.getCache("Authorization");
        cache.clear();
    }

    @Test
    public void testGetRentalInfoListAPI() {
        Map<String, String> headers = new HashMap<>();
        headers.put("AUTH_KEY", ADMIN_AUTH_KEY);
        headers.put("ACCESS_TOKEN", ADMIN_ACCESS_TOKEN);

        Response response = given()
                .contentType("application/json")
                .headers(headers)
                .when()
                .get(RENTAL_INFO_RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("errors", emptyCollectionOf(List.class))
                .extract().response();
    }

    @Test
    public void testPostRentalInfoAPI() {
        Map<String, String> headers = new HashMap<>();
        headers.put("AUTH_KEY", ADMIN_AUTH_KEY);
        headers.put("ACCESS_TOKEN", ADMIN_ACCESS_TOKEN);

        RentalInfo rentalInfo = new RentalInfo();
        rentalInfo.setCity("TestCityPOST");
        rentalInfo.setCloseToBeach(true);
        rentalInfo.setType("Studio");

        Response response = given()
                .contentType("application/json")
                .body(rentalInfo)
                .headers(headers)
                .when()
                .post(RENTAL_INFO_RESOURCE)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .body("errors", emptyCollectionOf(List.class))
                .body("rentalInfo", notNullValue())
                .extract().response();
    }

    @Test
    public void testGetRentalInfoDetailAPI() {
        Map<String, String> headers = new HashMap<>();
        headers.put("AUTH_KEY", ADMIN_AUTH_KEY);
        headers.put("ACCESS_TOKEN", ADMIN_ACCESS_TOKEN);

        RentalInfoDetailResponse rentalInfoDetailResponse = given()
                .pathParam("rental_info_id", RENTAL_INFO1_ID.toString())
                .headers(headers)
                .when()
                .get(RENTAL_INFO_DETAIL_RESOURCE)
                .as(RentalInfoDetailResponse.class);

        Assert.assertNotNull(rentalInfoDetailResponse);
        RentalInfo rentalInfoDetail = rentalInfoDetailResponse.getRentalInfo();
        Assert.assertEquals(rentalInfoDetail.getId(), RENTAL_INFO1_ID.toString());
    }

    @Test
    public void testPutRentalInfoDetailAPI() {
        Map<String, String> headers = new HashMap<>();
        headers.put("AUTH_KEY", ADMIN_AUTH_KEY);
        headers.put("ACCESS_TOKEN", ADMIN_ACCESS_TOKEN);

        Map<String, String> rentalInfoDetail = new HashMap<>();
        rentalInfoDetail.put("type", "TestPUTType");
        rentalInfoDetail.put("city", "TestPUTCity");
        RentalInfoDetailResponse rentalInfoDetailResponse = given()
                .contentType("application/json")
                .pathParam("rental_info_id", RENTAL_INFO2_ID.toString())
                .body(rentalInfoDetail)
                .headers(headers)
                .when()
                .put(RENTAL_INFO_DETAIL_RESOURCE)
                .as(RentalInfoDetailResponse.class);

        Assert.assertNotNull(rentalInfoDetailResponse);
        RentalInfo rentalInfo = rentalInfoDetailResponse.getRentalInfo();
        Assert.assertEquals(rentalInfo.getId(), RENTAL_INFO2_ID.toString());

    }
}