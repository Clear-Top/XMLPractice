package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import sejong.XMLproject.mainMenu;
import sejong.XMLproject.SaveMenu;

public class MakeMenu extends JFrame {

	public static JPanel panelCenter;
	private static JPanel panelSouth;
	private logFrame log;
	public static Node root;
	public static Document doc;
	public static ElementPanel ePanel;
	public static AttributePanel aPanel;
	public static TextPanel tPanel;
	public static CommentsPanel cPanel;

	public Document returnDoc() {
		return doc;
	}

	public MakeMenu() {
//		System.out.println("Make기능을 실행합니다.");

		// 루트 노드와 껍데기 DOM Tree 만들기 (연결)
		doc = new DocumentImpl();
//		System.out.println(doc.toString());
		String rootName = JOptionPane.showInputDialog("Root노드의 이름을 입력해주세요.");
		if (rootName == null) {
			dispose();
		} else {
			root = doc.createElement(rootName);
			root.appendChild(doc.createTextNode(""));
			doc.appendChild(root);
			startMake(doc);
		}
	}

	public void startMake(Document doc) {
		log = new logFrame();
		setTitle("Make");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridLayout(2, 2, 5, 5));
		panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout());

		// 아래side에 버튼3개 붙이기
		JButton saveBtn = new JButton("Save");
		JButton exitBtn = new JButton("Exit");
		JButton logBtn = new JButton("Log");
		JButton resetBtn = new JButton("Reset");

		// 버튼에 이벤트 각각달기
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
//				System.out.println("Save 버튼 클릭!");
				try {
					new SaveMenu(doc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		exitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					new SaveMenu(doc);
					dispose();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		logBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (log != null) {
					log.dispose();
				}
				log = new logFrame();
			}
		});

		// 컴포넌트 -> 패널2개 -> 컨테이너 (attaching)
		panelSouth.add(saveBtn);
		panelSouth.add(exitBtn);
		panelSouth.add(logBtn);
		panelSouth.add(resetBtn);

		ePanel = new ElementPanel(root);
		aPanel = new AttributePanel(root);
		tPanel = new TextPanel(root);
		cPanel = new CommentsPanel(root);
		panelCenter.add(ePanel);
		panelCenter.add(aPanel);
		panelCenter.add(tPanel);
		panelCenter.add(cPanel);

		c.add(panelCenter, BorderLayout.CENTER);
		c.add(panelSouth, BorderLayout.SOUTH);

		// Make 창닫혔을때, document 최상위노드 메인으로 넘기기
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				// 창닫힐때 mainMenu로 DOMtree 넘기기
				log.dispose();
				System.out.println("강제종료되었습니다. doc정보는 소실됩니다.");
			}
		});

		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				reset();
			}
		});
		setResizable(false);
		setLocationRelativeTo(null);
		setSize(430, 600);
		setVisible(true);
	}

	public void reset() {
		doc = null;
		root = null;
		this.dispose();
		new MakeMenu();
	}
}

class logFrame extends JFrame {
	public static JTextArea ta = new JTextArea();

	logFrame() {
		setTitle("작업로그창");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new FlowLayout());

		JButton okBtn = new JButton("확인");
		JButton backUpBtn = new JButton("저장(mLog.txt)");
		panelSouth.add(okBtn);
		panelSouth.add(backUpBtn);

		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		backUpBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				FileWriter writer = null;
				try {
					writer = new FileWriter(new File("mLog.txt"), false);
					writer.write(ta.getText());
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "알수없는 오류로 저장이 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					dispose();
				}finally {
					try {
						if(writer!=null) writer.close();
						JOptionPane.showMessageDialog(null, "성공적으로 저장되었습니다.","Successfully",JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				
			}
		});

		c.add(new JScrollPane(ta), BorderLayout.CENTER);
		c.add(panelSouth, BorderLayout.SOUTH);

		ta.setEditable(false);
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(400, 500);
		setVisible(true);
	}
}

class ElementPanel extends JPanel {
	private Node root;

