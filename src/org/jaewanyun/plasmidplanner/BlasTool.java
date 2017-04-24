//package org.jaewanyun.plasmidplanner;
//
//import java.io.OutputStreamWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.UnknownHostException;
//
//import javax.swing.SwingWorker;
//
//import org.jaewanyun.plasmidplanner.gui.ProgressPanel;
//import org.jaewanyun.plasmidplanner.gui.blast.BlastTextPane;
//import org.jaewanyun.plasmidplanner.gui.blast.TabbedBlastPane;
//
//import com.algosome.eutils.blast.Blast;
//import com.algosome.eutils.blast.BlastParser;
//import com.algosome.eutils.blast.GetCommand;
//import com.algosome.eutils.blast.PutCommand;
//
//public class BlasTool {
//
//	public static synchronized void blast(String insert, String vector) {
//		new SwingWorker<Void, String>() {
//			Integer id = ProgressPanel.createTask("Awaiting BLAST Results", 100, this);
//			volatile boolean stop = false;
//
//			@Override
//			protected Void doInBackground() {
//				/*
//				 * Check for connection with the database
//				 */
//				try {
//					URL url = new URL("http://www.ncbi.nlm.nih.gov/blast/Blast.cgi");
//					URLConnection conn = url.openConnection();
//					conn.setDoOutput(true);
//					conn.setRequestProperty("user-agent", "Mozilla/5.0");
//					OutputStreamWriter out;
//					try {
//						out = new OutputStreamWriter(conn.getOutputStream());
//					} catch (UnknownHostException uhe) {
//						uhe.printStackTrace();
//						return null;
//					}
//					out.flush();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//
//				new Thread(() -> {
//					int progress = 0;
//					while(!stop) {
//						ProgressPanel.setProgress(id, progress);
//						progress = (progress + 1) % 101;
//						try {
//							Thread.sleep(40);
//						} catch (Exception e) {}
//					}
//				}).start();
//
//				/*
//				 * Run BLAST on insert DNA
//				 */
//				Thread insertBlastThread = null;
//				Thread vectorBlastThread = null;
//
//				if(insert.length() > 0) {
//					System.out.println("Running insert");
//					PutCommand put = new PutCommand();
//					put.setQuery(insert);
//					put.setProgram("blastn");
//					put.setDatabase("nr");
//					GetCommand get = new GetCommand(new BlastParser(){
//						@Override
//						public void parseBlastOutput(String output) {
//							BlastTextPane.update(output, true); // Specify that it is an insert
//							TabbedBlastPane.update(output, true);
//						}
//					});
//					get.setFormatType("Text");
//					Blast blast = new Blast(put, get);
//					insertBlastThread = new Thread(blast);
//					insertBlastThread.start();
//				}
//				if(vector.length() > 0) {
//					System.out.println("Running vector");
//					PutCommand put = new PutCommand();
//					put.setQuery(vector);
//					put.setProgram("blastn");
//					put.setDatabase("nr");
//					GetCommand get = new GetCommand(new BlastParser(){
//
//						// TODO: For visual
//						@Override
//						public void parseBlastOutput(String output) {
//							BlastTextPane.update(output, false); // Specify that it is a vector
//							TabbedBlastPane.update(output, false);
//						}
//					});
//					get.setFormatType("Text");
//					Blast blast = new Blast(put, get);
//					vectorBlastThread = new Thread(blast);
//					vectorBlastThread.start();
//				}
//
//				try {
//					if(insertBlastThread != null)
//						insertBlastThread.join();
//					if(vectorBlastThread != null)
//						vectorBlastThread.join();
//				} catch (InterruptedException ie) {}
//
//				return null;
//			}
//
//			@Override
//			protected void done() {
//				stop = true;
//				ProgressPanel.clear(id);
//			}
//
//		}.execute();
//	}
//}
