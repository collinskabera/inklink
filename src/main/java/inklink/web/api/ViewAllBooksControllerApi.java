package inklink.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inklink.Book;
import inklink.Messenger;
import inklink.data.BookRepository;
import inklink.service.SL;

@RestController
@RequestMapping(path="/api/viewAllBooks",produces="application/json")
public class ViewAllBooksControllerApi {
	
	private BookRepository bookRepo;
	
	private final static int VIEW_ALL_BOOKS_PAGE_SIZE = 30;
	
	public ViewAllBooksControllerApi (BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}
	
	//Later add the sorting requirement
	@PostMapping
	public Messenger viewAllBooks(@RequestParam(name="page", defaultValue="1") String pageStr ) {
		
		try {
			List<Book> allBooks = bookRepo.findAllOrderByName();
			ArrayList<Book> books = new ArrayList<>();
			
			int page = Integer.parseInt(pageStr);
			
			//Code wastage cause I forgot how to round up to integer
			double pageCountDouble = ((double)allBooks.size()) / VIEW_ALL_BOOKS_PAGE_SIZE;
			int pageCount = allBooks.size() / VIEW_ALL_BOOKS_PAGE_SIZE;
			if(pageCountDouble>pageCount) {
				++pageCount;
			}
			
			if(page>pageCount)
				page = pageCount;
			
			int start = (page-1) * VIEW_ALL_BOOKS_PAGE_SIZE;
			for(int i=0; i<VIEW_ALL_BOOKS_PAGE_SIZE && start<allBooks.size(); start++, i++) {
				books.add(allBooks.get(start));
			}
			return SL.respondSuccess(page+","+VIEW_ALL_BOOKS_PAGE_SIZE, books);
		}catch(NumberFormatException nfe) {
			return SL.respondError("Use numbers to specify page.");
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Error viewing all books.");
		}
	}
}