	public ElementPanel(Node root) {
		// TODO Auto-generated constructor stub
		this.root = root;
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout());
		JLabel la = new JLabel("Element");
		la.setForeground(Color.RED);
		la.setPreferredSize(new Dimension(200, 50));
		la.setFont(new Font("Gothic", Font.BOLD, 14));
		la.setHorizontalAlignment(JLabel.CENTER);
		la.setVerticalAlignment(JLabel.CENTER);
		add(la);

		// JLabel과 JTextField 붙이기
		JLabel[] element = new JLabel[2];
		JTextField[] tf = new JTextField[2];
		String[] str = { "노드 이름", "노드 값" };
		for (int i = 0; i < element.length; i++) {
			element[i] = new JLabel(str[i]);
			tf[i] = new JTextField(15);
			tf[i].setHorizontalAlignment(JTextField.CENTER);
			if (i == 1) {
				tf[i].setText("null");
				tf[i].setEditable(false);
			}
			add(element[i]);
			add(tf[i]);
		}
		JButton inputBtn = new JButton("Input");
		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String tmp = "";
				try {
					// Element Node 추가하기 (처음은 공백노드 붙이기!)
					tmp += "Element : " + tf[0].getText();
					Element item = MakeMenu.doc.createElement(tf[0].getText());
					Text white = MakeMenu.doc.createTextNode("");
					root.appendChild(item);
					root.appendChild(white);
					JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.");
					tmp += "(이)가 성공적으로 삽입되었습니다.";
					tf[0].setText("");
					refresh();
					write.writeLog(tmp,1);
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "삽입이 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					tmp += "(이)가 삽입에 실패했습니다.";
					tf[0].setText("");
					write.writeLog(tmp,0);
				}
			}
		});
		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tf[0].setText("");
			}
		});
		add(inputBtn);
		add(clearBtn);
	}

	public void refresh() {
		MakeMenu.panelCenter.removeAll();
		MakeMenu.ePanel = new ElementPanel(MakeMenu.root);
		MakeMenu.aPanel = new AttributePanel(MakeMenu.root);
		MakeMenu.tPanel = new TextPanel(MakeMenu.root);
		MakeMenu.cPanel = new CommentsPanel(MakeMenu.root);
		MakeMenu.panelCenter.add(MakeMenu.ePanel);
		MakeMenu.panelCenter.add(MakeMenu.aPanel);
		MakeMenu.panelCenter.add(MakeMenu.tPanel);
		MakeMenu.panelCenter.add(MakeMenu.cPanel);
		MakeMenu.ePanel.revalidate();
		MakeMenu.ePanel.repaint();
		MakeMenu.aPanel.revalidate();
		MakeMenu.aPanel.repaint();
		MakeMenu.tPanel.revalidate();
		MakeMenu.tPanel.repaint();
		MakeMenu.cPanel.revalidate();
		MakeMenu.cPanel.repaint();
	}
}

class AttributePanel extends JPanel {
	public AttributePanel(Node root) {
		// TODO Auto-generated constructor stub
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout());
		JLabel la = new JLabel("Attribute");
		la.setForeground(Color.RED);
		la.setPreferredSize(new Dimension(250, 50));
		la.setFont(new Font("Gothic", Font.BOLD, 14));
		la.setHorizontalAlignment(JLabel.CENTER);
		la.setVerticalAlignment(JLabel.CENTER);
		add(la);

