package converter;
//test
import java.util.ArrayList;
import java.util.Map;

public class PE_Attributes {

	private String ID;
	private String PEName;
	private String Param;
	private String Ontology;
	private ArrayList<Map<String, String>> PEList;
	

	
	public PE_Attributes(String iD, String name, ArrayList<Map<String, String>> PE) {
		ID = iD.replaceAll("\\s","");
		PEName = name.replaceAll("\\s","");
		PEList = PE;
	}
	
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getParam() {
		return Param;
	}

	public void setParam(String param) {
		Param = param;
	}

	public String getOntology() {
		return Ontology;
	}

	public void setOntology(String ontology) {
		Ontology = ontology;
	}


	public String getPEName() {
		return PEName;
	}


	public void setPEName(String pEName) {
		PEName = pEName;
	}


	public ArrayList<Map<String, String>> getPEList() {
		return PEList;
	}


	public void setPEList(ArrayList<Map<String, String>> pEList) {
		PEList = pEList;
	}

	
	
	
}
