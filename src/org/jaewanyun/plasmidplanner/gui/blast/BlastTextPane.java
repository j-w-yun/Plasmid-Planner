package org.jaewanyun.plasmidplanner.gui.blast;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jaewanyun.plasmidplanner.gui.GUIsettings;

public class BlastTextPane extends JSplitPane {

	private static final long serialVersionUID = 1L;
	private static JTextArea insertBlastText;
	private static JTextArea vectorBlastText;
	private static JScrollPane insertBlastTextPane;
	private static JScrollPane vectorBlastTextPane;
	private static JPanel insertBlastTextPanel;
	private static JPanel vectorBlastTextPanel;
	private static BlastTextPane blastTextPane;

	private BlastTextPane() {

		insertBlastTextPanel = new JPanel(new BorderLayout());
		insertBlastTextPanel.setBorder(BorderFactory.createEmptyBorder());

		insertBlastText = new JTextArea("Run BLAST to view this information");
		insertBlastText.setEditable(true);
		insertBlastText.setMargin(new Insets(15, 15, 15, 15));
		insertBlastText.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		insertBlastText.setBackground(GUIsettings.textAreaColor);
		insertBlastTextPane = new JScrollPane(insertBlastText,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		insertBlastTextPanel.add(insertBlastTextPane);

		vectorBlastTextPanel = new JPanel(new BorderLayout());
		vectorBlastTextPanel.setBorder(BorderFactory.createEmptyBorder());

		vectorBlastText = new JTextArea("Run BLAST to view this information");
		vectorBlastText.setEditable(true);
		vectorBlastText.setMargin(new Insets(15, 15, 15, 15));
		vectorBlastText.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		vectorBlastText.setBackground(GUIsettings.textAreaColor);
		vectorBlastTextPane = new JScrollPane(vectorBlastText,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		vectorBlastTextPane.getVerticalScrollBar().setUnitIncrement(40);
		vectorBlastTextPanel.add(vectorBlastTextPane);

		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setContinuousLayout(true);
		setLeftComponent(insertBlastTextPanel);
		setRightComponent(vectorBlastTextPanel);

		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(0.5);
	}

	public static void update(String output, boolean isInsert) {
		// Append when the worker received the output from the server
		if(isInsert) {
			insertBlastText.setText("");
			insertBlastText.append("Insert DNA\n\n");
			insertBlastText.append(output);
		} else {
			vectorBlastText.setText("");
			vectorBlastText.append("Vector DNA\n\n");
			vectorBlastText.append(output);
		}
	}

	static BlastTextPane createBlastTextPane() {
		return blastTextPane == null ? blastTextPane = new BlastTextPane() : blastTextPane;
	}
}
