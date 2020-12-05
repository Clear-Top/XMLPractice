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

public class DomEcho02 extends JPanel {
	static Document document;
	static final int windowHeight = 460;
	static final int leftWidth = 300;
	static final int rightWidth = 340;
	static final int windowWidth = leftWidth + rightWidth;
	static final String[] typeName = { "none", "Element", "Attr", "Text", "CDATA", "EntityRef", "Entity", "ProcInstr",
			"Comment", "Document", "DocType", "DocFragment", "Notation", };

	public DomEcho02() {
		// Make a nice border
//		EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
//		BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
//		CompoundBorder CB = new CompoundBorder(eb, bb);
//		this.setBorder(new CompoundBorder(CB, eb));

		// Set up the tree
		JTree tree = new JTree(new DomToTreeModelAdapter());

		// Build left-side view
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setPreferredSize(new Dimension(leftWidth, windowHeight));

		// Build right-side view
		JTextArea htmlPane = new JTextArea();
		
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);
		htmlView.setPreferredSize(new Dimension(rightWidth, windowHeight));

		// Build split-pane view
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, htmlView);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerLocation(leftWidth);
		splitPane.setPreferredSize(new Dimension(windowWidth + 10, windowHeight + 10));

		// Add GUI components
		this.setLayout(new BorderLayout());
		this.add("Center", splitPane);

	}

	public static void main(String argv[]) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder
					.parse(new File("C:\\Users\\cndwj\\OneDrive\\바탕 화면\\XML01_MiniProject4_김현모_16011015.xml"));
			makeFrame();
		} catch (SAXParseException spe) {
			System.out.println("에러...");
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

	public static void makeFrame() {
		// Set up a GUI framework
		JFrame frame = new JFrame("DOM Echo");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Set up the tree, the views, and display it all
		final DomEcho02 echoPanel = new DomEcho02();
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

		// Construct an Adapter node from a DOM node
		public AdapterNode(org.w3c.dom.Node node) {
			domNode = node;
		}

		// Return a string that identifies this node
		// in the tree
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

				// Trim the value to get rid of NL's
				// at the front
				String t = domNode.getNodeValue().trim();
				int x = t.indexOf("\n");
				if (x >= 0)
					t = t.substring(0, x);
				s += t;
			}
			return s;
		}

		public int index(AdapterNode child) {
			// System.err.println("Looking for index of " + child);
			int count = childCount();
			for (int i = 0; i < count; i++) {
				AdapterNode n = this.child(i);
				if (child == n)
					return i;
			}
			return -1; // Should never get here.
		}

		public AdapterNode child(int searchIndex) {
			// Note: JTree index is zero-based.
			org.w3c.dom.Node node = domNode.getChildNodes().item(searchIndex);
			return new AdapterNode(node);
		}

		public int childCount() {
			return domNode.getChildNodes().getLength();
		}

	}

	public class DomToTreeModelAdapter implements javax.swing.tree.TreeModel {
		// Basic TreeModel operations
		public Object getRoot() {
			// System.err.println("Returning root: " +document);
			return new AdapterNode(document);
		}

		public boolean isLeaf(Object aNode) {
			// Determines whether the icon shows up to the left.
			// Return true for any node with no children
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

		public void valueForPathChanged(TreePath path, Object newValue) {
			// Null. We won't be making changes in the GUI
			// If we did, we would ensure the new value was
			// really new and then fire a TreeNodesChanged event.
		}

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
