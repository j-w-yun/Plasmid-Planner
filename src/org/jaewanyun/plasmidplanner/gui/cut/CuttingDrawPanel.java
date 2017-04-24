package org.jaewanyun.plasmidplanner.gui.cut;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

import org.jaewanyun.plasmidplanner.Enzyme;
import org.jaewanyun.plasmidplanner.Overhang;
import org.jaewanyun.plasmidplanner.Planner;
import org.jaewanyun.plasmidplanner.Plasmid;
import org.jaewanyun.plasmidplanner.gui.GUIsettings;


// TODO: Clean up
public class CuttingDrawPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private DrawPanel drawPanel;
	private JPanel optionPanel;
	private JPanel enzymeSidePanel;
	private JScrollPane enzymeScrollPane;
	private JRadioButton oneCutter;
	private JRadioButton twoCutter;
	private JRadioButton threeCutter;
	private JRadioButton fourCutter;
	private JRadioButton fiveCutter;
	private ArrayList<JButton> selectedEnzymes;
	private ArrayList<JButton> unselectedEnzymes;
	private JLabel selectedEnzymesLabel;
	private JLabel unselectedEnzymesLabel;
	private HashMap<Integer, HashSet<String>> locationAndEnzyme;
	private JTextField from;
	private JTextField to;
	private JButton show;
	private static CuttingDrawPanel insertCuttingDrawPanel;
	private static CuttingDrawPanel vectorCuttingDrawPanel;

	private CuttingDrawPanel() {
		super(new BorderLayout());

		// Panel that draws and adds all enzyme information and enzyme buttons
		drawPanel = new DrawPanel();
		drawPanel.setFocusable(true);

		// Panel with radio buttons and range of enzymes show components
		optionPanel = new JPanel(new FlowLayout());

		// Panel with a list of enzymes for custom view
		enzymeSidePanel = new JPanel(new GridLayout(0, 1));

		// Search bar
		JTextField searchBar = new JTextField(10);
		searchBar.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				String searchThis = searchBar.getText();
				for(int j = 0; j < unselectedEnzymes.size(); j++)
					unselectedEnzymes.get(j).setBackground(GUIsettings.buttonColor);
				for(int j = 0; j < unselectedEnzymes.size(); j++) {
					if(unselectedEnzymes.get(j).getText().toLowerCase().indexOf(searchThis.toLowerCase()) == 0 && searchThis.length() > 0) {
						unselectedEnzymes.get(j).setBackground(Color.YELLOW);
						try {
							unselectedEnzymes.get(j - 3).scrollRectToVisible(enzymeScrollPane.getBounds());
						} catch (Exception ex) {}
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		optionPanel.add(new JLabel("Search enzyme"));
		optionPanel.add(searchBar);
		JLabel space1 = new JLabel("                     ");
		optionPanel.add(space1);

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
		JLabel space2 = new JLabel("                     ");
		optionPanel.add(space2);

		// All enzymes that do not cut between button
		JLabel string1 = new JLabel("<html>Show all enzymes that<br>do not cut between</html>");
		string1.setFont(new Font(string1.getFont().getName(), Font.ITALIC, 10));
		from = new JTextField(7);
		from.setFont(new Font(from.getFont().getName(), Font.PLAIN, 10));
		JLabel string2 = new JLabel(" and ");
		string2.setFont(new Font(string2.getFont().getName(), Font.ITALIC, 10));
		to = new JTextField(7);
		to.setFont(new Font(to.getFont().getName(), Font.PLAIN, 10));
		show = new JButton("Show");
		show.setFont(new Font(show.getFont().getName(), Font.BOLD, 10));
		show.setMargin(new Insets(0, 0, 0, 0));
		show.addActionListener(drawPanel);
		optionPanel.add(string1);
		optionPanel.add(from);
		optionPanel.add(string2);
		optionPanel.add(to);
		optionPanel.add(show);

		// Enzyme list
		selectedEnzymesLabel = new JLabel("  Selected Enzymes ");
		selectedEnzymesLabel.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 10));
		selectedEnzymesLabel.setBackground(GUIsettings.buttonHighlightColor);
		selectedEnzymesLabel.setForeground(GUIsettings.buttonHighlightTextColor);
		selectedEnzymesLabel.setOpaque(true);

		unselectedEnzymesLabel = new JLabel(" Unselected Enzymes ");
		unselectedEnzymesLabel.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 10));
		unselectedEnzymesLabel.setBackground(GUIsettings.buttonColor);
		unselectedEnzymesLabel.setForeground(GUIsettings.buttonTextColor);
		unselectedEnzymesLabel.setOpaque(true);

		Enzyme[] enzymes = Enzyme.importFromFile(getClass().getResourceAsStream("/enzymes/enzymes.txt"));

		selectedEnzymes = new ArrayList<>();
		unselectedEnzymes = new ArrayList<>();

		locationAndEnzyme = new HashMap<>();
		for(int j = 0; j < enzymes.length; j++) {
			JButton enzymeSidePanelButton = new JButton(enzymes[j].getEnzymeName());
			enzymeSidePanelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					drawPanel.highlightFrom = 0;
					drawPanel.highlightTo = 0;
					drawPanel.analyzedButtons.clear();
					drawPanel.analyzedToDisplay.clear();
					drawPanel.removeAll();
					drawPanel.buttons.clear();
					locationAndEnzyme.clear();

					if(unselectedEnzymes.contains(enzymeSidePanelButton)) {
						// Move to selected before removing from unselected
						selectedEnzymes.add(enzymeSidePanelButton);
						unselectedEnzymes.remove(enzymeSidePanelButton);
						enzymeSidePanelButton.setBackground(GUIsettings.buttonHighlightColor);
						enzymeSidePanelButton.setForeground(GUIsettings.buttonHighlightTextColor);
					} else {
						unselectedEnzymes.add(enzymeSidePanelButton);
						unselectedEnzymes.sort(new Comparator<JButton>() {
							@Override
							public int compare(JButton one, JButton two) {
								return one.getText().compareTo(two.getText());
							}
						});
						selectedEnzymes.remove(enzymeSidePanelButton);
						enzymeSidePanelButton.setBackground(GUIsettings.buttonColor);
						enzymeSidePanelButton.setForeground(GUIsettings.buttonTextColor);
					}

					// Add to enzymeSidePanelButton list panel after clearing it
					enzymeSidePanel.removeAll();
					enzymeSidePanel.add(selectedEnzymesLabel);
					for(int k = 0; k < selectedEnzymes.size(); k++) {
						enzymeSidePanel.add(selectedEnzymes.get(k));
						try {
							locationAndEnzyme.putAll(Planner.getList(drawPanel.thisPlasmid, selectedEnzymes.get(k).getText()));
						} catch (Exception ex) {}
					}
					enzymeSidePanel.add(unselectedEnzymesLabel);
					for(int k = 0; k < unselectedEnzymes.size(); k++) {
						enzymeSidePanel.add(unselectedEnzymes.get(k));
					}

					enzymeSidePanel.revalidate();
					enzymeSidePanel.repaint();

					drawPanel.displayThis = locationAndEnzyme;

					drawPanel.revalidate();
					drawPanel.repaint();
				}
			});
			enzymeSidePanelButton.setMargin(new Insets(0, 0, 0, 0));
			enzymeSidePanelButton.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC, 10));
			enzymeSidePanelButton.setFocusPainted(false);
			enzymeSidePanelButton.setBackground(GUIsettings.buttonColor);
			enzymeSidePanelButton.setForeground(GUIsettings.buttonTextColor);
			unselectedEnzymes.add(enzymeSidePanelButton);
		}

		// Add to enzyme list panel after clearing it
		enzymeSidePanel.removeAll();
		enzymeSidePanel.add(selectedEnzymesLabel);
		for(int k = 0; k < selectedEnzymes.size(); k++) {
			unselectedEnzymes.add(selectedEnzymes.get(k));
		}
		enzymeSidePanel.add(unselectedEnzymesLabel);
		for(int k = 0; k < unselectedEnzymes.size(); k++) {
			enzymeSidePanel.add(unselectedEnzymes.get(k));
		}
		enzymeScrollPane = new JScrollPane(enzymeSidePanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		enzymeScrollPane.getVerticalScrollBar().setUnitIncrement(40);

		add(optionPanel, BorderLayout.NORTH);
		add(drawPanel, BorderLayout.CENTER);
		add(enzymeScrollPane, BorderLayout.WEST);

		revalidate();
		repaint();

		// UI
		enzymeScrollPane.setBorder(GUIsettings.border);
		enzymeSidePanel.setBorder(BorderFactory.createEmptyBorder());
		enzymeSidePanel.setBackground(GUIsettings.buttonHighlightColor);
		show.setBackground(GUIsettings.buttonColor);
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

	public static void resized() {
		insertCuttingDrawPanel.drawPanel.resized();
		vectorCuttingDrawPanel.drawPanel.resized();
	}

	public static void update() {
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

	private static void clearEnzymeSidePanel() {
		// Clear insert enzyme side panel
		insertCuttingDrawPanel.enzymeSidePanel.removeAll();
		insertCuttingDrawPanel.enzymeSidePanel.add(insertCuttingDrawPanel.selectedEnzymesLabel);
		for(int j = 0; j < insertCuttingDrawPanel.selectedEnzymes.size(); j++) {
			insertCuttingDrawPanel.unselectedEnzymes.add(insertCuttingDrawPanel.selectedEnzymes.get(j));
			insertCuttingDrawPanel.selectedEnzymes.get(j).setBackground(GUIsettings.buttonColor);
			insertCuttingDrawPanel.selectedEnzymes.get(j).setForeground(GUIsettings.buttonTextColor);
			insertCuttingDrawPanel.unselectedEnzymes.sort(new Comparator<JButton>() {
				@Override
				public int compare(JButton one, JButton two) {
					return one.getText().compareTo(two.getText());
				}
			});
		}
		insertCuttingDrawPanel.enzymeSidePanel.add(insertCuttingDrawPanel.unselectedEnzymesLabel);
		for(int j = 0; j < insertCuttingDrawPanel.unselectedEnzymes.size(); j++) {
			insertCuttingDrawPanel.enzymeSidePanel.add(insertCuttingDrawPanel.unselectedEnzymes.get(j));
		}
		insertCuttingDrawPanel.selectedEnzymes.clear();
		insertCuttingDrawPanel.enzymeSidePanel.revalidate();
		insertCuttingDrawPanel.enzymeSidePanel.repaint();

		// Clear vector enzyme side panel
		vectorCuttingDrawPanel.enzymeSidePanel.removeAll();
		vectorCuttingDrawPanel.enzymeSidePanel.add(vectorCuttingDrawPanel.selectedEnzymesLabel);
		for(int j = 0; j < vectorCuttingDrawPanel.selectedEnzymes.size(); j++) {
			vectorCuttingDrawPanel.unselectedEnzymes.add(vectorCuttingDrawPanel.selectedEnzymes.get(j));
			vectorCuttingDrawPanel.selectedEnzymes.get(j).setBackground(GUIsettings.buttonColor);
			vectorCuttingDrawPanel.selectedEnzymes.get(j).setForeground(GUIsettings.buttonTextColor);
			vectorCuttingDrawPanel.unselectedEnzymes.sort(new Comparator<JButton>() {
				@Override
				public int compare(JButton one, JButton two) {
					return one.getText().compareTo(two.getText());
				}
			});
		}
		vectorCuttingDrawPanel.enzymeSidePanel.add(vectorCuttingDrawPanel.unselectedEnzymesLabel);
		for(int j = 0; j < vectorCuttingDrawPanel.unselectedEnzymes.size(); j++) {
			vectorCuttingDrawPanel.enzymeSidePanel.add(vectorCuttingDrawPanel.unselectedEnzymes.get(j));
		}
		vectorCuttingDrawPanel.selectedEnzymes.clear();
		vectorCuttingDrawPanel.enzymeSidePanel.revalidate();
		vectorCuttingDrawPanel.enzymeSidePanel.repaint();
	}

	public static void addEnzymeToView(String ile, String vle, String ire, String vre, int ill, int vll, int irl, int vrl) {
		clearEnzymeSidePanel();

		/*
		 * Add insert enzymes
		 */
		for(int j = 0; j < insertCuttingDrawPanel.unselectedEnzymes.size(); j++) {
			if(insertCuttingDrawPanel.unselectedEnzymes.get(j).getText().equals(ile) || insertCuttingDrawPanel.unselectedEnzymes.get(j).getText().equals(ire)) {
				for(ActionListener a: insertCuttingDrawPanel.unselectedEnzymes.get(j).getActionListeners()) {
					a.actionPerformed(new ActionEvent(insertCuttingDrawPanel, ActionEvent.ACTION_PERFORMED, null));
				}
				j--;
			}
		}

		/*
		 * Add vector enzymes
		 */
		for(int j = 0; j < vectorCuttingDrawPanel.unselectedEnzymes.size(); j++) {
			if(vectorCuttingDrawPanel.unselectedEnzymes.get(j).getText().equals(vle) || vectorCuttingDrawPanel.unselectedEnzymes.get(j).getText().equals(vre)) {
				for(ActionListener a: vectorCuttingDrawPanel.unselectedEnzymes.get(j).getActionListeners()) {
					a.actionPerformed(new ActionEvent(vectorCuttingDrawPanel, ActionEvent.ACTION_PERFORMED, null));
				}
				j--;
			}
		}

		insertCuttingDrawPanel.drawPanel.highlightFrom = ill;
		insertCuttingDrawPanel.drawPanel.highlightTo = irl;

		vectorCuttingDrawPanel.drawPanel.highlightFrom = vrl;
		vectorCuttingDrawPanel.drawPanel.highlightTo = vll;
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
		private String drawOrig;
		private String drawComp;
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
			drawOrig = "";
			drawComp = "";
			buttons = new ArrayList<>();
			enzymeDescription = "";
			analyzedToDisplay = new ArrayList<>();
			analyzedButtons = new ArrayList<>();
		}

		void setLength(int length) {
			sequenceLength = Integer.toString(length);
		}

		void resized() {
			// Width and height should be the same for both panels
			width = getWidth();
			height = getHeight();

			buttons.clear();
			analyzedButtons.clear();
			analyzedToDisplay.clear();
			drawPanel.removeAll();

			revalidate();
			repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			/*
			 * Anti-aliasing
			 */
			Graphics2D g2 = (Graphics2D)g;
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			rh.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
			g2.setRenderingHints(rh);

			g.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 10));

			/*
			 * Draw a horizontal line separating option panel to this panel
			 */
			g.setColor(Color.WHITE);
			g.drawLine(0, 0, width, 0);
			g.drawLine(0, 1, width, 1);
			g.drawLine(0, 2, width, 2);
			g.setColor(Color.BLACK); // Reset to default color

			/*
			 * Draw a line of plasmid
			 */
			int lineStart = 100;
			int lineEnd = width - 100;
			int lineTotal = lineEnd - lineStart;
			int lineLocationHeight = height / 6;
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
			int numTickMarks = 9;
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
				// Draw oval
				g.fillOval(relativeLocationStart - 4, lineLocationHeight - 4, 8, 8);
				g.fillOval(relativeLocationEnd - 4, lineLocationHeight - 4, 8, 8);
				// Draw lines
				g.drawLine(relativeLocationStart, lineLocationHeight + 1, relativeLocationEnd, lineLocationHeight + 1);
				g.drawLine(relativeLocationStart, lineLocationHeight + 0, relativeLocationEnd, lineLocationHeight + 0);
				g.drawLine(relativeLocationStart, lineLocationHeight - 1, relativeLocationEnd, lineLocationHeight - 1);
				g.setColor(Color.BLACK);
			}
			else if(abstractLocationEnd < abstractLocationStart) {
				g.setColor(GUIsettings.buttonHighlightColor);
				// Draw oval
				g.fillOval(relativeLocationStart - 4, lineLocationHeight - 4, 8, 8);
				g.fillOval(relativeLocationEnd - 4, lineLocationHeight - 4, 8, 8);
				// Draw lines
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
			//			g.setColor(Color.BLUE); // 5 prime
			//			g.fillOval(width - 134, height - 64, 8, 8);
			//			g.drawLine(width - 130, height - 61, width - 110, height - 61);
			//			g.drawLine(width - 130, height - 60, width - 110, height - 60);
			//			g.drawLine(width - 130, height - 59, width - 110, height - 59);
			//			g.setColor(Color.BLACK);
			//			g.drawString("= 5' Overhang", width - 100, height - 57);
			//			g.setColor(Color.GREEN); // 3 prime
			//			g.fillOval(width - 134, height - 52, 8, 8);
			//			g.drawLine(width - 130, height - 49, width - 110, height - 49);
			//			g.drawLine(width - 130, height - 48, width - 110, height - 48);
			//			g.drawLine(width - 130, height - 47, width - 110, height - 47);
			//			g.setColor(Color.BLACK);
			//			g.drawString("= 3' Overhang", width - 100, height - 45);
			//			g.setColor(Color.RED); // Blunt
			//			g.fillOval(width - 134, height - 40, 8, 8);
			//			g.drawLine(width - 130, height - 37, width - 110, height - 37);
			//			g.drawLine(width - 130, height - 36, width - 110, height - 36);
			//			g.drawLine(width - 130, height - 35, width - 110, height - 35);
			//			g.setColor(Color.BLACK);
			//			g.drawString("= Blunt Overhang", width - 100, height - 33);
			//			g.setColor(Color.BLACK);

			/*
			 * Main display of enzymes and their information
			 */
			if(displayThis != null) {

				Set<Integer> locationSet = displayThis.keySet();

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


						// TODO: Fix runtime err at line 756
						/*
						 * All line / oval drawing calls
						 */
						// Draw enzyme cut sites and enzyme name
						g.setColor(GUIsettings.enzymeCutDrawLineColor);
						g.drawLine(relativeLocation, lineLocationHeight - 0, relativeLocation, lineLocationHeight + 10 + heightOffset); // Vertical Line
						g.drawLine(relativeLocation, lineLocationHeight + 10 + heightOffset, relativeLocation + 2, lineLocationHeight + 10 + heightOffset); // Horizontal Line
						// Check if the line(s) from this enzyme need to be bold due to user hovering on the button
						if(enzymes[k].equals(boldThisEnzyme)) {

							/*
							 * Bold the line originating from every site of this enzyme
							 */
							if(currentOverhang.isFivePrime())
								g.setColor(Color.BLUE);
							else if(currentOverhang.isThreePrime())
								g.setColor(Color.GREEN);
							else if(currentOverhang.isBlunt())
								g.setColor(Color.RED);
							g.drawLine(relativeLocation - 1, lineLocationHeight - 0, relativeLocation - 1, lineLocationHeight + 10 + heightOffset); // Vertical Line
							g.drawLine(relativeLocation, lineLocationHeight + 9 + heightOffset, relativeLocation + 2, lineLocationHeight + 9 + heightOffset); // Horizontal Line
							g.drawLine(relativeLocation, lineLocationHeight - 0, relativeLocation, lineLocationHeight + 10 + heightOffset); // Vertical Line
							g.drawLine(relativeLocation, lineLocationHeight + 10 + heightOffset, relativeLocation + 2, lineLocationHeight + 10 + heightOffset); // Horizontal Line
							g.drawLine(relativeLocation + 1, lineLocationHeight - 0, relativeLocation + 1, lineLocationHeight + 10 + heightOffset); // Vertical Line
							g.drawLine(relativeLocation, lineLocationHeight + 11 + heightOffset, relativeLocation + 2, lineLocationHeight + 11 + heightOffset); // Horizontal Line
							g.fillOval(relativeLocation - 4, lineLocationHeight - 4, 8, 8); // Circle indicating the cut site on the DNA

							/*
							 * Display the sequence near the selected enzyme's cutting site up at the top
							 */
							g.setColor(GUIsettings.buttonHighlightColor);
							g.drawString(drawOrig, width / 2 - g.getFontMetrics().stringWidth(drawOrig) / 2, height / 8 - 40);
							g.drawString(drawComp, width / 2 - g.getFontMetrics().stringWidth(drawComp) / 2, height / 8 - 20);
							g.setColor(Color.BLACK); // Reset to default color
						}


						/*
						 * Enzyme buttons
						 */
						JButton enzymeButton = new JButton(enzymes[k]);
						enzymeButton.addActionListener(this);
						enzymeButton.setToolTipText(toolTipString);
						enzymeButton.setFocusPainted(false);
						enzymeButton.addMouseListener(new MouseAdapter() {

							@Override
							public void mouseExited(MouseEvent e) {
								new SwingWorker<Void, Void>() {
									@Override
									protected Void doInBackground() throws Exception {
										JButton exited = (JButton) e.getSource();

										/*
										 * Change button color
										 */
										for(int j = 0; j < buttons.size(); j++) {
											if(buttons.get(j).getText().equals(exited.getText())) {
												buttons.get(j).setBackground(GUIsettings.buttonColor);
												buttons.get(j).setForeground(GUIsettings.buttonTextColor);
											}
										}

										/*
										 * Bold the line originating from every site of this enzyme
										 */
										boldThisEnzyme = null;

										/*
										 * Important
										 */
										revalidate();
										repaint();

										return null;
									}
								}.execute();
							}

							@Override
							public void mouseEntered(MouseEvent e) {
								new SwingWorker<Void, Void>() {
									@Override
									protected Void doInBackground() throws Exception {
										JButton pressed = (JButton) e.getSource();

										/*
										 * Get data for bottom info display
										 */
										Enzyme[] enzymes = Planner.getEnzymes();
										for(int j = 0; j < enzymes.length; j++) {
											if(enzymes[j].getEnzymeName().equals(pressed.getText())) {
												enzymeDescription = enzymes[j].toString();
											}
										}

										/*
										 * Change button color
										 */
										for(int j = 0; j < buttons.size(); j++) {
											if(buttons.get(j).getText().equals(pressed.getText())) {
												buttons.get(j).setBackground(GUIsettings.buttonHighlightColor);
												buttons.get(j).setForeground(GUIsettings.buttonHighlightTextColor);
											}
										}

										/*
										 * Bold the line originating from every site of this enzyme
										 */
										boldThisEnzyme = pressed.getText();

										/*
										 * Display the sequence which this enzyme cuts
										 */
										int drawSequenceLength = 30;
										StringBuilder origSB = new StringBuilder();
										StringBuilder compSB = new StringBuilder();
										String thisSequence = thisPlasmid.getSequence();
										// Get location by parsing tool tip text
										int cursor = 24;
										StringBuilder locationBuilder = new StringBuilder();
										String parse = pressed.getToolTipText();
										char readChar = parse.charAt(cursor);
										while(readChar != '<') {
											locationBuilder.append(readChar);
											cursor++;
											readChar = parse.charAt(cursor);
										}
										int parsedLocation = Integer.parseInt(locationBuilder.toString());
										Overhang overhangAtSite = enzymeAndLocationAndOverhang.get(pressed.getText() + parsedLocation);
										// Start appending what will be drawn
										origSB.append("(5')     [...]     ");
										compSB.append("(3')     [...]     ");
										for(int n = (-drawSequenceLength / 2) + parsedLocation; n < drawSequenceLength / 2 + parsedLocation; n++) {
											if(overhangAtSite.isFivePrime()) {
												if(n == parsedLocation)
													origSB.append("                    ");
												if(n == parsedLocation + (overhangAtSite.getOverhang().length() + 0))
													compSB.append("                    ");
											}
											else if(overhangAtSite.isThreePrime()) {
												if(n == parsedLocation)
													compSB.append("                    ");
												if(n == parsedLocation + (overhangAtSite.getOverhang().length() + 0))
													origSB.append("                    ");
											}
											else if(overhangAtSite.isBlunt()) {
												if(n == parsedLocation) {
													origSB.append("                    ");
													compSB.append("                    ");
												}
											}
											try { // Sequence may not have the index
												origSB.append(thisSequence.charAt(n) + " ");
												if(thisSequence.charAt(n) == 'c')
													compSB.append("G ");
												else if(thisSequence.charAt(n) == 'g')
													compSB.append("C ");
												else if(thisSequence.charAt(n) == 't')
													compSB.append("A ");
												else if(thisSequence.charAt(n) == 'a')
													compSB.append("T ");
											} catch (Exception e) {}

										}
										origSB.append("    [...]     (3')");
										compSB.append("    [...]     (5')");
										// Store into a wider scoped field
										drawOrig = origSB.toString().toUpperCase();
										drawComp = compSB.toString().toUpperCase();

										/*
										 * Important
										 */
										revalidate();
										repaint();

										return null;
									}
								}.execute();
							}
						});
						enzymeButton.setSize(55, 14);
						enzymeButton.setLocation(relativeLocation + 4, lineLocationHeight + 3 + heightOffset);
						enzymeButton.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 9));
						enzymeButton.setMargin(new Insets(0, 0, 0, 0));
						enzymeButton.setFocusPainted(false);
						// Button color
						enzymeButton.setBackground(GUIsettings.buttonColor);
						enzymeButton.setForeground(GUIsettings.buttonTextColor);
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
						// Prevent button overlap
						heightOffset += 12;


						/*
						 * Write text to bottom left info area
						 */
						if(enzymeDescription.length() > 0) {
							String[] enzymeData = enzymeDescription.split("\t");
							g.setColor(GUIsettings.buttonHighlightColor);
							g.drawString(enzymeData[0], 5, height - 123); // Name
							g.drawString(enzymeData[2], 5, height - 113); // Sequence
							g.setColor(Color.BLACK);

							if(enzymeData[4].contains("100%"))
								g.setColor(Color.RED);
							g.drawString(enzymeData[4], 5, height - 103); // 1.1
							g.setColor(Color.BLACK);

							if(enzymeData[5].contains("100%"))
								g.setColor(Color.RED);
							g.drawString(enzymeData[5], 5, height - 93); // 2.1
							g.setColor(Color.BLACK);

							if(enzymeData[6].contains("100%"))
								g.setColor(Color.RED);
							g.drawString(enzymeData[6], 5, height - 83); // 3.1
							g.setColor(Color.BLACK);

							if(enzymeData[7].contains("100%"))
								g.setColor(Color.RED);
							g.drawString(enzymeData[7], 5, height - 73); // CutSmart
							g.setColor(Color.BLACK);

							g.drawString(enzymeData[8], 5, height - 63); // Heat inactivation
							g.drawString(enzymeData[9], 5, height - 53); // Incubation temperature
							//							g.drawString(enzymeData[10], 5, height - 10); // Diluent
							g.drawString(enzymeData[3], 5, height - 43); // Supplied buffer

							g.setColor(GUIsettings.buttonHighlightColor);
							g.drawString(enzymeData[1], 5, height - 33); // Description
							g.setColor(Color.BLACK);
						}

						/*
						 * Write text to bottom center plasmid comparison area
						 */
						if(analyzedToDisplay.size() > 0) {
							g.setColor(GUIsettings.buttonHighlightColor);
							g.drawString("Compatible enzymes on other plasmid ", width / 4 + 30, height - 123);
						}
						int heightOffset2 = 0;
						int widthOffset2 = 0;
						for(int n = 0; n < analyzedToDisplay.size(); n++) {
							g.setColor(Color.RED);
							String display = analyzedToDisplay.get(n);
							if(n > 0)
								g.setColor(GUIsettings.buttonHighlightColor);
							if(n == 0)
								g.drawString(display, width / 4 + 30, height - 113 + heightOffset2);
							else
								g.drawString(display, width / 4 + 30 + widthOffset2 + 50, height - 103 + heightOffset2);
							heightOffset2 += 10;
							if(heightOffset2 >= 80) {
								heightOffset2 = 0;
								widthOffset2 += 100;
							}
							g.setColor(Color.BLACK); // Reset to default
						}
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			// Clear enzyme side panel
			if(e.getSource() == show || e.getSource() instanceof JRadioButton) {
				enzymeSidePanel.removeAll();
				enzymeSidePanel.add(selectedEnzymesLabel);
				for(int j = 0; j < selectedEnzymes.size(); j++) {
					unselectedEnzymes.add(selectedEnzymes.get(j));
					selectedEnzymes.get(j).setBackground(GUIsettings.buttonColor);
					selectedEnzymes.get(j).setForeground(GUIsettings.buttonTextColor);
					unselectedEnzymes.sort(new Comparator<JButton>() {
						@Override
						public int compare(JButton one, JButton two) {
							return one.getText().compareTo(two.getText());
						}
					});
				}
				enzymeSidePanel.add(unselectedEnzymesLabel);
				for(int j = 0; j < unselectedEnzymes.size(); j++) {
					enzymeSidePanel.add(unselectedEnzymes.get(j));
				}
				selectedEnzymes.clear();

				enzymeSidePanel.revalidate();
				enzymeSidePanel.repaint();
			}

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
						displayThis = Planner.findEnzymesNotCutting(thisPlasmid, start, end);

						// Line highlight
						highlightFrom = start;
						highlightTo = end;
					}
				} catch (Exception ex) {
					//					JOptionPane.showMessageDialog(
					//							null,
					//							"Error while trying to find enzymes.\n"
					//									+ "Please check digest.",
					//									"Invalid state",
					//									0);
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
				Overhang target = overhang;//.getComplement();

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
						analyzedToDisplay.add(" : " + Integer.toString(otherEnzymeAndLocation.get(compatibleEnzymes[j]).size()));
						JButton button = new JButton(compatibleEnzymes[j]);
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// Retrieve the other panel
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
								TabbedCuttingPane.setSelectedDrawPanel(other);

								other.drawPanel.removeAll();
								other.drawPanel.buttons.clear();
								other.drawPanel.analyzedButtons.clear();
								other.drawPanel.analyzedToDisplay.clear();

								other.drawPanel.highlightFrom = 0;
								other.drawPanel.highlightTo = 0;

								/*
								 * Clear other's enzyme side panel
								 */
								other.enzymeSidePanel.removeAll();
								other.enzymeSidePanel.add(other.selectedEnzymesLabel);
								for(int j = 0; j < other.selectedEnzymes.size(); j++) {
									other.unselectedEnzymes.add(other.selectedEnzymes.get(j));
									other.selectedEnzymes.get(j).setBackground(GUIsettings.buttonColor);
									other.selectedEnzymes.get(j).setForeground(GUIsettings.buttonTextColor);
									other.unselectedEnzymes.sort(new Comparator<JButton>() {
										@Override
										public int compare(JButton one, JButton two) {
											return one.getText().compareTo(two.getText());
										}
									});
								}
								other.enzymeSidePanel.add(other.unselectedEnzymesLabel);
								for(int j = 0; j < other.unselectedEnzymes.size(); j++) {
									other.enzymeSidePanel.add(other.unselectedEnzymes.get(j));
								}
								other.selectedEnzymes.clear();
								other.enzymeSidePanel.revalidate();
								other.enzymeSidePanel.repaint();
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
							analyzedButtons.get(n).setLocation(width / 4 + 30 + widthOffset2, height - 113 + heightOffset2);
							analyzedButtons.get(n).setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 8));
							analyzedButtons.get(n).setMargin(new Insets(0, 0, 0, 0));
							analyzedButtons.get(n).setBackground(GUIsettings.buttonHighlightColor);
							analyzedButtons.get(n).setForeground(GUIsettings.buttonHighlightTextColor);
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

	static CuttingDrawPanel createInsertDrawPanel() {
		return insertCuttingDrawPanel == null ? insertCuttingDrawPanel = new CuttingDrawPanel(): insertCuttingDrawPanel;
	}

	static CuttingDrawPanel createVectorDrawPanel() {
		return vectorCuttingDrawPanel == null ? vectorCuttingDrawPanel = new CuttingDrawPanel(): vectorCuttingDrawPanel;
	}
}