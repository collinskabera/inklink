package inklink.web.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import inklink.Book;
import inklink.Cart;
import inklink.LineItem;
import inklink.Messenger;
import inklink.Purchase;
import inklink.User;
import inklink.data.BookRepository;
import inklink.data.PurchaseRepository;
import inklink.service.SL;

@RestController
@SessionAttributes("cart")
@RequestMapping(path="/api", produces="application/json")
public class CartControllerApi {
	
	private BookRepository bookRepo;
	private PurchaseRepository purchaseRepo;
	
	public CartControllerApi(BookRepository bookRepo, PurchaseRepository purchaseRepo) {
		this.bookRepo = bookRepo;
		this.purchaseRepo = purchaseRepo;
	}
	
	@PostMapping("/cart")
	public Messenger viewCart(@ModelAttribute Cart cart, @AuthenticationPrincipal User user) {
		
		if(user==null) {
			return SL.respondError("You need to have an account and be logged in to view the cart.");
		}
		
		if(cart.getLineItems().size()==0) {
			return SL.respondError("You have no items in the cart");
		}
		else {
			/*List<Book> books = new ArrayList<>();
			for(int i=0;i<cart.getLineItems().size();i++) {
				Optional<Book> optBook = bookRepo.findById(cart.getLineItems().get(i).getBookId());
				if(optBook.isPresent())
					books.add(optBook.get());
			}*/
			return SL.respondSuccess("Your cart has "+cart.getLineItems().size()+" items.", cart);
		}
	}
	
	@GetMapping(path="/addToCart")
	public Messenger doAddToCart(@RequestParam(name="bookId", defaultValue = "") String bookIdStr, @ModelAttribute Cart cart, @AuthenticationPrincipal User user) {
		if(user==null) {
			return SL.respondError("You need to have an account and be logged in to add items to the cart.");
		}
		try {
			long idLong = Long.parseLong(bookIdStr);
			Optional<Book> optBook = bookRepo.findById(idLong);
			if(optBook.isPresent()) {
				Book book = optBook.get();
				boolean present = false;
				List<LineItem> lineItems = cart.getLineItems();
				
				for(int i=0;i<lineItems.size();i++) {
					if(lineItems.get(i).getBookId()==idLong) {
						present = true;
						break;
					}
				}
				
				if(present) {
					return SL.respondSuccess("'"+book.getName()+"' is already in the cart", cart);
				}else {
					cart.getLineItems().add(new LineItem(optBook.get()));
					return SL.respondSuccess("'"+book.getName()+"' successfully added to cart", cart);
				}
			}else {
				return SL.respondSuccess("Book not added not to cart.");
			}
		}catch(NumberFormatException nfe) {
			nfe.printStackTrace();
			return SL.respondError("Use numbers to specify book ID.");
			
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Could not add book to the cart.");
		}
	}
	
	@GetMapping(path="/removeFromCart")
	public Messenger doRemoveFromCart(@RequestParam(name="bookId", defaultValue = "") String bookIdStr, @ModelAttribute Cart cart, @AuthenticationPrincipal User user) {
		if(user==null) {
			return SL.respondError("You need to have an account and be logged in to remove items from the cart.");
		}
		try {
			long id = Long.parseLong(bookIdStr);
			Optional<Book> optBook = bookRepo.findById(id);
			
			if(optBook.isPresent()) {
				
				Book book = optBook.get();
				boolean present = false;
				List<LineItem> lineItems = cart.getLineItems();
				
				for(int i=0; i<lineItems.size(); i++) {
					if(lineItems.get(i).getBookId()==id) {
						present = true;
						lineItems.remove(i);
						break;
					}
				}
				
				if(present) {
					return SL.respondSuccess("'"+book.getName()+"' has been removed from the cart", cart);
				}else {
					return SL.respondError("'"+book.getName()+"' was not in the cart.");
				}
			}else {
				return SL.respondError("Book does not exist.");
			}
		}catch(NumberFormatException nfe) {
			nfe.printStackTrace();
			return SL.respondError("Use numbers to specify book ID.");
			
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Could not add book to the cart.");
		}
	}
	
	@PostMapping(path="/queryPhoneNumber")
	public Messenger queryPhoneNumber(@AuthenticationPrincipal User user) {
		if(user==null) {
			return SL.respondError("You need to have an account and be logged in to get a user's phone number.");
		}
		
		return SL.respondSuccess("Phone number", user);
	}
	
	//Get user ID
	@PostMapping(path="/checkout")
	public Messenger checkout(@RequestParam(name="phoneNumber", defaultValue="") String phoneNumber, @ModelAttribute Cart cart, @AuthenticationPrincipal User user) {
		if(user==null) {
			return SL.respondError("You need to have an account and be logged in to ceckout the items in the cart.");
		}
		if(cart.getLineItems().size()==0) {
			return SL.respondError("You have no books in your cart to check out. Shop first a couple of books then return here when you want to purchase the books.");
		}
		
		Purchase purchase = new Purchase(cart);
		if(!phoneNumber.equals("")) {
			purchase.setPhoneNumber(phoneNumber);
		}
		
		purchase.setUserId(user.getId());
		
		purchase = purchaseRepo.save(purchase);
		
		cart.setUserId(0);
		cart.setTotal(0);
		cart.setLineItems(new ArrayList<>());
		
		return SL.respondSuccess("https://www.google.com", purchase);
	}
	
	@ModelAttribute("cart")
	public Cart cart() {
		return new Cart();
	}

}
