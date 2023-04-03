package inklink.web.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inklink.Author;
import inklink.Book;
import inklink.BookAuthor;
import inklink.Messenger;
import inklink.SearchHolder;
import inklink.data.AuthorRepository;
import inklink.data.BookAuthorRepository;
import inklink.data.BookRepository;
import inklink.service.SL;

@RestController
@RequestMapping(path="/api/search",produces="application/json")
public class SearchControllerApi {
	
	private BookRepository bookRepo;
	private AuthorRepository authorRepo;
	private BookAuthorRepository bookAuthorRepo;
	
	//private final static int VIEW_ALL_BOOKS_PAGE_SIZE = 30;
	
	public SearchControllerApi(BookRepository bookRepo, AuthorRepository authorRepo ,BookAuthorRepository bookAuthorRepo) {
		this.bookRepo = bookRepo;
		this.bookAuthorRepo = bookAuthorRepo;
		this.authorRepo = authorRepo;
	}
	
	@GetMapping
	public Messenger search(@RequestParam(name="value", defaultValue="") String value, @RequestParam(name="page", defaultValue="1") String pageStr) {
		
		try {
			int page = Integer.parseInt(pageStr) - 1;
			

			List<SearchHolder> searches = new ArrayList<>();
			//Add more queries here and SearchHolders.
			
			//1. Search book name

			List<Book> nameBooks = bookRepo.findByNameContains(value);
			if(nameBooks.size()>0) {
				searches.add(new SearchHolder("Name", nameBooks));
			}
			
			//2. Search ISBN
			List<Book> isbnBooks = bookRepo.findByIsbnContains(value);
			if(isbnBooks.size()>0) {
				searches.add(new SearchHolder("ISBN", isbnBooks));
			}
			
			//3. Search by publisher
			List<Book> publisherBooks = bookRepo.findByPublisherContains(value);
			if(publisherBooks.size()>0) {
				searches.add(new SearchHolder("Publisher", publisherBooks));
			}
			
			//4. Search by price
			/*
			try {
				System.out.println("Val: "+value+" Page: "+pageStr);
				double price = Double.parseDouble(value);
				System.out.println("Val: "+value+" Page: "+pageStr);
				List<Book> priceBooks = bookRepo.findByPrice(price);
				if(priceBooks.size()>0) {
					searches.add(new SearchHolder("Book Price", priceBooks));
				}
			}catch(NumberFormatException nfe) {
				throw nfe;
			}*/
			
			//5. Search authors
			List<Author> authors = authorRepo.findByNameContains(value);
			if(authors.size()>0) {
				HashSet<Book> authorBooks = new HashSet<>();

				for(int i=0;i<authors.size();i++) {
					Author author = authors.get(i);
					List<BookAuthor> bookAuthors = bookAuthorRepo.findByAuthorId(author.getId());
					for(int j=0;j<bookAuthors.size();j++) {
						Optional<Book> optBook = bookRepo.findById(bookAuthors.get(j).getBookId());
						if(optBook.isPresent()) {
							authorBooks.add(optBook.get());
						}
					}
				}
				
				if(authorBooks.size()>0) {
					List<Book> authorBooksList = new ArrayList<>();
					for(Book book: authorBooks)
						authorBooksList.add(book);
					
					searches.add(new SearchHolder("Author Name", authorBooksList));
				}
			}
			
			
			return SL.respondSuccess(page+"", searches);
			
		}catch(NumberFormatException nfe) {
			return SL.respondError("Use natural numbers to specify page number.");
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Error searching.");
		}
	}
}
