package org.jaewanyun.plasmidplanner.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/* Format of SwingWorker that calls ProgressPanel

SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
	Integer id = ProgressPanel.createTask("Formatting Sequence", this);

	@Override
	protected Void doInBackground(){
		int total = 12345;
		int progress = 0;
		publish(progress++ * 100 / total);

		return null;
	}

	@Override
	protected void process(List<Integer> progress) {
		if(isCancelled())
			return;
		if(ProgressPanel.isDescriptionSet())
			ProgressPanel.update(progress.get(progress.size() - 1));
		else
			ProgressPanel.update(progress.get(progress.size() - 1), "Task Name");
	}

	@Override
	public void done() {
		ProgressPanel.clear();
	}
}
worker.execute();

 */
final class ProgressPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static int maxQueueSize = 100;
	private static HashMap<JButton, Integer> stopButtonAndIdentification;
	private static HashMap<Integer, JPanel> identificationAndPanel;
	private static HashMap<Integer, JProgressBar> identificationAndProgressBar;
	private static HashMap<Integer, JLabel> identificationAndLabel;
	private static HashMap<Integer, Integer> identificationAndProgress;
	private static HashMap<Integer, String> identificationAndDescription;
	private static HashMap<Integer, SwingWorker> identificationAndSwingWorker;
	private static HashSet<Integer> identifications;
	private static ProgressPanel progressPanel;

	private ProgressPanel() {
		super();
		//		super(new FlowLayout(FlowLayout.LEADING));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		identifications = new HashSet<>();
		stopButtonAndIdentification = new HashMap<>();
		identificationAndPanel = new HashMap<>();
		identificationAndProgressBar = new HashMap<>();
		identificationAndLabel = new HashMap<>();
		identificationAndProgress = new HashMap<>();
		identificationAndDescription = new HashMap<>();
		identificationAndSwingWorker = new HashMap<>();

		JPanel defaultPanel = new JPanel();
		defaultPanel.setBackground(GUIsettings.progressBarColor);
		defaultPanel.setBorder(BorderFactory.createEmptyBorder());
		add(defaultPanel);

		setBackground(GUIsettings.progressBarColor);
		setBorder(BorderFactory.createEmptyBorder());
	}

	static ProgressPanel getProgressPanel() {
		return progressPanel == null ? progressPanel = new ProgressPanel(): progressPanel;
	}

	static synchronized int createTask(String description, int max, SwingWorker worker) {
		// TODO: null ptr
		//		if(identifications.size() >= maxQueueSize)
		//			throw new IllegalStateException();

		Integer id = getUniqueID();

		JPanel panel = new JPanel((new FlowLayout(FlowLayout.LEADING)));
		panel.setBackground(GUIsettings.tabBackgroundColor);
		panel.setBorder(BorderFactory.createEmptyBorder());

		JProgressBar progressBar = new JProgressBar(0, max);
		progressBar.setStringPainted(false);
		progressBar.setPreferredSize(new Dimension(MainFrame.MIN_WIDTH / 2, 14)); // Width is half that of minimum width of JFrame

		JLabel progressDescription = new JLabel(description); // Width conforms to length of string contained within it
		progressDescription.setFont(new Font(progressDescription.getFont().getName(), Font.PLAIN, 10));
		progressDescription.setForeground(GUIsettings.progressTextColor);

		JButton stopButton = new JButton("Cancel");
		stopButton.addActionListener(progressPanel);
		stopButton.setPreferredSize(new Dimension(100, 14));
		stopButton.setFont(new Font(stopButton.getFont().getName(), Font.PLAIN, 9));
		stopButton.setBackground(GUIsettings.tabBackgroundColor);
		stopButton.setForeground(GUIsettings.progressTextColor);

		identificationAndPanel.put(id, panel);
		identificationAndProgressBar.put(id, progressBar);
		identificationAndLabel.put(id, progressDescription);
		stopButtonAndIdentification.put(stopButton, id);
		identificationAndProgress.put(id, 0);
		identificationAndDescription.put(id, description);
		identificationAndSwingWorker.put(id, worker);

		// Flow layout order justified to the left
		panel.add(progressBar);
		panel.add(progressDescription);
		panel.add(stopButton);
		progressPanel.add(panel);

		progressPanel.revalidate();
		progressPanel.repaint();

		identifications.add(id);
		return id;

	}

	private static int getUniqueID() {
		int potentialID = 0;

		while(identifications.contains(potentialID)) {
			potentialID = (int) System.currentTimeMillis() % maxQueueSize;
		}
		return potentialID;
	}

	static synchronized boolean taskAlreadyCreated(Integer key) {
		return identifications.contains(key);
	}

	//	static synchronized void increment(int id) {
	//		Integer key = id;
	//
	//		if(!identifications.contains(id))
	//			return;
	//
	//		Integer progress = identificationAndProgress.get(key);
	//		JProgressBar bar = identificationAndProgressBar.get(key);
	//		progress++;
	//		bar.setValue(progress);
	//		identificationAndProgress.put(key, progress);
	//	}

	static synchronized void increment(int id, int amount) {
		Integer key = id;

		if(!identifications.contains(id))
			return;

		Integer progress = identificationAndProgress.get(key);
		JProgressBar bar = identificationAndProgressBar.get(key);
		progress += amount;
		bar.setValue(progress);
		identificationAndProgress.put(key, progress);
	}

	static synchronized void setProgress(int id, int progress) {
		Integer key = id;

		if(!identifications.contains(id))
			return;

		identificationAndProgress.put(id, new Integer(progress));
		JProgressBar bar = identificationAndProgressBar.get(key);
		bar.setValue(progress);
	}

	static synchronized void setDescription(int id, String progressDescription) {
		Integer key = id;

		if(!identifications.contains(id))
			return;

		identificationAndDescription.put(id, progressDescription);
	}

	static synchronized void clear(Integer id) {
		if(!identifications.contains(id))
			return;

		SwingWorker worker = identificationAndSwingWorker.get(id);
		worker.cancel(true);
		JPanel panel = identificationAndPanel.get(id);
		progressPanel.remove(panel);

		panel = null;

		Set<JButton> buttonSet = stopButtonAndIdentification.keySet();
		JButton[] buttons = buttonSet.toArray(new JButton[buttonSet.size()]);
		for(int j = 0; j < buttons.length; j++) {
			if(stopButtonAndIdentification.get(buttons[j]) == id) {
				stopButtonAndIdentification.remove(buttons[j]);
				break;
			}
		}

		identifications.remove(id);
		identificationAndPanel.remove(id);
		identificationAndProgressBar.remove(id);
		identificationAndLabel.remove(id);
		identificationAndProgress.remove(id);
		identificationAndDescription.remove(id);
		identificationAndSwingWorker.remove(id);

		progressPanel.revalidate();
		progressPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object key = e.getSource();
		Integer id = stopButtonAndIdentification.get(key);

		if(!identifications.contains(id))
			return;

		SwingWorker worker = identificationAndSwingWorker.get(id);
		worker.cancel(true);
		JPanel panel = identificationAndPanel.get(id);

		if(panel != null) {
			progressPanel.remove(panel);
			panel = null;
		}


		identifications.remove(id);
		identificationAndPanel.remove(id);
		identificationAndProgressBar.remove(id);
		identificationAndLabel.remove(id);
		stopButtonAndIdentification.remove(key);
		identificationAndProgress.remove(id);
		identificationAndDescription.remove(id);
		identificationAndSwingWorker.remove(id);

		progressPanel.revalidate();
		progressPanel.repaint();
	}
}
