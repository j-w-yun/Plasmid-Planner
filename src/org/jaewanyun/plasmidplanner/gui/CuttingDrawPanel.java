package org.jaewanyun.plasmidplanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.jaewanyun.plasmidplanner.Enzyme;
import org.jaewanyun.plasmidplanner.Overhang;
import org.jaewanyun.plasmidplanner.Planner;
import org.jaewanyun.plasmidplanner.Plasmid;

class CuttingDrawPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static CuttingDrawPanel insertCuttingDrawPanel;
	private static CuttingDrawPanel vectorCuttingDrawPanel;
	private DrawPanel drawPanel;
	private JPanel optionPanel;
	private JRadioButton oneCutter;
	private JRadioButton twoCutter;
	private JRadioButton threeCutter;
	private JRadioButton fourCutter;
	private JRadioButton fiveCutter;
	private JTextField from;
	private JTextField to;
	private JButton show;

	private CuttingDrawPanel() {
		super(new BorderLayout());

		drawPanel = new DrawPanel();
		drawPanel.setFocusable(true);

		optionPanel = new JPanel(new FlowLayout());

		// Radio buttons
		oneCutter = new JRadioButton("1 Site");
		oneCutter.addActionListener(drawPanel);
		twoCutter = new JRadioButton("2 Sites");
		twoCutter.addActionListener(drawPanel);
		threeCutter = new JRadioButton("3 Sites");
		threeCutter.addActionListener(drawPanel);
		fourCutter = new JRadioButton("4 Sites");
		fourCutter.addActionListener(drawPanel);
		fiveCutter = new JRadioButton("5 Sites");
		fiveCutter.addActionListener(drawPanel);
		ButtonGroup group = new ButtonGroup();
		oneCutter.setFocusPainted(false);
		twoCutter.setFocusPainted(false);
		threeCutter.setFocusPainted(false);
		fourCutter.setFocusPainted(false);
		fiveCutter.setFocusPainted(false);
		group.add(oneCutter);
		group.add(twoCutter);
		group.add(threeCutter);
		group.add(fourCutter);
		group.add(fiveCutter);
		optionPanel.add(oneCutter);
		optionPanel.add(twoCutter);
		optionPanel.add(threeCutter);
		optionPanel.add(fourCutter);
		optionPanel.add(fiveCutter);

		// Spacer
		JLabel space1 = new JLabel("                ");
		optionPanel.add(space1);

		// All enzymes that do not cut between button
		JLabel string1 = new JLabel("<html>Show all enzymes that<br>do not cut between</html>");
		string1.setFont(new Font(string1.getFont().getName(), Font.ITALIC, 10));
		from = new JTextField(4);
		from.setFont(new Font(string1.getFont().getName(), Font.PLAIN, 10));
		JLabel string2 = new JLabel(" and ");
		string2.setFont(new Font(string2.getFont().getName(), Font.ITALIC, 10));
		to = new JTextField(4);
		to.setFont(new Font(string1.getFont().getName(), Font.PLAIN, 10));
		show = new JButton("Show");
		show.setFont(new Font(show.getFont().getName(), Font.BOLD, 10));
		show.setBackground(GUIsettings.buttonColor);
		show.setMargin(new Insets(0, 0, 0, 0));
		show.addActionListener(drawPanel);
		optionPanel.add(string1);
		optionPanel.add(from);
		optionPanel.add(string2);
		optionPanel.add(to);
		optionPanel.add(show);


		add(optionPanel, BorderLayout.NORTH);
		add(drawPanel, BorderLayout.CENTER);

		revalidate();
		repaint();

		drawPanel.setBackground(GUIsettings.drawPanelColor);
		optionPanel.setBackground(GUIsettings.drawPanelColor);
		oneCutter.setBackground(GUIsettings.drawPanelColor);
		twoCutter.setBackground(GUIsettings.drawPanelColor);
		threeCutter.setBackground(GUIsettings.drawPanelColor);
		fourCutter.setBackground(GUIsettings.drawPanelColor);
		fiveCutter.setBackground(GUIsettings.drawPanelColor);
		setBackground(GUIsettings.drawPanelColor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	void unselectRadioButtons() {
		oneCutter.setSelected(false);
		twoCutter.setSelected(false);
		threeCutter.setSelected(false);
		fourCutter.setSelected(false);
		fiveCutter.setSelected(false);
	}

	static CuttingDrawPanel getInsertDrawPanel() {
		return insertCuttingDrawPanel == null ? insertCuttingDrawPanel = new CuttingDrawPanel(): insertCuttingDrawPanel;
	}

	static CuttingDrawPanel getVectorDrawPanel() {
		return vectorCuttingDrawPanel == null ? vectorCuttingDrawPanel = new CuttingDrawPanel(): vectorCuttingDrawPanel;
	}

	static HashMap<Integer, HashSet<String>> getInsertList(int cuts) {
		HashMap<String, HashSet<Integer>> enzymeAndLocation = Planner.getInsert().getEnzymeAndLocation();
		Set<String> enzymeSet = enzymeAndLocation.keySet();
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

		HashMap<Integer, HashSet<String>> toReturn = new HashMap<>();

		for(int j = 0; j < enzymes.length; j++) {
			if(enzymeAndLocation.get(enzymes[j]).size() == cuts) {

				Set<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);

				for(int k = 0; k < locations.length; k++) {
					if(toReturn.get(locations[k]) == null) {
						HashSet<String> enzymeSet2 = new HashSet<>();
						enzymeSet2.add(enzymes[j]);
						toReturn.put(locations[k], enzymeSet2);
					}
					else {
						HashSet<String> enzymeSet2 = toReturn.get(locations[k]);
						enzymeSet2.add(enzymes[j]);
					}
				}
			}
		}
		return toReturn;
	}

	static HashMap<Integer, HashSet<String>> getInsertList(String find) {
		HashMap<String, HashSet<Integer>> enzymeAndLocation = Planner.getInsert().getEnzymeAndLocation();
		Set<String> enzymeSet = enzymeAndLocation.keySet();
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

		HashMap<Integer, HashSet<String>> toReturn = new HashMap<>();

		for(int j = 0; j < enzymes.length; j++) {
			if(enzymes[j].equals(find) && enzymeSet.contains(find)) {

				Set<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);

				for(int k = 0; k < locations.length; k++) {
					if(toReturn.get(locations[k]) == null) {
						HashSet<String> enzymeSet2 = new HashSet<>();
						enzymeSet2.add(enzymes[j]);
						toReturn.put(locations[k], enzymeSet2);
					}
					else {
						HashSet<String> enzymeSet2 = toReturn.get(locations[k]);
						enzymeSet2.add(enzymes[j]);
					}
				}
			}
		}
		return toReturn;
	}

	static HashMap<Integer, HashSet<String>> getVectorList(String find) {
		HashMap<String, HashSet<Integer>> enzymeAndLocation = Planner.getVector().getEnzymeAndLocation();
		Set<String> enzymeSet = enzymeAndLocation.keySet();
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

		HashMap<Integer, HashSet<String>> toReturn = new HashMap<>();

		for(int j = 0; j < enzymes.length; j++) {
			if(enzymes[j].equals(find) && enzymeSet.contains(find)) {

				Set<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);

				for(int k = 0; k < locations.length; k++) {
					if(toReturn.get(locations[k]) == null) {
						HashSet<String> enzymeSet2 = new HashSet<>();
						enzymeSet2.add(enzymes[j]);
						toReturn.put(locations[k], enzymeSet2);
					}
					else {
						HashSet<String> enzymeSet2 = toReturn.get(locations[k]);
						enzymeSet2.add(enzymes[j]);
					}
				}
			}
		}
		return toReturn;
	}

	static HashMap<Integer, HashSet<String>> getVectorList(int cuts) {
		HashMap<String, HashSet<Integer>> enzymeAndLocation = Planner.getVector().getEnzymeAndLocation();
		Set<String> enzymeSet = enzymeAndLocation.keySet();
		String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

		HashMap<Integer, HashSet<String>> toReturn = new HashMap<>();

		for(int j = 0; j < enzymes.length; j++) {
			if(enzymeAndLocation.get(enzymes[j]).size() == cuts) {

				Set<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);

				for(int k = 0; k < locations.length; k++) {
					if(toReturn.get(locations[k]) == null) {
						HashSet<String> enzymeSet2 = new HashSet<>();
						enzymeSet2.add(enzymes[j]);
						toReturn.put(locations[k], enzymeSet2);
					}
					else {
						HashSet<String> enzymeSet2 = toReturn.get(locations[k]);
						enzymeSet2.add(enzymes[j]);
					}
				}
			}
		}
		return toReturn;
	}

	static HashMap<String, HashSet<Integer>> getInsertEnzymeAndLocation() {
		return Planner.getInsert().getEnzymeAndLocation();
	}

	static HashMap<String, HashSet<Integer>> getVectorEnzymeAndLocation() {
		return Planner.getVector().getEnzymeAndLocation();
	}

	static HashMap<String, HashSet<Overhang>> getInsertEnzymeAndOverhang() {
		return Planner.getInsert().getEnzymeAndOverhang();
	}

	static HashMap<String, HashSet<Overhang>> getVectorEnzymeAndOverhang() {
		return Planner.getVector().getEnzymeAndOverhang();
	}

	static HashMap<String, Overhang> getInsertEnzymeAndLocationAndOverhang() {
		return Planner.getInsert().getEnzymeAndLocationAndOverhang();
	}

	static HashMap<String, Overhang> getVectorEnzymeAndLocationAndOverhang() {
		return Planner.getVector().getEnzymeAndLocationAndOverhang();
	}

	static void resized() {
		insertCuttingDrawPanel.drawPanel.resized();
		vectorCuttingDrawPanel.drawPanel.resized();
	}

	static void update() {
		insertCuttingDrawPanel.drawPanel.setLength(Planner.getInsert().getSequence().length());
		vectorCuttingDrawPanel.drawPanel.setLength(Planner.getVector().getSequence().length());

		// Unselect JRadioButtons
		insertCuttingDrawPanel.oneCutter.setSelected(false);
		insertCuttingDrawPanel.twoCutter.setSelected(false);
		insertCuttingDrawPanel.threeCutter.setSelected(false);
		insertCuttingDrawPanel.fourCutter.setSelected(false);
		insertCuttingDrawPanel.fiveCutter.setSelected(false);
		vectorCuttingDrawPanel.oneCutter.setSelected(false);
		vectorCuttingDrawPanel.twoCutter.setSelected(false);
		vectorCuttingDrawPanel.threeCutter.setSelected(false);
		vectorCuttingDrawPanel.fourCutter.setSelected(false);
		vectorCuttingDrawPanel.fiveCutter.setSelected(false);

		insertCuttingDrawPanel.drawPanel.oneCutterList = getInsertList(1);
		insertCuttingDrawPanel.drawPanel.twoCutterList = getInsertList(2);
		insertCuttingDrawPanel.drawPanel.threeCutterList = getInsertList(3);
		insertCuttingDrawPanel.drawPanel.fourCutterList = getInsertList(4);
		insertCuttingDrawPanel.drawPanel.fiveCutterList = getInsertList(5);
		insertCuttingDrawPanel.drawPanel.thisPlasmid = Planner.getInsert();
		insertCuttingDrawPanel.drawPanel.otherPlasmid = Planner.getVector();

		vectorCuttingDrawPanel.drawPanel.oneCutterList = getVectorList(1);
		vectorCuttingDrawPanel.drawPanel.twoCutterList = getVectorList(2);
		vectorCuttingDrawPanel.drawPanel.threeCutterList = getVectorList(3);
		vectorCuttingDrawPanel.drawPanel.fourCutterList = getVectorList(4);
		vectorCuttingDrawPanel.drawPanel.fiveCutterList = getVectorList(5);
		vectorCuttingDrawPanel.drawPanel.thisPlasmid = Planner.getVector();
		vectorCuttingDrawPanel.drawPanel.otherPlasmid = Planner.getInsert();

		vectorCuttingDrawPanel.drawPanel.revalidate();
		vectorCuttingDrawPanel.drawPanel.repaint();
		insertCuttingDrawPanel.drawPanel.revalidate();
		insertCuttingDrawPanel.drawPanel.repaint();
	}

	static void update(String find, boolean insert) {
		if(insert)
			insertCuttingDrawPanel.drawPanel.displayThis = getInsertList(find);
		else
			vectorCuttingDrawPanel.drawPanel.displayThis = getVectorList(find);
	}

	private class DrawPanel extends JPanel implements ActionListener {

		private static final long serialVersionUID = 1L;
		private int width;
		private int height;
		private String sequenceLength;
		private HashMap<Integer, HashSet<String>> oneCutterList;
		private HashMap<Integer, HashSet<String>> twoCutterList;
		private HashMap<Integer, HashSet<String>> threeCutterList;
		private HashMap<Integer, HashSet<String>> fourCutterList;
		private HashMap<Integer, HashSet<String>> fiveCutterList;
		private HashMap<Integer, HashSet<String>> displayThis;
		private String enzymeDescription;
		private String boldThisEnzyme;
		private ArrayList<JButton> buttons;
		private Plasmid thisPlasmid;
		private Plasmid otherPlasmid;
		private ArrayList<String> analyzedToDisplay;
		private int highlightFrom;
		private int highlightTo;
		private ArrayList<JButton> analyzedButtons;

		DrawPanel() {
			super(null);
			sequenceLength = "0";
			buttons = new ArrayList<>();
			enzymeDescription = "";
			analyzedToDisplay = new ArrayList<>();
			analyzedButtons = new ArrayList<>();
		}

		void setLength(int length) {
			sequenceLength = Integer.toString(length);
		}

		private HashMap<Integer, HashSet<String>> findValid(int start, int end) {
			//			HashMap<Integer, HashSet<Overhang>> locationAndOverhang = thisPlasmid.getLocationAndOverhang();
			HashMap<Integer, HashSet<String>> locationAndEnzymeValid = new HashMap<>();
			HashMap<String, HashSet<Integer>> enzymeAndLocation = thisPlasmid.getEnzymeAndLocation();
			Set<String> enzymeSet = enzymeAndLocation.keySet();
			String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);
			// Vector validity test
			for(int j = 0; j < enzymes.length; j++) {
				HashSet<Integer> locationSet = enzymeAndLocation.get(enzymes[j]);
				int cursor = start;
				boolean valid = true;
				// Run through each location from that enzyme
				while(cursor != end) {
					if(cursor == thisPlasmid.getLength())
						cursor = 0;
					if(locationSet.contains(cursor)) {
						valid = false;
						break;
					}
					cursor++;
				}
				// If valid, enter into hashmap
				if(valid) {
					Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
					for(int k = 0; k < locations.length; k++) {
						if(locationAndEnzymeValid.containsKey(locations[k])) {
							Set<String> enzymeSetValid = locationAndEnzymeValid.get(locations[k]);
							enzymeSetValid.add(enzymes[j]);
						}
						else {
							HashSet<String> enzymeSetValid = new HashSet<>();
							enzymeSetValid.add(enzymes[j]);
							locationAndEnzymeValid.put(locations[k], enzymeSetValid);
						}
					}
				}
			}
			return locationAndEnzymeValid;
		}

		void resized() {
			// Width and height should be the same for both panels
			width = getWidth();
			height = getHeight();

			buttons.clear();
			drawPanel.removeAll();

			/*
			 * Add center bottom buttons
			 */
			int heightOffset2 = 10;
			int widthOffset2 = 0;
			for(int n = 0; n < analyzedButtons.size(); n++) {
				analyzedButtons.get(n).setSize(50, 12);
				analyzedButtons.get(n).setLocation(width / 3 + widthOffset2, height - 113 + heightOffset2);
				analyzedButtons.get(n).setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 9));
				analyzedButtons.get(n).setMargin(new Insets(0, 0, 0, 0));
				analyzedButtons.get(n).setBackground(GUIsettings.buttonColor);
				analyzedButtons.get(n).setForeground(GUIsettings.buttonTextColor);
				analyzedButtons.get(n).setFocusPainted(false);
				add(analyzedButtons.get(n));
				heightOffset2 += 10;
				if(heightOffset2 >= 80) {
					heightOffset2 = 0;
					widthOffset2 += 120;
				}
			}

			revalidate();
			repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 10));

			/*
			 * Draw a line of plasmid
			 */
			int lineStart = 100;
			int lineEnd = width - 100;
			int lineTotal = lineEnd - lineStart;
			int lineLocationHeight = height / 8;
			g.drawLine(lineStart, lineLocationHeight, lineEnd, lineLocationHeight);

			/*
			 * Draw a text of size of plasmid
			 */
			g.drawString("0", 60, lineLocationHeight);
			// Centering string to the point
			g.drawString(sequenceLength, (width - 60) - g.getFontMetrics().stringWidth(sequenceLength) / 2, lineLocationHeight);

			/*
			 * Draw tick marks as a tip of location
			 */
			int numTickMarks = 10;
			boolean alternate = false;
			for(int j = 0; j < numTickMarks; j++) {
				int location = (Integer.parseInt(sequenceLength) / (numTickMarks + 1)) * (j + 1);
				int drawLocation = (lineTotal / (numTickMarks + 1)) * (j + 1) + lineStart;
				// Alternate length of tick marks
				if(alternate) {
					g.drawLine(drawLocation, lineLocationHeight, drawLocation, lineLocationHeight - 5);
					g.drawString(Integer.toString(location), drawLocation, lineLocationHeight - 7);
				}
				else {
					g.drawLine(drawLocation, lineLocationHeight, drawLocation, lineLocationHeight - 15);
					g.drawString(Integer.toString(location), drawLocation, lineLocationHeight - 17);
				}
				alternate = !alternate;
			}

			/*
			 * Draw highlight of line from JTextfield
			 */
			double abstractLocationStart =  (double) highlightFrom / Integer.parseInt(sequenceLength);
			int relativeLocationStart = (int) (abstractLocationStart * lineTotal) + lineStart;
			double abstractLocationEnd =  (double) highlightTo / Integer.parseInt(sequenceLength);
			int relativeLocationEnd = (int) (abstractLocationEnd * lineTotal) + lineStart;
			if(abstractLocationEnd > abstractLocationStart) {
				g.setColor(GUIsettings.buttonHighlightColor);
				g.drawLine(relativeLocationStart, lineLocationHeight + 1, relativeLocationEnd, lineLocationHeight + 1);
				g.drawLine(relativeLocationStart, lineLocationHeight + 0, relativeLocationEnd, lineLocationHeight + 0);
				g.drawLine(relativeLocationStart, lineLocationHeight - 1, relativeLocationEnd, lineLocationHeight - 1);
				g.setColor(Color.BLACK);
			}
			else if(abstractLocationEnd < abstractLocationStart) {
				g.setColor(GUIsettings.buttonHighlightColor);
				g.drawLine(relativeLocationStart, lineLocationHeight + 1, lineTotal + lineStart, lineLocationHeight + 1);
				g.drawLine(relativeLocationStart, lineLocationHeight + 0, lineTotal + lineStart, lineLocationHeight + 0);
				g.drawLine(relativeLocationStart, lineLocationHeight - 1, lineTotal + lineStart, lineLocationHeight - 1);
				g.drawLine(lineStart, lineLocationHeight + 1, relativeLocationEnd, lineLocationHeight + 1);
				g.drawLine(lineStart, lineLocationHeight + 0, relativeLocationEnd, lineLocationHeight + 0);
				g.drawLine(lineStart, lineLocationHeight - 1, relativeLocationEnd, lineLocationHeight - 1);
				g.setColor(Color.BLACK);
			}

			/*
			 * Draw legend
			 */
			g.setColor(Color.BLUE); // 5 prime
			g.fillOval(width - 134, height - 64, 8, 8);
			g.drawLine(width - 130, height - 61, width - 110, height - 61);
			g.drawLine(width - 130, height - 60, width - 110, height - 60);
			//			g.drawLine(width - 130, height - 59, width - 110, height - 59);
			g.setColor(Color.BLACK);
			g.drawString("= 5' Overhang", width - 100, height - 57);
			g.setColor(Color.GREEN); // 3 prime
			g.fillOval(width - 134, height - 52, 8, 8);
			g.drawLine(width - 130, height - 49, width - 110, height - 49);
			g.drawLine(width - 130, height - 48, width - 110, height - 48);
			//			g.drawLine(width - 130, height - 47, width - 110, height - 47);
			g.setColor(Color.BLACK);
			g.drawString("= 3' Overhang", width - 100, height - 45);
			g.setColor(Color.RED); // Blunt
			g.fillOval(width - 134, height - 40, 8, 8);
			g.drawLine(width - 130, height - 37, width - 110, height - 37);
			g.drawLine(width - 130, height - 36, width - 110, height - 36);
			//			g.drawLine(width - 130, height - 35, width - 110, height - 35);
			g.setColor(Color.BLACK);
			g.drawString("= Blunt Overhang", width - 100, height - 33);
			g.setColor(Color.BLACK);

			/*
			 * Main display of enzymes and their information
			 */
			if(displayThis != null) {

				Set<Integer> locationSet = displayThis.keySet();

				//				if(locationSet.size() > 50) {
				//					oneCutter.setSelected(false);
				//					twoCutter.setSelected(false);
				//					threeCutter.setSelected(false);
				//					fourCutter.setSelected(false);
				//					fiveCutter.setSelected(false);
				//					displayThis = null;
				//					return;
				//				}

				Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
				Arrays.sort(locations);

				// Backup of last locations for overlap calculation
				int lastLocation = 0;
				int heightOffset = 0;

				for(int j = locations.length - 1; j >= 0; j--) {

					/*
					 * Calculate relative distances needed for drawing everything
					 */
					double abstractLocation =  (double) locations[j] / Integer.parseInt(sequenceLength);
					int relativeLocation = (int) (abstractLocation * lineTotal) + lineStart;

					/*
					 * Get all enzymes stored in displayThis that was previously set to display what is stored within it
					 */
					Set<String> enzymeSet = displayThis.get(locations[j]);
					String[] enzymes = enzymeSet.toArray(new String[enzymeSet.size()]);

					for(int k = 0; k < enzymes.length; k++) {

						/*
						 * Overlap calculations
						 */
						// If button goes below viewable screen, reset offset
						if(relativeLocation + 60 < lastLocation || lineLocationHeight + 170 + heightOffset > height)
							heightOffset = 0; // If no overlap, reset offset to zero
						else
							heightOffset += 3;
						lastLocation = relativeLocation;


						/*
						 * Tool tip display
						 */
						// Set locationValues for bottom info display and String for the tool tip text
						HashMap<String, HashSet<Overhang>> enzymeAndOverhang = thisPlasmid.getEnzymeAndOverhang();
						HashMap<String, HashSet<Integer>> enzymeAndLocation = thisPlasmid.getEnzymeAndLocation();
						HashMap<String, Overhang> enzymeAndLocationAndOverhang = thisPlasmid.getEnzymeAndLocationAndOverhang();
						Overhang currentOverhang = enzymeAndLocationAndOverhang.get(enzymes[k] + Integer.toString(locations[j]));

						String toolTipString = "<html>Current location: " + locations[j] +
								"<br>Current overhang: " + currentOverhang +
								"<br><br>Total list of cuts:";
						Set<Integer> locationValueSet = enzymeAndLocation.get(enzymes[k]);
						Integer[] locationValues = locationValueSet.toArray(new Integer[locationValueSet.size()]);
						Arrays.sort(locationValues);

						int iterate = locationValues.length;
						if(locationValues.length > 10)
							iterate = 10;
						for(int n = 0; n < iterate; n++) {
							toolTipString += "<br>" + locationValues[n];
						}
						if(locationValues.length > 10)
							toolTipString += "<br>[...]";

						// Set overhangs for bottom info display and String for the tool tip text
						Set<Overhang> overhangSet = enzymeAndOverhang.get(enzymes[k]);
						Overhang[] overhangs = overhangSet.toArray(new Overhang[overhangSet.size()]);
						Arrays.sort(overhangs);
						toolTipString += "<br><br>Total list of overhangs:";

						iterate = overhangs.length;
						if(overhangs.length > 10)
							iterate = 10;
						for(int n = 0; n < iterate; n++) {
							toolTipString += "<br>" + overhangs[n];
						}
						if(overhangs.length > 10)
							toolTipString += "<br>[...]";
						toolTipString += "</html>";


						/*
						 * All line / oval drawing calls
						 */
						// Draw enzyme cut sites and enzyme name
						g.setColor(GUIsettings.enzymeCutDrawLineColor);
						g.drawLine(relativeLocation, lineLocationHeight - 0, relativeLocation, lineLocationHeight + 10 + heightOffset); // Vertical Line
						g.drawLine(relativeLocation, lineLocationHeight + 10 + heightOffset, relativeLocation + 2, lineLocationHeight + 10 + heightOffset); // Horizontal Line
						// Check if the line(s) from this enzyme need to be bold due to user hovering on the button
						if(enzymes[k].equals(boldThisEnzyme)) {
							if(currentOverhang.isFivePrime())
								g.setColor(Color.BLUE);
							else if(currentOverhang.isThreePrime())
								g.setColor(Color.GREEN);
							else if(currentOverhang.isBlunt())
								g.setColor(Color.RED);
							g.drawLine(relativeLocation, lineLocationHeight - 0, relativeLocation, lineLocationHeight + 10 + heightOffset); // Vertical Line
							g.drawLine(relativeLocation, lineLocationHeight + 10 + heightOffset, relativeLocation + 2, lineLocationHeight + 10 + heightOffset); // Horizontal Line
							g.drawLine(relativeLocation + 1, lineLocationHeight - 0, relativeLocation + 1, lineLocationHeight + 10 + heightOffset); // Vertical Line
							g.drawLine(relativeLocation, lineLocationHeight + 11 + heightOffset, relativeLocation + 2, lineLocationHeight + 11 + heightOffset); // Horizontal Line
							g.fillOval(relativeLocation - 4, lineLocationHeight - 4, 8, 8); // Circle indicating the cut site on the DNA
							g.setColor(GUIsettings.enzymeCutDrawLineColor);
						}


						/*
						 * Enzyme buttons
						 */
						buttons.clear();
						JButton enzymeButton = new JButton(enzymes[k]);
						enzymeButton.addActionListener(this);
						enzymeButton.setToolTipText(toolTipString);
						enzymeButton.setFocusPainted(false);
						enzymeButton.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseEntered(MouseEvent e) {
								new SwingWorker<Void, Void>() {
									@Override
									protected Void doInBackground() throws Exception {
										JButton pressed = (JButton) e.getSource();
										String pressedEnzyme = pressed.getText();
										for(int j = 0; j < buttons.size(); j++) {
											buttons.get(j).setBackground(GUIsettings.buttonColor);
											buttons.get(j).setForeground(GUIsettings.buttonTextColor);
											if(buttons.get(j).getText().equals(pressedEnzyme)) {
												// Button colors
												buttons.get(j).setBackground(GUIsettings.buttonHighlightColor);
												buttons.get(j).setForeground(GUIsettings.buttonHightlightTextColor);
											}
										}
										// Get data for bottom info display
										Enzyme[] enzymes = Planner.getEnzymes();
										for(int j = 0; j < enzymes.length; j++) {
											if(enzymes[j].getEnzymeName().equals(pressedEnzyme)) {
												enzymeDescription = enzymes[j].toString();
											}
										}
										// Bold the line originating from every site of this enzyme
										boldThisEnzyme = pressedEnzyme;

										revalidate();
										repaint();
										return null;
									}
								}.execute();
							}
						});
						enzymeButton.setSize(50, 14);
						enzymeButton.setLocation(relativeLocation + 4, lineLocationHeight + 3 + heightOffset);
						enzymeButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 9));
						enzymeButton.setMargin(new Insets(0, 0, 0, 0));
						enzymeButton.setBackground(GUIsettings.buttonColor);
						enzymeButton.setForeground(GUIsettings.buttonTextColor);
						enzymeButton.setFocusPainted(false);
						// Add buttons
						boolean duplicateExists = false;
						for(int n = 0; n < buttons.size(); n++) {
							if(buttons.get(n).getLocation().equals(enzymeButton.getLocation()))
								duplicateExists = true;
						}
						if(!duplicateExists) {
							add(enzymeButton);
							buttons.add(enzymeButton);
						}
						// Overlap prevention
						heightOffset += 12;


						/*
						 * Write text to bottom left info area
						 */
						if(enzymeDescription.length() > 0) {
							String[] enzymeData = enzymeDescription.split("\t");
							g.setColor(Color.RED);
							g.drawString(enzymeData[0], 5, height - 123); // Name
							g.drawString(enzymeData[2], 5, height - 113); // Sequence
							g.setColor(Color.BLACK);

							if(enzymeData[4].contains("100%"))
								g.setColor(Color.GREEN);
							g.drawString(enzymeData[4], 5, height - 103); // 1.1
							g.setColor(Color.BLACK);

							if(enzymeData[5].contains("100%"))
								g.setColor(Color.GREEN);
							g.drawString(enzymeData[5], 5, height - 93); // 2.1
							g.setColor(Color.BLACK);

							if(enzymeData[6].contains("100%"))
								g.setColor(Color.GREEN);
							g.drawString(enzymeData[6], 5, height - 83); // 3.1
							g.setColor(Color.BLACK);

							if(enzymeData[7].contains("100%"))
								g.setColor(Color.GREEN);
							g.drawString(enzymeData[7], 5, height - 73); // CutSmart
							g.setColor(Color.BLACK);

							g.drawString(enzymeData[8], 5, height - 63); // Heat inactivation
							g.drawString(enzymeData[9], 5, height - 53); // Incubation temperature
							//							g.drawString(enzymeData[10], 5, height - 10); // Diluent
							g.drawString(enzymeData[3], 5, height - 43); // Supplied buffer

							g.setColor(Color.RED);
							g.drawString(enzymeData[1], 5, height - 33); // Description
							g.setColor(Color.BLACK);
						}

						/*
						 * Write text to bottom center plasmid comparison area
						 */
						if(analyzedToDisplay.size() > 0) {
							g.setColor(Color.RED);
							g.drawString("Compatible enzymes on other plasmid ", width / 4, height - 123);
						}
						int heightOffset2 = 0;
						int widthOffset2 = 0;
						for(int n = 0; n < analyzedToDisplay.size(); n++) {
							String display = analyzedToDisplay.get(n);
							if(n > 0)
								g.setColor(Color.BLACK);
							if(n == 0)
								g.drawString(display, width / 4, height - 113 + heightOffset2);
							else
								g.drawString(display, width / 4 + widthOffset2 + 50, height - 103 + heightOffset2);
							heightOffset2 += 10;
							if(heightOffset2 >= 80) {
								heightOffset2 = 0;
								widthOffset2 += 100;
							}
						}


					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() instanceof JRadioButton) {

				highlightFrom = 0;
				highlightTo = 0;
				analyzedButtons.clear();
				analyzedToDisplay.clear();
				drawPanel.removeAll();
				buttons.clear();

				if(((JRadioButton) e.getSource()).getText().equals("1 Site")) {
					displayThis = oneCutterList;
				}
				else if(((JRadioButton) e.getSource()).getText().equals("2 Sites")) {
					displayThis = twoCutterList;
				}
				else if(((JRadioButton) e.getSource()).getText().equals("3 Sites")) {
					displayThis = threeCutterList;
				}
				else if(((JRadioButton) e.getSource()).getText().equals("4 Sites")) {
					displayThis = fourCutterList;
				}
				else if(((JRadioButton) e.getSource()).getText().equals("5 Sites")) {
					displayThis = fiveCutterList;
				}

				revalidate();
				repaint();
			}

			if(e.getSource() == show) {
				unselectRadioButtons();

				int start = -1;
				int end = -1;

				try {
					start = Integer.parseInt(from.getText());
					end = Integer.parseInt(to.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
							null,
							"The input numbers are not valid.\n"
									+ "Please try again.",
									"Invalid inputs",
									0);
					return;
				}
				try {
					if(start >= 0 && end < thisPlasmid.getLength())  {
						displayThis = findValid(start, end);

						// Line highlight
						highlightFrom = start;
						highlightTo = end;
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
							null,
							"Error while trying to find enzymes.\n"
									+ "Please check digest.",
									"Invalid state",
									0);
					return;
				}

				buttons.clear();
				analyzedToDisplay.clear();
				drawPanel.removeAll();


				revalidate();
				repaint();

				return;
			}

			if(e.getSource() instanceof JButton) {
				/*
				 * Retrieve hints on the button's enzyme name and location and get its overhang
				 */
				StringBuilder sb = new StringBuilder();
				int start = 24;
				char cursor = ((JButton) e.getSource()).getToolTipText().charAt(start);
				while(cursor != '<') {
					sb.append(cursor);
					cursor = ((JButton) e.getSource()).getToolTipText().charAt(++start);
				}
				String enzymeName = ((JButton) e.getSource()).getText();
				HashMap<String, Overhang> enzymeAndLocationAndOverhang = thisPlasmid.getEnzymeAndLocationAndOverhang();
				Overhang overhang = enzymeAndLocationAndOverhang.get(enzymeName + sb.toString());
				Overhang target = overhang.getComplement();

				/*
				 * Compare with other plasmid's overhangs and collect all those that are compatible
				 */
				HashSet<String> compatibleEnzymeSet = null;
				HashMap<Overhang, HashSet<String>> otherOverhangAndEnzyme = otherPlasmid.getOverhangAndEnzyme();
				Set<Overhang> overhangSet = otherOverhangAndEnzyme.keySet();
				Overhang[] overhangs = overhangSet.toArray(new Overhang[overhangSet.size()]);
				for(int j = 0; j < overhangs.length; j++) {
					if(target.equals(overhangs[j])) {
						compatibleEnzymeSet = otherOverhangAndEnzyme.get(overhangs[j]);
						break; // Only one complement so stop when a complement is found
					}
				}
				// Clear center bottom buttons
				for(int j = 0; j < analyzedButtons.size(); j++) {
					drawPanel.remove(analyzedButtons.get(j));
				}

				/*
				 * Store in a wider scoped field so that the drawer can draw this
				 */
				analyzedToDisplay.clear();
				analyzedButtons.clear();
				if(compatibleEnzymeSet != null) {
					analyzedToDisplay.add("For " + ((JButton) e.getSource()).getText());
					HashMap<String, HashSet<Integer>> otherEnzymeAndLocation = otherPlasmid.getEnzymeAndLocation();
					String[] compatibleEnzymes = compatibleEnzymeSet.toArray(new String[compatibleEnzymeSet.size()]);
					Arrays.sort(compatibleEnzymes);
					for(int j = 0; j < compatibleEnzymes.length; j++) {
						//						analyzedToDisplay.add(compatibleEnzymes[j] + " : " + Integer.toString(otherEnzymeAndLocation.get(compatibleEnzymes[j]).size()));
						analyzedToDisplay.add(" : " + Integer.toString(otherEnzymeAndLocation.get(compatibleEnzymes[j]).size()));
						JButton button = new JButton(compatibleEnzymes[j]);
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								CuttingDrawPanel other = TabbedCuttingPane.getNotSelectedDrawPanel();

								HashMap<Integer, HashSet<String>> selectedEnzyme = new HashMap<>();
								HashMap<String, HashSet<Integer>> enzymeAndLocation = other.drawPanel.thisPlasmid.getEnzymeAndLocation();
								Set<Integer> locationSet = enzymeAndLocation.get(button.getText());
								Integer[] locations = locationSet.toArray(new Integer[locationSet.size()]);
								for(int j = 0; j < locations.length; j++) {
									HashSet<String> temp = new HashSet<>();
									temp.add(button.getText());
									selectedEnzyme.put(locations[j], temp);
								}
								other.drawPanel.displayThis = selectedEnzyme;
								TabbedCuttingPane.setSelected(other);
								other.drawPanel.removeAll();
								other.drawPanel.analyzedToDisplay.clear();

								other.drawPanel.highlightFrom = 0;
								other.drawPanel.highlightTo = 0;

								unselectRadioButtons();
							}
						});
						analyzedButtons.add(button);

						/*
						 * Add center bottom buttons calculated above
						 */
						int heightOffset2 = 10;
						int widthOffset2 = 0;
						for(int n = 0; n < analyzedButtons.size(); n++) {
							analyzedButtons.get(n).setSize(55, 12);
							analyzedButtons.get(n).setLocation(width / 4 + widthOffset2, height - 113 + heightOffset2);
							analyzedButtons.get(n).setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 9));
							analyzedButtons.get(n).setMargin(new Insets(0, 0, 0, 0));
							analyzedButtons.get(n).setBackground(GUIsettings.buttonHighlightColor);
							analyzedButtons.get(n).setForeground(GUIsettings.buttonHightlightTextColor);
							analyzedButtons.get(n).setFocusPainted(false);
							add(analyzedButtons.get(n));
							heightOffset2 += 10;
							if(heightOffset2 >= 80) {
								heightOffset2 = 0;
								widthOffset2 += 100;
							}
						}
					}
				}
				else {
					analyzedToDisplay.add("None for " + ((JButton) e.getSource()).getText());
				}
			}

			revalidate();
			repaint();
		}
	}
}