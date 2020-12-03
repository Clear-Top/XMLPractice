package sejong.XMLproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

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

public class AddElement {

	private static String uri;
	private static Document doc;
	private static Node root;

	// 자식만드는 메소드
	public Element createChild(String nodeName) {
		Element child = doc.createElement(nodeName); // nodeName이 이름인 앨리먼트 생성

		return child;
	}

	// 자식만드는 메소드(오버라이딩)
	public Element createChild(String nodeName, String nodeName2) {
		Element child = createChild(nodeName);
		child.appendChild(doc.createTextNode(nodeName2));

		return child;
	}

	public void addElement(NodeList children, Element newNode, int index) {
		root.insertBefore(newNode, children.item(index));
		root.insertBefore(doc.createTextNode("\n"), children.item(index));
	}

	public AddElement() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fac.newDocumentBuilder();
		doc = builder.parse(uri);

		root = doc.getDocumentElement(); // root 노드 리턴
		NodeList children = root.getChildNodes(); // root 노드의 자식들 리턴(n개)

		System.out.println("<<Before Insert>>");
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			System.out.println(node.getNodeName());
		}

		Element newChild = createChild("melon","새롭게 추가된 노드입니다.");

		addElement(children, newChild, 2);

		children = root.getChildNodes();
		System.out.println();
		System.out.println("<<After Insert>>");
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			System.out.println(node.getNodeName());
		}
		
		saveFile();
		System.out.println("성공적으로 저장되었습니다.");
	}

	private void saveFile() throws IOException {
		// TODO Auto-generated method stub
		OutputFormat format = new OutputFormat(doc);
		format.setEncoding("EUC-KR");
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
		System.out.println("go");
		uri = "file:///C:\\XML01_MiniProject4_김현모_16011015.xml";
		try {
			new AddElement();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
