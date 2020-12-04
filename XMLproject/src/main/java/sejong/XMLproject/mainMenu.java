package sejong.XMLproject;

import javax.swing.*;

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

public class mainMenu extends JFrame {

	private static File explainFile = new File("explain.txt");
	private static JButton[] btn = new JButton[10]; // 초기메뉴의 10가지 버튼생성
	private static String[] btnName = { "Load", "Make", "Find", "Save", "Print", "Insert", "Update", "Delete", "Exit",
			"Manual" };
	private static JTextArea explanFunc = new JTextArea(7, 20);

	public mainMenu(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout(20, 20)); // 위쪽에는 Load~Exit버튼, 아래쪽에는 메뉴얼버튼

		JPanel paneNorth = new JPanel(); // 위쪽에 배치할 패널 (Load~Exit)
		JPanel paneSouth = new JPanel(); // 아래쪽에 배치할 패널 (메뉴얼버튼)

		paneNorth.setLayout(new GridLayout(3, 3, 5, 5));
		paneSouth.setLayout(new FlowLayout());

		// 위쪽&아래쪽 패널 내용
		for (int i = 0; i < btn.length; i++) {
			btn[i] = new JButton(btnName[i]);
			btn[i].setSize(100, 50);
			if (i == 9) { // 메뉴얼 버튼
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
			if (i == 8) { // Exit 버튼
				btn[i].setBackground(Color.gray);
			}
			btn[i].addMouseListener(new myMouse());
			paneNorth.add(btn[i]);
		}
		c.add(paneNorth, BorderLayout.NORTH);
		c.add(paneSouth, BorderLayout.SOUTH);

		// 가운데 텍스트area 내용
		c.add(new JScrollPane(explanFunc));

		setSize(400, 400);
		setVisible(true);
	}

	class myMouse extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			JButton btn = (JButton)e.getSource();
			if(btn.getText().equals("Exit")) {
				int num = JOptionPane.showConfirmDialog(null, "정말로 종료하시겠습니까?","종료창",JOptionPane.YES_NO_OPTION);
				if(num==JOptionPane.YES_OPTION) {
					System.exit(0);
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
				System.out.println(func + "로 호출");
				Scanner scan = new Scanner(explainFile);
				while(scan.hasNextLine()) {
					Manual.ta.append(scan.nextLine()+"\n");
				}
			} else {
				System.out.println(func + "로 호출");
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
	private static File explainFile = new File("explain.txt");

	Manual() {
		setTitle("메뉴얼");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});

		mainMenu.readExplain("all");

		panel.add(btn);
		c.add(new JScrollPane(ta), BorderLayout.CENTER);
		c.add(panel, BorderLayout.SOUTH);

		setSize(500, 600);
		setVisible(true);
	}
}
