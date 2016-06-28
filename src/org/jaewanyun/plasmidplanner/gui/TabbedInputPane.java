package org.jaewanyun.plasmidplanner.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.jaewanyun.plasmidplanner.Utility;

class TabbedInputPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private static volatile JTextArea insertText;
	private static volatile JTextArea vectorText;
	private static JScrollPane insertTextPane;
	private static JScrollPane vectorTextPane;
	private static volatile boolean focused;
	private static String insertPromptString;
	private static String vectorPromptString;
	private static TabbedInputPane tabbedInputPane;

	private TabbedInputPane() {
		super();
		TabbedInputPane.focused = false;

		insertPromptString = "Paste in a DNA sequence that will be used to derive a gene of interest <here>";
		insertText = new JTextArea(insertPromptString);
		insertText.setEditable(true);
		insertText.setMargin(new Insets(30, 30, 30, 30));
		insertText.setLineWrap(true);
		insertText.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		insertText.setBackground(GUIsettings.textAreaColor);
		insertTextPane = new JScrollPane(insertText,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		insertText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(insertText.getText().length() == 0)
					insertText.setText(insertPromptString);
				focused = false;
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				if(insertText.getText().equals(insertPromptString))
					insertText.setText("");
				focused = true;
			}
		});
		insertTextPane.setBorder(BorderFactory.createEmptyBorder());

		vectorPromptString = "Paste in a DNA sequence that will carry the gene of interest <here>";
		vectorText = new JTextArea(vectorPromptString);
		vectorText.setEditable(true);
		vectorText.setMargin(new Insets(30, 30, 30, 30));
		vectorText.setLineWrap(true);
		vectorText.setFont(new Font("Lucida Sans Typewriter", Font.ITALIC | Font.BOLD, 12));
		vectorText.setBackground(GUIsettings.textAreaColor);
		vectorTextPane = new JScrollPane(vectorText,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		vectorText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(vectorText.getText().length() == 0)
					vectorText.setText(vectorPromptString);
				focused = false;
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				if(vectorText.getText().equals(vectorPromptString))
					vectorText.setText("");
				focused = true;
			}
		});
		vectorTextPane.setBorder(BorderFactory.createEmptyBorder());

		addTab("   Insert  ", null, insertTextPane, "");
		addTab("   Vector  ", null, vectorTextPane, "");

		// Not a SwingWorker because it does not need to be keep track of its progress
		new Thread(() -> {
			while(true) {
				if(focused) {
					String sequence = getCurrentContext().getText();
					Highlighter highlighter = getCurrentContext().getHighlighter();
					HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.PINK);
					for(int j = 0; j < sequence.length(); j++) {
						if(sequence.charAt(j) != 'a' && sequence.charAt(j) != 'A' &&
								sequence.charAt(j) != 't' && sequence.charAt(j) != 'T' &&
								sequence.charAt(j) != 'g' && sequence.charAt(j) != 'G' &&
								sequence.charAt(j) != 'c' && sequence.charAt(j) != 'C') {
							try {
								if(j + 1 < sequence.length())
									highlighter.addHighlight(j, j + 1, painter);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
						}
					}
				}
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		setBorder(BorderFactory.createEmptyBorder());
		setBackground(GUIsettings.tabBackgroundColor);
		setForeground(GUIsettings.tabTextColor);
		setFont(new Font(getFont().getName(), Font.PLAIN, 12));
		setOpaque(true);
		setUI(GUIsettings.getUI());
		setToolTipText(null);
	}

	static TabbedInputPane getTabbedInputPane() {
		return tabbedInputPane == null ? tabbedInputPane = new TabbedInputPane(): tabbedInputPane;
	}

	static String getInsertSequence() {
		return SequenceFormatter.format(insertText.getText());
	}

	static String getVectorsequence() {
		return SequenceFormatter.format(vectorText.getText());
	}

	static void setCurrentContext(String[] sequence) {
		int currentContextTab = tabbedInputPane.getSelectedIndex();
		if(currentContextTab == 0) {
			insertText.setText(sequence[0]);
			for(int j = 1; j < sequence.length; j++) {
				insertText.append(sequence[j]);
			}
		}
		else {
			vectorText.setText(sequence[0]);
			for(int j = 1; j < sequence.length; j++) {
				vectorText.append(sequence[j]);
			}
		}
	}

	static JTextArea getCurrentContext() {
		if(tabbedInputPane == null)
			throw new IllegalArgumentException();
		int currentContextTab = tabbedInputPane.getSelectedIndex();
		if(currentContextTab == 0)
			return insertText;
		else
			return vectorText;
	}

	static void formatCurrentContext(boolean visible) {
		SequenceFormatter.getSequenceFormatter().setVisible(visible);
	}

	private static class SequenceFormatter extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;
		private static Button format;
		private static JCheckBox toUpperCase;
		private static JCheckBox toLowerCase;
		private static JCheckBox removeAllOtherCharacters;
		private static JCheckBox reverseSequence;
		private static JCheckBox complementSequence;
		private static SequenceFormatter sequenceFormatter;

		private SequenceFormatter() {
			super("Formatting Options");
			setVisible(false);

			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

			toUpperCase = new JCheckBox("To upper case");
			toUpperCase.setSelected(true);
			toLowerCase = new JCheckBox("To lower case");
			toLowerCase.setSelected(false);
			removeAllOtherCharacters = new JCheckBox("Remove non-ATGC characters");
			removeAllOtherCharacters.setSelected(true);
			reverseSequence = new JCheckBox("Reverse (flip) sequence");
			reverseSequence.setSelected(false);
			complementSequence = new JCheckBox("Complement sequence");
			complementSequence.setSelected(false);

			JLabel label = new JLabel("Select all that apply");
			label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
			panel.add(label);
			panel.add(new JLabel()); // Placeholder
			panel.add(new JLabel("Note that formatting is not required for cutting "));
			panel.add(new JLabel("or planning."));
			panel.add(new JLabel("It is solely included as an auxiliary utility."));
			panel.add(new JLabel()); // Placeholder
			panel.add(toUpperCase);
			panel.add(toLowerCase);
			panel.add(removeAllOtherCharacters);
			panel.add(reverseSequence);
			panel.add(complementSequence);
			panel.add(new JLabel()); // Placeholder

			format = new Button("Format");
			format.addActionListener(this);
			panel.add(format);

			add(panel);

			ImageIcon programIcon = new ImageIcon("src/org/jaewanyun/plasmidplanner/DNA.png");
			setIconImage(programIcon.getImage());

			setResizable(false);
			pack();
			centerWindow();
		}

		static SequenceFormatter getSequenceFormatter() {
			return sequenceFormatter == null ? sequenceFormatter = new SequenceFormatter(): sequenceFormatter;
		}

		private void centerWindow() {
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - getWidth()) / 2);
			int y = (int) ((dimension.getHeight() - getHeight()) / 2);
			setLocation(x, y - 15);
		}

		static String format(String sequence) {
			if(sequence.equals(insertPromptString) || sequence.equals(vectorPromptString))
				return "";

			// To lower case
			sequence = sequence.toLowerCase();

			// Reomve non-ATGC characters, including special and whitespace chars
			StringBuilder sb = new StringBuilder();
			for(int j = 0; j < sequence.length(); j++) {
				if(sequence.charAt(j) == 'a' || sequence.charAt(j) == 't' || sequence.charAt(j) == 'g' || sequence.charAt(j) == 'c')
					sb.append(sequence.charAt(j));
			}
			return sb.toString();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == format) {
				// Hide instance
				setVisible(false);

				new SwingWorker<String[], Integer>() {
					Integer id = ProgressPanel.createTask("Format: Formatting Sequence", 100, this);

					@Override
					protected String[] doInBackground() {
						String sequence = getCurrentContext().getText();

						publish(1);
						if(removeAllOtherCharacters.isSelected()) {
							sequence = format(sequence);
						}

						publish(20);
						if(complementSequence.isSelected()) {
							try {
								sequence = Utility.complementSequence(sequence, false);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}

						publish(40);
						if(toUpperCase.isSelected())
							sequence = sequence.toUpperCase();

						publish(60);
						if(toLowerCase.isSelected())
							sequence = sequence.toLowerCase();

						publish(80);
						if(reverseSequence.isSelected()) {
							StringBuilder sb = new StringBuilder(sequence);
							sb.reverse();
							sequence = sb.toString();
						}

						publish(99);
						String[] toSend = {sequence};
						return toSend;
					}

					@Override
					protected void process(List<Integer> progress) {
						if(isCancelled())
							return;
						ProgressPanel.setProgress(id, progress.get(progress.size() - 1));
					}

					@Override
					public void done() {
						ProgressPanel.clear(id);
						try {
							setCurrentContext(get());
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}.execute();
			}
		}
	}
}

