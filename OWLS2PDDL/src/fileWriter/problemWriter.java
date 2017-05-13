package fileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//test
import java.util.ArrayList;

public class problemWriter {
	
	String problemName = null;
	String domainName = null;

	ArrayList<String> selectedInput = new ArrayList<>();
	ArrayList<String> selectedOutput = new ArrayList<>();
	
	private static String FILENAME = "";
	public static String outputFile;
	private BufferedWriter bw = null;
	private FileWriter fw = null;
		
	public problemWriter() {
		
	}

	public String getProblemName() {
		return problemName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	public ArrayList<String> getSelectedInput() {
		return selectedInput;
	}

	public ArrayList<String> getSelectedOutput() {
		return selectedOutput;
	}

	public static String getFILENAME() {
		return FILENAME;
	}

	public void setSelectedInput(ArrayList<String> selectedInput) {
		this.selectedInput = selectedInput;
	}

	public void setSelectedOutput(ArrayList<String> selectedOutput) {
		this.selectedOutput = selectedOutput;
	}
	
	public void setPath(String outputFile2) {
		outputFile = outputFile2;
	}

	public void writeFile(String filename) {
		
		FILENAME = filename;
		
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
			domainWriter dw = new domainWriter();
			
			bw.write("(define (problem " + problemName +")\n");
			bw.write("(:domain " + dw.getDomainName() + ")\n");
			bw.write("(:init");
			
			if (selectedInput.size() > 1) {
				bw.write("\t(and \n");
				for (String input : selectedInput) {
					bw.write("\t\t(" + input + " ?" + input + ")\n");
				}
				bw.write("\t\t)\n");
				bw.write(")\n");
			}
			
			if (selectedInput.size() == 1) {
				for (String input : selectedInput) {
					bw.write("(" + input + " ?" + input + "))\n");
				}
			}
			
			if (selectedInput.size() == 0) {
				bw.write(")\n");
			}
			
			
			bw.write("(:goal");
			
			if (selectedOutput.size() > 1) {
				bw.write("\t(and \n");
				for (String output : selectedOutput) {
					bw.write("\t\t(" + output + " ?" + output + ")\n");
				}
				bw.write("\t\t)\n");
				bw.write(")\n");
			}
			
			if (selectedOutput.size() == 1) {
				for (String output : selectedOutput) {
					bw.write("(" + output + " ?" + output + "))\n");
				}
			}
			
			if (selectedOutput.size() == 0) {
				bw.write(")\n");
			}
			
			bw.write(")");
			
			System.out.println("Problem file written!");
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
	
	public void printProblem() {
		
		domainWriter dw = new domainWriter();
		
		System.out.print("(define (problem " + problemName +")\n");
		System.out.print("(:domain " + dw.getDomainName() + ")\n");
		System.out.print("(:init");
		
		if (selectedInput.size() > 1) {
			System.out.print("\t(and \n");
			for (String input : selectedInput) {
				System.out.print("\t(" + input + " ?" + input + ")\n");
			}
			System.out.print("\t)\n");
			System.out.print(")");
		}
		
		if (selectedInput.size() == 1) {
			for (String input : selectedInput) {
				System.out.print("(" + input + " ?" + input + "))");
			}
		}
		
		if (selectedInput.size() == 0) {
			System.out.print(")");
		}
		
		
		System.out.print("\n(:goal");
		
		if (selectedOutput.size() > 1) {
			System.out.print("\t(and \n");
			for (String output : selectedOutput) {
				System.out.print("\t(" + output + " ?" + output + ")\n");
			}
			System.out.print("\t)\n");
			System.out.print("\n)");
		}
		
		if (selectedOutput.size() == 1) {
			for (String output : selectedOutput) {
				System.out.print("(" + output + " ?" + output + "))");
			}
		}
		
		if (selectedOutput.size() == 0) {
			System.out.print(")");
		}
		
		System.out.print("\n)");
	}
}
