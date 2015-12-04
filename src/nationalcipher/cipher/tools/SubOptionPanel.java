package nationalcipher.cipher.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class SubOptionPanel extends JPanel {
	
	public SubOptionPanel(String label, JComponent field) {
		this(new JLabel(label), field);
	}
	
	public SubOptionPanel(JLabel label, JComponent field) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		label.setMinimumSize(new Dimension(190, 20));
		label.setPreferredSize(new Dimension(190, 20));
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		field.setMinimumSize(new Dimension(170, 20));
		field.setPreferredSize(new Dimension(170, 20));
		
		this.add(label);
		this.add(field);
	}

	@Override
	public SubOptionPanel add(Component comp) {
		super.add(comp);
		return this;
	}
}