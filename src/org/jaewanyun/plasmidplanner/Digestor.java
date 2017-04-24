package org.jaewanyun.plasmidplanner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import org.jaewanyun.plasmidplanner.gui.ProgressPanel;
import org.jaewanyun.plasmidplanner.gui.cut.CuttingDrawPanel;
import org.jaewanyun.plasmidplanner.gui.cut.EnzymeListPane;
import org.jaewanyun.plasmidplanner.gui.cut.OverhangListPane;
import org.jaewanyun.plasmidplanner.gui.cut.SiteListPane;
import org.jaewanyun.plasmidplanner.gui.plan.PlanningPanel;

public class Digestor {

	private static volatile Enzyme[] enzymes;
	private static boolean digestInsert;
	private static boolean digestVector;
	private static final int totalThreads = 200;
	private static ExecutorService executor;
	private static volatile Plasmid insert;
	private static volatile Plasmid vector;

	public static void digest(String insertSequence, String vectorSequence, boolean planAfterDigest) {
		enzymes = Enzyme.importFromFile(Digestor.class.getResourceAsStream("/enzymes/enzymes.txt"));
		// TODO: Enzyme has changed
		Planner.setEnzymes(enzymes);

		/*
		 * Check if digest of the current input has already been computed
		 */
		digestInsert = true;
		Plasmid lastInsert = Planner.getInsert();
		if(lastInsert != null) {
			String insert = insertSequence;
			if(insert.length() < 1)
				digestInsert = false;
			if(lastInsert.getSequence().equals(insert))
				digestInsert = false;
		}
		digestVector = true;
		Plasmid lastVector = Planner.getVector();
		if(lastVector != null) {
			String vector = vectorSequence;
			if(vector.length() < 1)
				digestVector = false;
			if(lastVector.getSequence().equals(vector))
				digestVector = false;
		}

		/*
		 * Give Planner String of sequence
		 * Make a copy because if the user stops the task, a visual panel may call a null reference
		 * that would otherwise have been guaranteed not to be null
		 */
		if(digestInsert) {
			Planner.setInsert(insertSequence);
			insert = new Plasmid(insertSequence);
		}
		if(digestVector) {
			Planner.setVector(vectorSequence);
			vector = new Plasmid(vectorSequence);
		}

		new SwingWorker<Void, Integer>() {
			Integer id = ProgressPanel.createTask("Aligning Recognition Sequence", enzymes.length, this);

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
			}

			@Override
			public void done() {
				// If this worker is canceled, do not update or set any results in place
				if(!isCancelled()) {
					ProgressPanel.setDescription(id, "Writing Results");

					/*
					 * Once the task completes successfully, set the data to be collected by visual panels
					 */
					if(digestInsert)
						Planner.setInsert(insert);
					if(digestVector)
						Planner.setVector(vector);

					// Enumerate the data into the tabbed cutting panel
					synchronized(this.getClass()) {
						if(digestInsert || digestVector) {
							/*
							 * Cutting panels
							 */
							EnzymeListPane.update();
							SiteListPane.update();
							OverhangListPane.update();
							CuttingDrawPanel.update();

							/*
							 * Planning panels
							 */
							PlanningPanel.update();
						}
					}
				}
				ProgressPanel.clear(id);

				if(planAfterDigest) {
					Planner.plan(PlanningPanel.getInsertStart(),
							PlanningPanel.getInsertEnd(),
							PlanningPanel.getVectorStart(),
							PlanningPanel.getVectorEnd());
				}
			}
		}.execute();
	}

	private static class Task implements Runnable {

		int threadNum;
		int totalThreads;
		Integer id;

		private Task(int threadNum, int totalThreads, Integer id) {
			this.threadNum = threadNum;
			this.totalThreads = totalThreads;
			this.id = id;
		}

		@Override
		public void run() {
			for(int j = enzymes.length*threadNum/totalThreads; j < enzymes.length*(threadNum+1)/totalThreads; j++) {
				if(digestInsert)
					enzymes[j].digest(insert);
				if(digestVector)
					enzymes[j].digest(vector);
			}
			ProgressPanel.increment(id, enzymes.length/totalThreads);
		}
	}
}
