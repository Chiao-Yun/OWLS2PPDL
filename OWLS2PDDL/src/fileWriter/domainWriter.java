package fileWriter;
//test
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import converter.ServiceReader;


public class domainWriter {
	
	private static String FILENAME = "";
	public static String outputFile;
	
	private BufferedWriter bw = null;
	private FileWriter fw = null;
	private static String domainName = null;
	
	
	public domainWriter() {
	}
	
	public domainWriter(int count) {

		FILENAME = "DomainFile" + ".pddl";

		try {
			
			File file = new File(FILENAME);

			// if file exists, delete it and create a new one.
			if (file.exists()) {
				file.delete();
			}

			if (!file.exists()) {
				file.createNewFile();
			}
			
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			bw.write("(define (domain " + getDomainName() +")\n");
			bw.write("\t(:requirements [:strips])");
			
			System.out.println("Domain file written!");
			outputFile = file.getName().toString();
			setPath(outputFile);
			
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {
				if (bw != null) 
					bw.close();
				if (fw != null) 
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public String getFileName() {
		return FILENAME;
	}
	
	public void setFileName(String filename) {
		FILENAME=filename;
	}
	
	public BufferedWriter getBw() {
		return bw;
	}

	public FileWriter getFw() {
		return fw;
	}

	public void setBw(BufferedWriter bw) {
		this.bw = bw;
	}

	public void setFw(FileWriter fw) {
		this.fw = fw;
	}

	public void setPath(String outputFile2) {
		outputFile = outputFile2;
	}
	
	public void setDomainName(String domain) {
		domainName = domain;
	}
	
	public String getDomainName() {
		return domainName;
	}
	
}
