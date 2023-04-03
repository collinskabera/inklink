package inklink.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;


import inklink.Author;
import inklink.Book;
import inklink.BookAuthor;
import inklink.BookCategory;
import inklink.Category;
import inklink.Purchase;

//import inkling.Book;
@Repository
public class GeneralRepository {
	
	private BookRepository bookRepo;
	private AuthorRepository authorRepo;
	private CategoryRepository categoryRepo;
	private BookAuthorRepository bookAuthorRepo;
	private BookCategoryRepository bookCategoryRepo;
	
	public GeneralRepository(BookRepository bookRepo, AuthorRepository authorRepo, CategoryRepository categoryRepo, 
			BookAuthorRepository bookAuthorRepo, BookCategoryRepository bookCategoryRepo){
	
		this.bookRepo = bookRepo;
		this.authorRepo = authorRepo;
		this.categoryRepo = categoryRepo;
		this.bookCategoryRepo = bookCategoryRepo;
		this.bookAuthorRepo = bookAuthorRepo;
	}
	
	public Book saveBook(Book book) {
		try {
			List<Book> books = bookRepo.findBookByIsbn(book.getIsbn());
			if(books.size()==1) {
				throw new RuntimeException("There's already a book with ISBN: "+book.getIsbn()+".");
			}else {
				book = bookRepo.save(book);
				
				for(int i=0;i<book.getAuthors().size();i++) {
					BookAuthor bookAuthor = new BookAuthor();
					bookAuthor.setBookId(book.getId());
					
					List<Author> authors = authorRepo.findByName(book.getAuthors().get(i).getName());
					if(authors.size()==0) {
						Author author = authorRepo.save(book.getAuthors().get(i));
						bookAuthor.setAuthorId(author.getId());
					}else {
						bookAuthor.setAuthorId(authors.get(0).getId());
					}
					
					bookAuthorRepo.save(bookAuthor);
				}
				
				for(int i=0;i<book.getCategories().size();i++) {
					BookCategory bookCategory = new BookCategory();
					bookCategory.setBookId(book.getId());
					
					List<Category> categories = categoryRepo.findByName(book.getCategories().get(i).getName());
					if(categories.size()==0) {
						Category category = categoryRepo.save(book.getCategories().get(i));
						bookCategory.setCategoryId(category.getId());
					}else {
						bookCategory.setCategoryId(categories.get(0).getId());
					}
					
					bookCategoryRepo.save(bookCategory);
				}
				
				return book;
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public Book viewBook(long id) {
		Optional<Book> optBook = bookRepo.findById(id);
		
		if(optBook.isPresent()) {
			Book book = optBook.get();
			book.setAuthors(new ArrayList<>());
			book.setCategories(new ArrayList<>());
			
			List<BookCategory> bookCategories = bookCategoryRepo.findByBookId(id);
			for(int i=0;i<bookCategories.size();i++) {
				Optional<Category> optCategory = categoryRepo.findById(bookCategories.get(i).getCategoryId());
				if(optCategory.isPresent()) {
					book.getCategories().add(optCategory.get());
				}
			}
			
			List<BookAuthor> bookAuthors = bookAuthorRepo.findByBookId(id);
			for(int i=0;i<bookAuthors.size();i++) {
				Optional<Author> optAuthor = authorRepo.findById(bookAuthors.get(i).getAuthorId());
				if(optAuthor.isPresent()) {
					book.getAuthors().add(optAuthor.get());
				}
			}
		
			
			try {
				
				File cover = new File("covers/"+book.getCoverFileName());
				if(cover.exists()) {
					book.setCover(cover);
					FileInputStream f = new FileInputStream(cover);
					byte[] fileContent = f.readAllBytes();
					//System.out.println(fileContent.length);
					book.setCoverBase64(Base64.getEncoder().encodeToString(fileContent));
					f.close();
				}
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return book;
		}else {
			throw new RuntimeException("No book with ID: "+id+".");
		}
	}

	public Book updateBook(Book book) {
		Optional<Book> optTestBook = bookRepo.findById(book.getId());
		if(optTestBook.isPresent()) {
			Book testBook = optTestBook.get();
			
			book.setDateAdded(testBook.getDateAdded());
			book.setStatus(testBook.getStatus());
			book.setCoverFileName(testBook.getCoverFileName());
			
			bookRepo.save(book);
			
			bookAuthorRepo.deleteByBookId(book.getId());
			for(int i=0;i<book.getAuthors().size();i++) {
				BookAuthor bookAuthor = new BookAuthor();
				bookAuthor.setBookId(book.getId());
				
				List<Author> authors = authorRepo.findByName(book.getAuthors().get(i).getName());
				if(authors.size()==0) {
					Author author = authorRepo.save(book.getAuthors().get(i));
					bookAuthor.setAuthorId(author.getId());
				}else {
					bookAuthor.setAuthorId(authors.get(0).getId());
				}
				
				bookAuthorRepo.save(bookAuthor);
			}
			
			bookCategoryRepo.deleteByBookId(book.getId());
			
			for(int i=0;i<book.getCategories().size();i++) {
				BookCategory bookCategory = new BookCategory();
				bookCategory.setBookId(book.getId());
				
				List<Category> categories = categoryRepo.findByName(book.getCategories().get(i).getName());
				if(categories.size()==0) {
					Category category = categoryRepo.save(book.getCategories().get(i));
					bookCategory.setCategoryId(category.getId());
				}else {
					bookCategory.setCategoryId(categories.get(0).getId());
				}
				
				bookCategoryRepo.save(bookCategory);
			}
			
			return book;
		}else {
			throw new RuntimeException("There is no book with the ID: "+book.getId()+" in the system.");
		}
	}

	public void deleteBook(long id) {
		Optional<Book> optTestBook = bookRepo.findById(id);
		if(optTestBook.isPresent()) {
			Book book = optTestBook.get();
			bookRepo.deleteById(book.getId());
			bookCategoryRepo.deleteByBookId(book.getId());
			bookAuthorRepo.deleteByBookId(book.getId());
			
			String coverFileName = optTestBook.get().getCoverFileName();
			try {
				File cover = new File("covers/"+coverFileName);
				if(cover.exists()) {
					cover.delete();
				}
				if(cover.exists()) {
					System.out.println("What are you still doing here?");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				File bookFile = new File("books/"+book.getBookFileName());
				if(bookFile.exists()) {
					bookFile.delete();
				}
				if(bookFile.exists()) {
					System.out.println("Even books defy you. How weak.");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			throw new RuntimeException("No book with ID: "+id+" exists in the system.");
		}
	}
	
	//Unused method, for now
	public Purchase savePurchase(Purchase purchase) {
		return null;
	}
}

