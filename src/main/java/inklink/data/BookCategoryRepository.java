package inklink.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import inklink.BookCategory;
import jakarta.transaction.Transactional;

@Transactional
public interface BookCategoryRepository extends CrudRepository<BookCategory, Long>{

	List<BookCategory> findByBookId(long id);
	
	List<BookCategory> findByCategoryId(long id);
		
	long deleteByBookId(long id);

	long deleteByCategoryId(long id);
}
