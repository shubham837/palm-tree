package crossover.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface CustomBaseRepository<T, ID extends Serializable> extends SolrCrudRepository<T, ID> {
}
