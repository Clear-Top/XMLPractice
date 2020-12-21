package sejong.XMLproject;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.event.WindowAdapter;

import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

public class PrintJTree extends JPanel {
	static Document document;
	static final int windowHeight = 460;
	static final int leftWidth = 300;
	static final int rightWidth = 340;
	static final int windowWidth = leftWidth + rightWidth;
	static final String[] typeName = { "none", "Element", "Attr", "Text", "CDATA", "EntityRef", "Entity", "ProcInstr",
			"Comment", "Document", "DocType", "DocFragment", "Notation", };

	public PrintJTree(Document document) {
		this.document = document;
		
		JTree tree = new JTree(new DomToTreeModelAdapter());

		JScrollPane treeView = new JScrollPane(tree);
		treeView.setPreferredSize(new Dimension(leftWidth, windowHeight));

		this.setLayout(new BorderLayout());
		this.add("Center", treeView);

	}

	public static void makeFrame() {
		JFrame frame = new JFrame("Print for JTree");
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

		final PrintJTree echoPanel = new PrintJTree(document);
		frame.getContentPane().add("Center", echoPanel);
		frame.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = windowWidth + 10;
		int h = windowHeight + 10;
		frame.setLocation(screenSize.width / 3 - w / 2, screenSize.height / 2 - h / 2);
		frame.setSize(w, h);
		frame.setVisible(true);
	}

	public class AdapterNode {
		org.w3c.dom.Node domNode;

		public AdapterNode(org.w3c.dom.Node node) {
			domNode = node;
		}

		public String toString() {
			String s = typeName[domNode.getNodeType()];
			String nodeName = domNode.getNodeName();
			if (!nodeName.startsWith("#")) {
				s += ": " + nodeName;
			}
			if (domNode.getNodeValue() != null) {
				if (s.startsWith("ProcInstr"))
					s += ", ";
				else
					s += ": ";

				String t = domNode.getNodeValue().trim();
				int x = t.indexOf("\n");
				if (x >= 0)
					t = t.substring(0, x);
				s += t;
			}
			return s;
		}

		public int index(AdapterNode child) {
			int count = childCount();
			for (int i = 0; i < count; i++) {
				AdapterNode n = this.child(i);
				if (child == n)
					return i;
			}
			return -1; // Should never get here.
		}

		public AdapterNode child(int searchIndex) {
			org.w3c.dom.Node node = domNode.getChildNodes().item(searchIndex);
			return new AdapterNode(node);
		}

		public int childCount() {
			return domNode.getChildNodes().getLength();
		}

	}

	public class DomToTreeModelAdapter implements javax.swing.tree.TreeModel {
		public Object getRoot() {
			return new AdapterNode(document);
		}

		public boolean isLeaf(Object aNode) {
			AdapterNode node = (AdapterNode) aNode;
			if (node.childCount() > 0)
				return false;
			return true;
		}

		public int getChildCount(Object parent) {
			AdapterNode node = (AdapterNode) parent;
			return node.childCount();
		}

		public Object getChild(Object parent, int index) {
			AdapterNode node = (AdapterNode) parent;
			return node.child(index);
		}

		public int getIndexOfChild(Object parent, Object child) {
			AdapterNode node = (AdapterNode) parent;
			return node.index((AdapterNode) child);
		}

		public void valueForPathChanged(TreePath path, Object newValue) {}

		private Vector listenerList = new Vector();

		public void addTreeModelListener(TreeModelListener listener) {
			if (listener != null && !listenerList.contains(listener)) {
				listenerList.addElement(listener);
			}
		}

		public void removeTreeModelListener(TreeModelListener listener) {
			if (listener != null) {
				listenerList.removeElement(listener);
			}
		}

		public void fireTreeNodesChanged(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeNodesChanged(e);
			}
		}

		public void fireTreeNodesInserted(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeNodesInserted(e);
			}
		}

		public void fireTreeNodesRemoved(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeNodesRemoved(e);
			}
		}

		public void fireTreeStructureChanged(TreeModelEvent e) {
			Enumeration listeners = listenerList.elements();
			while (listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener) listeners.nextElement();
				listener.treeStructureChanged(e);
			}
		}
	}

}
