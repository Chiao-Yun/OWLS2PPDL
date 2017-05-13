package PDDXML_Datatypes;
//test
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import converter.IO_Attributes;
import converter.PE_Attributes;
import fileWriter.actionWriter;;

public class PDDXML_Actions {

	public PDDXML_Actions() {
	}

	public class ActionElement {
		
		private String serviceName = null; 
		private ArrayList<IO_Attributes> inputObjects = new ArrayList<>();
		private ArrayList<IO_Attributes> outputObjects = new ArrayList<>();
		private ArrayList<PE_Attributes> condObjects = new ArrayList<>();
		private ArrayList<PE_Attributes> resultObjects = new ArrayList<>();
		private ArrayList<String> Predicates = new ArrayList<String>();
		private ArrayList<String> Param = new ArrayList<String>();
		private int hasInputnb=0;
		private int hasOutputnb=0;
		private int hasPredconditionNb=0;
		private int hasResultNb=0;
		private int inputnb=0;
		private int outputnb=0;
		private int prednb=0;
		private int resultnb=0;
		
		public ActionElement() {
		}
		
		public ActionElement cloneAction() {
			ActionElement result = new ActionElement();
			result.serviceName = this.serviceName;
			result.inputObjects = this.inputObjects;
			result.outputObjects = this.outputObjects;
			result.condObjects = this.condObjects;
			result.resultObjects = this.resultObjects;
			result.Predicates = this.Predicates;
			result.Param = this.Param;
			result.inputnb = this.inputnb;
			result.outputnb = this.outputnb;
			result.prednb = this.prednb;
			result.resultnb = this.resultnb;
			writeActionFile(result);
			return result;
		}
		
		public void setServiceName(String service) {
			serviceName = service;
		}
		
		public String getServiceName() {
			return serviceName;
		}
		
		public void addInput(IO_Attributes input) {
			inputObjects.add(input);
		}
		
		public void addOutput(IO_Attributes output) {
			outputObjects.add(output);
		}
		
		public void addPrecond(PE_Attributes precond) {
			condObjects.add(precond);
		}
		
		public void addResult(PE_Attributes result) {
			resultObjects.add(result);
		}
		
		public ArrayList<IO_Attributes> getInputObjects() {
			return inputObjects;
		}

		public ArrayList<IO_Attributes> getOutputObjects() {
			return outputObjects;
		}

		public ArrayList<PE_Attributes> getCondObjects() {
			return condObjects;
		}

		public ArrayList<PE_Attributes> getResultObjects() {
			return resultObjects;
		}

		public void setInputObjects(ArrayList<IO_Attributes> inputObjects) {
			this.inputObjects = inputObjects;
		}

		public void setOutputObjects(ArrayList<IO_Attributes> outputObjects) {
			this.outputObjects = outputObjects;
		}

		public void setCondObjects(ArrayList<PE_Attributes> condObjects) {
			this.condObjects = condObjects;
		}

		public void setResultObjects(ArrayList<PE_Attributes> resultObjects) {
			this.resultObjects = resultObjects;
		}

		public void setPredicates(ArrayList<String> predicates) {
			Predicates = predicates;
		}
		
		public void setParam(ArrayList<String> param) {
			Param = param;
		}
		
		public ArrayList<String> getPredicates() {
			return Predicates;
		}
		
		public ArrayList<String> getParam() {
			return Param;
		}
		
		public void setHasInputNb(int hasinput) {
			hasInputnb = hasinput;
		}
		
		public int getHasInputNb() {
			return hasInputnb;
		}
		
		public void setHasOutputNb(int hasOutput) {
			hasOutputnb = hasOutput;
		}
		
		public int getHasOutputNb() {
			return hasOutputnb;
		}
		
		public void setHasPredNb(int hasPred) {
			hasPredconditionNb = hasPred;
		}
		
		public int getHasPredNb() {
			return hasPredconditionNb;
		}
		
		public void setHasResultNb(int hasResult) {
			hasResultNb = hasResult;
		}
		
		public int getHasResultNb() {
			return hasResultNb;
		}
		
		public void setInputNb(int input) {
			inputnb = input;
		}
		
		public int getInputNb() {
			return inputnb;
		}
		
		public void setOutputNb(int output) {
			outputnb = output;
		}
		
		public int getOutputNb() {
			return outputnb;
			
		}
		
		public void setPredNb(int pred) {
			prednb = pred;
		}
		
		public int getPredNb() {
			return prednb;
			
		}
		
		public void setResultNb(int result) {
			resultnb = result;
		}
		
		public int getResultNb() {
			return resultnb;
			
		}
		
		public void writeActionFile(ActionElement actionElem) {
			actionWriter aw = new actionWriter(actionElem);
		}
		
		public void printIOPE(ActionElement actionElem) {
			System.out.println("---------------List of IOPE from OWL-S---------------");
			if (inputObjects.size() != 0) {
				System.out.println("---------------INPUT---------------");
				for (IO_Attributes IO_elem : inputObjects) {
					System.out.print(IO_elem.getID() + " ");
					System.out.print(IO_elem.getParam() + " ");
					System.out.print(IO_elem.getOntology());
				}
				System.out.println("");
			}
			if (outputObjects.size() != 0) {
				System.out.println("---------------OUTPUT---------------");
				for (IO_Attributes IO_elem : outputObjects) {
					System.out.print(IO_elem.getID() + " ");
					System.out.print(IO_elem.getParam() + " ");
					System.out.print(IO_elem.getOntology());
				}
				System.out.println("");
			}
			if (condObjects.size() != 0) {
				System.out.println("---------------PRECONDITION---------------");
				for (PE_Attributes PE_elem : condObjects) {
					System.out.println(PE_elem.getID() + " ");
					System.out.println(PE_elem.getPEName());
					for (Map<String, String> parPE : PE_elem.getPEList()) {
						List<String> ParamKeys = new ArrayList<String>(parPE.keySet());
						for (String ParamKey: ParamKeys) {
							System.out.println(ParamKey + ":" + parPE.get(ParamKey));
						}
					}
				}
				System.out.println("");
			}
			if (resultObjects.size() != 0) {
				System.out.println("---------------RESULT---------------");
				for (PE_Attributes PE_elem : resultObjects) {
					System.out.println(PE_elem.getID() + " ");
					System.out.println(PE_elem.getPEName());
					for (Map<String, String> parPE : PE_elem.getPEList()) {
						List<String> ParamKeys = new ArrayList<String>(parPE.keySet());
						for (String ParamKey: ParamKeys) {
							System.out.println(ParamKey + ":" + parPE.get(ParamKey));
						}
					}
				}
				System.out.println("");
			}
			
		}
		
		public void printPredicates(){
			for (String k: Predicates)
				System.out.print(k + " ");
			System.out.println("");
		}
	
		public void printParam(){
			for (String k: Param)
				System.out.print(k + " ");
			System.out.println("");
		}
		
	}

	public ActionElement newActionElement() {
		return new ActionElement();
	}
		 
}
