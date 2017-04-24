package org.jaewanyun.plasmidplanner;

import javax.swing.SwingUtilities;

import org.jaewanyun.plasmidplanner.gui.MainFrame;

public class Main {

	public static MainFrame mainFrame;

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					mainFrame = MainFrame.createGUI();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


/*
 *  TODO
 *
 *  Automate length calculation
 *  Selection -> No enzyme hit - let the user know
 *  Re-digest -> enzyme
 *  Check if only one enzyme is chosen -> same recognition sites
 *
 *
 *  Error
 *  	at org.jaewanyun.plasmidplanner.gui.cut.CuttingDrawPanel$DrawPanel.paintComponent(CuttingDrawPanel.java:755)
 *
 *
 *
 *  Low
 *  Enzyme list popup
 *
 */


/**
VALIDATION
----------

BSKS bx MX Shuttle	689-3700	 length 3011
CMV-mCherry 		6942-2471	 length 2589

Solution #1
	Insert			BspHI-AclI
	Vector			StyI -NarI
	total	7517
Validation #1
	Insert			6825-3082
		len 3420
	Vector			3569-1738
		len 4097
	total	7517

Solution #2
	Insert			BspHI-BspMI
	Vector			StyI-StyI
	total	7621
Validtion #2
	Insert			6825-3033
		len 3371
	Vector			3416-1738
		len 4250
	total	7621
 */
