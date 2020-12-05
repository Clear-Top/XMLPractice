package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FindMenu extends JFrame{
	private static String[] type = {"검색할 노드의 Type","Element","Text","Attribute","Comment"};
	private static JComboBox<String> cb = new JComboBox<String>(type);
	private static JLabel la = new JLabel("Name");
	private static JTextField tf = new JTextField(10);
	private static JButton btn = new JButton("검색");
	public static JPanel panel2 = new JPanel();
	public static Container c;
	
	public FindMenu() {
		setTitle("Find");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		c = getContentPane();
		c.setLayout(new BorderLayout(10,10));
		
		JPanel panel1 = new JPanel();
		JPanel panel3 = new JPanel();
		
		JButton btnReset = new JButton("초기화");
		JButton btnEnd = new JButton("닫기");
		btnReset.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				panel2.removeAll();
				panel2.validate();
				panel2.repaint();
			}
		});
		btnEnd.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				panel2.removeAll();
				panel2.validate();
				panel2.repaint();
				dispose();
			}
		});
		
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		panel3.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		
		btn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				panel2.removeAll();
				panel2.validate();
				panel2.repaint();
				Node root = mainMenu.doc.getDocumentElement();
				System.out.println("Name : "+tf.getText()+", Type : "+cb.getSelectedItem());
				nodeFind(root, tf.getText(),cb.getSelectedItem().toString());
				c.validate();
				c.repaint();
			}
		});
		cb.setSelectedIndex(0);
		panel1.add(cb);
		panel1.add(la);
		panel1.add(tf);
		panel1.add(btn);
		panel2.setBackground(Color.DARK_GRAY);
		panel3.add(btnReset);
		panel3.add(btnEnd);
		
		c.add(panel1,BorderLayout.NORTH);
		c.add(panel2,BorderLayout.CENTER);
		c.add(panel3,BorderLayout.SOUTH);
		
		setSize(500,400);
		setVisible(true);
	}
	
	public void nodeFind(Node node,String name,String type) {
		int t = 0;
		if(node==null)	return;
		NodeList children = node.getChildNodes();
		if(type.equals("Element"))
			t = Node.ELEMENT_NODE;
		else if(type.equals("Attribute")) {
			t = Node.ATTRIBUTE_NODE;
		}else if(type.equals("Text")) {
			t = Node.TEXT_NODE;
		}else if(type.equals("Comment")) {
			t = Node.COMMENT_NODE;
		}
		for(int i=0;i<children.getLength();i++) {
			Node child = children.item(i);
			if(child.getNodeName().equals(name)&&child.getNodeType()==t) {
				String nodeName = child.getNodeName();
				String nodeValue = child.getNodeValue();
				short nodeType = child.getNodeType();
				String parentNodeName = child.getParentNode().getNodeName();
				int depth = depth(child);
				int sibling = sibling(child);
				JButton ne = new structNode(nodeName, nodeValue, parentNodeName, nodeType,depth,sibling);
				panel2.add(ne);
				ne.repaint();
			}
			nodeFind(child, name,type);
		}
	}
	
	public int depth(Node node) {
		int index=0;
		while((node=node.getParentNode())!=null) {
			index++;
		}
		return index;
	}
	
	public int sibling(Node node) {
		int index=1;
		while((node=node.getPreviousSibling())!=null) {
			index++;
		}
		return index;
	}
}

class structNode extends JButton{
	static short nodeType;
	static String nodeName;
	static String nodeValue;
	static String parentNodeName;
	static int depth;
	static int sibling;
	public structNode(String nodeName, String nodeValue, String parentNodeName, short nodeType, int depth, int sibling){
		this.nodeName = nodeName;
		this.nodeValue = nodeValue;
		this.nodeType = nodeType;
		this.parentNodeName = parentNodeName;
		
		this.setText(nodeName);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.print("노드종류 : ");
				if(nodeType==Node.ELEMENT_NODE) {
					System.out.println("ELEMENT_NODE");
				}else if(nodeType==Node.TEXT_NODE) {
					System.out.println("TEXT_NODE");
				}else if(nodeType==Node.COMMENT_NODE) {
					System.out.println("COMMENT_NODE");
				}else if(nodeType==Node.ATTRIBUTE_NODE) {
					System.out.println("ATTRIBUTE_NODE");
				}
				System.out.println("노드이름 :"+nodeName);
				System.out.println("노드 값 : "+nodeValue);
				System.out.println("부모노드이름 : "+parentNodeName);
				System.out.println("root로부터 깊이 : "+depth);
				System.out.println("몇번째 자식 : "+sibling);
			}
		});
		
		System.out.println(nodeName+"생성완료!");
	}
}