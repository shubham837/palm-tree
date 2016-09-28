package crossover.dao;

import crossover.models.RentalInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;

import java.util.List;

/**
 * Created by shubham.singhal on 26/09/16.
 */
public interface RentalInfoSolrDao extends org.springframework.data.solr.repository.SolrCrudRepository<RentalInfo, String> {


    @Query(name = "RentalInfo.findByGeneralSearchQuery")
    public List<RentalInfo> findByGeneralSearchQuery(String searchTerm, Pageable page) throws Exception;

    @Query(name = "RentalInfo.findByNamedQuery")
    public List<RentalInfo> findByNamedQuery(String type, String city, String province, String Country, String zipCode,
                                             String hasAirCondition, String hasGarden, String hasPool, String isCloseToBeach,
                                             String dailyPriceLow, String dailyPriceHigh, String roomsNumberLow, String roomsNumberHigh) throws Exception;

    @Query("type:*?0* OR city:*?0*")
    public List<RentalInfo> findByQueryAnnotation(String searchTerm, Pageable page) throws Exception;


    @Query(value = "*:*")
    @Facet(fields = { "type" })
    FacetPage<RentalInfo> findAllFacetOnType(Pageable page);

    @Query(value = "*:*")
    @Facet(fields = { "type", "isCloseToBeach" })
    FacetPage<RentalInfo> findAllFacetOnTypeAndIsCloseToBeach(Pageable page);

    @Query(value = "*:*")
    @Facet(queries = { "roomsNumber:[* TO ?0]" })
    FacetPage<RentalInfo> findAllFacetQueryRoomsNumber(double roomsNumber, Pageable page);

    @Query(value = "*:*")
    @Facet(queries = { "dailyPrice:[* TO ?0]" })
    FacetPage<RentalInfo> findAllFacetQueryDailyPrice(double dailyPrice, Pageable page);


    @Query(value = "*:*")
    @Facet(fields = "type", prefix = "?0")
    FacetPage<RentalInfo> findAllFacetOnTypeWithDynamicPrefix(String prefix, Pageable page);


    @Query(value = "*:*", timeAllowed = 250)
    List<RentalInfo> findAllWithExecutiontimeRestriction();


    @Query("name:?0*")
    @Highlight
    HighlightPage<RentalInfo> findByTypeHighlightAll(String type, Pageable page);

    @Query("name:?0*")
    @Highlight(prefix = "<b>", postfix = "</b>")
    HighlightPage<RentalInfo> findByTypeHighlightAllWithPreAndPostfix(String type, Pageable page);

    @Query("type:?0* AND hasGarden:?1")
    List<RentalInfo> findByTypeAndHasGarden(String type, Boolean hasGarden);

    public List<RentalInfo> findByType(String type, Pageable page) throws Exception;

    List<RentalInfo> findByCity(String city, Pageable page);

    List<RentalInfo> findByProvince(String province, Pageable page);

    List<RentalInfo> findByCountry(String country, Pageable page);

    List<RentalInfo> findByZipCode(String zipCode, Pageable page);

    List<RentalInfo> findByTypeAndHasAirConditionTrue();

    List<RentalInfo> findByTypeAndIsCloseToBeachTrue();

    List<RentalInfo> findByTypeAndHasGardenFalse();

    List<RentalInfo> findByTypeAndHasPoolTrue();

    List<RentalInfo> findByDailyPriceBetween(double low, double up);

    List<RentalInfo> findByRoomsNumberBetween(double low, double up);

    List<RentalInfo> findByDailyPriceLessThanEqual(double up);

    List<RentalInfo> findByDailyPriceGreaterThanEqual(double up);

    List<RentalInfo> findByTypeLike(String type);

    List<RentalInfo> findByTypeStartsWith(String type);

}