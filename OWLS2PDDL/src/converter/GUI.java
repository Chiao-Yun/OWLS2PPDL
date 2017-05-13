package converter;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

import PDDXML_Datatypes.PDDXML_Object;
import fileWriter.actionWriter;
import fileWriter.domainWriter;
import fileWriter.problemWriter;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GUI extends JFrame implements ActionListener{
	JButton button1,button2;
    JFileChooser fileChooser1;
    JTextArea textArea1;
    JTextArea textArea2;
    JLabel label1, label2, label3;
    JTextField textField1, textfield2;
    JPanel thePanel, buttonPanel, leftPanel, rightPanel, centerPanel;
    String pathArray[];
    JList jlistInput, jlistOutput;
    public ArrayList selectedIn;
	public ArrayList selectedOut;
    
	public GUI()
	{
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		this.setVisible(true);
		
		
        Toolkit tk = Toolkit.getDefaultToolkit();
        
        Dimension dim = tk.getScreenSize();
        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);
        this.setLocation(xPos, yPos);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("OWL-S to PDDL");
        thePanel = new JPanel();
        thePanel.setLayout(new BorderLayout());
        button1 = new JButton("Browse");
        button1.setBorderPainted(true);
        button1.setText("Browse");
        button1.setToolTipText("Select OWL-S file");
        buttonPanel = new JPanel();
        
        textField1 = new JTextField("domain_name");
        textfield2 = new JTextField("problem_name");
        buttonPanel.add(textField1);
        buttonPanel.add(textfield2);
        buttonPanel.add(button1, BorderLayout.CENTER);
        thePanel.add(buttonPanel, BorderLayout.NORTH);
        textArea1 = new JTextArea(35,45);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        leftPanel = new JPanel();
        leftPanel.add(textArea1, BorderLayout.CENTER);
        
        
        JScrollPane scrollbar1 = new JScrollPane(textArea1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(scrollbar1, BorderLayout.CENTER);
        thePanel.add(leftPanel, BorderLayout.WEST);
        textArea2 = new JTextArea(35,45);
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        rightPanel = new JPanel();
        rightPanel.add(textArea2);
        JScrollPane scrollbar2 = new JScrollPane(textArea2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(scrollbar2, BorderLayout.CENTER);
        thePanel.add(rightPanel, BorderLayout.EAST);
        
        label1 = new JLabel("Input Services");
        label2 = new JLabel("Output Services");
        centerPanel = new JPanel();
        button2 = new JButton("Create Problem!");
        Set<String> InOut = new HashSet<String>();
        PDDXML_Object obj = new PDDXML_Object();
        obj.readFile();
        InOut = obj.IO;
		obj.printIO(); 
        String[] InOutArray = InOut.toArray(new String[InOut.size()]);
        DefaultListModel inputs = new DefaultListModel();
        for (int i =0; i< InOutArray.length;i++)
        {
        	inputs.addElement(InOutArray[i]);
        }
        
        jlistInput = new JList(inputs);
        jlistInput.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jlistInput.setVisible(true);
        jlistInput.setVisibleRowCount(10);
        JScrollPane InputScroll = new JScrollPane();
        InputScroll.setViewportView(jlistInput);
        
        
        final DefaultListModel outputs = new DefaultListModel();
        for (int i =0; i< InOutArray.length;i++)
        {
        	outputs.addElement(InOutArray[i]);
        }
        
        jlistOutput = new JList(outputs);
        jlistOutput.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jlistOutput.setVisible(true);
        jlistOutput.setVisibleRowCount(10);
        
        JScrollPane OutputScroll = new JScrollPane();
        OutputScroll.setViewportView(jlistOutput);
        
        centerPanel.add(label1);
        centerPanel.add(InputScroll, BorderLayout.NORTH);
        centerPanel.add(label2, BorderLayout.CENTER);
        centerPanel.add(OutputScroll, BorderLayout.NORTH);
        centerPanel.add(button2, BorderLayout.CENTER);
        thePanel.add(centerPanel, BorderLayout.CENTER);

        
        this.add(thePanel);
        this.setVisible(true);
        
        fileChooser1 = new JFileChooser();
        fileChooser1.setMultiSelectionEnabled(true);
        button1.addActionListener(this);
        button2.addActionListener(this);
        
	}
	
	public void actionPerformed(ActionEvent e) {
		
		  //Handle open button action.
        if (e.getSource() == button1) {
            int returnVal = fileChooser1.showOpenDialog(GUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] file = fileChooser1.getSelectedFiles();
                //This is where a real application would open the file.
                String domainName = textField1.getText();
                domainWriter dw = new domainWriter();
                dw.setDomainName(domainName);
                dw = new domainWriter(file.length);
                
                for (int i =0; i< file.length; i++) {
                	try{
                   	 Scanner input = new Scanner(file[i]);
                   	 while (input.hasNext()) {
                   		 	System.out.println(input.nextLine());
                   	    }
                   	 input.close();
                   	 String path = file[i].getAbsolutePath().toString();
                   	 
                   	 ServiceReader sr = new ServiceReader(path);
                   	 
                   }
                   catch (FileNotFoundException fe) {
                       fe.printStackTrace();
                   }
                }
                
                
            }
                 else {
                label1.setText("command cancelled by user.");
            }
         String outputPath = domainWriter.outputFile;
       	 File outputFile = new File(outputPath);
       	 Scanner output;
		try {
			output = new Scanner(outputFile);
			while (output.hasNext()) {
    	        textArea1.append(output.nextLine());      
    	    }
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
       	 
        //Handle save button action.
        }
        
        if (e.getSource() == button2) {
        	selectedIn =  new ArrayList();
        	selectedIn =	(ArrayList) jlistInput.getSelectedValuesList();
        	for(int i=0;i < selectedIn.size();i++){
        	    System.out.println(selectedIn.get(i));
        	} 
        	
        	selectedOut = new ArrayList();
        	selectedOut = (ArrayList) jlistOutput.getSelectedValuesList();
        	for(int i=0;i < selectedOut.size();i++){
        	    System.out.println(selectedOut.get(i));
        	} 
        	
        	domainWriter dw = new domainWriter();
        	String domainName = textField1.getText();
    		dw.setDomainName(domainName);
    		problemWriter pw = new problemWriter();
    		
    		    pw.setSelectedInput(selectedIn);
        	
    		    pw.setSelectedOutput(selectedOut);
        	
        	    pw.setDomainName(dw.getDomainName()); //Enable users to input domain name
        		String problemName = textfield2.getText();
        		pw.setProblemName(problemName); //Enable users to input problem name
        		pw.setPath("git/OWLS2PDDL/OWLS2PDDL");
        		
        		pw.writeFile("problem.pddl");
        		pw.printProblem();
        		String problemPath = new String("problem.pddl");
        		File problemFile = new File(problemPath);
        		Scanner problem;
        		try{
        			problem = new Scanner(problemFile);
        			while (problem.hasNext()) {
            	        textArea2.append(problem.nextLine());      
            	    }
        		} catch (FileNotFoundException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}	
    		
        }
        
	}
	
	public ArrayList GetInputs(){
		return selectedIn;
	}
	
	public ArrayList GetOutput(){
		return selectedOut;
	}

}
