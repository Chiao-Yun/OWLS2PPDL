package fileWriter;
//test
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import PDDXML_Datatypes.PDDXML_Actions.ActionElement;
import converter.IO_Attributes;
import converter.PE_Attributes;
import sun.security.util.Length;

public class actionWriter {
	
	private static String FILENAME = "";
	String outputPath;
	public static String outputFile;
	
	public actionWriter(){
		
	}

	
	public actionWriter(ActionElement actionElem) {
	
		FILENAME = actionElem.getServiceName() + ".txt";
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		ArrayList<IO_Attributes> inputElements = actionElem.getInputObjects();
		ArrayList<IO_Attributes> outputElements = actionElem.getOutputObjects();
		ArrayList<PE_Attributes> predElements = actionElem.getCondObjects();
		ArrayList<PE_Attributes> resultElements = actionElem.getResultObjects();
		ArrayList<String> ParamPerAction = actionElem.getParam();
		ArrayList<String> PredPerAction = actionElem.getPredicates();

		try {
			domainWriter dw = new domainWriter();
			
			File file = new File(dw.getFileName());

			
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			String service = "\n\t(:action " + actionElem.getServiceName();
			bw.write(service);
			
			
			bw.write("\n\t\t:parameters\t(");
			
			if (ParamPerAction.size()!=0) {
				bw.write("?" + ParamPerAction.get(0));
				for (int k=1; k<ParamPerAction.size(); k++)
					bw.write(" ?" + ParamPerAction.get(k));
			}
			if (PredPerAction.size()!=0) {
				bw.write(" ");
				bw.write("?" + ParamPerAction.get(0));
				for (int t=1; t<PredPerAction.size()-1; t++)
					bw.write(" ?"+ PredPerAction.get(t));
				bw.write(" ?" + PredPerAction.get(PredPerAction.size()-1));
			}
			bw.write(")");
			
			
			bw.write("\n"+"\t\t:precondition");
			
			if (inputElements.size() + predElements.size() > 1) {
				bw.write(" (and\n");
			}
			
			if (inputElements.size() != 0) {
				for (IO_Attributes IO_elem : inputElements) {
					String input = "(" + IO_elem.getOntology() + " ?" + IO_elem.getParam() + ")\n";
					if (inputElements.size() + predElements.size() == 1)
						bw.write("\t"+input);
					else
						bw.write("\t\t\t"+input);
				}
			}
			
			if (predElements.size() != 0) {
				for (PE_Attributes PE_elem : predElements) {
					String PEName = PE_elem.getPEName();
					bw.write("\t\t\t(" + PEName + " ");
					for (Map<String, String> parPE : PE_elem.getPEList()) {
						List<String> ParamKeys = new ArrayList<String>(parPE.keySet());
						for (String ParamKey: ParamKeys) {
							bw.write("\n");
							String Type = ParamKey;
							String param = parPE.get(ParamKey);
							String arg = param + " ?" + Type;
							bw.write("\t\t\t"+arg);
						}
					}
					bw.write(")");
				}
			}
			
			if (inputElements.size() + predElements.size() > 1) {
				bw.write("\n\t\t)");
			}
			
			bw.write("\n\t\t"+":effects");

			if (outputElements.size() + resultElements.size() > 1) {
				bw.write(" (and\n");
				
			}
			
			if (outputElements.size() != 0) {
				for (IO_Attributes IO_elem : outputElements) {
					String output = "(" + IO_elem.getOntology() + " ?" + IO_elem.getParam() + ")\n";
					if (inputElements.size() + predElements.size() == 1)
						bw.write("\t"+output);
					else
						bw.write("\t\t"+output);
				}
			}
			
			if (resultElements.size() != 0) {
				for (PE_Attributes PE_elem : resultElements) {
					String PEName = PE_elem.getPEName();
					bw.write("\t\t(" + PEName + " ");
					for (Map<String, String> parPE : PE_elem.getPEList()) {
						List<String> ParamKeys = new ArrayList<String>(parPE.keySet());
						for (String ParamKey: ParamKeys) {
							bw.write("\n");
							String Type = ParamKey;
							String param = parPE.get(ParamKey);
							String arg = param + " ?" + Type;
							bw.write("\t\t"+arg);
						}
					}
					bw.write(")");
				}
			}
			
			if (outputElements.size() + resultElements.size() > 1) {
				bw.write("\n\t\t)\n");
			}
			
			bw.write("\t)");
			
			System.out.println("Action file written!");
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
	
	public void setPath(String outputFile2) {
		outputFile = outputFile2;
	}


}
	
	

