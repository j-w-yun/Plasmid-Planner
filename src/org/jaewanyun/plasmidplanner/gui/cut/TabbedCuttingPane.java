package org.jaewanyun.plasmidplanner.gui.cut;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.jaewanyun.plasmidplanner.gui.GUIsettings;

/*
 * All panels listed in here that are updated upon digest need to implements DigestListener
 */
public class TabbedCuttingPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static CuttingDrawPanel insertCutPanel;
	private static CuttingDrawPanel vectorCutPanel;
	private static EnzymeListPane enzymeListPane;
	private static SiteListPane siteListPanel;
	private static OverhangListPane overhangListPanel;
	private static TabbedCuttingPane tabbedCuttingPane;

	private TabbedCuttingPane() {
		super();

		// Visual panels
		insertCutPanel = CuttingDrawPanel.createInsertDrawPanel();
		vectorCutPanel = CuttingDrawPanel.createVectorDrawPanel();

		// Text panels
		enzymeListPane = EnzymeListPane.createEnzymeListPane();
		siteListPanel = SiteListPane.createSiteListPane();
		overhangListPanel = OverhangListPane.createOverhangListPane();

		// Add the panels to tabs
		addTab("     Insert    ", null, insertCutPanel, "");
		addTab("     Vector    ", null, vectorCutPanel, "");
		addTab("    Enzymes    ", null, enzymeListPane, "");
		addTab("     Sites     ", null, siteListPanel, "");
		addTab("   Overhangs   ", null, overhangListPanel, "");

		// UI
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(GUIsettings.tabBackgroundColor);
		setForeground(GUIsettings.tabTextColor);
		setFont(new Font(getFont().getName(), Font.PLAIN, 12));
		setOpaque(true);
		setUI(GUIsettings.getUI());
		setToolTipText(null);
	}

	static CuttingDrawPanel getNotSelectedDrawPanel() {
		return tabbedCuttingPane.getSelectedIndex() == 0 ? vectorCutPanel : insertCutPanel;
	}

	static void setSelectedDrawPanel(Component component) {
		tabbedCuttingPane.setSelectedComponent(component);
	}

	public static void setSelectedInsertDrawPanel() {
		tabbedCuttingPane.setSelectedComponent(insertCutPanel);
	}

	public static TabbedCuttingPane createTabbedCuttingPane() {
		return tabbedCuttingPane == null ? tabbedCuttingPane = new TabbedCuttingPane(): tabbedCuttingPane;
	}
}
