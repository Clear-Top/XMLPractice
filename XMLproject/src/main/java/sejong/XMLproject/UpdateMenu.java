package sejong.XMLproject;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.awt.*;
import java.awt.event.*;

/*
 * 아이디어
 * 1. NodeList<>를 네개로 두어 묶는다
 * 2. 하나씩 들여다보면서 수정하고 싶은내용을 수정
 */

public class UpdateMenu extends JFrame {
	private Document doc;
	public static JComboBox<Object> ele = new JComboBox<Object>();
	public static JComboBox<Object> text = new JComboBox<Object>();
	public static JComboBox<Object> com = new JComboBox<Object>();
	public static int flag=0;

	public UpdateMenu(Document doc) {
		// TODO Auto-generated constructor stub
		this.doc = doc; // 메인doc이랑 연결
		setTitle("Update");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());

		Node root = doc.getDocumentElement(); // 루트노드
		String str = "수정할 노드이름을 선택해주세요.";
		ele.addItem(str);
		text.addItem(str);
		com.addItem(str);
		ele.setSelectedIndex(0);
		text.setSelectedIndex(0);
		com.setSelectedIndex(0);
		traverse(root);

		JLabel la1 = new JLabel("Element 모음 (앨리먼트이름 & 속성값 수정)");
		JLabel la2 = new JLabel("Text 모음 (텍스트내용 수정)");
		JLabel la3 = new JLabel("Comments 모음 (주석내용 수정)");
		la1.setFont(new Font("Gothic", Font.BOLD, 14));
		la2.setFont(new Font("Gothic", Font.BOLD, 14));
		la3.setFont(new Font("Gothic", Font.BOLD, 14));
		la1.setForeground(Color.RED);
		la2.setForeground(Color.RED);
		la3.setForeground(Color.RED);

		la1.setPreferredSize(new Dimension(300, 40));
		la2.setPreferredSize(new Dimension(300, 40));
		la3.setPreferredSize(new Dimension(300, 40));
		ele.setPreferredSize(new Dimension(300, 40));
		ele.setFont(new Font("Gothic", Font.BOLD, 13));
		text.setPreferredSize(new Dimension(300, 40));
		text.setFont(new Font("Gothic", Font.BOLD, 13));
		com.setPreferredSize(new Dimension(300, 40));
		com.setFont(new Font("Gothic", Font.BOLD, 13));

		la1.setHorizontalAlignment(JLabel.CENTER);
		la2.setHorizontalAlignment(JLabel.CENTER);
		la3.setHorizontalAlignment(JLabel.CENTER);

		c.add(la1);
		c.add(ele);

		c.add(la2);
		c.add(text);

		c.add(la3);
		c.add(com);

		JButton okBtn = new JButton("확인");
		JButton cancelBtn = new JButton("닫기");
		c.add(okBtn);
		c.add(cancelBtn);

		ele.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				text.setEnabled(false);
				com.setEnabled(false);
				flag=Node.ELEMENT_NODE;
			}
		});
		text.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ele.setEnabled(false);
				com.setEnabled(false);
				flag=Node.ATTRIBUTE_NODE;
			}
		});
		com.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ele.setEnabled(false);
				text.setEnabled(false);
				flag=Node.COMMENT_NODE;
			}
		});

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// 선택한 노드에 대한 정보를 출력
				
				if (flag==Node.ELEMENT_NODE) {
					Object element = ele.getSelectedItem();
					E newElement = new E(element);
				} else if (flag==Node.ATTRIBUTE_NODE) {
					Object tex = text.getSelectedItem();
					T newText = new T(tex);
				} else if (flag==Node.COMMENT_NODE) {
					Object comment = com.getSelectedItem();
					C newComment = new C(comment);
				}

