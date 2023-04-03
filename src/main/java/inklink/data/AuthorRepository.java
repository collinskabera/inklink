package inklink.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import inklink.Author;

public interface AuthorRepository extends CrudRepository<Author, Long>{
	
	List<Author> findByName(String name);
	
	@Query(nativeQuery=true, value="SELECT * FROM author")
	List<Author> selectAll();
	
	@Query(nativeQuery=true, value="SELECT * FROM author ORDER BY name")
	List<Author> findAllOrderByName();
	
	//Searching
	List<Author> findByNameContains(String name);
	
}
