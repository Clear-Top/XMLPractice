package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FindMenu extends JFrame{
	private static String[] type = {"검색할 노드의 Type","Element","Text","Attribute","Comment"};
	private static JComboBox<String> cb = new JComboBox<String>(type);
	private static JLabel la = new JLabel("Name");
	private static JTextField tf = new JTextField(10);
	private static JButton btn = new JButton("검색");
	
	public FindMenu() {
		setTitle("Find");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout(10,10));
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		
		cb.setSelectedIndex(0);
		panel1.add(cb);
		panel1.add(la);
		panel1.add(tf);
		panel1.add(btn);
		panel2.setBackground(Color.DARK_GRAY);
		
		c.add(panel1,BorderLayout.NORTH);
		c.add(panel2,BorderLayout.CENTER);
		
		setSize(400,400);
		setVisible(true);
	}
}

class structNode{
	static String nodeType;
	static String nodeName;
	static String nodeValue;
	static String parentNodeName;
	static int depth;
	structNode(){
		
	}
}