package inklink.web.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import inklink.Book;
import inklink.Messenger;
import inklink.data.BookRepository;
import inklink.data.GeneralRepository;
import inklink.service.StorageService;
import inklink.service.SL;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/api/admin", produces="application/json")
public class AdminBooksControllerApi {
	
	private BookRepository bookRepo;
	private GeneralRepository generalRepo;

	private final StorageService storageService;
	
	private final static int VIEW_ALL_BOOKS_PAGE_SIZE = 30;

	public AdminBooksControllerApi(BookRepository bookRepo, GeneralRepository generalRepo, StorageService storageService) {
		this.bookRepo = bookRepo;
		this.generalRepo = generalRepo;	
		this.storageService = storageService;
	}
	
	@PostMapping("/addBook")
	public Messenger addABook(@RequestBody @Valid Book book, Errors errors) {
	
		book.setStatus("Listed");

		if(errors.hasErrors()) {
			List<ObjectError> errs = errors.getAllErrors();
			List<String> errorMessages = new ArrayList<>();
			
			for(ObjectError err: errs) {
				errorMessages.add(err.getDefaultMessage());
			}
			return SL.respondError(errors);
		}
		else {
			try {
				//Modify coverFileName to also include bookId for uniqueness in case of changing ISBN, for it to remain the same, unless new cover is being added, delete old one
				book = generalRepo.saveBook(book);
				//book = repoSaveBook(book);
				return SL.respondSuccess(book.getName()+" added successfully. Book Id is "+book.getId()+".", book);
			}catch(RuntimeException re) {
				re.printStackTrace();
				return SL.respondError(re.getMessage());
			}catch(Exception e) {
				e.printStackTrace();
				return SL.respondError("Error adding book to database. "+e.getMessage());
			}	
		}
	}
	
	@PostMapping("/addBookCoverAndFile")
	public Messenger addBookCoverAndFile(@RequestParam("cover") MultipartFile file, @RequestParam("coverName") String coverName,
			@RequestParam("bookFile") MultipartFile book, @RequestParam("bookFileName") String bookName) {
		try {
			byte[] fileContent = file.getBytes();
			
			File actualFile = new File("covers/"+coverName);
			if(actualFile.exists()) {
				actualFile.delete();
				actualFile = new File("covers/"+coverName);
			}
			
			actualFile.setExecutable(true);
			actualFile.setReadable(true);
			actualFile.setWritable(true);
			
			byte[] bookContent = book.getBytes();
			File bookFile = new File("books/"+bookName);
			if(bookFile.exists()) {
				bookFile.delete();
				bookFile = new File("books/"+bookName);
			}
			
			bookFile.setExecutable(true);
			bookFile.setReadable(true);
			bookFile.setWritable(true);
			
			try(FileOutputStream outputStream = new FileOutputStream(actualFile);
					FileOutputStream bookStream = new FileOutputStream(bookFile);){
				outputStream.write(fileContent);
				bookStream.write(bookContent);
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			
			//Base 64 conversion
			
			/*File cover = new File("");
			file.transferTo(cover);
			if(cover!=null)
				System.out.println("Before File RWX: R - "+cover.canRead()+" W - "+cover.canWrite()+" E - "+cover.canExecute());
			
			
			if(cover!=null) {
				cover.setExecutable(true);
				cover.setWritable(true);
				cover.setReadable(true);
				System.out.println("After File RWX: R - "+cover.canRead()+" W - "+cover.canWrite()+" E - "+cover.canExecute());
			}*/
			
			storageService.store(file, coverName);
			
			return SL.respondSuccess("Saved cover.");
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError(e.getMessage());
		}
		
	}
	
	@GetMapping("/viewAllBooks")
	public Messenger getViewAllBooks() {
	
		List<Book> books = bookRepo.findAllOrderByName();	
		return SL.respondSuccess(VIEW_ALL_BOOKS_PAGE_SIZE+"", books);
	}
	
	@PostMapping("/viewBook")
	public Messenger viewBook(@RequestParam(name="id", defaultValue="") String idStr) {

		try {
			long id = Long.parseLong(idStr);
			Book book = generalRepo.viewBook(id);
			
			return SL.respondSuccess("Viewing book with ID: "+idStr+".", book);
		}catch(NumberFormatException nfe) {
			return SL.respondError("Use numbers only to specify the book ID. You entered: "+idStr+".");
		}catch(RuntimeException re) {
			re.printStackTrace();
			return SL.respondError(re.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Error viewing book ID: "+idStr+" from database. "+e.getMessage());
		}
	}
	
	@PostMapping("/editBook")
	public Messenger editBook(@RequestBody @Valid Book book, Errors errors) {
	
		
		if(errors.hasErrors()) {
			
			return SL.respondError(errors);
		}
		else {
			try {
				//Modify coverFileName to also include bookId for uniqueness in case of changing ISBN, for it to remain the same, unless new cover is being added, delete old one
				book = generalRepo.updateBook(book);
				return SL.respondSuccess(book.getName()+" successfully updated. ", book);
			}catch(RuntimeException re) {
				re.printStackTrace();
				return SL.respondError(re.getMessage());
			}catch(Exception e) {
				e.printStackTrace();
				return SL.respondError("Error updating book in database. "+e.getMessage());
			}	
		}
	}
	
	@PostMapping("/deleteBook")
	public Messenger deleteBook(@RequestParam(name="id",defaultValue="") String idStr) {
		
		try {
			long id = Long.parseLong(idStr);
			
			generalRepo.deleteBook(id);
			
			return SL.respondSuccess("Deleted book with ID: "+id+".");
			
		}catch(NumberFormatException nfe) {
			return SL.respondError("Use numbers only to specify the book ID. You entered: "+idStr+".");
		}catch(RuntimeException re) {
			re.printStackTrace();
			return SL.respondError(re.getMessage());
			
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Error deleting book ID: "+idStr+". "+e.getMessage());
		}		
	}

	
}
