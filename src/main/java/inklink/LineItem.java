package inklink;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class LineItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false)
	private long bookId;
	
	@Column(nullable = false)
	private long purchaseId;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false)
	private double price;
	
	private double total;
	
	@Transient
	@JsonInclude()
	private String name;
	
	@Transient
	@JsonInclude()
	private String isbn;
	
	public LineItem() {
		
	}
	
	public LineItem(Book book) {
		this.bookId = book.getId();
		this.price = book.getPrice();
		this.quantity = 1;
		this.total = book.getPrice();
		this.name = book.getName();
		this.isbn = book.getIsbn();
	}
	
	//For future. Physical books, someone may specify quantity
	public LineItem(Book book, int quantity) {
		this.bookId = book.getId();
		this.price = book.getPrice();
		this.quantity = quantity;
		this.total = book.getPrice() * quantity;
		this.name = book.getName();
		this.isbn = book.getIsbn();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	
	public String toString() {
		return "ID: "+id+" Purchase ID: "+purchaseId+" Book ID: "+bookId+" Quantity: "+quantity+" Price: "+price+" Total: "+total;
	}
}
