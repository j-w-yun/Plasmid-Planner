package org.jaewanyun.plasmidplanner.gui.cut;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jaewanyun.plasmidplanner.Digestor;
import org.jaewanyun.plasmidplanner.gui.GUIsettings;
import org.jaewanyun.plasmidplanner.gui.input.TabbedInputPane;

/*
 * For every click of the digest button, the following tasks occur :
 * 		this class retrieves insert and vector sequences then sends them to static Planner
 *		this class retrieves the enzyme database file then sends them to static Planner
 */
public class CuttingButtonPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton digestButton;
	private static CuttingButtonPanel cuttingButtonPanel;

	private CuttingButtonPanel() {
		super();

		setLayout(new BorderLayout());

		digestButton = new JButton("Digest");
		digestButton.addActionListener(this);

		add(digestButton, BorderLayout.CENTER);

		// UI
		digestButton.setFocusPainted(false);
		digestButton.setBackground(GUIsettings.buttonColor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Digestor.digest(TabbedInputPane.getInsertSequence(), TabbedInputPane.getVectorSequence(), false);
	}

	public static CuttingButtonPanel createCuttingButtonPanel() {
		return cuttingButtonPanel == null ? cuttingButtonPanel = new CuttingButtonPanel(): cuttingButtonPanel;
	}
}
