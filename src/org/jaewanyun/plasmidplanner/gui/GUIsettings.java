package org.jaewanyun.plasmidplanner.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

final class GUIsettings {

	// All JTabbedPanes
	static final Color tabBackgroundColor = new Color(70, 95, 95);
	static final Color tabTextColor = Color.WHITE;
	static final BasicTabbedPaneUI getUI() {
		return (new BasicTabbedPaneUI() {
			@Override
			protected void installDefaults() {
				super.installDefaults();
				highlight = Color.BLACK;
				lightHighlight = Color.DARK_GRAY;
				shadow = Color.DARK_GRAY;
				darkShadow = Color.BLACK;
				focus = Color.WHITE;
			}
		});
	}

	// ProgressPanel
	static final Color progressBarColor = new Color(100, 128, 128);
	static final Color progressTextColor = Color.WHITE;

	// Menu
	static final Color menubarBackgroundColor = progressBarColor;
	static final Color menubarForegroundColor = Color.LIGHT_GRAY;

	// All JButtons
	static final Color buttonColor = new Color(250, 250, 220);
	static final Color buttonTextColor = progressBarColor;
	static final Color buttonHighlightColor = progressBarColor;
	static final Color buttonHightlightTextColor = Color.WHITE;

	// All JTextAreas
	static final Color textAreaColor = new Color(250, 250, 220);

	// CuttingDrawPanel
	static final Color drawPanelColor = new Color(250, 250, 220);
	static final Color enzymeCutDrawLineColor = progressBarColor;
	static final Color enzymeCutDrawHighlightLineColor = Color.RED;
	//	static final Color enzymeCutDrawTextColor = new Color(160, 82, 45);

	static final Border border = BorderFactory.createBevelBorder(
			EtchedBorder.RAISED, Color.GRAY
			, Color.DARK_GRAY);

}
