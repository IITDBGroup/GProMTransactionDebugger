package gui.transactionDebuggerFrame.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

public class Tutorial1
{
	public static void main(String args[])
	{

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		Graph graph = new DefaultGraph("Tutorial1");
		graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: grey;}"); // size-mode:
																								// fit

		// Node a = graph.addNode("t2[0]");
		// Node b = graph.addNode("t2[1]");
		// Node c = graph.addNode("t2[2]");
		//
		//
		// a.setAttribute("xy",0,0);
		// b.setAttribute("xy",1,0);
		// c.setAttribute("xy",2,0);
		//
		// a.addAttribute("label", "t2[0]");
		// b.addAttribute("label", "t2[1]");
		// c.addAttribute("label", "t2[2]");
		//
		//
		//
		// graph.addEdge("u1", b, a,true);
		// graph.addEdge("u2", c, b,true);
		//
		// graph.display(false); //false is to disable automatic placement of
		// the nodes

		// graph.addAttribute("ui.sreeenshot", "/home/david/export.jpg");

		// node to pre and next;
		// Map<String, List<String>> next = new HashMap<String, List<String>>();
		// Map<String, List<String>> prev = new HashMap<String, List<String>>();
		//
		// int level = 3;
		// List<String> list = new ArrayList<String>();
		// list.add("t[1]1");
		// next.put("t[1]0", list);
		// list.clear();
		//
		// list.add("t[1]2");
		// next.put("t[1]1", list);
		// list.clear();
		//
		// list.add("t[1]3");
		// next.put("t[1]2", list);
		// list.clear();
		////
		// list.add("t[1]0)");
		// prev.put("t[1]1", list);
		// list.clear();
		////
		// list.add("t[1]1");
		// prev.put("t[1]2", list);
		// list.clear();
		////
		// list.add("t[1]2");
		// prev.put("t[1]3", list);
		// list.clear();
		//
		// System.out.println(next.size());
		// System.out.println(next.get("t[1]0"));
		HashMap<String, String> next = new HashMap<String, String>();
		next.put("t[1]0", "t[1]1");
		next.put("t[1]1", "t[1]2");
		next.put("t[1]2", "t[1]3");
		HashMap<String, String> prev = new HashMap<String, String>();
		prev.put("t[1]1", "t[1]0");
		prev.put("t[1]2", "t[1]1");
		prev.put("t[1]3", "t[1]2");

		// Set<String> nodes = new HashSet<String>();
		// int attribute = 0;
		// int edgeNum = 0;
		// for(int i = 0; i < level; i++) {
		//// System.out.println(i);
		// String start = "t[1]" + String.valueOf(i);
		//// System.out.println(start);
		//
		// if(!nodes.contains(start)) {
		// nodes.add(start);
		// graph.addNode(start);
		// graph.setAttribute("xy",attribute, 0);
		// attribute++;
		// graph.addAttribute("label", start);
		// List<String> n = next.get(start);
		// for(int j = 0; j < n.size(); j++) {
		// String subnode = n.get(j);
		// nodes.add(subnode);
		//
		//
		//
		// }
		// for(int j = 0; j < next.get(start).size();j++) {
		// String subnode = next.get(start).get(j);
		// System.out.println(subnode);
		//// nodes.add(next.get(start).get(j)));
		// nodes.add(subnode);
		// graph.setAttribute("xy", attribute, 0);
		// attribute++;
		// graph.addAttribute("label", subnode);
		// graph.addEdge("u" + String.valueOf(edgeNum), subnode, start, true);
		// edgeNum++;
		// }
		// }
		// else {
		// for(int j = 0; j < next.get(start).size();j++) {
		// String subnode = next.get(start).get(j);
		//// nodes.add(next.get(start).get(j)));
		// nodes.add(subnode);
		// graph.setAttribute("xy", attribute, 0);
		// attribute++;
		// graph.addAttribute("label", subnode);
		// graph.addEdge("u" + String.valueOf(edgeNum), start, subnode, true);
		// edgeNum++;
		// }
		// }
		//
		// }