		// JLabel과 JTextField 붙이기
		String[] str = { "선택", "속성이름", "속성 값" };
		JLabel[] element = new JLabel[3];
		JTextField[] tf = new JTextField[3];
		JComboBox<Element> combo = new JComboBox<Element>();
		combo.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setHorizontalAlignment(DefaultListCellRenderer.CENTER);
				super.paint(g);
			}
		});
		JButton inputBtn = new JButton("Input");
		JButton clearBtn = new JButton("Clear");
		for (int i = 0; i < 3; i++) {
			element[i] = new JLabel(str[i]);
			add(element[i]);
			if (i == 0) {
				addComboBox(root, combo);
				add(combo);
				combo.setPreferredSize(new Dimension(180, 30));
			} else {
				tf[i] = new JTextField(15);
				tf[i].setHorizontalAlignment(JTextField.CENTER);
				add(tf[i]);
			}
		}
		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String tmp = "";
				try {
					tmp += "Attribute : "+tf[1].getText()+"--"+tf[2].getText(); 
					String attrName = tf[1].getText();
					String attrValue = tf[2].getText();
					Element selected_item = (Element) combo.getSelectedItem();
					selected_item.setAttribute(attrName, attrValue);
					JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.");
					tf[1].setText("");
					tf[2].setText("");
					tmp += "(이)가 성공적으로 삽입되었습니다.";
					write.writeLog(tmp,0);
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "삽입에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					tmp += "(이)가 삽입에 실패했습니다.";
					tf[1].setText("");
					tf[2].setText("");
					write.writeLog(tmp,0);
				}

			}
		});
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tf[1].setText("");
				tf[2].setText("");
			}
		});
		add(inputBtn);
		add(clearBtn);
	}

	public void addComboBox(Node node, JComboBox<Element> combo) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
//				System.out.println("콤보박스에 " + children.item(i).getNodeName() + "추가");
				combo.addItem((Element) children.item(i));
			}
		}
//		System.out.println("현재 콤보박스의 내용");
//		for (int i = 0; i < combo.getItemCount(); i++) {
//			System.out.println(combo.getItemAt(i));
//		}
	}
}

class TextPanel extends JPanel {
	public TextPanel(Node root) {
		// TODO Auto-generated constructor stub
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout());
		JLabel la = new JLabel("Text");
		la.setForeground(Color.RED);
		la.setPreferredSize(new Dimension(250, 50));
		la.setFont(new Font("Gothic", Font.BOLD, 14));
		la.setHorizontalAlignment(JLabel.CENTER);
		la.setVerticalAlignment(JLabel.CENTER);
		add(la);

		// JLabel과 JTextField 붙이기
		// JLabel과 JTextField 붙이기
		String[] str = { "선택", "노드이름", "노드 값" };
		JLabel[] element = new JLabel[3];
		JTextField[] tf = new JTextField[3];
		JComboBox<Element> combo = new JComboBox<Element>();
		combo.setRenderer(new DefaultListCellRenderer() {
			public void paint(Graphics g) {
				setHorizontalAlignment(DefaultListCellRenderer.CENTER);
				super.paint(g);
			}
		});
		JButton inputBtn = new JButton("Input");
		JButton clearBtn = new JButton("Clear");
		for (int i = 0; i < 3; i++) {
			element[i] = new JLabel(str[i]);
			add(element[i]);
			if (i == 0) {
				addComboBox(root, combo);
				add(combo);
				combo.setPreferredSize(new Dimension(180, 30));
			} else if (i == 1) {
				tf[i] = new JTextField(15);
				tf[i].setHorizontalAlignment(JTextField.CENTER);
				tf[i].setText("#Text");
				tf[i].setEditable(false);
				add(tf[i]);
			} else {
				tf[i] = new JTextField(15);
				tf[i].setHorizontalAlignment(JTextField.CENTER);
				add(tf[i]);
			}
		}
		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String tmp = "";
				try {
					// 선택한 노드에 텍스트노드 삽입하기
					tmp += "#Text : " + tf[2].getText();
					Element selected_item = (Element) combo.getSelectedItem();
					Text item = MakeMenu.doc.createTextNode(tf[2].getText());
					selected_item.appendChild(item);
					JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.");
					tf[2].setText("");
					refresh();
					tmp += "(이)가 성공적으로 삽입되었습니다.";
					write.writeLog(tmp,0);
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "삽입에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					tmp += "(이)가 삽입에 실패했습니다.";
					tf[2].setText("");
					write.writeLog(tmp,0);
				}

			}
		});
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tf[2].setText("");
			}
		});
		add(inputBtn);
		add(clearBtn);
	}

	public void addComboBox(Node node, JComboBox<Element> combo) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				combo.addItem((Element) children.item(i));
			}
		}
	}

	public void refresh() {
		MakeMenu.panelCenter.removeAll();
		MakeMenu.ePanel = new ElementPanel(MakeMenu.root);
		MakeMenu.aPanel = new AttributePanel(MakeMenu.root);
		MakeMenu.tPanel = new TextPanel(MakeMenu.root);
		MakeMenu.cPanel = new CommentsPanel(MakeMenu.root);
		MakeMenu.panelCenter.add(MakeMenu.ePanel);
		MakeMenu.panelCenter.add(MakeMenu.aPanel);
		MakeMenu.panelCenter.add(MakeMenu.tPanel);
		MakeMenu.panelCenter.add(MakeMenu.cPanel);
		MakeMenu.ePanel.revalidate();
		MakeMenu.ePanel.repaint();
		MakeMenu.aPanel.revalidate();
		MakeMenu.aPanel.repaint();
		MakeMenu.tPanel.revalidate();
		MakeMenu.tPanel.repaint();
		MakeMenu.cPanel.revalidate();
		MakeMenu.cPanel.repaint();
	}
}

