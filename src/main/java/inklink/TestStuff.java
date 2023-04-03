package inklink;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestStuff {
	
	public static void main(String[] args) {
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		System.out.println(enc.encode("guess"));
		
		/*long time = System.currentTimeMillis();
		System.out.println("Nano: "+System.nanoTime()+" Millis: "+System.currentTimeMillis());
		
		try(PrintWriter out = new PrintWriter(System.currentTimeMillis()+".txt")){
			out.print(new Date());
			
			out.close();
			
			File file = new File(time+".txt");
			System.out.println("RWX: "+file.canRead()+file.canWrite()+file.canExecute());
			file.setExecutable(true);
			file.setWritable(true);
			file.setReadable(true);
			System.out.println("RWX: "+file.canRead()+file.canWrite()+file.canExecute());
			
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}*/
	}
	
}
