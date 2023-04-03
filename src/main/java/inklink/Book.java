package inklink;

import java.io.File;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

import jakarta.validation.constraints.*;
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@NotBlank(message="Book name is required.")
	private String name;
	
	@Column(length=500)
	private String description;
	
	@JsonInclude()
	@Transient
	private File cover;
	
	private String coverFileName;
	
	@JsonInclude()
	@Transient
	private File bookFile;
	
	private String bookFileName;
	
	@Size(min=1, message="You must have at least one author.")
	/*@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
		@JoinTable(name="book_author",
			joinColumns= {@JoinColumn(name="book_id", referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="author_id", referencedColumnName="id")}
	)*/
	@Transient
	@JsonInclude()
	private List<Author> authors;
	
	@NotBlank(message="Publisher name required.")
	private String publisher;
	
	@Min(value=1600, message="Minimum year accepted is 1600.")
	private int year;
	
	@NotBlank(message="Enter the language the book has been written in.")
	private String language;
	
	private int edition;
	
	private String country;
	
	@Positive(message="Specify the number of pages using positive integers.")
	private int numberOfPages;
	
	@Size(min=1, message="You must select one category to which this book belongs.")
	/*@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
		@JoinTable(name="book_category",
			joinColumns= {@JoinColumn(name="book_id", referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="category_id", referencedColumnName="id")}
	)*/
	@Transient
	@JsonInclude()
	private List<Category> categories;
	
	private double width;
	
	private double height;
	
	private long worldwideSales;
	
	@Column(nullable=false)
	@PositiveOrZero(message="Book price can be 0 or greater but not negative.")
	private double price;
	
	@NotBlank(message="Book ISBN cannot be empty.")
	@Column(unique=true, nullable = false)
	private String isbn;
	
	private String status;
	
	private Date dateAdded;
	
	@Transient
	@JsonInclude
	private String coverBase64;
	
	@PrePersist
	void setStuff() {
		this.dateAdded = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getCover() {
		return cover;
	}

	public void setCover(File cover) {
		this.cover = cover;
	}

	public String getCoverFileName() {
		return coverFileName;
	}

	public void setCoverFileName(String coverFileName) {
		this.coverFileName = coverFileName;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getEdition() {
		return edition;
	}

	public void setEdition(int edition) {
		this.edition = edition;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public long getWorldwideSales() {
		return worldwideSales;
	}

	public void setWorldwideSales(long worldwideSales) {
		this.worldwideSales = worldwideSales;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public String getCoverBase64() {
		return coverBase64;
	}

	public void setCoverBase64(String coverBase64) {
		this.coverBase64 = coverBase64;
	}
	
	public File getBookFile() {
		return bookFile;
	}

	public void setBookFile(File bookFile) {
		this.bookFile = bookFile;
	}

	public String getBookFileName() {
		return bookFileName;
	}

	public void setBookFileName(String bookFileName) {
		this.bookFileName = bookFileName;
	}
	public String toString() {
		return "Id: "+id+ " Name: "+name+" Edition: "+edition+" ISBN: "+isbn+" Price: "+price
				+" Language: "+language+" Year: "+year+" Number of Pages: "+numberOfPages+" Publisher: "+publisher
				+" Authors: "+authors+" Categories: "+categories+" Width: "+width +" Height: "+height+" Country: "+country+".";
	}

}
