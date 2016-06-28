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

import org.jaewanyun.plasmidplanner.Overhang;
import org.jaewanyun.plasmidplanner.Planner;

public class OverhangListPane extends JSplitPane {

	private static JTextArea insertOverhangList;
	private static JTextArea vectorOverhangList;
	private static JScrollPane insertOverhangListPane;
	private static JScrollPane vectorOverhangListPane;
	private static JPanel insertOverhangListPanel;
	private static JPanel vectorOverhangListPanel;
	private static OverhangListPane overhangListPane;

	private OverhangListPane() {

		insertOverhangListPanel = new JPanel(new BorderLayout());
		insertOverhangListPanel.setBorder(BorderFactory.createEmptyBorder());

		insertOverhangList = new JTextArea("Digest to View");
		insertOverhangList.setEditable(true);
		insertOverhangList.setMargin(new Insets(15, 15, 15, 15));
		insertOverhangList.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		insertOverhangList.setBackground(GUIsettings.textAreaColor);
		insertOverhangListPane = new JScrollPane(insertOverhangList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		insertOverhangListPanel.add(insertOverhangListPane);

		vectorOverhangListPanel = new JPanel(new BorderLayout());
		vectorOverhangListPanel.setBorder(BorderFactory.createEmptyBorder());

		vectorOverhangList = new JTextArea("Digest to View");
		vectorOverhangList.setEditable(true);
		vectorOverhangList.setMargin(new Insets(15, 15, 15, 15));
		vectorOverhangList.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		vectorOverhangList.setBackground(GUIsettings.textAreaColor);
		vectorOverhangListPane = new JScrollPane(vectorOverhangList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		vectorOverhangListPanel.add(vectorOverhangListPane);

		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setLeftComponent(insertOverhangListPanel);
		setRightComponent(vectorOverhangListPanel);

		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(0.5);
		//		BasicSplitPaneDivider divider = (BasicSplitPaneDivider) getComponent(2);
		//		divider.setDividerSize(5);
		//		divider.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}

	static OverhangListPane getOverhangListPane() {
		return overhangListPane == null ? overhangListPane = new OverhangListPane(): overhangListPane;
	}

	static void update() {
		// Clear
		insertOverhangList.setText("");
		vectorOverhangList.setText("");

		// Insert
		HashMap<Overhang, HashSet<String>> insertOverhangAndEnzyme = Planner.getInsert().getOverhangAndEnzyme();
		Set<Overhang> insertOverhangSet = insertOverhangAndEnzyme.keySet();
		Overhang[] insertOverhangs = insertOverhangSet.toArray(new Overhang[insertOverhangSet.size()]);
		Arrays.sort(insertOverhangs);
		// Header
		insertOverhangList.append("Insert DNA\n\n");
		StringBuilder insertStringBuilder = new StringBuilder();
		Formatter insertFormatter = new Formatter(insertStringBuilder, Locale.US);
		insertFormatter.format("%-18s", "Overhang");
		insertOverhangList.append(insertStringBuilder.toString());
		insertStringBuilder.setLength(0);
		insertFormatter.format("%-4s", "Number of Enzyme(s)");
		insertOverhangList.append(insertStringBuilder.toString());
		insertStringBuilder.setLength(0);
		insertFormatter.format("%-20s", "\t\tEnzyme(s) with Overhang");
		insertOverhangList.append(insertStringBuilder.toString() + "\n");
		// Data append
		for(int j = 0; j < insertOverhangs.length; j++) {
			Set<String> insertEnzymeSet = insertOverhangAndEnzyme.get(insertOverhangs[j]);
			String[] insertEnzymes = insertEnzymeSet.toArray(new String[insertEnzymeSet.size()]);
			Arrays.sort(insertEnzymes);
			insertStringBuilder = new StringBuilder();
			insertFormatter = new Formatter(insertStringBuilder, Locale.US);
			insertFormatter.format("%-20s", insertOverhangs[j]);
			insertOverhangList.append("\n" + insertStringBuilder.toString() + " |\t");
			insertStringBuilder.setLength(0);
			insertFormatter.format("%-4s", "  " + Integer.toString(insertEnzymes.length) + " ");
			insertOverhangList.append(insertStringBuilder.toString() + "\t|\t");
			for(int k = 0; k < insertEnzymes.length; k++) {
				insertStringBuilder.setLength(0);

				if(k + 1 == insertEnzymes.length) {
					insertOverhangList.append("[" + insertEnzymes[k] + "]");
				}
				else {
					insertFormatter.format("%-15s", "[" + insertEnzymes[k].toString() + "],");
					insertOverhangList.append(insertStringBuilder.toString());
				}
			}
		}
		insertFormatter.close();
		insertOverhangList.setCaretPosition(0); // Back to top when complete


		// Vector
		HashMap<Overhang, HashSet<String>> vectorOverhangAndEnzyme = Planner.getVector().getOverhangAndEnzyme();
		Set<Overhang> vectorOverhangSet = vectorOverhangAndEnzyme.keySet();
		Overhang[] vectorOverhangs = vectorOverhangSet.toArray(new Overhang[vectorOverhangSet.size()]);
		Arrays.sort(vectorOverhangs);
		// Header
		vectorOverhangList.append("Vector DNA\n\n");
		StringBuilder vectorStringBuilder = new StringBuilder();
		Formatter vectorFormatter = new Formatter(vectorStringBuilder, Locale.US);
		vectorFormatter.format("%-19s", "Overhang");
		vectorOverhangList.append(vectorStringBuilder.toString());
		vectorStringBuilder.setLength(0);
		vectorFormatter.format("%-4s", "Number of Enzyme(s)");
		vectorOverhangList.append(vectorStringBuilder.toString());
		vectorStringBuilder.setLength(0);
		vectorFormatter.format("%-20s", "\t\tEnzyme(s) with Overhang");
		vectorOverhangList.append(vectorStringBuilder.toString() + "\n");
		// Data append
		for(int j = 0; j < vectorOverhangs.length; j++) {
			Set<String> vectorEnzymeSet = vectorOverhangAndEnzyme.get(vectorOverhangs[j]);
			String[] vectorEnzymes = vectorEnzymeSet.toArray(new String[vectorEnzymeSet.size()]);
			Arrays.sort(vectorEnzymes);
			vectorStringBuilder = new StringBuilder();
			vectorFormatter = new Formatter(vectorStringBuilder, Locale.US);
			vectorFormatter.format("%-20s", vectorOverhangs[j]);
			vectorOverhangList.append("\n" + vectorStringBuilder.toString() + " |\t");
			vectorStringBuilder.setLength(0);
			vectorFormatter.format("%-4s", "  " + Integer.toString(vectorEnzymes.length) + " ");
			vectorOverhangList.append(vectorStringBuilder.toString() + "\t|\t");
			for(int k = 0; k < vectorEnzymes.length; k++) {
				vectorStringBuilder.setLength(0);

				if(k + 1 == vectorEnzymes.length) {
					vectorOverhangList.append("[" + vectorEnzymes[k] + "]");
				}
				else {
					vectorFormatter.format("%-15s", "[" + vectorEnzymes[k].toString() + "],");
					vectorOverhangList.append(vectorStringBuilder.toString());
				}
			}
		}
		vectorFormatter.close();
		vectorOverhangList.setCaretPosition(0); // Back to top when complete
	}
}
