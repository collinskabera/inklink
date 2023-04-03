package inklink.web.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inklink.Category;
import inklink.Messenger;
import inklink.data.CategoryRepository;
import inklink.service.SL;

@RestController
@RequestMapping(path="/api/viewAllCategories", produces="application/json")
public class ViewAllCategoriesControllerApi {

	private CategoryRepository categoryRepo;
	
	public ViewAllCategoriesControllerApi (CategoryRepository categoryRepo) {
		this.categoryRepo = categoryRepo;
	}
	
	@PostMapping
	public Messenger viewAllCategories() {
		List<Category> categories = categoryRepo.findAllOrderByName();
		if(categories.size()==0) {
			return SL.respondError("No categories added to system yet.");
		}else {
			return SL.respondSuccess("",categories);
		}
	}
	
}
