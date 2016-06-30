package org.jaewanyun.plasmidplanner.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.jaewanyun.plasmidplanner.Enzyme;
import org.jaewanyun.plasmidplanner.Planner;
import org.jaewanyun.plasmidplanner.Plasmid;

/*
 * For every click of the digest button, the following tasks occur :
 * 		this class retrieves insert and vector sequences then sends them to static Planner
 *		this class retrieves the enzyme database file then sends them to static Planner
 *
 *
 */
class CuttingButtonPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton digestButton;
	private volatile Plasmid insert;
	private volatile Plasmid vector;
	private volatile Enzyme[] enzymes;
	private ExecutorService executor;
	private final int totalThreads = 50;

	CuttingButtonPanel() {
		super();

		setLayout(new BorderLayout());

		digestButton = new JButton("Digest");
		digestButton.addActionListener(this);
		digestButton.setBackground(GUIsettings.buttonColor);

		add(digestButton, BorderLayout.CENTER);

		setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == digestButton) {
			enzymes = Enzyme.importFromFile(getClass().getResourceAsStream("/enzymes/enzymes.txt"));

			// Give Planner String of sequence
			Planner.setInsert(TabbedInputPane.getInsertSequence());
			Planner.setVector(TabbedInputPane.getVectorsequence());
			Planner.setEnzymes(enzymes);

			/*
			 * Make a copy because if the user stops the task, a visual panel may call a null reference
			 * that would otherwise have been guaranteed not to be null
			 */
			insert = new Plasmid(Planner.getInsert().getSequence());
			vector = new Plasmid(Planner.getVector().getSequence());

			new SwingWorker<Void, Integer>() {
				Integer id = ProgressPanel.createTask("Recognition Sequence Alignment", enzymes.length, this);

				@Override
				protected Void doInBackground(){

					// Digest the two sequences
					executor = Executors.newFixedThreadPool(totalThreads);
					for(int j = 0; j < totalThreads; j++) {
						executor.execute(new Task(j, totalThreads, id));
					}
					executor.shutdown();
					try {
						executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					return null;
				}

				@Override
				protected void process(List<Integer> progress) {
					if(isCancelled()) {
						ProgressPanel.setDescription(id, "Canceling Task");
						return;
					}
					//					ProgressPanel.increment(id, progress.size());
				}

				@Override
				public void done() {
					if(!isCancelled()) {
						ProgressPanel.setDescription(id, "Writing Results");

						// Once the task completes successfully, set the data to be collected by visual panels
						Planner.setInsert(insert);
						Planner.setVector(vector);

						// Enumerate the data into the tabbed cutting panel
						synchronized(this.getClass()) {
							EnzymeListPane.update();
							SiteListPane.update();
							OverhangListPane.update();
							CuttingDrawPanel.update();
						}
					}
					ProgressPanel.clear(id);
				}
			}.execute();
		}
	}

	private class Task implements Runnable {

		int threadNum;
		int totalThreads;
		Integer id;

		Task(int threadNum, int totalThreads, Integer id) {
			this.threadNum = threadNum;
			this.totalThreads = totalThreads;
			this.id = id;
		}

		String getName() {
			return "Thread " + Integer.toString(threadNum);
		}

		@Override
		public void run() {
			for(int j = enzymes.length*threadNum/totalThreads; j < enzymes.length*(threadNum+1)/totalThreads; j++) {
				enzymes[j].digest(insert);
				enzymes[j].digest(vector);
			}
			ProgressPanel.increment(id, enzymes.length/totalThreads);
		}
	}
}
