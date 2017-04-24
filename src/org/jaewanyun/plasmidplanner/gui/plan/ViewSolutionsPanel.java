package org.jaewanyun.plasmidplanner.gui.plan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Comparator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.jaewanyun.plasmidplanner.Planner;
import org.jaewanyun.plasmidplanner.Solution;
import org.jaewanyun.plasmidplanner.gui.GUIsettings;
import org.jaewanyun.plasmidplanner.gui.MainTabbedPane;
import org.jaewanyun.plasmidplanner.gui.cut.CuttingDrawPanel;
import org.jaewanyun.plasmidplanner.gui.cut.TabbedCuttingPane;

public class ViewSolutionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JTable solutionTable;
	private static CellRenderer cr;
	private static ViewSolutionsPanel viewSolutionsPanel;

	private ViewSolutionsPanel() {
		cr = new CellRenderer();

		JLabel temporaryLabel = new JLabel("Run the automated planner to view solutions");
		temporaryLabel.setBackground(GUIsettings.textAreaColor);
		add(temporaryLabel);

		// UI
		setBackground(GUIsettings.textAreaColor);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void update() {
		viewSolutionsPanel.setLayout(new BorderLayout());
		viewSolutionsPanel.removeAll();

		solutionTable = new JTable(new DefaultTableModel(new Object[]{"Insert Left Enzyme", "Vector Left Enzyme", "Insert Right Enzyme", "Vector Right Enzyme", "Size of Final Vector", "Remove Row"}, 0));
		solutionTable.setEnabled(true);
		solutionTable.setRowHeight(60);

		DefaultTableModel model = (DefaultTableModel) solutionTable.getModel();
		TableRowSorter trs = new TableRowSorter<>(model);

		/*
		 * Add sorter
		 */
		solutionTable.setAutoCreateRowSorter(true);
		class IntComparator implements Comparator {
			@Override
			public int compare(Object o1, Object o2) {
				Integer int1 = (Integer) o1;
				Integer int2 = (Integer) o2;
				return int1.compareTo(int2);
			}
			@Override
			public boolean equals(Object o2) {
				return this.equals(o2);
			}
			@Override
			public int hashCode() {
				return super.getClass().hashCode();
			}
		}
		trs.setComparator(4, new IntComparator());
		solutionTable.setRowSorter(trs);
		solutionTable.getRowSorter().toggleSortOrder(4);

		/*
		 * Range of inputs was probably not valid
		 */
		Solution[] solutions = Planner.getSolutions();
		if(solutions == null)
			return;

		/*
		 * Add solutions data to the table
		 */
		for(int j = 0; j < solutions.length; j++) {
			model.addRow(new Object[]{"<html>Enzyme Name: " + (solutions[j].getInsertLeftEnzyme() + "<br>Cut Location: " + solutions[j].getInsertLeftLocation() + "<br>Overhang: " + solutions[j].getInsertLeftOverhang() + "</html>"),
			                          "<html>Enzyme Name: " + (solutions[j].getVectorLeftEnzyme() + "<br>Cut Location: " + solutions[j].getVectorLeftLocation() + "<br>Overhang: " + solutions[j].getVectorLeftOverhang() + "</html>"),
			                          "<html>Enzyme Name: " + (solutions[j].getInsertRightEnzyme() + "<br>Cut Location: " + solutions[j].getInsertRightLocation() + "<br>Overhang: " + solutions[j].getInsertRightOverhang() + "</html>"),
			                          "<html>Enzyme Name: " + (solutions[j].getVectorRightEnzyme() + "<br>Cut Location: " + solutions[j].getVectorRightLocation() + "<br>Overhang: " + solutions[j].getVectorRightOverhang() + "</html>"),
			                          solutions[j].getFinalSize(),
			                          "<html>Remove<br>solution</html>"
			});
		}

		/*
		 * Color code rows according to overhang types
		 */
		TableColumnModel tcModel = solutionTable.getColumnModel();
		tcModel.getColumn(0).setCellRenderer(cr);
		tcModel.getColumn(1).setCellRenderer(cr);
		tcModel.getColumn(2).setCellRenderer(cr);
		tcModel.getColumn(3).setCellRenderer(cr);

		/*
		 * Add buttons to the view column
		 */
		@SuppressWarnings("serial")
		Action switchView = new AbstractAction() {
			@Override
			public synchronized void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());

				String ile = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
				String vle = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 1);
				String ire = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 2);
				String vre = (String) ((DefaultTableModel)table.getModel()).getValueAt(modelRow, 3);

				CuttingDrawPanel.addEnzymeToView(
						parseEnzyme(ile),
						parseEnzyme(vle),
						parseEnzyme(ire),
						parseEnzyme(vre),
						parseInt(ile),
						parseInt(vle),
						parseInt(ire),
						parseInt(vre));

				TabbedCuttingPane.setSelectedInsertDrawPanel();
				MainTabbedPane.setSelectedTabbedCuttingPane();
			}

			private String parseEnzyme(String unparsed) {
				StringBuilder sb = new StringBuilder();
				for(int j = 19; j < unparsed.length(); j++) {
					if(unparsed.charAt(j) == '<')
						break;
					sb.append(unparsed.charAt(j));
				}
				return sb.toString();
			}

			private int parseInt(String unparsed) {
				StringBuilder sb = new StringBuilder();
				for(int j = unparsed.indexOf("Cut Location") + 14; j < unparsed.length(); j++) {
					if(unparsed.charAt(j) == '<')
						break;
					sb.append(unparsed.charAt(j));
				}
				return Integer.parseInt(sb.toString());
			}
		};
		ButtonColumn buttonColumn1 = ButtonColumn.getButtonColumn(solutionTable, switchView, 4);
		buttonColumn1.setMnemonic(KeyEvent.VK_V);

		/*
		 * Add buttons to the delete column
		 */
		@SuppressWarnings("serial")
		Action delete = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable)e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				((DefaultTableModel)table.getModel()).removeRow(modelRow);
			}
		};
		ButtonColumn.addButtonsToTable(solutionTable, delete, 5);

		/*
		 * Add to scroll pane
		 */
		JScrollPane scrollPane = new JScrollPane(solutionTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		viewSolutionsPanel.add(scrollPane, BorderLayout.CENTER);

		// Redraw
		viewSolutionsPanel.revalidate();
		viewSolutionsPanel.repaint();

		// UI
		solutionTable.setBackground(GUIsettings.textAreaColor);
		viewSolutionsPanel.setBackground(GUIsettings.textAreaColor);
	}

	class CellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			String overhang1 = (String) table.getValueAt(row, 0);
			int index = overhang1.indexOf("Overhang:");
			StringBuilder sb = new StringBuilder();
			for(int j = index + 10; j < overhang1.length() - 7; j++) {
				sb.append(overhang1.charAt(j));
			}
			overhang1 = sb.toString();

			String overhang2 = (String) table.getValueAt(row, 2);
			index = overhang2.indexOf("Overhang:");
			sb = new StringBuilder();
			for(int j = index + 10; j < overhang2.length() - 7; j++) {
				sb.append(overhang2.charAt(j));
			}
			overhang2 = sb.toString();

			if(((String) table.getValueAt(row, 0)).contains("Blunt")) {
				setOpaque(true);
				setBackground(Color.RED);
			} else if(((String) table.getValueAt(row, 1)).contains("Blunt")) {
				setOpaque(true);
				setBackground(Color.RED);
			} else if(((String) table.getValueAt(row, 2)).contains("Blunt")) {
				setOpaque(true);
				setBackground(Color.RED);
			} else if(((String) table.getValueAt(row, 3)).contains("Blunt")) {
				setOpaque(true);
				setBackground(Color.RED);
			} else if(overhang1.equals(overhang2)) {
				setOpaque(true);
				setBackground(Color.GREEN);
			} else {
				setOpaque(false);
			}

			return this;
		}
	}

	static ViewSolutionsPanel getViewSolutionsPanel() {
		return viewSolutionsPanel == null ? viewSolutionsPanel = new ViewSolutionsPanel(): viewSolutionsPanel;
	}
}