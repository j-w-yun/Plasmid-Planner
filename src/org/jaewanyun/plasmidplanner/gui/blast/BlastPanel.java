package org.jaewanyun.plasmidplanner.gui.blast;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jaewanyun.plasmidplanner.gui.GUIsettings;

class BlastPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private String sequenceLength;

	BlastPanel() {
		super();
		setLayout(null);

		sequenceLength = "0";

		// UI
		setBackground(GUIsettings.drawPanelColor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	void resized() {
		// Width and height should be the same for both panels
		width = getWidth();
		height = getHeight();

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
	}
}
