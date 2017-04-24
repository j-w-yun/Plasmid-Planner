package org.jaewanyun.plasmidplanner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.jaewanyun.plasmidplanner.gui.blast.TabbedBlastPane;
import org.jaewanyun.plasmidplanner.gui.cut.CuttingButtonPanel;
import org.jaewanyun.plasmidplanner.gui.cut.TabbedCuttingPane;
import org.jaewanyun.plasmidplanner.gui.input.InputButtonPanel;
import org.jaewanyun.plasmidplanner.gui.input.TabbedInputPane;
import org.jaewanyun.plasmidplanner.gui.plan.PlanningButtonPanel;
import org.jaewanyun.plasmidplanner.gui.plan.TabbedPlanningPane;

public class MainTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static TabbedInputPane tabbedInputPane;
	private static InputButtonPanel inputButtonPanel;

	private static TabbedCuttingPane tabbedCuttingPane;
	private static CuttingButtonPanel cuttingButtonPanel;
	private static JPanel tabbedCuttingPaneWithButtons;

	private static TabbedPlanningPane tabbedPlanningPane;
	private static PlanningButtonPanel planningButtonPanel;

	private static TabbedBlastPane tabbedBlastPane;
	//	private static BlastButtonPanel blastButtonPanel;

	private static MainTabbedPane mainTabbedPane;

	private MainTabbedPane() {
		super();

		setTabPlacement(SwingConstants.LEFT);

		/*
		 * Create tabbed input pane
		 */
		JPanel tabbedInputPanelWithButtons = new JPanel(new BorderLayout());
		tabbedInputPanelWithButtons.setBorder(BorderFactory.createEmptyBorder());

		tabbedInputPane = TabbedInputPane.createTabbedInputPane();
		inputButtonPanel = InputButtonPanel.createInputButtonPanel();

		tabbedInputPanelWithButtons.add(tabbedInputPane, BorderLayout.CENTER);
		tabbedInputPanelWithButtons.add(inputButtonPanel, BorderLayout.SOUTH);
		addTab(null , null, tabbedInputPanelWithButtons, "");

		/*
		 * Create tabbed cutting pane
		 */
		tabbedCuttingPaneWithButtons = new JPanel(new BorderLayout());
		tabbedCuttingPaneWithButtons.setBorder(BorderFactory.createEmptyBorder());

		tabbedCuttingPane = TabbedCuttingPane.createTabbedCuttingPane();
		cuttingButtonPanel = CuttingButtonPanel.createCuttingButtonPanel();

		tabbedCuttingPaneWithButtons.add(tabbedCuttingPane, BorderLayout.CENTER);
		tabbedCuttingPaneWithButtons.add(cuttingButtonPanel, BorderLayout.SOUTH);
		addTab(null , null, tabbedCuttingPaneWithButtons, "");

		/*
		 * Create tabbed planning pane
		 */
		JPanel tabbedPlanningPaneWithButtons = new JPanel(new BorderLayout());
		tabbedPlanningPaneWithButtons.setBorder(BorderFactory.createEmptyBorder());

		tabbedPlanningPane = TabbedPlanningPane.createTabbedPlanningPane();
		planningButtonPanel = PlanningButtonPanel.createPlanningButtonPanel();

		tabbedPlanningPaneWithButtons.add(tabbedPlanningPane, BorderLayout.CENTER);
		tabbedPlanningPaneWithButtons.add(planningButtonPanel, BorderLayout.SOUTH);
		addTab(null, null, tabbedPlanningPaneWithButtons, "");

		/*
		 * Create BLAST pane
		 */
		JPanel tabbedBlastPaneWithButtons = new JPanel(new BorderLayout());
		tabbedBlastPaneWithButtons.setBorder(BorderFactory.createEmptyBorder());

		tabbedBlastPane = TabbedBlastPane.createTabbedBlastPane();
		//		blastButtonPanel = BlastButtonPanel.createBlastButtonPanel();

		tabbedBlastPaneWithButtons.add(tabbedBlastPane, BorderLayout.CENTER);
		//		tabbedBlastPaneWithButtons.add(blastButtonPanel, BorderLayout.SOUTH);
		addTab(null, null, tabbedBlastPaneWithButtons, "");

		/*
		 * Label the tabs
		 */
		setTabComponentAt(0, createTabLabel("<html>Enter DNA<br>Sequence</html>"));
		setTabComponentAt(1, createTabLabel("<html>Restriction<br>Digest</html>"));
		setTabComponentAt(2, createTabLabel("<html>Automated<br>Cloning</html>"));
		setTabComponentAt(3, createTabLabel("<html>BLAST</html>"));

		// UI
		setBackground(GUIsettings.tabBackgroundColor);
		setOpaque(true);
		setUI(GUIsettings.getUI());
		setBorder(BorderFactory.createEmptyBorder());
		setToolTipText(null);
	}

	public static void setSelectedTabbedCuttingPane() {
		mainTabbedPane.setSelectedComponent(tabbedCuttingPaneWithButtons);
	}

	static MainTabbedPane getMainTabbedPane() {
		return mainTabbedPane == null ? mainTabbedPane = new MainTabbedPane(): mainTabbedPane;
	}

	private static JLabel createTabLabel(String name) {
		JLabel tabLabel = new JLabel(name);
		tabLabel.setForeground(GUIsettings.tabTextColor);
		tabLabel.setFont(new Font(tabLabel.getFont().getName(), Font.BOLD, 12));
		tabLabel.setPreferredSize(new Dimension(100, 50));
		return tabLabel;
	}
}
