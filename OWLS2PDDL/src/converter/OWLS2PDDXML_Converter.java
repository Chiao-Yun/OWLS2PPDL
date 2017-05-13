package converter;
//test
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

import fileWriter.actionWriter;
import fileWriter.domainWriter;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.io.*;
import java.util.Scanner;
//import PDDXML_Datatypes.test;

public class OWLS2PDDXML_Converter {
	
	public static void main(String[] args) {
		
		new GUI();
		
	}
	
	
	public void appendText(String filename, String text) {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			String data = text;

			File file = new File("DomainFile.pddl");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(data);

			System.out.println("New text added");

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


}
