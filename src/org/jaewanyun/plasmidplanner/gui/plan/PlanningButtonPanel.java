package org.jaewanyun.plasmidplanner.gui.plan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jaewanyun.plasmidplanner.Digestor;
import org.jaewanyun.plasmidplanner.gui.GUIsettings;
import org.jaewanyun.plasmidplanner.gui.input.TabbedInputPane;

public class PlanningButtonPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JButton planButton;
	private static PlanningButtonPanel planningButtonPanel;

	private PlanningButtonPanel() {
		super();
		setLayout(new BorderLayout());

		planButton = new JButton("Plan Expression Vector");
		planButton.addActionListener(this);

		add(planButton, BorderLayout.CENTER);

		// UI
		planButton.setFocusPainted(false);
		planButton.setBackground(GUIsettings.buttonColor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == planButton) {
			Digestor.digest(TabbedInputPane.getInsertSequence(), TabbedInputPane.getVectorSequence(), true);
		}
	}

	public static PlanningButtonPanel createPlanningButtonPanel() {
		return planningButtonPanel == null ? planningButtonPanel = new PlanningButtonPanel() : planningButtonPanel;
	}
}
