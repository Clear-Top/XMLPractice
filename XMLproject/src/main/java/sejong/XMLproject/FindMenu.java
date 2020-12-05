package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	public static JLabel showLa = new JLabel();
	public static JTextArea ta = new JTextArea(20,30);
	public static NodeList selectedNodeChildren=null;
	
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
		
		setSize(500,600);
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
				selectedNodeChildren = child.getChildNodes();
				JButton ne = new structNode(selectedNodeChildren,nodeName, nodeValue, parentNodeName, nodeType,depth,sibling);
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
			if(node.getNodeType()==Node.TEXT_NODE)
				continue;
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
	
	public structNode(NodeList children,String nodeName, String nodeValue, String parentNodeName, short nodeType, int depth, int sibling){
		this.nodeName = nodeName;
		this.nodeValue = nodeValue;
		this.nodeType = nodeType;
		this.parentNodeName = parentNodeName;
		
		this.setText(nodeName);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				FindMenu.ta.setText("");
				String str = "노드종류 : ";
				if(nodeType==Node.ELEMENT_NODE) {
					str+="ELEMENT";
				}else if(nodeType==Node.TEXT_NODE) {
					str+="TEXT";
				}else if(nodeType==Node.COMMENT_NODE) {
					str+="COMMENT";
				}else if(nodeType==Node.ATTRIBUTE_NODE) {
					str+="ATTRIBUTE";
				}
				
				str+="\n\n";
				str+="노드이름 :"+nodeName+"\n\n";
				str+="노드 값 : "+nodeValue+"\n\n";
				str+="부모노드이름 : "+parentNodeName+"\n\n";
				str+="root로부터 깊이 : "+depth+"\n\n";
				str+="몇번째 자식 : "+sibling+"\n\n";
				str+="자식앨리먼트 개수(공백텍스트제외) : "+countChild(children)+"\n\n";
				str+="자식앨리먼트\n";
				FindMenu.ta.setText(str);
				showChildren(children);
				FindMenu.ta.setFont(new Font("Serif",Font.BOLD,14));
				FindMenu.ta.setForeground(Color.BLACK);
				FindMenu.ta.setEditable(false);
				JScrollPane scroll = new JScrollPane(FindMenu.ta);
				FindMenu.panel2.add(scroll);
				FindMenu.panel2.validate();
				FindMenu.panel2.repaint();
			}
		});
//		System.out.println(nodeName+"생성완료!");
	}
	
	public int countChild(NodeList childs) {
		int index=0;
		for(int i=0;i<childs.getLength();i++) {
			Node item = childs.item(i);
			if(item.getNodeType()==Node.COMMENT_NODE||item.getNodeType()==Node.ATTRIBUTE_NODE||item.getNodeType()==Node.TEXT_NODE)
				continue;
			index++;
		}
		return index;
	}
	
	public void showChildren(NodeList childs) {
		for(int i=0;i<childs.getLength();i++) {
			Node item = childs.item(i);
			if(item.getNodeType()==Node.COMMENT_NODE||item.getNodeType()==Node.ATTRIBUTE_NODE||item.getNodeType()==Node.TEXT_NODE)
				continue;
			FindMenu.ta.append(item.getNodeName()+"\n");
		}
	}
}