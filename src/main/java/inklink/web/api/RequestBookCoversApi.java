package inklink.web.api;

import java.io.File;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inklink.Messenger;
import inklink.service.SL;

@RestController
@RequestMapping(path="/api/requestBookCovers", produces="application/json")
public class RequestBookCoversApi {
	@PostMapping
	public Messenger requestBookCovers(@RequestBody String[] coverNames) {
		
		if(coverNames==null || coverNames.length==0) {
			return SL.respondError("No cover names specified.");
		}
		
		try {
			ArrayList<File> covers = new ArrayList<>();
			for(int i=0; i< coverNames.length; i++) {
				File file = new File("covers/"+coverNames[i]);
				if(file.exists() && file.canRead()) {
					covers.add(file);
				}
			}
			
			return SL.respondSuccess("Book covers", covers);
		}catch(Exception e) {
			return SL.respondError(e.getMessage());
		}
	}
}
