/*
 * OWLS2PDDL Converter - Service Reader
 *
 * Originally developed by Andreas Gerber, Matthias Klusch
 * Modified by Chiao-Yun Li
 *
 * The code is for a course project to parse OWL-S into PDDL using JAXP (DOM used here).
 * The original code was developed without JAXP.
 * 
 */

//test
package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.scenario.effect.impl.prism.PrDrawable;

import PDDXML_Datatypes.PDDXML_Actions;
import PDDXML_Datatypes.PDDXML_Actions.ActionElement;
import sun.awt.SunHints.Value;


public class ServiceReader {
	// tess
	
	private Map<IOPE_Object, IO_Attributes> IOAttributes = new HashMap<IOPE_Object, IO_Attributes>();
	private Map<IOPE_Object, PE_Attributes> PEAttributes = new HashMap<IOPE_Object, PE_Attributes>();
	private Set<IOPE_Object> IO_keys = IOAttributes.keySet();
	private Set<IOPE_Object> PE_keys = PEAttributes.keySet();
	private Map<String, String> PE_param = new HashMap<String, String>();
	private ArrayList<Map<String, String>> PE_arrayList = new ArrayList<Map<String, String>>();
	private ArrayList<String> Predicates = new ArrayList<String>();
	private ArrayList<String> Param = new ArrayList<String>();
	
	private String fileName;
	private String service;
	private Document doc;
	private String precondiNodeName = "expr:SWRL-Condition";
	private String effectNodeName = "process:Result";
	
	/**
     * Empty constructor.
     */
    public ServiceReader() {
	}

    /**
     * Parses the service file of the owl-description and build an internal data structure.
     * @param fileName - File name
     */
    public ServiceReader(String file_name) { 
    	
    	fileName = file_name;
    		
    	try {
    		
    		//Open the file
    		File fOwlsFile = new File(fileName);
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		doc = dBuilder.parse(fOwlsFile);
    		doc.getDocumentElement().normalize();

    		//Service
    		System.out.println("---------------Service------------------");
			NodeList serviceList = doc.getElementsByTagName("service:Service");
			Node serviceNode = serviceList.item(0);
			if (serviceNode.getNodeType() == Node.ELEMENT_NODE) {
				Element ServiceElement = (Element) serviceNode;
				String actionName = ServiceElement.getAttribute("rdf:ID");
				service = actionName;
				System.out.println("Action Name (Service) : " + actionName);
			}
			
			//Service Profile
			System.out.println("---------------Profile------------------");
			NodeList profileList = doc.getElementsByTagName("profile:serviceName");
			Node profileNode = profileList.item(0);
			if (profileNode.getNodeType() == Node.ELEMENT_NODE) {
				Element profileElement = (Element) profileNode;
				String actionName = profileElement.getTextContent();
				System.out.println("Action Name (Profile) : " + actionName);	
			}
			
    		//System.out.println("---------------Input------------------");
    		hasConverter("profile:hasInput");
    		//hasConverter("process:hasInput");
    		IOConverter("process:Input");    		

			//System.out.println("---------------Output------------------");
			hasConverter("profile:hasOutput");
			//hasConverter("process:hasOutput");
    		IOConverter("process:Output");
    		
    		//System.out.println("---------------Precondition------------------");
			hasConverter("profile:hasPrecondition");
			//hasConverter("process:hasPrecondition");
			PEConverter("Precondition") ;
			
			//System.out.println("---------------Result------------------");
			hasConverter("profile:hasResult");
			//hasConverter("process:hasResult");
			PEConverter("Result");
			
    		
    		System.out.println("---------------IOPE Element------------------");
			printIO_result(IOAttributes);
			printPE_result(PEAttributes);
			setPredicate(PEAttributes);
			setParam(IOAttributes);
			createAction(IOAttributes, PEAttributes);
			
			//printPredicates(Predicates);
			//printParam(Param);
			
		} catch ( FileNotFoundException e ) { // File does not exists
	          System.err.println( "ServiceReader: File not found ! " + e);
	    }
        catch ( IOException e ) {           // Read / write error
          System.err.println( "ServiceReader: Read / write error" + e);
        }
        catch ( Exception e ) {             // any other problems
          System.err.println( "ServiceReader: An error occurs while parsing this object: " + fileName + "  " );
          e.printStackTrace();
        }
    }
    
