package org.jaewanyun.plasmidplanner.gui.blast;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.jaewanyun.plasmidplanner.gui.GUIsettings;

public class TabbedBlastPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static BlastPanel insertBlastPanel;
	private static BlastPanel vectorBlastPanel;
	private static BlastTextPane blastTextPane;
	private static TabbedBlastPane tabbedBlastPane;

	private TabbedBlastPane() {
		super();

		insertBlastPanel = new BlastPanel();
		vectorBlastPanel = new BlastPanel();
		blastTextPane = BlastTextPane.createBlastTextPane();

		addTab("     Insert    ", null, insertBlastPanel, "");
		addTab("     Vector    ", null, vectorBlastPanel, "");
		addTab("    Text View  ", null, blastTextPane, "");

		// UI
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(GUIsettings.tabBackgroundColor);
		setForeground(GUIsettings.tabTextColor);
		setFont(new Font(getFont().getName(), Font.PLAIN, 12));
		setOpaque(true);
		setUI(GUIsettings.getUI());
		setToolTipText(null);
	}

	public static void resized() {
		insertBlastPanel.resized();
		vectorBlastPanel.resized();
	}

	public static void update(String output, boolean isInsert) {

		// TODO: Parse output
		if(isInsert) {

		} else {

		}
	}

	public static TabbedBlastPane createTabbedBlastPane() {
		return tabbedBlastPane == null ? tabbedBlastPane = new TabbedBlastPane() : tabbedBlastPane;
	}
}
