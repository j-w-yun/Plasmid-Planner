package org.jaewanyun.plasmidplanner.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public final class GUIsettings {

	// All JTabbedPanes
	public static final Color tabBackgroundColor = new Color(70, 95, 95);
	public static final Color tabTextColor = Color.WHITE;
	public static final BasicTabbedPaneUI getUI() {
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
	public static final Color progressBarColor = new Color(100, 128, 128);
	public static final Color progressTextColor = Color.WHITE;

	// Menu
	public static final Color menubarBackgroundColor = progressBarColor;
	public static final Color menubarForegroundColor = Color.LIGHT_GRAY;

	// All JButtons
	public static final Color buttonColor = new Color(250, 250, 220);
	public static final Color buttonTextColor = progressBarColor;
	public static final Color buttonHighlightColor = new Color(100, 128, 128);
	public static final Color buttonHighlightTextColor = Color.WHITE;

	// All JTextAreas
	public static final Color textAreaColor = new Color(250, 250, 220);

	// CuttingDrawPanel
	public static final Color drawPanelColor = new Color(250, 250, 220);
	public static final Color enzymeCutDrawLineColor = progressBarColor;
	public static final Color enzymeCutDrawHighlightLineColor = Color.RED;
	//	public static final Color enzymeCutDrawTextColor = new Color(160, 82, 45);

	public static final Border border = BorderFactory.createBevelBorder(
			EtchedBorder.RAISED, Color.GRAY
			, Color.DARK_GRAY);

}
