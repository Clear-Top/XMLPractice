package sejong.XMLproject;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import sejong.XMLproject.mainMenu;

public class SaveMenu {
	public SaveMenu() throws IOException {
		if(mainMenu.doc!=null) {
			String fileName = (String)JOptionPane.showInputDialog(null,"현재작업중인 DOM트리를 저장합니다.\n저장할 \"파일명\"을 입력해주세요.\n(.xml확장자는 생략)","파일명입력",JOptionPane.QUESTION_MESSAGE);
			if(fileName!=null) {
				int num = JOptionPane.showConfirmDialog(null, "현재생성된 DOM트리를 XML로 변환하시겠습니까?","확인창",JOptionPane.YES_NO_OPTION);
				if(num==JOptionPane.YES_OPTION) {
					// Save 기능
					OutputFormat format = new OutputFormat(mainMenu.doc);
					format.setEncoding("UTF-8");
					StringWriter stringOut = new StringWriter();
					XMLSerializer serial = new XMLSerializer(stringOut, format);
					serial.asDOMSerializer();
					
					serial.serialize(mainMenu.doc.getDocumentElement());
					FileWriter fw = new FileWriter(fileName+".xml");
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(stringOut.toString());
					bw.close();
					System.out.println("성공적으로 저장되었습니다.");
					mainMenu.doc=null;
					mainMenu.existLabel.setText("현재 로딩중인 파일이 없습니다.");
					mainMenu.existLabel.setFont(new Font("Dialog", Font.BOLD, 12));
					mainMenu.existLabel.setForeground(Color.red);
				}
			}
		}else {
			JOptionPane.showMessageDialog(null, "로딩된 Dom트리가 없습니다!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
