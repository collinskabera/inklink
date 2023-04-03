package inklink.web.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inklink.Messenger;
import inklink.service.SL;

@RestController
@RequestMapping(path= "/api/requestBookCover", produces="application/json")
public class RequestBookCoverApi {

	@PostMapping
	public Messenger requestBookCover(@RequestParam(value="coverFileName", defaultValue="") String coverFileName) {
		try {
			File file = new File("covers/"+coverFileName);
			if(file.exists()) {
				if(file.canRead()){
					FileInputStream f = new FileInputStream(file);
					byte[] fileContent = f.readAllBytes();
					f.close();
					return SL.respondSuccess("Cover for "+coverFileName, Base64.getEncoder().encodeToString(fileContent));
				}				
				else {
					return SL.respondError("No read for cover by the name: "+coverFileName+".");
				}
			}else {
				return SL.respondError("No cover by the name: "+coverFileName+".");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return SL.respondError(e.getMessage());
		}
	}
}
