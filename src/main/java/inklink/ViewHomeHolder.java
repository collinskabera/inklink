package inklink;

import java.util.List;

public class ViewHomeHolder {
	
	private String name;
	private String value;
	private List<Book> books;
	
	public ViewHomeHolder() {
		
	}
	
	public ViewHomeHolder(String name, List<Book> books) {
		this.name = name;
		this.books = books;
	}
	
	public ViewHomeHolder(String name, String value, List<Book> books) {
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}	
}
