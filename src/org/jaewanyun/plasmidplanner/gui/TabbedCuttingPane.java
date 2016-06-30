package org.jaewanyun.plasmidplanner.gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.jaewanyun.plasmidplanner.Plasmid;

/*
 * All panels listed in here that are updated upon digest need to implements DigestListener
 */
class TabbedCuttingPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static Plasmid insert;
	private static Plasmid vector;
	private static CuttingDrawPanel insertCutPanel;
	private static CuttingDrawPanel vectorCutPanel;
	private static EnzymeListPane enzymeListPane;
	private static SiteListPane siteListPanel;
	private static OverhangListPane overhangListPanel;
	private static TabbedCuttingPane tabbedCuttingPane;

	private TabbedCuttingPane() {
		super();

		insertCutPanel = CuttingDrawPanel.getInsertDrawPanel();
		vectorCutPanel = CuttingDrawPanel.getVectorDrawPanel();

		enzymeListPane = EnzymeListPane.getEnzymeListPane();
		siteListPanel = SiteListPane.getSiteListPane();
		overhangListPanel = OverhangListPane.getOverhangListPane();

		addTab("     Insert    ", null, insertCutPanel, "");
		addTab("     Vector    ", null, vectorCutPanel, "");
		addTab("    Enzymes    ", null, enzymeListPane, "");
		addTab("     Sites     ", null, siteListPanel, "");
		addTab("   Overhangs   ", null, overhangListPanel, "");

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

	static void setSelected(Component component) {
		tabbedCuttingPane.setSelectedComponent(component);
	}

	static TabbedCuttingPane getTabbedCuttingPane() {
		return tabbedCuttingPane == null ? tabbedCuttingPane = new TabbedCuttingPane(): tabbedCuttingPane;
	}
}
