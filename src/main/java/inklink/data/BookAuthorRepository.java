package inklink.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import inklink.BookAuthor;
import jakarta.transaction.Transactional;

@Transactional
public interface BookAuthorRepository extends CrudRepository<BookAuthor, Long> {

	List<BookAuthor> findByBookId(long id);
	
	List<BookAuthor> findByAuthorId(long id);

	long deleteByBookId(long id);
	
	long deleteByAuthorId(long id);
}
