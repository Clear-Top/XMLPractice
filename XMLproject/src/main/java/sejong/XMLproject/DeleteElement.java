package sejong.XMLproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class DeleteElement {

	private static String uri;
	private static Document doc;
	private static Node root;
	
	public DeleteElement() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fac.newDocumentBuilder();
		doc = builder.parse(uri);
		
		root = doc.getDocumentElement();		// 루트노드 반환
		NodeList children = root.getChildNodes();	// 루트노드의 자식들을 반환
		
		// 루트의 현재 자식들 조회
		for(int i=0;i<children.getLength();i++) {
			Node child = children.item(i);
			System.out.println(child.getNodeName()+"  ["+i+"]");
		}
		
		String index = JOptionPane.showInputDialog("몇번째 자식을 삭제하시겠습니까?");
		Node removeElement = children.item(Integer.parseInt(index));
		Node removeElement_pair = children.item(Integer.parseInt(index)+1);		//삭제할 노드의 다음 WhiteSpace 노드ㄴ
		
		String message = "다음을 삭제하시겠습니까?\n"+removeElement.getNodeName()+"&"+removeElement_pair.getNodeName();
		int n = JOptionPane.showConfirmDialog(null, message, "확인창", JOptionPane.YES_NO_OPTION);
		if(n==1) {		//no
			JOptionPane.showMessageDialog(null, "Error! 종료합니다.","종료",JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}else {
			root.removeChild(removeElement);
			root.removeChild(removeElement_pair);
			System.out.println();
			System.out.println("<<삭제 후 root노드의 자식들...>>");
			for(int i=0;i<children.getLength();i++) {
				Node child = children.item(i);
				System.out.println(child.getNodeName()+"  ["+i+"]");
			}
			JOptionPane.showMessageDialog(null, "성공적으로 삭제되었습니다.","삭제완료",JOptionPane.INFORMATION_MESSAGE);
		}
		saveFile();		// XML 파일로 저장
	}
	
	private void saveFile() throws IOException {
		// TODO Auto-generated method stub
		OutputFormat format = new OutputFormat(doc);
		format.setEncoding("UTF-8");
		StringWriter stringOut = new StringWriter();
		XMLSerializer serial = new XMLSerializer(stringOut, format);
		serial.asDOMSerializer();
		
		serial.serialize(doc.getDocumentElement());
		FileWriter fw = new FileWriter("C:\\new.xml");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stringOut.toString());
		bw.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		uri = "file:///C:\\XML01_MiniProject4_김현모_16011015.xml";
		try {
			new DeleteElement();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