		// int level = 3;
		// int attribute = 0;
		// String start = "t[1]0";
		// Node stNode = graph.addNode(start);
		// stNode.setAttribute("xy", attribute);
		// attribute++;
		// stNode.addAttribute("label", start);
		// int edgeNum = 0;
		// for(int i = 0; i < level; i++) {
		// String currName = next.get(start);
		// Node currNode = graph.addNode(currName);
		// currNode.setAttribute("xy", attribute, 0);
		// attribute++;
		// currNode.addAttribute("label", currName);
		// graph.addEdge("u" + String.valueOf(edgeNum), stNode, currNode);
		// edgeNum++;
		// stNode = currNode;
		//
		// }
		// graph.display(false);
		// String startName = "t[1]0";
		//
		// Node startNode = graph.addNode(startName);
		// int attribute = 0;
		// startNode.setAttribute("xy", attribute, 0);
		// attribute++;
		// startNode.addAttribute("label", startName);
		//
		// int edgeNum = 0;
		// for(int i = 0; i < level; i++) {
		// String currName = next.get(startName);
		// Node currNode = graph.addNode(currName);
		//
		// currNode.setAttribute("xy", attribute, 0);
		// attribute++;
		// currNode.addAttribute("label", currName);
		// graph.addEdge("u" + String.valueOf(edgeNum), startNode, currNode,
		// true);
		// edgeNum++;
		// startNode = currNode;
		// }
		//
		// graph.display();
		int level = 1;
		ArrayList<Node> nodes = new ArrayList<Node>();
		int attribute = 0;
		for (int i = 0; i <= level; i++)
		{
			String nodeName = "t[1]" + String.valueOf(i);
			Node tempNode = graph.addNode(nodeName);
			tempNode.setAttribute("xy", attribute, 0);
			attribute++;
			tempNode.addAttribute("label", nodeName);
			nodes.add(tempNode);
		}
		// System.out.println(nodes.size());

		for (int i = 0; i < level; i++)
		{
			graph.addEdge("u" + String.valueOf(i), nodes.get(i), nodes.get(i + 1), true);
		}

//		graph.display(false);
		g();

		// for(int i = 0; i < level; i++) {
		// String start = "t[1]" + String.valueOf(i);
		// nodes.add(start);
		// for(int j = 0; j < next.get(start).size(); j++) {
		// nodes.add(next.get(start).get(j));
		// }
		//
		// }
		//
		// int attribute = 0;
		// ArrayList<Node> listNode = new ArrayList<Node>();
		// for(String s : nodes) {
		//
		// Node curNode = graph.addNode(s);
		// curNode.setAttribute("xy", attribute, 0);
		// curNode.addAttribute("label", s);
		// attribute++;
		// }
		// int edgeNum = 0;
		// for(int i = 0; i < level; i++) {
		// String start = "t[1]" + String.valueOf(i);
		//
		// for(int j = 0; j < next.get(start).size(); j++) {
		// String subNode = next.get(start).get(j);
		//
		// graph.addEdge("u" + String.valueOf(edgeNum), subNode, start,true);
		// start = subNode;
		// edgeNum++;
		// }
		// }

		// graph.display(true);
	}

	public static void g()
	{
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		Graph graph = new DefaultGraph("Tutorial1");
		graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: grey;}"); // size-mode:
																								// fit

		Node r = graph.addNode("Overdraft");
		r.setAttribute("xy", 0, 0);
		r.addAttribute("label", "OverDraft");

		Node leftPi1 = graph.addNode("leftPi1");
		leftPi1.setAttribute("xy", 1, 0);
		leftPi1.addAttribute("label", "Π");
		graph.addEdge("u1", r, leftPi1, true);

		Node leftPi2 = graph.addNode("leftPi2");
		leftPi2.setAttribute("xy", 2, 0);
		leftPi2.addAttribute("label", "Π");
		graph.addEdge("u2", leftPi1, leftPi2, true);

		Node union = graph.addNode("union");
		union.setAttribute("xy", 3, 0);
		union.addAttribute("label", "∪");
		graph.addEdge("u3", leftPi2, union, true);

		Node topPi = graph.addNode("toppi");
		topPi.setAttribute("xy", 4, 0);
		topPi.addAttribute("label", "Π");
		graph.addEdge("u4", union, topPi);
		
		
		
		
		
		
		
		graph.display(false);
	}

}