//				System.out.println(ele.getSelectedItem());
//				System.out.println(text.getSelectedItem());
//				System.out.println(com.getSelectedItem());
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		ele.setEnabled(true);
		text.setEnabled(true);
		com.setEnabled(true);
		
		setResizable(false);
		setSize(380, 400);
		setVisible(true);
	}

	public void traverse(Node node) {
		if (node == null)
			return;

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				ele.addItem(children.item(i));
			} else if (children.item(i).getNodeType() == Node.COMMENT_NODE) {
				com.addItem(children.item(i));
			} else if (children.item(i).getNodeType() == Node.TEXT_NODE) {
				if (!isWhitespaceNode(children.item(i))) {
					text.addItem(children.item(i));
				}
			} else {
				System.out.println("앨리먼트, 속성, 주석, 텍스트말고는 처리하지 않았습니다..");
			}
			traverse(children.item(i)); // 재귀함수시작
		}
	}

	public Object findNode(Object node, String name) {
		if (node == null)
			return null;
		NodeList children = ((Element) node).getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName().equals(name)) {
				return children.item(i);
			}
		}
		return null;
	}

	public boolean isWhitespaceNode(Node n) {
		if (n.getNodeType() == Node.TEXT_NODE) {
			String val = n.getNodeValue();
			return val.trim().length() == 0;
		} else {
			return false;
		}
	}
}

class E extends JFrame {
	private String[] str = { "Element 이름", "Element 값", "Attribute 이름", "Attribute 값" };
	private JLabel[] la = new JLabel[4];
	private JTextField[] tf = new JTextField[4];
	private JComboBox<Object> attr = new JComboBox<Object>();
	private String name;
	private String attValue;

	public E(Object node) {
		// TODO Auto-generated constructor stub
		setTitle("Element");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());

		NamedNodeMap att = ((Element) node).getAttributes();
		for (int i = 0; i < att.getLength(); i++) {
			attr.addItem(att.item(i));
		}

