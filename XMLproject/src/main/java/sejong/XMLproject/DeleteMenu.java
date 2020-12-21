package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sejong.XMLproject.InsertMenu.ElementFrame;
import sejong.XMLproject.InsertMenu.myMouse;

public class DeleteMenu extends JFrame {
	public static Document doc = mainMenu.doc;
	public static Node root = mainMenu.doc.getDocumentElement();
	public static JPanel northPanel = new JPanel();
	public static JPanel southPanel = new JPanel();
	private static String[] nodeName = { "Element", "Attribute", "Comment", "Text" };
	public static JComboBox<String> cb = new JComboBox<String>(nodeName);

	public static Node currentNodePointer = root;
	public static Node currentSelectPoint = null;

	public DeleteMenu() {
		setTitle("Insert (현재위치 : " + currentNodePointer.getNodeName() + ")");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		northPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new FlowLayout());

		// 루트노드부터 시작
		JButton backBtn = new JButton("부모로 이동");
		JButton resetBtn = new JButton("초기화");
		JButton startBtn = new JButton("조회");
		JButton cancelBtn = new JButton("나가기");

		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (currentNodePointer.getParentNode() == doc) {
					JOptionPane.showMessageDialog(null, "현재노드가 최상위 노드입니다.");
				} else {
					currentNodePointer = currentNodePointer.getParentNode();
					northPanel.removeAll();
					northPanel.validate();
					northPanel.repaint();
					setTitle("Delete (현재위치 : " + currentNodePointer.getNodeName() + ")");
					makeFrame(currentNodePointer);
				}
			}
		});

		resetBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				currentNodePointer = root;
				northPanel.removeAll();
				northPanel.validate();
				northPanel.repaint();
				setTitle("Delete (현재위치 : " + root.getNodeName() + ")");
			}
		});

		startBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				makeFrame(currentNodePointer);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				southPanel.removeAll();
				southPanel.revalidate();
				southPanel.repaint();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				southPanel.removeAll();
				southPanel.revalidate();
				southPanel.repaint();
			}
		});
		
		cancelBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		southPanel.add(backBtn);
		southPanel.add(startBtn);
		southPanel.add(resetBtn);
		southPanel.add(cancelBtn);

		c.add(northPanel, BorderLayout.CENTER);
		c.add(southPanel, BorderLayout.SOUTH);

		setSize(500, 600);
		setVisible(true);
	}

	public void makeFrame(Node node) {
		northPanel.removeAll();
		northPanel.validate();
		northPanel.repaint();
		if (node.getChildNodes() != null) {
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				JButton btn = new JButton(children.item(i).getNodeName() + "//" + sibling(children.item(i)));
				btn.addMouseListener(new myMouse(i, children.item(i)));
				btn.setPreferredSize(new Dimension(100, 30));
				northPanel.add(btn);
			}
		}
		northPanel.validate();
		northPanel.repaint();
	}

	public class myMouse extends MouseAdapter {
		private int index;
		private Node node;

		public myMouse(int index, Node node) {
			this.index = index;
			this.node = node;
		}

		public void mouseClicked(MouseEvent e) {
			JButton src = (JButton) e.getSource();
			String[] position = src.getText().split("//");
			String[] message = { "선택", "Element", "Attribute", "Comment", "Text" };

			if (e.getButton() == MouseEvent.BUTTON3) { // 우클릭하면 진입
				System.out.println("노드진입");
				currentNodePointer = node;
				DeleteMenu.this.setTitle("Delete (현재위치 : " + currentNodePointer.getNodeName() + ")");
				northPanel.removeAll();
				northPanel.validate();
				northPanel.repaint();
				makeFrame(currentNodePointer);
			} else { // 아니면 노드 삽입
				System.out.println("노드삭제");
				NodeList children = currentNodePointer.getChildNodes();
				new delete(children, index);
			}
		}
	}
	public int sibling(Node node) {
		int index = 1;
		while ((node = node.getPreviousSibling()) != null) {
			index++;
		}
		return index;
	}

}

class delete extends JFrame {
	private NodeList children;
	private JButton deleteEleBtn = new JButton("노드삭제");
	private JButton deleteAttBtn = new JButton("속성삭제");
	private JButton cancelBtn = new JButton("취소");

	delete(NodeList children,int index) {
		this.children = children;	//현재 노드포인터에서의 자식들 받음
		setTitle("삭제창");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());

		if (children.item(index).hasAttributes()) {
			JLabel la = new JLabel("** 해당 노드에는 속성이 존재합니다 **");
			la.setFont(new Font("Gothic", Font.BOLD, 14));
			la.setForeground(Color.red);
			c.add(la);
			c.add(deleteAttBtn);
		}
		
		// 앨리먼트&텍스트노드&주석삭제 버튼 리스너
		c.add(deleteEleBtn);
		deleteEleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int select = JOptionPane.showConfirmDialog(null, "선택한노드를 삭제하시겠습니까?", "삭제확인창",
						JOptionPane.YES_NO_OPTION);
				if (select == JOptionPane.YES_OPTION) {
					Node node = children.item(index);
					DeleteMenu.currentNodePointer.removeChild(node);	//노드삭제
					if(node.getNodeType()==Node.ELEMENT_NODE||node.getNodeType()==Node.COMMENT_NODE) {
						System.out.println("공백노드도 삭제합니다.");
						node = children.item(index);
						DeleteMenu.currentNodePointer.removeChild(node);	//공백텍스트 노드삭제
					}
					JOptionPane.showMessageDialog(null, "성공적으로 삭제되었습니다.");
					DeleteMenu.northPanel.removeAll();
					DeleteMenu.northPanel.validate();
					DeleteMenu.northPanel.repaint();
					dispose();
				}else {
					dispose();
				}
			}
		});
		
		// 창닫기 버튼리스너
		c.add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		// 속성삭제 버튼리스너
		deleteAttBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Node node = children.item(index);	//버튼으로 선택한 노드
				NamedNodeMap attrNode = node.getAttributes();	//모든 속성
//				for(int i=0;i<attrNode.getLength();i++) {
//					System.out.println(attrNode.item(i).getNodeName()+" "+attrNode.item(i).getNodeValue());
//				}
				MiniDelete attrName = new MiniDelete();
				attrName.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						try {
							attrNode.removeNamedItem(attrName.attrName);
							JOptionPane.showMessageDialog(null, "성공적으로 삭제되었습니다.");
						}catch (Exception e1) {
							// TODO: handle exception
							JOptionPane.showMessageDialog(null, "없는 속성입니다. Print를 참고하세요.","Error",JOptionPane.ERROR_MESSAGE);
						}
						DeleteMenu.northPanel.removeAll();
						DeleteMenu.northPanel.validate();
						DeleteMenu.northPanel.repaint();
						dispose();
					}
				});
			}
		});
		setResizable(false);
		setSize(300, 150);
		setVisible(true);
	}
}

class MiniDelete extends JFrame{
	String attrName="";
	public MiniDelete() {
		// TODO Auto-generated constructor stub
		setTitle("삭제할 속성입력");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JButton btn = new JButton("입력완료");
		JLabel la = new JLabel("삭제할 속성이름을 입력해주세요");
		JTextField tf = new JTextField(10);
		c.add(la);
		c.add(tf);
		c.add(btn);
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				attrName = tf.getText();
				dispose();
			}
		});
		setResizable(false);
		setSize(200,150);
		setVisible(true);
	}
}