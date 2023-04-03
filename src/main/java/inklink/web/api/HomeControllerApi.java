package inklink.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inklink.Author;
import inklink.Book;
import inklink.BookAuthor;
import inklink.BookCategory;
import inklink.Category;
import inklink.Messenger;
import inklink.ViewHomeHolder;
import inklink.data.AuthorRepository;
import inklink.data.BookAuthorRepository;
import inklink.data.BookCategoryRepository;
import inklink.data.BookRepository;
import inklink.data.CategoryRepository;
import inklink.service.SL;

@RestController
@RequestMapping(path={"/api"}, produces="application/json")
public class HomeControllerApi {
	
	private BookRepository bookRepo;
	private AuthorRepository authorRepo;
	private CategoryRepository categoryRepo;
	private BookAuthorRepository bookAuthorRepo;
	private BookCategoryRepository bookCategoryRepo;
	
	private static final int VIEW_HOME_MAXIMUM_COLUMN_COUNT = 12;
	
	public HomeControllerApi(BookRepository bookRepo, AuthorRepository authorRepo, CategoryRepository categoryRepo
			, BookCategoryRepository bookCategoryRepo, BookAuthorRepository bookAuthorRepo) {
		this.bookRepo = bookRepo;
		this.authorRepo = authorRepo;
		this.categoryRepo = categoryRepo;
		this.bookCategoryRepo = bookCategoryRepo;
		this.bookAuthorRepo = bookAuthorRepo;
	}
	
	@PostMapping(path= {"/","/home","/index"})
	public Messenger getHomeContent() {
		
		List<ViewHomeHolder> homeHolders = new ArrayList<>();
		
		//1. Get the most purchased book
		
		//2. Get the newest arrivals
		List<Book> newestBooks = bookRepo.findAllOrderByDateAddedDesc();
		homeHolders.add(new ViewHomeHolder("Newest Books", newestBooks));
		
		//3. 2 Random categories
		List<Category> categories = categoryRepo.selectAll();
		int[] catIndexes = new int[3];
		for(int i=0;i<catIndexes.length;i++) {
			catIndexes[i] = (int)(Math.random()* categories.size());
		}
		//Ignore duplicate values
		
		for(int i=0;i<catIndexes.length;i++) {
			Category category = categories.get(catIndexes[i]);
			List<BookCategory> bookCategories = bookCategoryRepo.findByCategoryId(category.getId());
			List<Long> bookIds = new ArrayList<>();
			for(BookCategory bookCategory: bookCategories) {
				bookIds.add(bookCategory.getBookId());
			}
			
			Iterable<Book> categoryBooksIter = bookRepo.findAllById(bookIds);
			ArrayList<Book> categoryBooksAL = new ArrayList<>();
			int count = 0;
			for(Book book: categoryBooksIter) {
				if(count>=VIEW_HOME_MAXIMUM_COLUMN_COUNT) {
					break;
				}
				categoryBooksAL.add(book);
			}
			
			if(categoryBooksAL.size()>0) {
				ViewHomeHolder holder = new ViewHomeHolder();
				holder.setName("Category");
				holder.setValue(category.getName());
				holder.setBooks(categoryBooksAL);
				homeHolders.add(holder);
			}
			
		}
		
		//4. 2 Random authors
		List<Author> authors = authorRepo.selectAll();
		int[] authIndexes = new int[3];
		for(int i=0;i<authIndexes.length;i++) {
			authIndexes[i] = (int)(Math.random()* authors.size());
		}
		//Ignore duplicate values
		
		for(int i=0;i<authIndexes.length;i++) {
			Author author = authors.get(authIndexes[i]);
			List<BookAuthor> bookAuthors = bookAuthorRepo.findByAuthorId(author.getId());
			List<Long> bookIds = new ArrayList<>();
			for(BookAuthor bookAuthor: bookAuthors) {
				bookIds.add(bookAuthor.getBookId());
			}
			
			Iterable<Book> authBooksIter = bookRepo.findAllById(bookIds);
			ArrayList<Book> authBooksAL = new ArrayList<>();
			int count = 0;
			for(Book book: authBooksIter) {
				if(count>=VIEW_HOME_MAXIMUM_COLUMN_COUNT) {
					break;
				}
				authBooksAL.add(book);
			}
			
			if(authBooksAL.size()>0) {
				ViewHomeHolder holder = new ViewHomeHolder();
				holder.setName("Author");
				holder.setValue(author.getName());
				holder.setBooks(authBooksAL);
				homeHolders.add(holder);
			}	
		}
		
		return SL.respondSuccess("", homeHolders);
	}
}