class CommentsPanel extends JPanel {
	public CommentsPanel(Node root) {
		// TODO Auto-generated constructor stub
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout());
		JLabel la = new JLabel("Comments");
		la.setForeground(Color.RED);
		la.setPreferredSize(new Dimension(250, 35));
		la.setFont(new Font("Gothic", Font.BOLD, 14));
		la.setHorizontalAlignment(JLabel.CENTER);
		la.setVerticalAlignment(JLabel.CENTER);
		add(la);

		// JLabel과 JTextField 붙이기
		JTextArea ta = new JTextArea();
		JScrollPane scroll = new JScrollPane(ta);
		scroll.setPreferredSize(new Dimension(200, 170));
		add(scroll);
		JButton inputBtn = new JButton("Input");
		JButton clearBtn = new JButton("Clear");

		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String tmp = "";
				try {
					// TODO Auto-generated method stub
					String num = JOptionPane.showInputDialog("로그를 참조하여 삽입할 위치를 입력하시오(정수).");
					int index = Integer.parseInt(num);
					tmp += "Comments가 index="+index+"에";
					Comment comment = MakeMenu.doc.createComment(ta.getText());
					NodeList children = root.getChildNodes();
					if (index == 0) {
						root.insertBefore(comment, children.item(index));
						root.insertBefore(MakeMenu.doc.createTextNode(""), children.item(0));
					} else if (children.item(index).getNodeType() == Node.TEXT_NODE) {
						root.insertBefore(comment, children.item(index));
						root.insertBefore(MakeMenu.doc.createTextNode(""), children.item(index));
					} else {
						root.insertBefore(MakeMenu.doc.createTextNode(""), children.item(index));
						root.insertBefore(comment, children.item(index));
					}
					JOptionPane.showMessageDialog(null, "성공적으로 삽입되었습니다.");
					ta.setText("");
					tmp += "(이)가 성공적으로 삽입되었습니다.";
					write.writeLog(tmp,1);
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "삽입에 실패했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					ta.setText("");
					tmp += "(이)가 삽입에 실패했습니다.";
					write.writeLog(tmp,0);
				}
			}
		});
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ta.setText("");
			}
		});

		add(inputBtn);
		add(clearBtn);

	}
}

class write {
	private static int cnt=1;
	public static void writeLog(String tmp,int flag) {
		Date date = new Date();
		SimpleDateFormat writtenFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = writtenFormat.format(date);
		if(flag==1) {
			str += "\n["+cnt+"] " + tmp + "\n\n";
			cnt++;
		}else {
			str +="\n"+tmp+"\n\n";
		}

		logFrame.ta.append(str);
		logFrame.ta.setCaretPosition(logFrame.ta.getDocument().getLength()); // 맨아래로 스크롤(아래로 쌓일거니까)
		logFrame.ta.revalidate();
		logFrame.ta.repaint();
	}
}
