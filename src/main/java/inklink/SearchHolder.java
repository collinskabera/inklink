package inklink;

import java.util.List;

public class SearchHolder {
	
	private String name;
	private String value;
	private List<Book> books;
	
	public SearchHolder() {
		
	}
	
	public SearchHolder(String name, List<Book> books) {
		this.name = name;
		this.value = "";
		this.books = books;
	}
	
	public SearchHolder(String name, String value, List<Book> books) {
		this.name = name;
		this.value = value;
		this.books = books;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