	private boolean isElementExists(Node nNode, String tagName) {
		Element nNodeElement = (Element) nNode;
	    NodeList nodeList = nNodeElement.getElementsByTagName(tagName);
	    return nodeList.getLength() == 0 ? false : true;
	}
	
	private boolean isElementExists(Document doc, String tagName) {
	    NodeList nodeList = doc.getElementsByTagName(tagName);
	    return nodeList.getLength() == 0 ? false : true;
	}
	
	//profile:hasIOPE; process:hasIOPE in AtomicProcess (Assume the same)
	private void hasConverter(String IOPE) {
		String IOPE_type = IOPE.split("has")[1];
		if (isElementExists(doc, IOPE)) {
			NodeList hasIOPEProfileList = doc.getElementsByTagName(IOPE);
			for (int eleCount = 0; eleCount < hasIOPEProfileList.getLength(); eleCount++) {
				Node hasIOPENode = hasIOPEProfileList.item(eleCount);			
				if (hasIOPENode.getNodeType() == Node.ELEMENT_NODE) {
					Element hasIOPEElement = (Element) hasIOPENode;
					String hasIOPEObjName = hasIOPEElement.getAttribute("rdf:resource").split("#")[1];
					IOPE_Object hasIOPE = new IOPE_Object("has"+IOPE_type, hasIOPEObjName);
					if (hasIOPE.getType().contains("put"))
						IOAttributes.put(hasIOPE, null);
					if (hasIOPE.getType().contains("condition") || hasIOPE.getType().contains("esult"))
						PEAttributes.put(hasIOPE, null);
				}
			}
		} else {System.out.println("No has" + IOPE_type + "in Profile.");}
	}
	
	private void IOConverter(String IO) {
		if (isElementExists(doc,IO)) {
			NodeList IOList = doc.getElementsByTagName(IO);
			for (int eleCount = 0; eleCount < IOList.getLength(); eleCount++) {
				Node IONode = IOList.item(eleCount);			
				if (IONode.getNodeType() == Node.ELEMENT_NODE) {
					Element IOElement = (Element) IONode;
					String IOObjName = IOElement.getAttribute("rdf:ID");
					String IOObjType = IOElement.getElementsByTagName("process:parameterType").item(0).getTextContent().split("#")[1];
					String IOURI = IOElement.getTextContent();
					String IOOntology = IOURI.split("127.0.0.1/")[1];
					IO_Attributes IO_Attributes = new IO_Attributes(IOObjName, IOObjType, IOOntology);	//Set the OWL-S attributes to PDDL attributes
					for (IOPE_Object key: IO_keys) {
						if (key.getType().equals("has"+IO.split(":")[1]) && key.getID().equals(IO_Attributes.getID())) {
							IOAttributes.put(key, IO_Attributes);
						}
					}
				}				
			}
		} else {System.out.println("No " + IO.split(":")[1] + " in Process.");}	
	}
	
	private void PEConverter(String PE) {
		if (PE.equals("Precondition")) 
			PE = precondiNodeName;
		if (PE.equals("Result"))
			PE = effectNodeName;
		if (isElementExists(doc, PE)) {
			NodeList PEList = doc.getElementsByTagName(PE);
			for (int eleCount = 0; eleCount < PEList.getLength(); eleCount++) {
				Node PENode = PEList.item(eleCount);			
				if (PENode.getNodeType() == Node.ELEMENT_NODE) {
					Element PEElement = (Element) PENode;
					String PEObjName = PEElement.getAttribute("rdf:ID");
					NodeList predNodeList = PEElement.getElementsByTagName("swrl:propertyPredicate");
					Node predNode = predNodeList.item(0);
					Element predElement = (Element) predNode;
					String predName = predElement.getAttribute("rdf:resource").split("#")[1];
					PE_arrayList = new ArrayList<Map<String, String>>();
					int counter = 1;
					while (isElementExists(PENode,"swrl:argument" + String.valueOf(counter))) {
						PE_param = new HashMap<String, String>();
						NodeList argNodeList = PEElement.getElementsByTagName("swrl:argument" + String.valueOf(counter));
						Node argNode = argNodeList.item(0);
						Element argElement = (Element) argNode;
						String PEObjType = argElement.getAttribute("rdf:resource").split("#")[1];
						String PEOntology = argElement.getAttribute("rdf:resource").split("127.0.0.1/")[1];
						counter++;
						PE_param.put(PEObjType, PEOntology);
						PE_arrayList.add(PE_param);
					}
					PE_Attributes PE_Attributes = new PE_Attributes(PEObjName, predName, PE_arrayList);	//Set the OWL-S attributes to PDDL attributes
					for (IOPE_Object key: PE_keys) {
						if (PE.equals(precondiNodeName)) 
							PE = "Precondition";
						if (PE.equals(effectNodeName)) 
							PE = "Result";
						if (key.getType().equals("has"+PE) && key.getID().equals(PE_Attributes.getID())) {
							PEAttributes.put(key, PE_Attributes);
						}
					}
				}
			}
		} else {System.out.println("No " + PE.split(":")[1] + " in Process.");}	
	}
	
