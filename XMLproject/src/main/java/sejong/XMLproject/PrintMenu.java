package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sejong.XMLproject.mainMenu;

public class PrintMenu extends JFrame {
	private static Document doc;
	private static JTextArea ta = new JTextArea();
	private static JButton[] colorBtn = new JButton[4];
	private static JButton cancelBtn = new JButton("확인");
//	private static String[] btnName = { "Element 강조", "Text 강조", "Comment 강조", "Attribute 강조" };
//	private static Color[] color = { new Color(102, 102, 255), new Color(102, 153, 102), new Color(204, 102, 204),
//			new Color(204, 153, 051) };
	public static String setStr = "";
	private static int plusWidth=0;

	public PrintMenu() {
		setTitle("Print(White Space 생략)");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout());

//		for (int i = 0; i < colorBtn.length; i++) {
//			colorBtn[i] = new JButton(btnName[i]);
//			colorBtn[i].setBackground(color[i]);
//			panelSouth.add(colorBtn[i]);
//		}
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				setStr="";
				dispose();
			}
		});
		panelSouth.add(cancelBtn);
		c.add(new JScrollPane(ta), BorderLayout.CENTER);
		c.add(panelSouth, BorderLayout.SOUTH);

		doc = mainMenu.doc;
//		ta.setText("");
		ta.setFont(new Font("SanSerif", Font.BOLD, 15));
		traverse(doc.getDocumentElement(), "");
		ta.setText(setStr);
		setStr="";

		setSize(600+plusWidth, 600);
		setVisible(true);
	}

	public void traverse(Node node, String indent) {
		String str = "";
		if (node == null) {
			return;
		}
		int type = node.getNodeType();
		switch (type) {
		case Node.DOCUMENT_NODE:
			str = indent + "[/Document] " + node.getNodeName();
			System.out.println(indent + "[Document] " + node.getNodeName());
			break;
		case Node.ENTITY_NODE:
			str = indent + "[/ENTITY] " + node.getNodeName();
			System.out.println(indent + "[ENTITY] " + node.getNodeName());
			break;
		case Node.ENTITY_REFERENCE_NODE:
			str = indent + "[/ENTITY_REFERENCE] " + node.getNodeName();
			System.out.print(indent + "[ENTITY_REFERENCE] " + node.getNodeName());
			break;
		case Node.CDATA_SECTION_NODE:
			str = indent + "[/CDATA_SECTION] " + node.getNodeName();
			System.out.print(indent + "[CDATA_SECTION] ");
			System.out.print(node.getNodeName());
			System.out.println(" " + node.getNodeValue());
			break;
		case Node.ELEMENT_NODE:	//앨리먼트
			str = indent + "[/Element] " + node.getNodeName();
			System.out.println(indent + "[Element] " + node.getNodeName());
			setStr += indent + "[Element] " + node.getNodeName() + "\n";
			break;
		case Node.COMMENT_NODE:	//주석
			System.out.print(indent + "[COMMENT] ");
			System.out.print(node.getNodeName());
			System.out.print(" " + node.getNodeValue());
			System.out.print("[/COMMENT]");
			setStr += indent + "[COMMNET]" + node.getNodeName() + "[/COMMENT]";
			break;
		case Node.TEXT_NODE:	//텍스트
			if (isWhitespaceNode(node)) {
				break;
			}
			System.out.print(indent + "[TEXT] ");
			System.out.print(node.getNodeName());
			System.out.print(" " + node.getNodeValue());
			System.out.print("[/TEXT]");
			setStr += indent + "[TEXT]" + node.getNodeName() + " " + node.getNodeValue() + "[/TEXT]";
			break;
		}

		if (node.hasAttributes()) {
			NamedNodeMap attr = node.getAttributes();
			for (int i = 0; i < attr.getLength(); i++) {
				System.out.println(
						" " + indent + "[Attribute]" + attr.item(i).getNodeName() + "=" + attr.item(i).getNodeValue());
				setStr += " " + indent + "[Attribute]" + attr.item(i).getNodeName() + "=" + attr.item(i).getNodeValue()
						+ "[/Attribute]\n";
			}
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				if (isWhitespaceNode(children.item(i)))
					continue;
				traverse(children.item(i), indent + "\t");
				plusWidth+=5;
			}
			System.out.println(str);
			setStr += str+"\n";
		}

	}

	private static boolean isWhitespaceNode(Node n) {
		if (n.getNodeType() == Node.TEXT_NODE) {
			String val = n.getNodeValue();
			return val.trim().length() == 0;
		} else {
			return false;
		}
	}
}