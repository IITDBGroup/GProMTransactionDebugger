package gui.transactionDebuggerFrame.graph;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.graphstream.ui.*;

import org.graphstream.stream.file.*;
import java.io.IOException;


public class Tutorial1{
	public static void main(String args[]){
		
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		Graph graph = new DefaultGraph("Tutorial1");
		graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: green;}");  // size-mode: fit
		
		
		Node a = graph.addNode("t2[0]");
		Node b = graph.addNode("t2[1]");
		Node c = graph.addNode("t2[2]");
		
		
		a.setAttribute("xy",0,0);
		b.setAttribute("xy",1,0);
		c.setAttribute("xy",2,0);
		
		a.addAttribute("label", "t2[0]");
		b.addAttribute("label", "t2[1]");
		c.addAttribute("label", "t2[2]");
		
		
		
		graph.addEdge("u1", b, a,true);
		graph.addEdge("u2", c, b,true);
		
		graph.display(false);		//false is to disable automatic placement of the nodes
		
		
		
		
		graph.addAttribute("ui.sreeenshot", "/home/david/export.jpg");
		

	}
}