package sejong.XMLproject;
import sejong.XMLproject.mainMenu;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class InsertMenu extends JFrame{
	public static Document doc = mainMenu.doc;
	public static Node root=mainMenu.doc.getDocumentElement();
	public static JPanel northPanel = new JPanel();
	public static JPanel southPanel = new JPanel();
	private static String[] nodeName = {"Element","Attribute","Comment","Text"};
	public static JComboBox<String> cb = new JComboBox<String>(nodeName);
	public JButton backBtn;
	public JButton resetBtn;
	public JButton startBtn;
	public JButton cancelBtn;
	
	public static Node currentNodePointer = root;
	public static Node currentSelectPoint = null;
	
	public InsertMenu() {
		setTitle("Insert (현재위치 : "+currentNodePointer.getNodeName()+")");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		northPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new FlowLayout());
		
		// 루트노드부터 시작
		backBtn = new JButton("부모로 이동");
		resetBtn = new JButton("초기화");
		startBtn = new JButton("조회");
		cancelBtn = new JButton("나가기");
		
		backBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(currentNodePointer.getParentNode()==doc) {
					JOptionPane.showMessageDialog(null, "현재노드가 최상위 노드입니다.");
				}else {
					currentNodePointer = currentNodePointer.getParentNode();
					northPanel.removeAll();
					northPanel.validate();
					northPanel.repaint();
					setTitle("Insert (현재위치 : "+currentNodePointer.getNodeName()+")");
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
				setTitle("Insert (현재위치 : "+root.getNodeName()+")");
			}
		});
		
		startBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				makeFrame(currentNodePointer);
			}
		});
		
		cancelBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
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
			
			
		
		southPanel.add(backBtn);
		southPanel.add(startBtn);
		southPanel.add(resetBtn);
		southPanel.add(cancelBtn);
		
		c.add(northPanel,BorderLayout.CENTER);
		c.add(southPanel,BorderLayout.SOUTH);

		setSize(500,600);
		setVisible(true);
	}
	public void makeFrame(Node node) {
		northPanel.removeAll();
		northPanel.validate();
		northPanel.repaint();
		if(node.getChildNodes()!=null) {
			NodeList children = node.getChildNodes();
			for(int i=0;i<children.getLength();i++) {
				JButton btn = new JButton(children.item(i).getNodeName()+"//"+sibling(children.item(i)));
				btn.addMouseListener(new myMouse(i,children.item(i)));
				northPanel.add(btn);
			}
		}
		northPanel.validate();
		northPanel.repaint();
	}
	
	public class myMouse extends MouseAdapter{
		private int index; 
		private Node node;
		public myMouse(int index,Node node) {
			this.index = index;
			this.node = node;
		}
		public void mouseClicked(MouseEvent e) {
			JButton src = (JButton) e.getSource();
			String[] position = src.getText().split("//");
//			System.out.println("현재위치 : "+currentNode.getNodeName());
//			System.out.println("몇번째자식? : "+position[1]);
			String[] message = {"선택","Element","Attribute","Comment","Text"};
			
			if(e.getButton()==MouseEvent.BUTTON3) {	// 우클릭하면 진입
				System.out.println("노드진입");
				currentNodePointer = node;
				InsertMenu.this.setTitle("Insert (현재위치 : "+currentNodePointer.getNodeName()+")");
				northPanel.removeAll();
				northPanel.validate();
				northPanel.repaint();
				makeFrame(currentNodePointer);
			}else {						// 아니면 노드 삽입
				System.out.println("노드삽입");
				try {
					while(true) {
						String choice = (String)JOptionPane.showInputDialog(
								null,
								"삽입할 노드를 선택해주세요!",
								"노드종류선택",
								JOptionPane.INFORMATION_MESSAGE,
								null,
								message,
								message[0]
								);
						if(choice.equals(message[1])) {			//앨리먼트
//							System.out.println(choice);
							new ElementFrame(choice,index);
							break;
						}else if(choice.equals(message[2])){	// 속성
//							System.out.println(choice);
							new ElementFrame(choice,index);
							break;
						}else if(choice.equals(message[3])) {	// 주석
//							System.out.println(choice);
							new ElementFrame(choice,index);
							break;
						}else if(choice.equals(message[4])) {	// 텍스트
//							System.out.println(choice);
							new ElementFrame(choice,index);
							break;
						}
						else {
							JOptionPane.showMessageDialog(null, "무언가를 선택 해주세요","잘못된선택",JOptionPane.ERROR_MESSAGE);
						}
					}
				}catch(NullPointerException e1) {
					System.out.println("실행취소");
				}
			}
		}
	}
	
	// 선택입력창	// 부모노드랑 인덱스가 필요!
	public class ElementFrame extends JFrame{
		private int width;
		private int height;
		private JButton okBtn = new JButton("삽입");
		private JButton cancelBtn = new JButton("닫기");
		
		ElementFrame(String msg, int index){
//			System.out.println("앨리먼트 클래스 생성완료");
			setTitle(msg);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(new BorderLayout());
			
			JPanel paCenter = new JPanel();
			JPanel paSouth = new JPanel();
			paSouth.setLayout(new FlowLayout());
			paSouth.add(okBtn);
			paSouth.add(cancelBtn);
			cancelBtn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					dispose();
				}
			});
			
			if(msg.equals("Element")) {
				paCenter.setLayout(new FlowLayout());
				JLabel la = new JLabel("노드이름");
				JTextField tf = new JTextField(10);
				paCenter.add(la);
				paCenter.add(tf);
				okBtn.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
//						String str = ((JButton)e.getSource()).getText();
//						System.out.println(str);
//						System.out.println("\""+tf.getText()+"\"라는 이름을 가진 노드를 ");
//						System.out.println("\""+root.getNodeName()+"\"노드의 자식으로 삽입! 그리고 \""+index+"\"번째 자식으로 삽입!");
						NodeList children = currentNodePointer.getChildNodes();	//현재부모노드의 자식들 리턴
						Element item = doc.createElement(tf.getText());
						item.appendChild(doc.createTextNode(""));	//WhiteSpace 노드삽입
						
//						System.out.println(index);
						if(index==0) {
							currentNodePointer.insertBefore(item, children.item(index));
							currentNodePointer.insertBefore(doc.createTextNode(""), children.item(0));
						}else if(children.item(index).getNodeType()==Node.TEXT_NODE) {
							currentNodePointer.insertBefore(item, children.item(index));
							currentNodePointer.insertBefore(doc.createTextNode(""), children.item(index));
						}else {
							currentNodePointer.insertBefore(doc.createTextNode(""), children.item(index));
							currentNodePointer.insertBefore(item, children.item(index));
						}
						
						JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.\n\"초기화\" 후 \"조회\"를 눌러주세요");
						dispose();
					}
				});
				width=250;
				height=100;
			}else if(msg.equals("Attribute")) {
				paCenter.setLayout(new FlowLayout());
				JLabel la1 = new JLabel("속성이름");
				JTextField tf1 = new JTextField(15);
				JLabel la2 = new JLabel("속성값");
				JTextField tf2 = new JTextField(15);
				
				paCenter.add(la1);
				paCenter.add(tf1);
				paCenter.add(la2);
				paCenter.add(tf2);
				okBtn.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						NodeList children = currentNodePointer.getChildNodes();	//현재부모노드의 자식들 리턴
						((Element)children.item(index)).setAttribute(tf1.getText(), tf2.getText());
//						System.out.println(tf1.getText()+"   "+tf2.getText());
						JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.\n\"초기화\" 후 \"조회\"를 눌러주세요");
						dispose();
					}
				});
				width=500;
				height=100;
			}else if(msg.equals("Comment")) {
				paCenter.setLayout(new BorderLayout(30,20));
				JLabel la = new JLabel("<html><br/>주석내용</html>");
				la.setHorizontalAlignment(SwingConstants.CENTER);
				la.setFont(new Font("SansSerif", Font.BOLD, 20));
				JTextArea ta = new JTextArea();
				ta.setFont(new Font("Gothic",Font.BOLD,15));
				paCenter.add(la,BorderLayout.NORTH);
				paCenter.add(new JScrollPane(ta),BorderLayout.CENTER);
				okBtn.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
//						String str = ((JButton)e.getSource()).getText();
//						System.out.println(str);
//						System.out.println("\""+tf.getText()+"\"라는 이름을 가진 노드를 ");
//						System.out.println("\""+root.getNodeName()+"\"노드의 자식으로 삽입! 그리고 \""+index+"\"번째 자식으로 삽입!");
						NodeList children = currentNodePointer.getChildNodes();	//현재부모노드의 자식들 리턴
						Comment comment = doc.createComment(ta.getText());
//						System.out.println(ta.getText());
//						System.out.println(index);
						if(index==0) {
							currentNodePointer.insertBefore(comment, children.item(index));
							currentNodePointer.insertBefore(doc.createTextNode("\n"), children.item(0));
						}else if(children.item(index).getNodeType()==Node.TEXT_NODE) {
							currentNodePointer.insertBefore(comment, children.item(index));
							currentNodePointer.insertBefore(doc.createTextNode("\n"), children.item(index));
						}else {
							currentNodePointer.insertBefore(doc.createTextNode("\n"), children.item(index));
							currentNodePointer.insertBefore(comment, children.item(index));
						}
						JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.\n\"초기화\" 후 \"조회\"를 눌러주세요");
						dispose();
					}
				});
				width=400;
				height=400;
			}else if(msg.equals("Text")) {
				paCenter.setLayout(new BorderLayout(30,20));
				JLabel la = new JLabel("<html><br/>Text</html>");
				la.setHorizontalAlignment(SwingConstants.CENTER);
				la.setFont(new Font("SansSerif", Font.BOLD, 20));
				
				JTextArea ta = new JTextArea();
				ta.setFont(new Font("Gothic",Font.BOLD,15));
				paCenter.add(la,BorderLayout.NORTH);
				paCenter.add(new JScrollPane(ta),BorderLayout.CENTER);
				okBtn.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						NodeList children = currentNodePointer.getChildNodes();
						Text textNode = doc.createTextNode(ta.getText());
						children.item(index).appendChild(textNode);
						JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.\n\"초기화\" 후 \"조회\"를 눌러주세요");
						dispose();
					}
				});
				width=400;
				height=400;
			}
			
			this.add(paCenter,BorderLayout.CENTER);
			this.add(paSouth,BorderLayout.SOUTH);
			
			setResizable(false);
			setSize(width,height);
			setVisible(true);
		}
	}
	
	public int sibling(Node node) {
		int index=1;
		while((node=node.getPreviousSibling())!=null) {
			index++;
		}
		return index;
	}

}
