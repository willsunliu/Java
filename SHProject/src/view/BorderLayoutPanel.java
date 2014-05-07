package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BorderLayoutPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BorderLayoutPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 5, 10, 10));
	}
	
	public BorderLayoutPanel(int hgap, int vgap) {
		this.setLayout(new BorderLayout(hgap, vgap));
		this.setBorder(new EmptyBorder(10, 5, 10, 10));
	}

}
