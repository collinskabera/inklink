package inklink.web.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inklink.Book;
import inklink.Messenger;
import inklink.data.GeneralRepository;
import inklink.service.SL;

@RestController
@RequestMapping(path="/api/viewBook", produces="application/json")
public class ViewBookControllerApi {
	
	private GeneralRepository generalRepo;
	
	public ViewBookControllerApi(GeneralRepository generalRepo) {
		this.generalRepo = generalRepo;
	}

	@PostMapping
	public Messenger doViewBook(@RequestParam(name="bookId", defaultValue="") String idStr) {
		try {
			long id = Long.parseLong(idStr);
			Book book = generalRepo.viewBook(id);
			
			return SL.respondSuccess("", book);
			
		}catch(NumberFormatException nfe) {
			nfe.printStackTrace();
			return SL.respondError("Use numbers to specify errors.");
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError("Error viewing book.");
		}
	}
}
