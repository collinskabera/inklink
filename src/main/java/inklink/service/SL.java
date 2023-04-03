package inklink.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import inklink.Messenger;

@Service
public class SL {
	
	public static Messenger respondSuccess(String message) {
		Messenger messenger = new Messenger();
		messenger.setStatus("Success");
		messenger.setMessage(message);
		return messenger;
	}
	
	public static Messenger respondSuccess(String message, Object data) {
		Messenger messenger = new Messenger();
		messenger.setStatus("Success");
		messenger.setMessage(message);
		messenger.setData(data);
		return messenger;
	}
	
	public static Messenger respondError(String errorMessage) {
		ArrayList<String> erm = new ArrayList<>();
		erm.add(errorMessage);
		
		Messenger messenger = new Messenger();
		messenger.setStatus("Error");
		messenger.setErrorMessages(erm);
		return messenger;
	}
	
	public static Messenger respondError(ArrayList<String> erm) {
		Messenger mess = new Messenger();
		mess.setStatus("Error");
		mess.setErrorMessages(erm);
		return mess;
	}
	
	public static Messenger respondError(Errors errors) {
		Messenger mess = new Messenger();
		mess.setStatus("Error");

		List<ObjectError> errs = errors.getAllErrors();
		List<String> errorMessages = new ArrayList<>();
		
		for(ObjectError err: errs) {
			errorMessages.add(err.getDefaultMessage());
		}
		
		mess.setData(errorMessages);
		
		return mess;
	}
}
