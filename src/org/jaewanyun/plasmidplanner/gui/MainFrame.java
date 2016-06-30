package org.jaewanyun.plasmidplanner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;

public class MainFrame extends JFrame implements ComponentListener {

	private static final long serialVersionUID = 1L;
	private MainTabbedPane tabbedPane;
	private ProgressPanel progressPanel;
	private int width;
	private int height;
	private static MainFrame mainFrame;

	static final int MIN_WIDTH = 1200;
	static final int MIN_HEIGHT = 800;

	public static MainFrame createGUI() {
		return mainFrame == null ? mainFrame = new MainFrame("Plasmid Planner") : mainFrame;
	}

	private MainFrame(String title) {
		super(title);

		// Main visual
		tabbedPane = MainTabbedPane.getMainTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);

		// Progress visual
		progressPanel = ProgressPanel.getProgressPanel();
		add(progressPanel, BorderLayout.SOUTH);

		// Console for debugging
		//		add(ConsolePane.getConsole(), BorderLayout.EAST);

		if(this.isAncestorOf(ConsolePane.getConsole())) {
			Dimension dim = new Dimension(MIN_WIDTH + ConsolePane.WIDTH, MIN_HEIGHT);
			setMinimumSize(dim);
			setPreferredSize(dim);
		}
		else {
			Dimension dim = new Dimension(MIN_WIDTH, MIN_HEIGHT);
			setMinimumSize(dim);
			setPreferredSize(dim);
		}

		// Icon of the program
		try (InputStream stream = getClass().getResourceAsStream("/images/dna.jpeg");) {
			ImageIcon programIcon = new ImageIcon(ImageIO.read(stream));
			Image image = programIcon.getImage();
			image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			setIconImage(image);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		addComponentListener(this);
		initializeMenuBar();
		centerWindow();
		setResizable(true);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	private void centerWindow() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y - 15);
	}

	private void initializeMenuBar() {
		// Create menu bar
		JMenuBar menubar = new JMenuBar();

		// Tab "File"
		JMenu file = new JMenu("File");
		// Drop-down item
		JMenuItem fMenuItem_1 = new JMenuItem("Exit", null);
		fMenuItem_1.setMnemonic(KeyEvent.VK_E);
		fMenuItem_1.setToolTipText("Close application");
		fMenuItem_1.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		file.add(fMenuItem_1);

		// Tab "About"
		JMenu about = new JMenu("About");
		// Drop-down item
		JMenuItem aMenuItem_1 = new JMenuItem("Developers", null);
		aMenuItem_1.setMnemonic(KeyEvent.VK_D);
		aMenuItem_1.setToolTipText("Show developers");
		aMenuItem_1.addActionListener((ActionEvent event) -> {
			JOptionPane.showMessageDialog(
					null,
					"Narasimhan Jayanth Venkatachari\n"
							+ "M.D. Ph.D.\n"
							+ "Infectious Diseases and Microbiology\n"
							+ "University of Pittsburgh\n\n"
							+ "Jaewan Yun\n"
							+ "Undergraduate Student\n"
							+ "Molecular Biology\n"
							+ "University of Pittsburgh\n\n",
							"Developers",
							-1);
		});
		about.add(aMenuItem_1);

		// Tab "Help"
		JMenu help = new JMenu("Help");
		// Drop-down item
		JMenuItem hMenuItem_1 = new JMenuItem("License", null);
		hMenuItem_1.setMnemonic(KeyEvent.VK_L);
		hMenuItem_1.setToolTipText("Show MIT license");
		hMenuItem_1.addActionListener((ActionEvent event) -> {
			JOptionPane.showMessageDialog(
					null,
					"The MIT License (MIT)\n\n"
							+ "Copyright (c) 2016 Jaewan Yun <jay50@pitt.edu>\n"
							+ "Copyright (c) 2016 Narasimhan Jayanth Venkatachari <njv12@pitt.edu>\n\n"
							+ "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
							+ "of this software and associated documentation files (the \"Software\"), to deal\n"
							+ "in the Software without restriction, including without limitation the rights\n"
							+ "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
							+ "copies of the Software, and to permit persons to whom the Software is\n"
							+ "furnished to do so, subject to the following conditions:\n\n"
							+ "The above copyright notice and this permission notice shall be included in\n"
							+ "all copies or substantial portions of the Software.\n\n"
							+ "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
							+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
							+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
							+ "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
							+ "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
							+ "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n"
							+ "THE SOFTWARE.",
							"LICENSE",
							-1);
		});
		help.add(hMenuItem_1);

		// Add to menu bar
		menubar.add(file);
		menubar.add(about);
		menubar.add(help);
		// Add menu bar to JFrame
		setJMenuBar(menubar);

		file.setForeground(GUIsettings.menubarForegroundColor);
		about.setForeground(GUIsettings.menubarForegroundColor);
		help.setForeground(GUIsettings.menubarForegroundColor);
		menubar.setBackground(GUIsettings.menubarBackgroundColor);
		menubar.setOpaque(true);
		menubar.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		this.width = this.getWidth();
		this.height = this.getHeight();
		//		ConsolePane.println("MainFrame width changed to :\t" + width);
		//		ConsolePane.println("MainFrame height changed to :\t" + height);
		CuttingDrawPanel.resized();
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}
}