	private void printIO_result(Map<IOPE_Object, IO_Attributes> IO_element) {
		Iterator<IOPE_Object> iterator = IO_element.keySet().iterator();

		while (iterator.hasNext()) {
			IOPE_Object key = iterator.next();
			IO_Attributes value = IO_element.get(key);
			System.out.print(value.getParam() + " ");
			System.out.print(value.getOntology());
			System.out.println("");
		}		
	} 
	
	private void printPE_result(Map<IOPE_Object, PE_Attributes> ActionPreparation) {
		Iterator<IOPE_Object> iterator = ActionPreparation.keySet().iterator();

		while (iterator.hasNext()) {
			IOPE_Object key = iterator.next();
			PE_Attributes value = ActionPreparation.get(key);

			System.out.print(key.getType() + " ");
			System.out.print(key.getID() + " "); 

			//if (key.getType().contains("dition") || key.getType().contains("esult")) {
				System.out.println(value.getPEName());
				for (Map<String, String> parPE : value.getPEList()) {
					List<String> ParamKeys = new ArrayList<String>(parPE.keySet());
					for (String ParamKey: ParamKeys) {
						System.out.println(ParamKey + ":" + parPE.get(ParamKey));
					}
				}
			//}
			System.out.println("");
		}		
	}
	
	private void createAction(Map<IOPE_Object, IO_Attributes> IO_element, Map<IOPE_Object, PE_Attributes> PE_element) {
		
		PDDXML_Actions actions = new PDDXML_Actions();
		ActionElement action = actions.newActionElement();
		action.setServiceName(service);
		
		
		Iterator<IOPE_Object> iterator_IO = IO_element.keySet().iterator();

		while (iterator_IO.hasNext()) {
			IOPE_Object key = iterator_IO.next();
			IO_Attributes value = IO_element.get(key);

			System.out.print(key.getType() + " ");
			System.out.println(key.getID() + " "); 
			if (key.getType().contains("Input")) {
				action.addInput(value);
			}
			if (key.getType().contains("Output")) {
				action.addOutput(value);
			}
		}
		
		Iterator<IOPE_Object> iterator_PE = PE_element.keySet().iterator();

		while (iterator_PE.hasNext()) {
			IOPE_Object key = iterator_PE.next();
			PE_Attributes value = PE_element.get(key);
			System.out.print(key.getType() + " ");
			System.out.println(key.getID() + " "); 
			if (key.getType().contains("Precondition")) {
				action.addPrecond(value);
			}
			if (key.getType().contains("Result")) {
				action.addResult(value);
			}
		}
		
		
		
		action.setPredicates(Predicates);
		action.setParam(Param);
		action.printIOPE(action);
		action.printPredicates();
		action.printParam();
		action.cloneAction();
		
	}
	
	//public ArrayList<String> getInput; =getPredicate+getParam
	
	//public ArrayList<String> getOutput; = getInput
	
	public ArrayList<String> setPredicate(Map<IOPE_Object, PE_Attributes> ActionPreparation){
		
		Iterator<IOPE_Object> iterator = ActionPreparation.keySet().iterator();

		while (iterator.hasNext()) {
			IOPE_Object key = iterator.next();
			PE_Attributes value = ActionPreparation.get(key);
			Predicates.add(value.getPEName());
		}
		return Predicates;
	}
	
	public ArrayList<String> setParam (Map<IOPE_Object, IO_Attributes> IO_element) {
			
		Iterator<IOPE_Object> iterator = IO_element.keySet().iterator();

		while (iterator.hasNext()) {
			IOPE_Object key = iterator.next();
			IO_Attributes value = IO_element.get(key);
			Param.add(value.getParam());
		}
			
		return Param;

	}
	
}
