package inklink.web.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inklink.Author;
import inklink.Messenger;
import inklink.data.AuthorRepository;
import inklink.service.SL;

@RestController
@RequestMapping(path="/api/viewAllAuthors", produces="application/json")
public class ViewAllAuthorsControllerApi {
	private AuthorRepository authorRepo;
	
	public ViewAllAuthorsControllerApi(AuthorRepository authorRepo) {
		this.authorRepo = authorRepo;
	}
	
	@PostMapping
	public Messenger viewAllAuthors() {
		List<Author> authors = authorRepo.findAllOrderByName();
		if(authors.size()==0) {
			return SL.respondError("No authors have been added to the system yet.");
		}else {
			return SL.respondSuccess("", authors);
		}
	}
}
