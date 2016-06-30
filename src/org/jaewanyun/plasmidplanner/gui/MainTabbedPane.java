package org.jaewanyun.plasmidplanner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

class MainTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static TabbedInputPane tabbedInputPane;
	private static InputButtonPanel inputButtonPanel;
	private static TabbedCuttingPane tabbedCuttingPane;
	private static CuttingButtonPanel cuttingButtonPanel;
	private static MainTabbedPane mainTabbedPane;

	private MainTabbedPane() {
		super();

		setTabPlacement(SwingConstants.LEFT);

		// Tabbed input pane
		JPanel tabbedInputPanelWithButtons = new JPanel(new BorderLayout());
		tabbedInputPanelWithButtons.setBorder(BorderFactory.createEmptyBorder());

		tabbedInputPane = TabbedInputPane.getTabbedInputPane();
		inputButtonPanel = InputButtonPanel.getInputButtonPanel();

		tabbedInputPanelWithButtons.add(tabbedInputPane, BorderLayout.CENTER);
		tabbedInputPanelWithButtons.add(inputButtonPanel, BorderLayout.SOUTH);
		addTab(null , null, tabbedInputPanelWithButtons, "");


		// Tabbed cutting pane
		JPanel tabbedCuttingPaneWithButtons = new JPanel(new BorderLayout());
		tabbedCuttingPaneWithButtons.setBorder(BorderFactory.createEmptyBorder());

		tabbedCuttingPane = TabbedCuttingPane.getTabbedCuttingPane();
		cuttingButtonPanel = new CuttingButtonPanel();

		tabbedCuttingPaneWithButtons.add(tabbedCuttingPane, BorderLayout.CENTER);
		tabbedCuttingPaneWithButtons.add(cuttingButtonPanel, BorderLayout.SOUTH);
		addTab(null , null, tabbedCuttingPaneWithButtons, "");


		JPanel panel3 = new JPanel(new FlowLayout());
		JLabel toBeImplemented = new JLabel("Not yet implemented");
		panel3.add(toBeImplemented);
		addTab(null , null, panel3, "");


		setTabComponentAt(0, createTabLabel("<html>Input DNA<br>Data</html>"));
		setTabComponentAt(1, createTabLabel("<html>Restriction<br>Digest</html>"));
		setTabComponentAt(2, createTabLabel("<html>Automated<br>Planner</html>"));

		setBackground(GUIsettings.tabBackgroundColor);
		setOpaque(true);
		setUI(GUIsettings.getUI());
		setBorder(BorderFactory.createEmptyBorder());
		setToolTipText(null);
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
