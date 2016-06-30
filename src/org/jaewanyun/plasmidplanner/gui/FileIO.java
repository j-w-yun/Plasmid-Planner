package org.jaewanyun.plasmidplanner.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

final class FileIO {

	static File mostRecentDirectory;

	static String load() {
		JFileChooser c;
		if(mostRecentDirectory != null)
			c = new JFileChooser(mostRecentDirectory);
		else
			c = new JFileChooser();
		// Set filter to read .txt
		String[] textFile = {".txt", "txt"};
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Text File", textFile);
		c.setFileFilter(filter);
		int rVal = c.showOpenDialog(c);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			mostRecentDirectory = c.getCurrentDirectory();
			return c.getCurrentDirectory().toString() + "\\"
			+ c.getSelectedFile().getName();
		}
		return "";
	}

	static String save() {
		JFileChooser c;
		if(mostRecentDirectory != null)
			c = new JFileChooser(mostRecentDirectory);
		else
			c = new JFileChooser();
		int rVal = c.showSaveDialog(c);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			mostRecentDirectory = c.getCurrentDirectory();
			return c.getCurrentDirectory().toString() + "\\"
			+ c.getSelectedFile().getName();
		}
		return "";
	}

	static void save(String fileName, String content) {
		boolean noSuchFile = false;
		try {
			FileReader check = new FileReader(fileName);
			check.close();
		} catch (Exception e) {
			// No such file. Safe to save as fileName
			noSuchFile = true;
		}
		if(!noSuchFile) {
			int selected = JOptionPane.showConfirmDialog(
					null,
					"Overwrite existing file?",
					"File Name Exists",
					JOptionPane.YES_NO_OPTION);
			if(selected != JOptionPane.YES_OPTION)
				return;
		}
		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println(content);
			writer.close();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	// Same as the one in Utility
	static String[] load(String fileName) {
		ArrayList<String> readLines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = br.readLine();
			while (line != null) {
				readLines.add(line);
				line = br.readLine();
			}
		} catch (Exception e1) {
			// Try again with .txt added
			try (BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"))) {
				String line = br.readLine();
				while (line != null) {
					readLines.add(line);
					line = br.readLine();
				}
			} catch (Exception e2) {
				throw new IllegalArgumentException();
			}
		}
		return readLines.toArray(new String[readLines.size()]);
	}
}