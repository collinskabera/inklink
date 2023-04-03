package inklink.web;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import inklink.Book;
import inklink.Cart;
import inklink.data.BookRepository;

@Controller
@RequestMapping("/")
@SessionAttributes("cart")
public class ViewController {
	
	private BookRepository bookRepo;
	
	public ViewController(BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}
	
	@GetMapping("/cart")
	public String getCart(@ModelAttribute Cart cart, Model model) {
		cart.total();
		model.addAttribute("cart", cart);
		return "cart";
	}
	
	@GetMapping
	public String getHome() {
		return "home";
	}
	
	@GetMapping("/error")
	public String defaultError() {
		return "home";
	}
	
	@GetMapping({"/index","/home"})
	public String getIndex() {
		return "home";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping(path={"/signUp","/register"})
	public String getSignUp() {
		return "signUp";
	}
	
	@GetMapping("/viewAllAuthors")
	public String getViewAllAuthors() {
		return "viewAllAuthors";
	}
	
	@GetMapping("/viewAllBooks")
	public String getViewAllBooks() {
		return "viewAllBooks";
	}
	
	@GetMapping("/viewAllCategories")
	public String getViewAllCategories() {
		return "viewAllCategories";
	}
	
	@GetMapping("/viewBook")
	public String getViewBook(@RequestParam(name="bookId", defaultValue="") String idStr, Model model) {
		Book book = null;
		try {
			long id = Long.parseLong(idStr);
			Optional<Book> optBook = bookRepo.findById(id);
			if(optBook.isPresent()) {
				//Load image file here
				
				book = optBook.get();
				//return SL.respondSuccess("Book found", optBook.get());
			}
			
		}catch(NumberFormatException nfe) {
			nfe.printStackTrace();
			//return SL.respondError("Use numbers to specify errors.");
		}catch(Exception e) {
			e.printStackTrace();
			//return SL.respondError("Error viewing book.");
		}
		model.addAttribute("book", book);
		return "viewBook";
	}
	
	@ModelAttribute("cart")
	public Cart cart() {
		return new Cart();
	}
	
}
