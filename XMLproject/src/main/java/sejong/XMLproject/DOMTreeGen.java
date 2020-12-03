package sejong.XMLproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;


import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DOMTreeGen {
	public static void main(String[] argv) {
		try {
			Document doc = new DocumentImpl();
			Element root = doc.createElement("book_catalog");
			Element item = doc.createElement("book");
			Element subitem = doc.createElement("title");
			subitem.appendChild(doc.createTextNode("XML Programming"));
			item.appendChild(subitem);
			subitem = doc.createElement("author");
			subitem.appendChild(doc.createTextNode("J. Chae"));
			item.appendChild(subitem);
			subitem = doc.createElement("publisher");
			subitem.appendChild(doc.createTextNode("Hongreung publishing company"));
			item.appendChild(subitem);
			root.appendChild(item);
			item = doc.createElement("book");
			subitem = doc.createElement("title");
			subitem.appendChild(doc.createTextNode("Web Programming"));
			item.appendChild(subitem);
			subitem = doc.createElement("author");
			subitem.appendChild(doc.createTextNode("G. Hong"));
			item.appendChild(subitem);
			subitem = doc.createElement("publisher");
			subitem.appendChild(doc.createTextNode("Science publishing company"));
			item.appendChild(subitem);
			root.appendChild(item);
			doc.appendChild(root);
			
			OutputFormat format = new OutputFormat(doc);
			format.setEncoding("EUC-KR");
			StringWriter stringOut = new StringWriter();
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.asDOMSerializer();
			
			serial.serialize(doc.getDocumentElement());
			FileWriter fw = new FileWriter(argv[0]);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(stringOut.toString());
			bw.close();
			System.out.println("성공적으로 저장되었습니다.");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}