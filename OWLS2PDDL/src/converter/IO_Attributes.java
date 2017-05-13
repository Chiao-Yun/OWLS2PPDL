package converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class IO_Attributes {

	private String ID;
	private String Param;
	private String Ontology;

	
	public IO_Attributes(String iD, String param, String ontology) {
		ID = iD.replaceAll("\\s","");
		Param = param.replaceAll("\\s","");
		Ontology = ontology.replaceAll("\\s","");
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
	
}
