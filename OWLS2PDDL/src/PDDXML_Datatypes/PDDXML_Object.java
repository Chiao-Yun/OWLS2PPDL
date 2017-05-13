package PDDXML_Datatypes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PDDXML_Object {
	
	public static Set<String> IO = new HashSet<String>();	// Save all the objects from Concetps.owl for Input and Output
	Set<ObjectOntology> Ontology =  new HashSet<ObjectOntology>(); // Save the ontology
	
	//Create an empty constructor
	public PDDXML_Object() {
		
	}
	
	//Read the file and save all the 
	
	public void readFile() {
		
		try {

			File ontologyFile = new File("Concepts.owl");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ontologyFile);
			doc.getDocumentElement().normalize();
			Node root = doc.getDocumentElement();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = root.getChildNodes();

			System.out.println("----------------------------");
			
			parse(root);
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
		 	
	}
	
	
	public void parse(Node node) {
	    
		NodeList nList = node.getChildNodes();

	    for (int i = 0; i < nList.getLength(); i++) {
	        
	    	Node childNode = nList.item(i);
	        
	        if (childNode.getNodeName().equals("owl:Class")) {
	        	
	        	if (childNode.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) childNode;
					
					ObjectOntology concept = new ObjectOntology();
					
					//Get the parameter
					if (element.hasAttribute("rdf:ID")) {
						//System.out.println(element.getAttribute("rdf:ID"));
						concept = new ObjectOntology(element.getAttribute("rdf:ID"));
						IO.add(element.getAttribute("rdf:ID"));
						Ontology.add(concept);
					}
					
					if (element.hasAttribute("rdf:about")) {
						//System.out.println(element.getAttribute("rdf:about"));
						concept = new ObjectOntology(element.getAttribute("rdf:about").split("#")[1]);
						IO.add(element.getAttribute("rdf:about").split("#")[1]);
						Ontology.add(concept);
					}
					
					//Get the parent class
					if (element.hasChildNodes()) {
						
						NodeList childNode_subClassOf = childNode.getChildNodes();
	
						for (int k = 0; k < childNode_subClassOf.getLength(); k++) {
							
						    Node node_parentClass = childNode_subClassOf.item(k);
						    
						    if (node_parentClass.getNodeName().equals("rdfs:subClassOf")) {
						    	
						    	if (node_parentClass.getNodeType() == Node.ELEMENT_NODE) {
	
									Element parentClass = (Element) node_parentClass;
									
									String attr = "";
									
									if (parentClass.hasAttribute("rdf:ID") || parentClass.hasAttribute("rdf:about") || parentClass.hasAttribute("rdf:resource")) {
									
										if (parentClass.hasAttribute("rdf:ID")) {
											attr = "rdf:ID";
										}
										
										if (parentClass.hasAttribute("rdf:about")) {
											attr = "rdf:about";
										}
										
										if (parentClass.hasAttribute("rdf:resource")) {
											attr = "rdf:resource";
										}
										
										concept.setParent(parentClass.getAttribute(attr).split("#")[1]);
										ObjectOntology parent_concept = new ObjectOntology(parentClass.getAttribute(attr).split("#")[1]);
										Ontology.add(parent_concept);
										IO.add(parentClass.getAttribute(attr).split("#")[1]);
										parent_concept.setChild(concept.getName());
			
									}
					    		}
					    	}

						    
						}
					}

				}
			}

	        if (childNode.hasChildNodes()) {
	        	parse(childNode);
	        }
	    }
	}
	
	public Set<String> getIO() {
		return IO;
	}
	
	public void printIO() {
		for (String IOput : IO) {
			System.out.println(IOput);
		}
	}
	
	public void printOntology() {
		
		for (ObjectOntology node : Ontology) {
			System.out.println(node.getName());
			System.out.println("\tParent: " + node.getParent());
			System.out.println("\tChild: " + node.getChild());
		}
		
	}
	
	public ObjectOntology getConcept(String node) {
		
		ObjectOntology obj = new ObjectOntology();

		for (ObjectOntology temp : Ontology) {
			if (temp.getName().equals(node)) {
				obj = temp;
				break;
			}
		}
		return obj;
	}
	
	public ObjectOntology getChild(String node) {
				
		ObjectOntology obj = new ObjectOntology();

		for (ObjectOntology temp : Ontology) {
			if (temp.getName().equals(node)) {
				String child = temp.getChild();
				for (ObjectOntology child_temp : Ontology) {
					if (child_temp.getName().equals(child)) {
						obj = child_temp;
						break;
					}
				}				
			}
		}
		return obj;
	}
	
	public ObjectOntology getParent(String node) {
		
		ObjectOntology obj = new ObjectOntology();

		for (ObjectOntology temp : Ontology) {
			if (temp.getName().equals(node)) {
				String parent = temp.getParent();
				for (ObjectOntology parent_temp : Ontology) {
					if (parent_temp.getName().equals(parent)) {
						obj = parent_temp;
						break;
					}
				}				
			}
		}
		return obj;
		
	}
	
	public class ObjectOntology {
		
		String name = null;		//Name of the element (parameter in PDDL)
		String parent = null;	//Super-class of the element
		String child = null;	//Sub-class of the element
		
		
		//Create an constructor as a element
		public ObjectOntology() {
			
		}
		
		public ObjectOntology(String element) {
			name = element;
		}


		public String getName() {
			return name;
		}


		public String getParent() {
			return parent;
		}


		public String getChild() {
			return child;
		}


		public void setParent(String parent) {
			this.parent = parent;
		}


		public void setChild(String child) {
			this.child = child;
		}
		
		public void printName() {
			System.out.println(name);
		}
 		
		public void printChild() {
			System.out.println(child);
		}

		public void printParent() {
			System.out.println(parent);
		}
	}

}
