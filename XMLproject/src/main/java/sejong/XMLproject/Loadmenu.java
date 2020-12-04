package sejong.XMLproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sejong.XMLproject.mainMenu;

public class Loadmenu extends JFrame {
	private static File dir = new File(".");
	private static String[] filenames = dir.list((f, name) -> name.endsWith(".xml"));
	private static JButton btn = new JButton("Load");
	private static JComboBox<String> cb = new JComboBox<String>(filenames);

	private static JLabel la1 = new JLabel("로딩할 파일을 선택하고, 버튼을 눌러주세요.");
	private static JLabel la2 = new JLabel("현재디렉토리 : " + dir.getAbsolutePath());
	private static JLabel la3 = new JLabel("현재로딩중인 파일이 없습니다.");


	private static String selectedFile = "";
	
	public Loadmenu() {
		setTitle("Loading");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		JPanel panelNorth = new JPanel();
		JPanel panelCenter = new JPanel();
		JPanel panelSouth = new JPanel();

		panelCenter.setLayout(new FlowLayout());
		panelCenter.add(cb);
		panelCenter.add(btn);
		

		cb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				selectedFile = cb.getSelectedItem().toString();
//				if(!selectedFile.equals("선택"))
//					System.out.println(selectedFile);
			}
		});

		btn.addMouseListener(new myMouse());

		la1.setHorizontalAlignment(JLabel.CENTER);
		la1.setVerticalAlignment(JLabel.CENTER);
		la2.setHorizontalAlignment(JLabel.CENTER);
		la2.setVerticalAlignment(JLabel.CENTER);

		panelNorth.add(la1);
		panelSouth.add(la2);

		c.add(panelNorth, BorderLayout.NORTH);
		c.add(panelCenter, BorderLayout.CENTER);
		c.add(panelSouth, BorderLayout.SOUTH);

		setSize(380, 130);
		setVisible(true);
	}
	class myMouse extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int num = JOptionPane.showConfirmDialog(null, selectedFile + "을 로딩하시겠습니까?", "확인창",
					JOptionPane.YES_NO_OPTION);
			if (num == JOptionPane.YES_OPTION) {
				try {
					DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = fac.newDocumentBuilder();
					String direct = dir.getAbsolutePath().toString();
					String uri = "file:///"+direct.substring(0, direct.length()-1)+selectedFile;
					System.out.println("파일경로 : "+uri);
					mainMenu.doc = builder.parse(uri);
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ParserConfigurationException e3) {
					e3.printStackTrace();
				}
//				System.out.println(selectedFile + "로딩 성공!");
				JOptionPane.showMessageDialog(null, "성공적으로 로딩하였습니다.");
				dispose();
			}
		}
	}
}
