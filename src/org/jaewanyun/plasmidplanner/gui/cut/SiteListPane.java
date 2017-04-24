package org.jaewanyun.plasmidplanner.gui.cut;

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
import org.jaewanyun.plasmidplanner.gui.GUIsettings;

public class SiteListPane extends JSplitPane {

	private static final long serialVersionUID = 1L;
	private static JTextArea insertSiteList;
	private static JTextArea vectorSiteList;
	private static JScrollPane insertSiteListPane;
	private static JScrollPane vectorSiteListPane;
	private static JPanel insertSiteListPanel;
	private static JPanel vectorSiteListPanel;
	private static SiteListPane siteListPane;

	private SiteListPane() {

		insertSiteListPanel = new JPanel(new BorderLayout());
		insertSiteListPanel.setBorder(BorderFactory.createEmptyBorder());

		insertSiteList = new JTextArea("Run the digest to view cutting location information");
		insertSiteList.setEditable(true);
		insertSiteList.setMargin(new Insets(15, 15, 15, 15));
		insertSiteList.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		insertSiteList.setBackground(GUIsettings.textAreaColor);
		insertSiteListPane = new JScrollPane(insertSiteList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		insertSiteListPane.getVerticalScrollBar().setUnitIncrement(40);
		insertSiteListPanel.add(insertSiteListPane);

		vectorSiteListPanel = new JPanel(new BorderLayout());
		vectorSiteListPanel.setBorder(BorderFactory.createEmptyBorder());

		vectorSiteList = new JTextArea("Run the digest to view cutting location information");
		vectorSiteList.setEditable(true);
		vectorSiteList.setMargin(new Insets(15, 15, 15, 15));
		vectorSiteList.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		vectorSiteList.setBackground(GUIsettings.textAreaColor);
		vectorSiteListPane = new JScrollPane(vectorSiteList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		vectorSiteListPane.getVerticalScrollBar().setUnitIncrement(40);
		vectorSiteListPanel.add(vectorSiteListPane);

		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setContinuousLayout(true);
		setLeftComponent(insertSiteListPanel);
		setRightComponent(vectorSiteListPanel);

		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(0.5);
		//		BasicSplitPaneDivider divider = (BasicSplitPaneDivider) getComponent(2);
		//		divider.setDividerSize(5);
		//		divider.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}

	@SuppressWarnings("resource")
	public static void update() {
		// Clear
		insertSiteList.setText("");
		vectorSiteList.setText("");

		// Insert
		HashMap<Integer, HashSet<String>> insertLocationAndEnzyme = Planner.getInsert().getLocationAndEnzyme();
		Set<Integer> insertLocationSet = insertLocationAndEnzyme.keySet();
		Integer[] insertLocations = insertLocationSet.toArray(new Integer[insertLocationSet.size()]);
		Arrays.sort(insertLocations);
		// Header
		insertSiteList.append("Insert DNA\n\n");
		StringBuilder insertStringBuilder = new StringBuilder();
		Formatter insertFormatter = new Formatter(insertStringBuilder, Locale.US);
		insertFormatter.format("%-9s", "Site");
		insertSiteList.append(insertStringBuilder.toString());
		insertStringBuilder.setLength(0);
		insertFormatter.format("%-4s", "Number of Enzyme(s)");
		insertSiteList.append(insertStringBuilder.toString());
		insertStringBuilder.setLength(0);
		insertFormatter.format("%-20s", "\t\tEnzyme(s) that Cut");
		insertSiteList.append(insertStringBuilder.toString() + "\n");
		// Data append
		for(int j = 0; j < insertLocations.length; j++) {
			Set<String> insertEnzymeSet = insertLocationAndEnzyme.get(insertLocations[j]);
			String[] insertEnzymes = insertEnzymeSet.toArray(new String[insertEnzymeSet.size()]);
			Arrays.sort(insertEnzymes);
			insertStringBuilder = new StringBuilder();
			insertFormatter = new Formatter(insertStringBuilder, Locale.US);
			insertFormatter.format("%-10s", Integer.toString(insertLocations[j]));
			insertSiteList.append("\n" + insertStringBuilder.toString() + " |\t");
			insertStringBuilder.setLength(0);
			insertFormatter.format("%-4s", "  " + Integer.toString(insertEnzymes.length) + " ");
			insertSiteList.append(insertStringBuilder.toString() + "\t|\t");
			for(int k = 0; k < insertEnzymes.length; k++) {
				insertStringBuilder.setLength(0);

				if(k + 1 == insertEnzymes.length) {
					insertSiteList.append("[" + insertEnzymes[k] + "]");
				}
				else {
					insertFormatter.format("%-15s", "[" + insertEnzymes[k].toString() + "],");
					insertSiteList.append(insertStringBuilder.toString());
				}
			}
		}
		insertFormatter.close();
		insertSiteList.setCaretPosition(0); // Back to top when complete


		// Vector
		HashMap<Integer, HashSet<String>> vectorLocationAndEnzyme = Planner.getVector().getLocationAndEnzyme();
		Set<Integer> vectorLocationSet = vectorLocationAndEnzyme.keySet();
		Integer[] vectorLocations = vectorLocationSet.toArray(new Integer[vectorLocationSet.size()]);
		Arrays.sort(vectorLocations);
		// Header
		vectorSiteList.append("Vector DNA\n\n");
		StringBuilder vectorStringBuilder = new StringBuilder();
		Formatter vectorFormatter = new Formatter(vectorStringBuilder, Locale.US);
		vectorFormatter.format("%-9s", "Site");
		vectorSiteList.append(vectorStringBuilder.toString());
		vectorStringBuilder.setLength(0);
		vectorFormatter.format("%-4s", "Number of Enzyme(s)");
		vectorSiteList.append(vectorStringBuilder.toString());
		vectorStringBuilder.setLength(0);
		vectorFormatter.format("%-20s", "\t\tEnzyme(s) that Cut");
		vectorSiteList.append(vectorStringBuilder.toString() + "\n");
		// Data append
		for(int j = 0; j < vectorLocations.length; j++) {
			Set<String> vectorEnzymeSet = vectorLocationAndEnzyme.get(vectorLocations[j]);
			String[] vectorEnzymes = vectorEnzymeSet.toArray(new String[vectorEnzymeSet.size()]);
			Arrays.sort(vectorEnzymes);
			vectorStringBuilder = new StringBuilder();
			vectorFormatter = new Formatter(vectorStringBuilder, Locale.US);
			vectorFormatter.format("%-10s", Integer.toString(vectorLocations[j]));
			vectorSiteList.append("\n" + vectorStringBuilder.toString() + " |\t");
			vectorStringBuilder.setLength(0);
			vectorFormatter.format("%-4s", "  " + Integer.toString(vectorEnzymes.length) + " ");
			vectorSiteList.append(vectorStringBuilder.toString() + "\t|\t");
			for(int k = 0; k < vectorEnzymes.length; k++) {
				vectorStringBuilder.setLength(0);

				if(k + 1 == vectorEnzymes.length) {
					vectorSiteList.append("[" + vectorEnzymes[k] + "]");
				}
				else {
					vectorFormatter.format("%-15s", "[" + vectorEnzymes[k].toString() + "],");
					vectorSiteList.append(vectorStringBuilder.toString());
				}
			}
		}
		vectorFormatter.close();
		vectorSiteList.setCaretPosition(0); // Back to top when complete
	}

	static SiteListPane createSiteListPane() {
		return siteListPane == null ? siteListPane = new SiteListPane(): siteListPane;
	}
}
