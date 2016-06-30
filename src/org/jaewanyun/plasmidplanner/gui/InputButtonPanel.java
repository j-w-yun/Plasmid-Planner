package org.jaewanyun.plasmidplanner.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class InputButtonPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton loadPlasmidButton;
	private JButton savePlasmidButton;
	private JButton formatPlasmidButton;
	private static InputButtonPanel inputButtonPanel;

	private InputButtonPanel() {
		super(new GridLayout(0, 3));

		loadPlasmidButton = new JButton("Load DNA");
		savePlasmidButton = new JButton("Save DNA");
		formatPlasmidButton = new JButton("Format Input");

		add(loadPlasmidButton);
		add(savePlasmidButton);
		add(formatPlasmidButton);

		loadPlasmidButton.addActionListener(this);
		savePlasmidButton.addActionListener(this);
		formatPlasmidButton.addActionListener(this);

		loadPlasmidButton.setBackground(GUIsettings.buttonColor);
		savePlasmidButton.setBackground(GUIsettings.buttonColor);
		formatPlasmidButton.setBackground(GUIsettings.buttonColor);

		loadPlasmidButton.setBorder(GUIsettings.border);
		savePlasmidButton.setBorder(GUIsettings.border);
		formatPlasmidButton.setBorder(GUIsettings.border);
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(GUIsettings.tabBackgroundColor);
	}

	static InputButtonPanel getInputButtonPanel() {
		return inputButtonPanel == null ? inputButtonPanel = new InputButtonPanel(): inputButtonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loadPlasmidButton) {
			String fileName = FileIO.load();
			if(fileName.equals(""))
				return;
			try {
				String[] sequence = FileIO.load(fileName);
				TabbedInputPane.setCurrentContext(sequence);
			} catch (Exception fileNotFound) {
				JOptionPane.showMessageDialog(
						null,
						"The specified file could not be found.\n"
								+ "File was not imported.",
								"File Not Found",
								0);
			}
		}
		else if(e.getSource() == savePlasmidButton) {
			String fileName = FileIO.save();
			if(fileName.equals(""))
				return;
			String sequence = TabbedInputPane.getCurrentContext().getText();
			try {
				FileIO.save(fileName + ".txt", sequence);
			} catch (Exception couldNotSave) {
				JOptionPane.showMessageDialog(
						null,
						"The specified directory could not be accessed.\n"
								+ "File was not saved.",
								"Could Not Save",
								0);
			}
		}
		else if(e.getSource() == formatPlasmidButton) {
			TabbedInputPane.formatCurrentContext(true);
		}
	}
}
