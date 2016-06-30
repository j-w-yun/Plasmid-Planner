package org.jaewanyun.plasmidplanner.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public final class ConsolePane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private static JTextArea consoleText;
	private static JScrollPane console;
	static final int WIDTH = 300;

	static {
		consoleText = new JTextArea();
		consoleText.setEditable(false);

		console = new JScrollPane(consoleText,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// Height conforms to JFrame height
		console.setPreferredSize(new Dimension(WIDTH, 0));
		console.setBorder(BorderFactory.createEmptyBorder());
	}

	static JScrollPane getConsole() {
		return console;
	}

	public static void print(String printThis) {
		consoleText.append(printThis);
	}

	public static void println(String printThis) {
		consoleText.append(printThis + "\n");
	}
}
