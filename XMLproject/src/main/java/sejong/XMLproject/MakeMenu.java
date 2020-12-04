package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sejong.XMLproject.mainMenu;

public class MakeMenu extends JFrame{
	
	public MakeMenu() {
//		System.out.println("Make기능을 실행합니다.");
		
		// 루트 노드와 껍데기 DOM Tree 만들기 (연결)
		Document doc = new DocumentImpl();
		String rootName = JOptionPane.showInputDialog("Root노드의 이름을 입력해주세요.");
		Element root = doc.createElement(rootName);
		root.appendChild(doc.createTextNode("\n"));
		doc.appendChild(root);
		startMake(doc);
	}
	public void startMake(Document doc) {
		setTitle("Make");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		// 창닫힐때 mainMenu로 DOMtree 넘기기
		mainMenu.doc = doc;
		
		setSize(300,400);
		setVisible(true);
	}
}
