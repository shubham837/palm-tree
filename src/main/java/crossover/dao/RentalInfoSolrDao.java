package crossover.dao;

import crossover.models.RentalInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;

import java.util.List;

/**
 * Created by shubham.singhal on 26/09/16.
 */
public interface RentalInfoSolrDao extends CustomBaseRepository<RentalInfo, String> {

    public List<RentalInfo> findByType(String type, Pageable page) throws Exception;

    @Query(name = "RentalInfo.findByNamedQuery")
    public List<RentalInfo> findByNamedQuery(String searchTerm, Pageable page) throws Exception;

    @Query("type:*?0* OR city:*?0*")
    public List<RentalInfo> findByQueryAnnotation(String searchTerm, Pageable page) throws Exception;
}