		for (int i = 0; i < 4; i++) {
			name = ((Element) node).getNodeName();
			String value = ((Element) node).getNodeValue();
			la[i] = new JLabel(str[i]);
			la[i].setPreferredSize(new Dimension(200, 40));
			la[i].setFont(new Font("Gothic", Font.BOLD, 14));
			la[i].setHorizontalAlignment(JLabel.CENTER);
			c.add(la[i]);
			if (i != 2) {
				tf[i] = new JTextField(15);
				tf[i].setPreferredSize(new Dimension(200, 40));
				tf[i].setFont(new Font("Gothic", Font.BOLD, 14));
				c.add(tf[i]);
			} else {
				attr.setPreferredSize(new Dimension(200, 40));
				c.add(attr);
			}
			if (i == 0) {
				tf[i].setText(name);
			}
			if (i == 1) {
				tf[i].setText("null");
				tf[i].setEditable(false);
			}
			if (i == 3) {
				tf[i].setText(((Node)node).getNodeValue());
			}
		}
		attr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				tf[3].setText(((Node) attr.getSelectedItem()).getNodeValue());
			}
		});

		JButton okBtn = new JButton("수정하기");
		JButton cancelBtn = new JButton("나가기");

		c.add(okBtn);
		c.add(cancelBtn);

		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				UpdateMenu.text.setEnabled(true);
				UpdateMenu.com.setEnabled(true);
			}
		});

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Node selectedNode = (Node) attr.getSelectedItem();
				NamedNodeMap a = selectedNode.getAttributes();
				if (!name.equals(tf[0].getText())) {
//					System.out.println("앨리먼트 이름 변경확인\n 변경사항을 적용합니다.");
					try {
						JOptionPane.showMessageDialog(null, name + "->" + tf[0].getText() + "로 변경을 시도합니다.");
						((Element) node).getOwnerDocument().renameNode((Node) node, null, tf[0].getText());
						JOptionPane.showMessageDialog(null, "Successfully!");
						dispose();
					} catch (Exception e) {
						// TODO: handle exception
						JOptionPane.showMessageDialog(null, "Fail...", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
//				System.out.println(selectedNode.getNodeName());
//				System.out.println(selectedNode.getNodeValue());
				if (!selectedNode.getNodeValue().equals(tf[3].getText())) {
					try {
						JOptionPane.showMessageDialog(null,
								selectedNode.getNodeValue() + "속성값 -> " + tf[3].getText() + "속성값으로 변경을 시도합니다.");
						((Element) node).setAttribute(selectedNode.getNodeName(), tf[3].getText());
						JOptionPane.showMessageDialog(null, "Successfully!");
						dispose();
					} catch (Exception e) {
						// TODO: handle exception
						JOptionPane.showMessageDialog(null, "Fail...", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		setResizable(false);
		setSize(250, 450);
		setVisible(true);
	}
}

class T extends JFrame {
	public T(Object node) {
		// TODO Auto-generated constructor stub
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				UpdateMenu.ele.setEnabled(true);
				UpdateMenu.com.setEnabled(true);
			}
		});
		
		setTitle("Text (부모노드:"+((Text)node).getParentNode().getNodeName()+")");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JLabel la1 = new JLabel("기존 내용 (복사가능 & 편집불가)");
		JLabel la2 = new JLabel("변경할 내용");
		
		JTextArea ta1 = new JTextArea();
		JTextArea ta2 = new JTextArea();
		ta1.setPreferredSize(new Dimension(300, 100));
		ta2.setPreferredSize(new Dimension(300, 100));
		
		JButton okBtn = new JButton("수정하기");
		JButton cancelBtn = new JButton("나가기");
		
		ta1.setText(((Text)node).getTextContent());
		ta1.setEditable(false);
		ta2.setText(((Text)node).getTextContent());
		
		c.add(la1);
		c.add(new JScrollPane(ta1));
		c.add(la2);
		c.add(new JScrollPane(ta2));
		c.add(okBtn);
		c.add(cancelBtn);
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					JOptionPane.showMessageDialog(null, "텍스트 값 변경을 시도합니다.");
					((Text)node).setTextContent(ta2.getText());
					JOptionPane.showMessageDialog(null, "Successfully!");
					dispose();
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Fail...", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		setResizable(false);
		setSize(350,350);
		setVisible(true);
	}
}

class C extends JFrame {
	public C(Object node) {
		// TODO Auto-generated constructor stub
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				UpdateMenu.ele.setEnabled(true);
				UpdateMenu.text.setEnabled(true);
			}
		});
		
		setTitle("Comment (부모노드:"+((Comment)node).getParentNode().getNodeName()+")");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JLabel la1 = new JLabel("기존 내용 (복사가능 & 편집불가)");
		JLabel la2 = new JLabel("변경할 내용");
		
		JTextArea ta1 = new JTextArea();
		JTextArea ta2 = new JTextArea();
		ta1.setPreferredSize(new Dimension(325, 125));
		ta2.setPreferredSize(new Dimension(325, 125));
		
		JButton okBtn = new JButton("수정하기");
		JButton cancelBtn = new JButton("나가기");
		
		ta1.setText(((Comment)node).getTextContent());
		ta1.setEditable(false);
		ta2.setText(((Comment)node).getTextContent());
		
		c.add(la1);
		c.add(new JScrollPane(ta1));
		c.add(la2);
		c.add(new JScrollPane(ta2));
		c.add(okBtn);
		c.add(cancelBtn);
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					JOptionPane.showMessageDialog(null, "텍스트 값 변경을 시도합니다.");
//					((Comment)node).setTextContent(ta2.getText());
					((Comment)node).setNodeValue(ta2.getText());
//					((Comment)node).setTextContent(ta2.getText());
					JOptionPane.showMessageDialog(null, "Successfully!");
					dispose();
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "Fail...", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		setResizable(false);
		setSize(400,400);
		setVisible(true);
	}
}
