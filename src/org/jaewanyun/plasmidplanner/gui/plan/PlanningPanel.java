package org.jaewanyun.plasmidplanner.gui.plan;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jaewanyun.plasmidplanner.gui.GUIsettings;
import org.jaewanyun.plasmidplanner.gui.input.TabbedInputPane;
public class PlanningPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static int width;
	private static int height;
	private static String insertLength;
	private static String vectorLength;
	private static PlanningPanel planningPanel;
	private static JTextField insertStartTF;
	private static JTextField insertEndTF;
	private static JTextField vectorStartTF;
	private static JTextField vectorEndTF;
	private static JLabel insertLabel1;
	private static JLabel insertLabel2;
	private static JLabel vectorLabel1;
	private static JLabel vectorLabel2;
	private static int insertStart;
	private static int insertEnd;
	private static int vectorStart;
	private static int vectorEnd;

	PlanningPanel() {
		super(null); // No layout manager

		insertLength = "0";
		vectorLength = "0";

		insertStartTF = new JTextField(15);
		insertStartTF.setFont(new Font(insertStartTF.getFont().getName(), Font.PLAIN, 10));
		insertStartTF.addKeyListener(this);
		insertEndTF = new JTextField(15);
		insertEndTF.setFont(new Font(insertEndTF.getFont().getName(), Font.PLAIN, 10));
		insertEndTF.addKeyListener(this);
		vectorStartTF = new JTextField(15);
		vectorStartTF.setFont(new Font(vectorStartTF.getFont().getName(), Font.PLAIN, 10));
		vectorStartTF.addKeyListener(this);
		vectorEndTF = new JTextField(15);
		vectorEndTF.setFont(new Font(vectorEndTF.getFont().getName(), Font.PLAIN, 10));
		vectorEndTF.addKeyListener(this);

		insertLabel1 = new JLabel("<html>DNA fragment to insert starts at</html>");
		insertLabel2 = new JLabel("<html>and ends at</html>");
		vectorLabel1 = new JLabel("<html>Insert the fragment anywhere between</html>");
		vectorLabel2 = new JLabel("<html>and</html>");

		insertStart = -1;
		insertEnd = -1;
		vectorStart = -1;
		vectorEnd = -1;

		add(insertLabel1);
		add(insertStartTF);
		add(insertLabel2);
		add(insertEndTF);

		/*
		 * Place JTextFields
		 */
		insertLabel1.setLocation(30, 30);
		insertLabel1.setSize(200, 20);
		insertStartTF.setLocation(220, 30);
		insertStartTF.setSize(70, 20);
		insertLabel2.setLocation(295, 30);
		insertLabel2.setSize(100, 20);
		insertEndTF.setLocation(370, 30);
		insertEndTF.setSize(70, 20);

		vectorLabel1.setLocation(30, 80);
		vectorLabel1.setSize(240, 20);
		vectorStartTF.setLocation(260, 80);
		vectorStartTF.setSize(70, 20);
		vectorLabel2.setLocation(335, 80);
		vectorLabel2.setSize(50, 20);
		vectorEndTF.setLocation(365, 80);
		vectorEndTF.setSize(70, 20);

		add(vectorLabel1);
		add(vectorStartTF);
		add(vectorLabel2);
		add(vectorEndTF);

		// UI
		setBackground(GUIsettings.drawPanelColor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	public static void update() {
		insertLength = Integer.toString(TabbedInputPane.getInsertSequence().length());
		vectorLength = Integer.toString(TabbedInputPane.getVectorSequence().length());

		planningPanel.revalidate();
		planningPanel.repaint();
	}

	public static void resized() {
		// Width and height should be the same for both panels
		width = planningPanel.getWidth();
		height = planningPanel.getHeight();

		// TODO: clear all buttons

		planningPanel.revalidate();
		planningPanel.repaint();
	}

	public static int getInsertStart() {
		try {
			insertStart = Integer.parseInt(insertStartTF.getText());
			return insertStart;
		} catch (Exception e) {}
		return -1;
	}

	public static int getInsertEnd() {
		try {
			insertEnd = Integer.parseInt(insertEndTF.getText());
			return insertEnd;
		} catch (Exception e) {}
		return -1;
	}

	public static int getVectorStart() {
		try {
			vectorStart = Integer.parseInt(vectorStartTF.getText());
			return vectorStart;
		} catch (Exception e) {}
		return -1;
	}

	public static int getVectorEnd() {
		try {
			vectorEnd = Integer.parseInt(vectorEndTF.getText());
			return vectorEnd;
		} catch (Exception e) {}
		return -1;
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

		// Draw a horizontal line that divides insert from vector
		g.setColor(Color.WHITE);
		g.drawLine(0, height / 5 - 1, width, height / 5 - 1);
		g.drawLine(0, height / 5 + 0, width, height / 5 + 0);
		g.drawLine(0, height / 5 + 1, width, height / 5 + 1);
		g.setColor(Color.BLACK);

		////////////////////INSERT

		/*
		 * Draw a line of insert
		 */
		int lineStart = 200;
		int lineEnd = width - 100;
		int lineTotal = lineEnd - lineStart;
		int lineLocationHeight1 = height * 2 / 5;
		g.drawLine(lineStart, lineLocationHeight1, lineEnd, lineLocationHeight1);

		/*
		 * Insert label
		 */
		g.setColor(GUIsettings.buttonHighlightColor);
		g.drawString("Insert Plasmid", 40, lineLocationHeight1);
		g.setColor(Color.BLACK);

		/*
		 * Draw a text of size of insert
		 */
		g.drawString("0", 160, lineLocationHeight1);
		// Centering string to the point
		g.drawString(insertLength, (width - 60) - g.getFontMetrics().stringWidth(insertLength) / 2, lineLocationHeight1);

		/*
		 * Draw tick marks as a tip of location
		 */
		int numTickMarks = 9;
		boolean alternate = false;
		for(int j = 0; j < numTickMarks; j++) {
			int location = (Integer.parseInt(insertLength) / (numTickMarks + 1)) * (j + 1);
			int drawLocation = (lineTotal / (numTickMarks + 1)) * (j + 1) + lineStart;
			// Alternate length of tick marks
			if(alternate) {
				g.drawLine(drawLocation, lineLocationHeight1, drawLocation, lineLocationHeight1 - 5);
				g.drawString(Integer.toString(location), drawLocation, lineLocationHeight1 - 7);
			}
			else {
				g.drawLine(drawLocation, lineLocationHeight1, drawLocation, lineLocationHeight1 - 15);
				g.drawString(Integer.toString(location), drawLocation, lineLocationHeight1 - 17);
			}
			alternate = !alternate;
		}

		/*
		 * Draw visual of user input
		 */
		int insertRelativeLocation1 = 0;
		int insertRelativeLocation2 = 0;
		if(insertStart > -1 && insertEnd > -1 && insertStart < Integer.parseInt(insertLength) && insertEnd < Integer.parseInt(insertLength)) {

			// If the previous condition has been met, draw the visual
			double ratio1 = (double) insertStart / Integer.parseInt(insertLength);
			insertRelativeLocation1 = (int) (ratio1 * lineTotal + lineStart);
			double ratio2 = (double) insertEnd / Integer.parseInt(insertLength);
			insertRelativeLocation2 = (int) (ratio2 * lineTotal + lineStart);

			/*
			 * Insert save portion
			 */
			g.setColor(GUIsettings.buttonHighlightColor);
			// Draw ovals at the locations
			g.fillOval(insertRelativeLocation1 - 4, lineLocationHeight1 - 4, 8, 8);
			g.fillOval(insertRelativeLocation2 - 4, lineLocationHeight1 - 4, 8, 8);
			if(insertRelativeLocation2 > insertRelativeLocation1) { // No need to loop around
				g.drawLine(insertRelativeLocation1, lineLocationHeight1 - 1, insertRelativeLocation2, lineLocationHeight1 - 1);
				g.drawLine(insertRelativeLocation1, lineLocationHeight1 + 0, insertRelativeLocation2, lineLocationHeight1 + 0);
				g.drawLine(insertRelativeLocation1, lineLocationHeight1 + 1, insertRelativeLocation2, lineLocationHeight1 + 1);
			} else { // Need to loop around
				g.drawLine(insertRelativeLocation1, lineLocationHeight1 - 1, lineEnd, lineLocationHeight1 - 1);
				g.drawLine(insertRelativeLocation1, lineLocationHeight1 + 0, lineEnd, lineLocationHeight1 + 0);
				g.drawLine(insertRelativeLocation1, lineLocationHeight1 + 1, lineEnd, lineLocationHeight1 + 1);

				g.drawLine(lineStart, lineLocationHeight1 - 1, insertRelativeLocation2, lineLocationHeight1 - 1);
				g.drawLine(lineStart, lineLocationHeight1 + 0, insertRelativeLocation2, lineLocationHeight1 + 0);
				g.drawLine(lineStart, lineLocationHeight1 + 1, insertRelativeLocation2, lineLocationHeight1 + 1);
			}
			/*
			 * Insert subject to deletion portion
			 */
			g.setColor(Color.RED);
			// Draw ovals at the locations
			g.fillOval(insertRelativeLocation1 - 4, lineLocationHeight1 - 4, 8, 8);
			g.fillOval(insertRelativeLocation2 - 4, lineLocationHeight1 - 4, 8, 8);
			if(insertRelativeLocation2 > insertRelativeLocation1) { // No need to loop around
				g.drawLine(insertRelativeLocation2, lineLocationHeight1 - 1, lineEnd, lineLocationHeight1 - 1);
				g.drawLine(insertRelativeLocation2, lineLocationHeight1 + 0, lineEnd, lineLocationHeight1 + 0);
				g.drawLine(insertRelativeLocation2, lineLocationHeight1 + 1, lineEnd, lineLocationHeight1 + 1);

				g.drawLine(lineStart, lineLocationHeight1 - 1, insertRelativeLocation1, lineLocationHeight1 - 1);
				g.drawLine(lineStart, lineLocationHeight1 + 0, insertRelativeLocation1, lineLocationHeight1 + 0);
				g.drawLine(lineStart, lineLocationHeight1 + 1, insertRelativeLocation1, lineLocationHeight1 + 1);
			} else { // Need to loop around
				g.drawLine(insertRelativeLocation2, lineLocationHeight1 - 1, insertRelativeLocation1, lineLocationHeight1 - 1);
				g.drawLine(insertRelativeLocation2, lineLocationHeight1 + 0, insertRelativeLocation1, lineLocationHeight1 + 0);
				g.drawLine(insertRelativeLocation2, lineLocationHeight1 + 1, insertRelativeLocation1, lineLocationHeight1 + 1);
			}

			g.setColor(Color.BLACK);
		}



		////////////////////VECTOR

		/*
		 * Draw a line of vector
		 */
		lineStart = 200;
		lineEnd = width - 100;
		lineTotal = lineEnd - lineStart;
		int lineLocationHeight2 = height * 7 / 9;
		g.drawLine(lineStart, lineLocationHeight2, lineEnd, lineLocationHeight2);

		/*
		 * Vector label
		 */
		g.setColor(GUIsettings.buttonHighlightColor);
		g.drawString("Vector Plasmid", 40, lineLocationHeight2);
		g.setColor(Color.BLACK);

		/*
		 * Draw a text of size of vector
		 */
		g.drawString("0", 160, lineLocationHeight2);
		// Centering string to the point
		g.drawString(vectorLength, (width - 60) - g.getFontMetrics().stringWidth(vectorLength) / 2, lineLocationHeight2);

		/*
		 * Draw tick marks as a tip of location
		 */
		numTickMarks = 9;
		alternate = false;
		for(int j = 0; j < numTickMarks; j++) {
			int location = (Integer.parseInt(vectorLength) / (numTickMarks + 1)) * (j + 1);
			int drawLocation = (lineTotal / (numTickMarks + 1)) * (j + 1) + lineStart;
			// Alternate length of tick marks
			if(alternate) {
				g.drawLine(drawLocation, lineLocationHeight2, drawLocation, lineLocationHeight2 + 5);
				g.drawString(Integer.toString(location), drawLocation, lineLocationHeight2 + 16);
			}
			else {
				g.drawLine(drawLocation, lineLocationHeight2, drawLocation, lineLocationHeight2 + 15);
				g.drawString(Integer.toString(location), drawLocation, lineLocationHeight2 + 26);
			}
			alternate = !alternate;
		}

		/*
		 * Draw visual of user input
		 */
		int vectorRelativeLocation1 = 0;
		int vectorRelativeLocation2 = 0;
		if(vectorStart > -1 && vectorEnd > -1 && vectorStart < Integer.parseInt(vectorLength) && vectorEnd < Integer.parseInt(vectorLength)) {

			// If the previous condition has been met, draw the visual
			double ratio1 = (double) vectorStart / Integer.parseInt(vectorLength);
			vectorRelativeLocation1 = (int) (ratio1 * lineTotal + lineStart);
			double ratio2 = (double) vectorEnd / Integer.parseInt(vectorLength);
			vectorRelativeLocation2 = (int) (ratio2 * lineTotal + lineStart);

			/*
			 * Vector subject to deletion portion
			 */
			g.setColor(Color.RED);
			// Draw ovals at the locations
			g.fillOval(vectorRelativeLocation1 - 4, lineLocationHeight2 - 4, 8, 8);
			g.fillOval(vectorRelativeLocation2 - 4, lineLocationHeight2 - 4, 8, 8);
			if(vectorRelativeLocation2 > vectorRelativeLocation1) { // No need to loop around
				g.drawLine(vectorRelativeLocation1, lineLocationHeight2 - 1, vectorRelativeLocation2, lineLocationHeight2 - 1);
				g.drawLine(vectorRelativeLocation1, lineLocationHeight2 + 0, vectorRelativeLocation2, lineLocationHeight2 + 0);
				g.drawLine(vectorRelativeLocation1, lineLocationHeight2 + 1, vectorRelativeLocation2, lineLocationHeight2 + 1);
			} else { // Need to loop around
				g.drawLine(vectorRelativeLocation1, lineLocationHeight2 - 1, lineEnd, lineLocationHeight2 - 1);
				g.drawLine(vectorRelativeLocation1, lineLocationHeight2 + 0, lineEnd, lineLocationHeight2 + 0);
				g.drawLine(vectorRelativeLocation1, lineLocationHeight2 + 1, lineEnd, lineLocationHeight2 + 1);

				g.drawLine(lineStart, lineLocationHeight2 - 1, vectorRelativeLocation2, lineLocationHeight2 - 1);
				g.drawLine(lineStart, lineLocationHeight2 + 0, vectorRelativeLocation2, lineLocationHeight2 + 0);
				g.drawLine(lineStart, lineLocationHeight2 + 1, vectorRelativeLocation2, lineLocationHeight2 + 1);
			}

			/*
			 * Vector save portion
			 */
			g.setColor(GUIsettings.buttonHighlightColor);
			if(vectorRelativeLocation2 > vectorRelativeLocation1) { // No need to loop around
				g.drawLine(vectorRelativeLocation2, lineLocationHeight2 - 1, lineEnd, lineLocationHeight2 - 1);
				g.drawLine(vectorRelativeLocation2, lineLocationHeight2 + 0, lineEnd, lineLocationHeight2 + 0);
				g.drawLine(vectorRelativeLocation2, lineLocationHeight2 + 1, lineEnd, lineLocationHeight2 + 1);

				g.drawLine(lineStart, lineLocationHeight2 - 1, vectorRelativeLocation1, lineLocationHeight2 - 1);
				g.drawLine(lineStart, lineLocationHeight2 + 0, vectorRelativeLocation1, lineLocationHeight2 + 0);
				g.drawLine(lineStart, lineLocationHeight2 + 1, vectorRelativeLocation1, lineLocationHeight2 + 1);
			} else { // Need to loop around
				g.drawLine(vectorRelativeLocation2, lineLocationHeight2 - 1, vectorRelativeLocation1, lineLocationHeight2 - 1);
				g.drawLine(vectorRelativeLocation2, lineLocationHeight2 + 0, vectorRelativeLocation1, lineLocationHeight2 + 0);
				g.drawLine(vectorRelativeLocation2, lineLocationHeight2 + 1, vectorRelativeLocation1, lineLocationHeight2 + 1);
			}
			g.setColor(Color.BLACK);


			/*
			 * Connect the two plasmids together with lines
			 */
			if(insertStart > -1 && insertEnd > -1 && insertStart < Integer.parseInt(insertLength) && insertEnd < Integer.parseInt(insertLength)) {
				// From insertStart to vectorEnd
				g.setColor(GUIsettings.buttonHighlightColor);

				// Connect
				g.drawLine(insertRelativeLocation1 - 1, lineLocationHeight1, vectorRelativeLocation1 - 1, lineLocationHeight2);
				g.drawLine(insertRelativeLocation1 + 0, lineLocationHeight1, vectorRelativeLocation1 + 0, lineLocationHeight2);
				g.drawLine(insertRelativeLocation1 + 1, lineLocationHeight1, vectorRelativeLocation1 + 1, lineLocationHeight2);

				// Connect
				g.drawLine(insertRelativeLocation2 - 1, lineLocationHeight1, vectorRelativeLocation2 - 1, lineLocationHeight2);
				g.drawLine(insertRelativeLocation2 + 0, lineLocationHeight1, vectorRelativeLocation2 + 0, lineLocationHeight2);
				g.drawLine(insertRelativeLocation2 + 1, lineLocationHeight1, vectorRelativeLocation2 + 1, lineLocationHeight2);

				g.setColor(Color.RED);

				// Insert
				g.drawString("Red portion", insertRelativeLocation2 + 30, lineLocationHeight1 - 55);
				g.drawString("is subject", insertRelativeLocation2 + 30, lineLocationHeight1 - 45);
				g.drawString("to deletion", insertRelativeLocation2 + 30, lineLocationHeight1 - 35);

				// Vector
				g.drawString("Red portion", vectorRelativeLocation1 + 30, lineLocationHeight2 + 40);
				g.drawString("is subject", vectorRelativeLocation1 + 30, lineLocationHeight2 + 50);
				g.drawString("to deletion", vectorRelativeLocation1 + 30, lineLocationHeight2 + 60);

				g.setColor(Color.BLACK);
			}
		}
	}

	static PlanningPanel getPlanningPanel() {
		return planningPanel == null ? planningPanel = new PlanningPanel(): planningPanel;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		getInsertStart();
		getInsertEnd();
		getVectorStart();
		getVectorEnd();

		revalidate();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}