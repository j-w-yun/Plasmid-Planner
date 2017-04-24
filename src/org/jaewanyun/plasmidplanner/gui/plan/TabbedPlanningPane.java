package org.jaewanyun.plasmidplanner.gui.plan;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.jaewanyun.plasmidplanner.gui.GUIsettings;

public class TabbedPlanningPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static PlanningPanel planningPanel;
	private static ViewSolutionsPanel viewSolutionsPanel;
	private static TabbedPlanningPane tabbedPlanningPane;

	private TabbedPlanningPane() {
		super();

		planningPanel = PlanningPanel.getPlanningPanel();
		viewSolutionsPanel = ViewSolutionsPanel.getViewSolutionsPanel();

		addTab("     Plan      ", null, planningPanel, "");
		addTab("View Solutions", null, viewSolutionsPanel, "");

		// Update sequence lengths on drawing
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {
				PlanningPanel.update();
			}
		});

		// UI
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(GUIsettings.tabBackgroundColor);
		setForeground(GUIsettings.tabTextColor);
		setFont(new Font(getFont().getName(), Font.PLAIN, 12));
		setOpaque(true);
		setUI(GUIsettings.getUI());
		setToolTipText(null);
	}

	public static void setSelectedSolutionsPanel() {
		tabbedPlanningPane.setSelectedComponent(viewSolutionsPanel);
	}

	public static TabbedPlanningPane createTabbedPlanningPane() {
		return tabbedPlanningPane == null ? tabbedPlanningPane = new TabbedPlanningPane(): tabbedPlanningPane;
	}
}
