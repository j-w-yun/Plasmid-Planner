//package org.jaewanyun.plasmidplanner.gui.blast;
//
//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.BorderFactory;
//import javax.swing.JButton;
//import javax.swing.JPanel;
//
//import org.jaewanyun.plasmidplanner.BlasTool;
//import org.jaewanyun.plasmidplanner.gui.GUIsettings;
//import org.jaewanyun.plasmidplanner.gui.input.TabbedInputPane;
//
//public class BlastButtonPanel extends JPanel implements ActionListener {
//
//	private static final long serialVersionUID = 1L;
//	private static BlastButtonPanel blastButtonPanel;
//	private static JButton blastButton;
//
//	private BlastButtonPanel() {
//		super();
//		setLayout(new BorderLayout());
//
//		blastButton = new JButton("Run Basic Local Alignment Search Tool");
//		blastButton.addActionListener(this);
//
//		add(blastButton, BorderLayout.CENTER);
//
//		// UI
//		blastButton.setFocusPainted(false);
//		blastButton.setBackground(GUIsettings.buttonColor);
//		setBorder(BorderFactory.createEmptyBorder());
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == blastButton) {
//			// Run blast
//			try {
//				BlasTool.blast(TabbedInputPane.getInsertSequence(), TabbedInputPane.getVectorSequence());
//			} catch (Exception ex) {}
//		}
//	}
//
//	public static BlastButtonPanel createBlastButtonPanel() {
//		return blastButtonPanel == null ? blastButtonPanel = new BlastButtonPanel() : blastButtonPanel;
//	}
//}
