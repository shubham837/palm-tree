package crossover.controller;

import crossover.config.UnitTestContext;
import crossover.dao.RentalInfoDao;
import crossover.dao.RentalInfoSolrDao;
import crossover.models.RentalInfo;
import crossover.responses.RentalInfoDetailResponse;
import crossover.responses.RentalInfoListResponse;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class RentalInfoControllerTest {

    private static final String RENTAL_CITY = "TEST_CITY";
    private static final String RENTAL_COUNTRY = "TEST_COUNTRY";
    private static final String RENTAL_CURRENCY = "TEST_CURRENCY";
    private static final String RENTAL_TYPE = "TEST_TYPE";
    private static final String RENTAL_ID = "TEST_ID";

    private RentalInfoController controller;

    private RentalInfoDao rentalInfoDao;

    private RentalInfoSolrDao rentalInfoSolrDao;

    @Before
    public void setUp() {
        controller = new RentalInfoController();
        rentalInfoDao = mock(RentalInfoDao.class);
        rentalInfoSolrDao = mock(RentalInfoSolrDao.class);
        ReflectionTestUtils.setField(controller, "rentalInfoDao", rentalInfoDao);
        ReflectionTestUtils.setField(controller, "rentalInfoSolrDao", rentalInfoSolrDao);
    }

    @Test
    public void testPostRentalInfoSuccess() {
        RentalInfo rentalInfo = getRentalInfo();
        UUID userId = UUID.randomUUID();

        when(rentalInfoDao.save(rentalInfo)).thenReturn(rentalInfo);
        when(rentalInfoSolrDao.save(rentalInfo)).thenReturn(rentalInfo);

        ResponseEntity<RentalInfoDetailResponse> actual = controller.postRentalInfo(userId, rentalInfo);
        verify(rentalInfoDao, times(1)).save(rentalInfo);
        verifyNoMoreInteractions(rentalInfoDao);
        verify(rentalInfoSolrDao, times(1)).save(rentalInfo);

        assertEquals(actual.getStatusCode(), HttpStatus.CREATED);
        assertEquals(actual.getBody().getRentalInfo().getCity(), RENTAL_CITY);
        assertEquals(actual.getBody().getRentalInfo().getCountry(), RENTAL_COUNTRY);
        assertEquals(actual.getBody().getRentalInfo().getType(), RENTAL_TYPE);
    }

    @Test
    public void testGetRentalInfoListSolrSuccess() throws Exception{

        RentalInfo rentalInfo = getRentalInfo();

        List<RentalInfo> rentalInfos = new ArrayList<>();
        rentalInfos.add(rentalInfo);

        when(rentalInfoSolrDao.findByGeneralSearchQuery("Villa", new SolrPageRequest(1, 20))).thenReturn(rentalInfos);

        ResponseEntity<RentalInfoListResponse> actual = controller.getRentalInfoList("Villa",
                "Villa", "TESTCITY", "TESTPROVINCE", "TESTCOUNTRY", "TESTZIPCODE",
                Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Double.valueOf(43.5),
                Double.valueOf(24), Double.valueOf(10), Double.valueOf(2));
        verify(rentalInfoSolrDao, times(1)).findByGeneralSearchQuery("Villa", new SolrPageRequest(1, 20));
        verifyZeroInteractions(rentalInfoDao);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody().getRentalInfos().size(), 1);
        assertEquals(actual.getBody().getRentalInfos().get(0).getCity(), RENTAL_CITY);
        assertEquals(actual.getBody().getRentalInfos().get(0).getCountry(), RENTAL_COUNTRY);
        assertEquals(actual.getBody().getRentalInfos().get(0).getType(), RENTAL_TYPE);
    }

    @Test
    public void testGetRentalInfoListDBFallbackSuccess() throws Exception{

        RentalInfo rentalInfo = getRentalInfo();

        List<RentalInfo> rentalInfos = new ArrayList<>();
        rentalInfos.add(rentalInfo);

        when(rentalInfoSolrDao.findByGeneralSearchQuery("Villa", new SolrPageRequest(1, 20))).thenThrow(new Exception("Solr Exception"));
        when(rentalInfoDao.findAll()).thenReturn(rentalInfos);

        ResponseEntity<RentalInfoListResponse> actual = controller.getRentalInfoList("Villa",
                "Villa", "TESTCITY", "TESTPROVINCE", "TESTCOUNTRY", "TESTZIPCODE",
                Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Double.valueOf(43.5),
                Double.valueOf(24), Double.valueOf(10), Double.valueOf(2));

        verify(rentalInfoSolrDao, times(1)).findByGeneralSearchQuery("Villa", new SolrPageRequest(1, 20));
        verify(rentalInfoDao, times(1)).findAll();
        verifyNoMoreInteractions(rentalInfoDao);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody().getRentalInfos().size(), 1);
        assertEquals(actual.getBody().getRentalInfos().get(0).getCity(), RENTAL_CITY);
        assertEquals(actual.getBody().getRentalInfos().get(0).getCountry(), RENTAL_COUNTRY);
        assertEquals(actual.getBody().getRentalInfos().get(0).getType(), RENTAL_TYPE);
    }


    @Test
    public void testGetRentalInfoDetailSuccess() throws Exception{
        RentalInfo rentalInfo = getRentalInfo();
        UUID userId = UUID.randomUUID();

        rentalInfo.setId(RENTAL_ID);

        when(rentalInfoDao.findById(RENTAL_ID)).thenReturn(rentalInfo);

        ResponseEntity<RentalInfoDetailResponse> actual = controller.getRentalInfoDetail(userId, RENTAL_ID);
        verify(rentalInfoDao, times(1)).findById(RENTAL_ID);
        verifyNoMoreInteractions(rentalInfoDao);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody().getRentalInfo(), rentalInfo);
    }

    @Test
    public void testGetRentalInfoDetailDatabaseFailure() throws Exception{

        UUID userId = UUID.randomUUID();

        when(rentalInfoDao.findById(RENTAL_ID)).thenThrow(new Exception());

        ResponseEntity<RentalInfoDetailResponse> actual = controller.getRentalInfoDetail(userId, RENTAL_ID);
        verify(rentalInfoDao, times(1)).findById(RENTAL_ID);
        verifyNoMoreInteractions(rentalInfoDao);

        assertEquals(actual.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertNull(actual.getBody().getRentalInfo());

    }

    @Test
    public void testPutRentalInfoSuccess() {
        RentalInfo rentalInfo = getRentalInfo();
        UUID userId = UUID.randomUUID();

        when(rentalInfoDao.save(rentalInfo)).thenReturn(rentalInfo);
        when(rentalInfoSolrDao.save(rentalInfo)).thenReturn(rentalInfo);

        ResponseEntity<RentalInfoDetailResponse> actual = controller.putRentalInfo(userId, RENTAL_ID, rentalInfo);
        verify(rentalInfoDao, times(1)).save(rentalInfo);
        verifyNoMoreInteractions(rentalInfoDao);
        verify(rentalInfoSolrDao, times(1)).save(rentalInfo);

        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody().getRentalInfo().getCity(), RENTAL_CITY);
        assertEquals(actual.getBody().getRentalInfo().getCountry(), RENTAL_COUNTRY);
        assertEquals(actual.getBody().getRentalInfo().getType(), RENTAL_TYPE);
        assertEquals(actual.getBody().getRentalInfo().getId(), RENTAL_ID);
    }


    private RentalInfo getRentalInfo() {
        RentalInfo rentalInfo = new RentalInfo();
        rentalInfo.setCity(RENTAL_CITY);
        rentalInfo.setCountry(RENTAL_COUNTRY);
        rentalInfo.setCurrency(RENTAL_CURRENCY);
        rentalInfo.setHasAirCondition(true);
        rentalInfo.setType(RENTAL_TYPE);
        rentalInfo.setCloseToBeach(false);
        return rentalInfo;
    }

}
