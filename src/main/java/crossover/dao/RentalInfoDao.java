package crossover.dao;

import crossover.models.RentalInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.UUID;

/**
 * Created by shubham.singhal on 26/09/16.
 */
public interface RentalInfoDao extends CassandraRepository<RentalInfo> {

    @Query("SELECT * FROM cr_rental_info WHERE id=?0")
    RentalInfo findById(String id) throws Exception;

    @Query("DELETE FROM cr_rental_info WHERE id=?0")
    Boolean deleteById(String id);
}
