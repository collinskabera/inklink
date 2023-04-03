package inklink.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import inklink.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	@Query(value="SELECT * FROM book ORDER BY name", nativeQuery=true)
	List<Book> findAllOrderByName();
	
	List<Book> findBookByIsbn(String isbn);
	
	List<Book> findByName(String name);
	
	List<Book> findByPrice(double price);
	
	//For home controller
	@Query(value="SELECT * FROM book ORDER BY date_added DESC LIMIT 12", nativeQuery=true)
	List<Book> findAllOrderByDateAddedDesc();
	
	//For searching, using LIKE
	List<Book> findByNameContains(String name);
	
	List<Book> findByIsbnContains(String isbn);
	
	List<Book> findByCountryContains(String country);
	
	List<Book> findByPublisherContains(String publisher);
	
	List<Book> findByPriceGreaterThan(double price);
	
	List<Book> findByPriceLessThan(double price);
	
	List<Book> findByPriceBetween(double small, double big);
	
}
