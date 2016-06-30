package org.jaewanyun.plasmidplanner.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jaewanyun.plasmidplanner.Planner;

class EnzymeListPane extends JSplitPane {

	private static JTextArea insertEnzymeList;
	private static JTextArea vectorEnzymeList;
	private static JScrollPane insertEnzymeListPane;
	private static JScrollPane vectorEnzymeListPane;
	private static JPanel insertEnzymeListPanel;
	private static JPanel vectorEnzymeListPanel;
	private static EnzymeListPane enzymeListPane;

	private EnzymeListPane() {

		insertEnzymeListPanel = new JPanel(new BorderLayout());
		insertEnzymeListPanel.setBorder(BorderFactory.createEmptyBorder());

		insertEnzymeList = new JTextArea("Digest to View");
		insertEnzymeList.setEditable(true);
		insertEnzymeList.setMargin(new Insets(15, 15, 15, 15));
		insertEnzymeList.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		insertEnzymeList.setBackground(GUIsettings.textAreaColor);
		insertEnzymeListPane = new JScrollPane(insertEnzymeList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		insertEnzymeListPanel.add(insertEnzymeListPane);

		vectorEnzymeListPanel = new JPanel(new BorderLayout());
		vectorEnzymeListPanel.setBorder(BorderFactory.createEmptyBorder());

		vectorEnzymeList = new JTextArea("Digest to View");
		vectorEnzymeList.setEditable(true);
		vectorEnzymeList.setMargin(new Insets(15, 15, 15, 15));
		vectorEnzymeList.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		vectorEnzymeList.setBackground(GUIsettings.textAreaColor);
		vectorEnzymeListPane = new JScrollPane(vectorEnzymeList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		vectorEnzymeListPanel.add(vectorEnzymeListPane);

		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setLeftComponent(insertEnzymeListPanel);
		setRightComponent(vectorEnzymeListPanel);

		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(0.5);
		//		BasicSplitPaneDivider divider = (BasicSplitPaneDivider) getComponent(2);
		//		divider.setDividerSize(5);
		//		divider.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}

	static EnzymeListPane getEnzymeListPane() {
		return enzymeListPane == null ? enzymeListPane = new EnzymeListPane(): enzymeListPane;
	}

	static void update() {
		// Clear
		insertEnzymeList.setText("");
		vectorEnzymeList.setText("");

		// Insert
		HashMap<String, HashSet<Integer>> insertEnzymeAndLocation = Planner.getInsert().getEnzymeAndLocation();
		Set<String> insertEnzymeNameSet = insertEnzymeAndLocation.keySet();
		String[] insertEnzymeNames = insertEnzymeNameSet.toArray(new String[insertEnzymeNameSet.size()]);
		Arrays.sort(insertEnzymeNames);
		// Header
		insertEnzymeList.append("Insert DNA\n\n");
		StringBuilder insertStringBuilder = new StringBuilder();
		Formatter insertFormatter = new Formatter(insertStringBuilder, Locale.US);
		insertFormatter.format("%-20s", "Enzyme Name");
		insertEnzymeList.append(insertStringBuilder.toString());
		insertStringBuilder.setLength(0);
		insertFormatter.format("%-4s", "Number of Cut(s)");
		insertEnzymeList.append(insertStringBuilder.toString());
		insertStringBuilder.setLength(0);
		insertFormatter.format("%-20s", "\t\tSite(s) of Cut");
		insertEnzymeList.append(insertStringBuilder.toString() + "\n");
		// Data append
		for(int j = 0; j < insertEnzymeNames.length; j++) {
			Set<Integer> locationSet = insertEnzymeAndLocation.get(insertEnzymeNames[j]);
			Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
			Arrays.sort(locations);
			insertStringBuilder = new StringBuilder();
			insertFormatter = new Formatter(insertStringBuilder, Locale.US);
			insertFormatter.format("%-20s", insertEnzymeNames[j]);
			insertEnzymeList.append("\n" + insertStringBuilder.toString() + " |\t");
			insertStringBuilder.setLength(0);
			insertFormatter.format("%-4s", "  " + Integer.toString(locations.length) + " ");
			insertEnzymeList.append(insertStringBuilder.toString() + "\t|\t");
			for(int k = 0; k < locations.length; k++) {
				insertStringBuilder.setLength(0);

				if(k + 1 == locations.length) {
					insertEnzymeList.append("[" + locations[k].toString() + "]");
				}
				else {
					insertFormatter.format("%-9s", "[" + locations[k].toString() + "],");
					insertEnzymeList.append(insertStringBuilder.toString());
				}
			}
		}
		insertFormatter.close();
		insertEnzymeList.setCaretPosition(0); // Back to top when complete


		// Vector
		HashMap<String, HashSet<Integer>> vectorEnzymeAndLocation = Planner.getVector().getEnzymeAndLocation();
		Set<String> vectorEnzymeNameSet = vectorEnzymeAndLocation.keySet();
		String[] vectorEnzymeNames = vectorEnzymeNameSet.toArray(new String[vectorEnzymeNameSet.size()]);
		Arrays.sort(vectorEnzymeNames);
		// Header
		vectorEnzymeList.append("Vector DNA\n\n");
		StringBuilder vectorStringBuilder = new StringBuilder();
		Formatter vectorFormatter = new Formatter(vectorStringBuilder, Locale.US);
		vectorFormatter.format("%-20s", "Enzyme Name");
		vectorEnzymeList.append(vectorStringBuilder.toString());
		vectorStringBuilder.setLength(0);
		vectorFormatter.format("%-4s", "Number of Cut(s)");
		vectorEnzymeList.append(vectorStringBuilder.toString());
		vectorStringBuilder.setLength(0);
		vectorFormatter.format("%-20s", "\t\tSite(s) of Cut");
		vectorEnzymeList.append(vectorStringBuilder.toString() + "\n");
		// Data append
		for(int j = 0; j < vectorEnzymeNames.length; j++) {
			Set<Integer> locationSet = vectorEnzymeAndLocation.get(vectorEnzymeNames[j]);
			Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
			Arrays.sort(locations);
			vectorStringBuilder = new StringBuilder();
			vectorFormatter = new Formatter(vectorStringBuilder, Locale.US);
			vectorFormatter.format("%-20s", vectorEnzymeNames[j]);
			vectorEnzymeList.append("\n" + vectorStringBuilder.toString() + " |\t");
			vectorStringBuilder.setLength(0);
			vectorFormatter.format("%-4s", "  " + Integer.toString(locations.length) + " ");
			vectorEnzymeList.append(vectorStringBuilder.toString() + "\t|\t");
			for(int k = 0; k < locations.length; k++) {
				vectorStringBuilder.setLength(0);
				if(k + 1 == locations.length) {
					vectorEnzymeList.append("[" + locations[k].toString() + "]");
				}
				else {
					vectorFormatter.format("%-9s", "[" + locations[k].toString() + "],");
					vectorEnzymeList.append(vectorStringBuilder.toString());
				}
			}
		}
		vectorFormatter.close();
		vectorEnzymeList.setCaretPosition(0); // Back to top when complete
	}
}
