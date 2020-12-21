package sejong.XMLproject;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * 
 * @author cndwj Load : O (이미로딩 상태일때 두번묻는 문제해결하기) Make : O (Perfect!) Find : O
 *         (버튼 누를때마다 점 생김 해결하기) Save : O (Perfect!) Print : O (Text사이드에서
 *         Comment의 내용이 안보임 + JTree에서는 속성정보가 한개도 안보임) Insert : Δ (Test 필요!)
 *         Update : O (Perfect!) Delete : Δ (Test 필요!) Exit : O (Perfect!)
 * 
 */

public class mainMenu extends JFrame {

	private static File explainFile = new File("manual.txt");
	private static JButton[] btn = new JButton[10]; // 초기메뉴의 10가지 버튼생성
	private static String[] btnName = { "Load", "Make", "Find", "Save", "Print", "Insert", "Update", "Delete", "Exit",
			"Manual" };
	public static JTextArea explanFunc = new JTextArea();
	public static JLabel existLabel = new JLabel("현재 로딩중인 파일이 없습니다.");

	public static Document doc = null;
	private static Loadmenu load;

	public mainMenu(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout(20, 10)); // 위쪽에는 Load~Exit버튼, 아래쪽에는 메뉴얼버튼

		JPanel paneNorth = new JPanel(); // 위쪽에 배치할 패널 (Load~Exit)
		JPanel paneSouth = new JPanel(); // 아래쪽에 배치할 패널 (메뉴얼버튼)

		paneNorth.setLayout(new GridLayout(3, 3, 5, 5));
		paneSouth.setLayout(new FlowLayout(FlowLayout.CENTER));

		existLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		existLabel.setForeground(Color.red);
		paneSouth.add(existLabel);
		// 위쪽&아래쪽 패널 내용
		for (int i = 0; i < btn.length; i++) {
			btn[i] = new JButton(btnName[i]);
			btn[i].setSize(100, 50);
			if (i == 9) { // 메뉴얼 버튼 // 구현완료
				btn[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						new Manual();
					}
				});
				paneSouth.add(btn[i]);
				continue;
			}
			if (i == 8) { // Exit 버튼 // 구현완료
				btn[i].setBackground(Color.gray);
			}
			btn[i].addMouseListener(new myMouse());
			paneNorth.add(btn[i]);
		}

		c.add(paneNorth, BorderLayout.NORTH);
		c.add(paneSouth, BorderLayout.SOUTH);

		// 가운데 텍스트area 내용
		c.add(new JScrollPane(explanFunc));

		explanFunc.setEditable(false);

		setResizable(false); // 창 크기는 고정(컴포넌트배치 고정)
		setSize(400, 400);
		setVisible(true);
	}

	class myMouse extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			JButton btn = (JButton) e.getSource();
			if (btn.getText().equals("Exit")) {
				try {
					if (doc != null) {
						new SaveMenu(doc);
					}
					int num = JOptionPane.showConfirmDialog(null, "정말로 종료하시겠습니까?", "종료창", JOptionPane.YES_NO_OPTION);
					if (num == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (btn.getText().equals("Load")) { // 구현완료
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");

				if (doc == null) {
					load = new Loadmenu();
					load.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent arg0) {
							// TODO Auto-generated method stub
							if (doc != null) {
								existLabel.setText("로딩되었습니다.");
							}
						}
					});
				} else {
					System.out.println("이미파일이 로딩중");
					JOptionPane.showMessageDialog(null, "이미파일이 로딩중입니다.");
				}
			} else if (btn.getText().equals("Make")) {
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				try {
					if (doc != null) {
						new SaveMenu(doc);
					}
					MakeMenu make = new MakeMenu();
					make.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent arg0) {
							// TODO Auto-generated method stub
							if (doc != null) {
								existLabel.setText("로딩되었습니다.");
							}
						}

						public void windowClosing(WindowEvent e) {
							JOptionPane.showMessageDialog(null, "강제종료되었습니다.\n doc정보는 소실됩니다.", "재시도요망",
									JOptionPane.WARNING_MESSAGE);
						}
					});
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (btn.getText().equals("Find")) {
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				if (doc == null) {
					JOptionPane.showMessageDialog(null, "로딩이 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					FindMenu find = new FindMenu(doc);
				}
			} else if (btn.getText().equals("Save")) {
				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				try {
					if (doc == null) {
						JOptionPane.showMessageDialog(null, "로딩이 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						SaveMenu save = new SaveMenu(doc);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (btn.getText().equals("Print")) {
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				if (doc == null) {
					JOptionPane.showMessageDialog(null, "로딩이 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					PrintMenu print = new PrintMenu();
				}
			} else if (btn.getText().equals("Insert")) {
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				if (doc == null) {
					JOptionPane.showMessageDialog(null, "로딩이 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					InsertMenu insert = new InsertMenu();
				}
			} else if (btn.getText().equals("Update")) {
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				if (doc == null) {
					JOptionPane.showMessageDialog(null, "로딩이 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					UpdateMenu update = new UpdateMenu(doc);
				}
			} else if (btn.getText().equals("Delete")) {
//				System.out.println(btn.getText() + "버튼을 클릭했습니다.");
				if (doc == null) {
					JOptionPane.showMessageDialog(null, "로딩이 필요합니다.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					DeleteMenu delete = new DeleteMenu();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			// explain.txt 설명란 읽기
			JButton btn = (JButton) e.getSource();
			readExplain(btn.getText());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			// 마우스떼는 순간 다시 초기화
			explanFunc.setText("");
		}
	}

	public static void readExplain(String func) {
		try {
			FileReader fd = new FileReader(explainFile);
			BufferedReader rd = new BufferedReader(fd);
			String line = "";
			if (func.equals("all")) {
//				System.out.println(func + "로 호출");
				Manual.ta.setText("");
				Scanner scan = new Scanner(explainFile);
				while (scan.hasNextLine()) {
					Manual.ta.append(scan.nextLine() + "\n");
				}
			} else {
//				System.out.println(func + "로 호출");
				while ((line = rd.readLine()) != null) {
					if (line.equals("#" + func)) {
						String str = "";
						while (!(str = rd.readLine()).equals("#End")) {
							explanFunc.append(str + "\n");
						}
					}
				}
			}
			rd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainMenu menu = new mainMenu("Menu");
	}

}

// 매뉴얼 버튼클릭시 생성되는 JFrame
class Manual extends JFrame {
	public static JTextArea ta = new JTextArea();
	private static JButton btn = new JButton("확인");
	private static JPanel panel = new JPanel();
	private static File explainFile = new File("manual.txt");

	Manual() {
		setTitle("메뉴얼");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				mainMenu.explanFunc.setText("");
				dispose();
			}
		});

		mainMenu.readExplain("all");

		panel.add(btn);
		c.add(new JScrollPane(ta), BorderLayout.CENTER);
		c.add(panel, BorderLayout.SOUTH);

		ta.setEditable(false);

		setSize(500, 600);
		setVisible(true);
	}
}
