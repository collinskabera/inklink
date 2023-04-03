package inklink.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import inklink.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>{
	
	List<Category> findByName(String name);
	
	@Query(nativeQuery=true, value="SELECT * from category")
	List<Category> selectAll();
	
	@Query(nativeQuery=true, value="SELECT * FROM category ORDER BY name")
	List<Category> findAllOrderByName();
	
	//Searching
	List<Category> findByNameLike(String name);
	
}
