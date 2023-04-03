package inklink;

import java.io.File;

public class Testing {
	
	public static void main(String[] args) {
		try {
			File dir = new File("upload-dir");
			if(dir.isDirectory()) {
				System.out.println("Directory");
				
				File[] files = dir.listFiles();
				
				for(int i=0;i<files.length;i++) {
					if(files[i].exists()) {
						System.out.print(i+". Exists");
						System.out.println("Name: "+files[i].getName()+" RWX: "+files[i].canRead()+files[i].canWrite()+files[i].canExecute()+"\n");
					}
					else {
						System.out.println("Noob Saibot laughing.");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
