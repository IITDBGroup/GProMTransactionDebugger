package org.gprom.tdebug.gui.optframe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gprom.tdebug.main.GUIUtility;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

public class OptimizationInternalsFrame extends JFrame implements ActionListener
{

	private JButton g1;
	private JButton g2;
	private JButton g3;

	public OptimizationInternalsFrame(String title)
	{
		super(title);
		setup();
		setupListeners();
	}

	private void setup()
	{

		// JPanel jp1 = new JPanel();
		// JPanel jp2 = new JPanel();
		// JPanel jp3 = new JPanel();
		// JTextArea ja1=new JTextArea(5,20);
		JTextArea ja1 = new JTextArea();
		JTextArea ja2 = new JTextArea();
		JTextArea ja3 = new JTextArea();
		JScrollPane jp1 = new JScrollPane(ja1);
		JScrollPane jp2 = new JScrollPane(ja2);
		JScrollPane jp3 = new JScrollPane(ja3);
		// jp1.setLayout(new BorderLayout());

		// ja1.setFont(Bold);
		String str1 = "WITH temp_view_of_0 AS ("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, (CASE  WHEN ((F0.CUST = 'Alice') AND (F0.TYP = 'Savings')) THEN (F0.BAL - 70) ELSE F0.BAL END) AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS \"PROV_ACCOUNT_1_CUST\", F0.TYP AS \"PROV_ACCOUNT_1_TYP\", F0.BAL AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (ACCOUNT AS OF SCN 1478778490 F0)) F0)) F0))"
				+ "\nSELECT F0.CUST AS CUST, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM ((("
				+ "\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\", NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.CUST AS PROV_OVERDRAFT_CUST, F0.BAL AS PROV_OVERDRAFT_BAL"
				+ "\nFROM (OVERDRAFT AS OF SCN 1478778490 F0)) F0)) UNION ALL ("
				+ "\nSELECT F0.CUST AS CUST, F0.\"(BAL+BAL)\" AS \"(BAL+BAL)\", NULL AS PROV_OVERDRAFT_CUST, NULL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, (F0.BAL + F0.BAL) AS \"(BAL+BAL)\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F1.CUST AS CUST, F1.TYP AS TYP, F1.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F1.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F1.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (((temp_view_of_0) F0) CROSS JOIN ((temp_view_of_0) F1))) F0)"
				+ "\nWHERE ((((F0.CUST = 'Alice') AND (F0.CUST = F0.CUST)) AND (F0.TYP != F0.TYP)) AND ((F0.BAL + F0.BAL) < 0))) F0))) F0);";

		String str2 = "WITH temp_view_of_0 AS ("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, (CASE  WHEN ((F0.CUST = 'Alice') AND (F0.TYP = 'Savings')) THEN (F0.BAL - 70) ELSE F0.BAL END) AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS \"PROV_ACCOUNT_1_CUST\", F0.TYP AS \"PROV_ACCOUNT_1_TYP\", F0.BAL AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (ACCOUNT AS OF SCN 1478778490 F0)) F0)) F0)) F0)"
				+ "\nWHERE (F0.PROV_ACCOUNT_BAL = F0.\"PROV_ACCOUNT_1_BAL\"))"
				+ "\nSELECT F0.CUST AS CUST, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM ((("
				+ "\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\", NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.CUST AS PROV_OVERDRAFT_CUST, F0.BAL AS PROV_OVERDRAFT_BAL"
				+ "\nFROM (OVERDRAFT AS OF SCN 1478778490 F0)) F0)) UNION ALL ("
				+ "\nSELECT F0.CUST AS CUST, F0.\"(BAL+BAL)\" AS \"(BAL+BAL)\", NULL AS PROV_OVERDRAFT_CUST, NULL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, (F0.BAL + F0.BAL) AS \"(BAL+BAL)\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F1.CUST AS CUST, F1.TYP AS TYP, F1.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F1.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F1.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (((temp_view_of_0) F0) CROSS JOIN ((temp_view_of_0) F1))"
				+ "\nWHERE (((F0.CUST = 'Alice') AND (F0.PROV_ACCOUNT_CUST = 'Alice')) AND (F0.\"PROV_ACCOUNT_1_CUST\" = 'Alice'))) F0)"
				+ "\nWHERE ((F0.TYP != F0.TYP) AND ((F0.BAL + F0.BAL) < 0))) F0)"
				+ "\nWHERE (F0.PROV_ACCOUNT_TYP = F0.\"PROV_ACCOUNT_1_TYP\"))) F0)";

		String str3 = "WITH temp_view_of_0 AS ("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, (CASE  WHEN ((F0.CUST = 'Alice') AND (F0.TYP = 'Savings')) THEN (F0.BAL - 70) ELSE F0.BAL END) AS BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.CUST AS \"PROV_ACCOUNT_1_CUST\", F0.TYP AS \"PROV_ACCOUNT_1_TYP\", F0.BAL AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (ACCOUNT AS OF SCN 1478778490 F0)" + "\nWHERE (F0.BAL = F0.BAL))"
				+ "\nSELECT F0.CUST AS CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_CUST, F0.PROV_ACCOUNT_CUST AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_TYP\", F0.PROV_ACCOUNT_CUST AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_TYP\", F0.PROV_ACCOUNT_CUST AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM ((("
				+ "\nSELECT F0.CUST AS CUST, F0.CUST AS PROV_OVERDRAFT_CUST, F0.BAL AS PROV_OVERDRAFT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\", NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (OVERDRAFT AS OF SCN 1478778490 F0)) UNION ALL ("
				+ "\nSELECT F0.CUST AS CUST, NULL AS PROV_OVERDRAFT_CUST, NULL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (("
				+ "\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
				+ "\nFROM (((temp_view_of_0) F0) CROSS JOIN ((temp_view_of_0) F1))"
				+ "\nWHERE (((F0.TYP != F0.TYP) AND ((F0.BAL + F0.BAL) < 0)) AND (((F0.CUST = 'Alice') AND (F0.PROV_ACCOUNT_CUST = 'Alice')) AND (F0.\"PROV_ACCOUNT_1_CUST\" = 'Alice')))) F0)"
				+ "\nWHERE (F0.PROV_ACCOUNT_TYP = F0.\"PROV_ACCOUNT_1_TYP\"))) F0)";

		// String str1 = "\n UPDATE employee \n SET name = 'Alice'\n WHERE ID =
		// 1";
		// String str2 = "\n UPDATE employee \n SET name = 'Bob'\n WHERE ID =
		// 2";
		// String str3 = "\n UPDATE employee \n SET name = 'Mike'\n WHERE ID =
		// 3";
		ja1.setText(str1);
		ja2.setText(str2);
		ja3.setText(str3);

		// ja1.setCaretColor(Color.BLUE);
		// ja2.setCaretColor(Color.BLUE);
		// ja3.setCaretColor(Color.BLUE);

		ja1.setEditable(false);
		ja2.setEditable(false);
		ja3.setEditable(false);

		// jp1.add(ja1);
		// jp2.add(ja2);
		// jp3.add(ja3);

		this.setLayout(null);

		this.add(jp1);
		this.add(jp2);
		this.add(jp3);

		jp1.setBounds(135, 35, 260, 95);
		jp1.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jp2.setBounds(390, 35, 260, 95);
		jp2.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jp3.setBounds(648, 35, 260, 95);
		jp3.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

		ja1.setBounds(0, 10, 5, 20);
		ja2.setBounds(0, 10, 5, 20);
		ja3.setBounds(0, 10, 5, 20);

		// second line
		// JPanel jp4 = new JPanel();
		// JPanel jp5 = new JPanel();
		// JPanel jp6 = new JPanel();
		// jp4.setBounds(135, 100, 260, 65);
		// jp4.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// jp5.setBounds(390, 100, 260, 65);
		// jp5.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// jp6.setBounds(648, 100, 260, 65);
		// jp6.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// this.add(jp4);
		// this.add(jp5);
		// this.add(jp6);
		//
		//
		// String[] cn4 = {"Id","Name","Salary"};
		// String[] cn5 = {"Id","Name","Salary"};
		// String[] cn6 = {"Id","Name","Salary"};
		// Object[][] cd4 =
		// {{"1","Alice","3600"},{"2","Alex","4300"},{"3","Peter","5000"}};
		// Object[][] cd5 =
		// {{"1","Alice","3600"},{"2","Bob","4300"},{"3","Peter","5000"}};
		// Object[][] cd6 =
		// {{"1","Alice","3600"},{"2","Bob","4300"},{"3","Mike","5000"}};
		// JTable ta4 = new JTable(cd4,cn4);
		// JTable ta5 = new JTable(cd5,cn5);
		// JTable ta6 = new JTable(cd6,cn6);
		//
		// jp4.add(ta4);
		// jp5.add(ta5);
		// jp6.add(ta6);

		// JPanel jpF = new JPanel();
		// jpthis.setBounds(135, 165, 773, 200);
		// jpthis.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// this.add(jpF);
		// third line
		JPanel jp7 = new JPanel();
		JPanel jp8 = new JPanel();
		JPanel jp9 = new JPanel();
		// jp7.setBounds(135, 130, 260, 300);
		// jp7.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		// jp8.setBounds(390, 130, 260, 300);
		// jp8.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		// jp9.setBounds(648, 130, 260, 300);
		// jp9.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

		// new design
		jp7.setBounds(135, 130, 260, 70);
		jp7.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jp8.setBounds(390, 130, 260, 70);
		jp8.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jp9.setBounds(648, 130, 260, 70);
		jp9.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

		// dotFlow.png
		
		
		//12.1
		/*
		GUIUtility.createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/0_beforeOpt.png",
				"/Users/xun/Documents/java_workspace/gprom-gui/test1.png", 300, 280);
		File file1 = new File("/Users/xun/Documents/java_workspace/gprom-gui/test1.png");
		GUIUtility.createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/1_selectionMoveAround.png",
				"/Users/xun/Documents/java_workspace/gprom-gui/test22.png", 300, 280);
		File file2 = new File("/Users/xun/Documents/java_workspace/gprom-gui/test22.png");
		GUIUtility.createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/5_mgAdProSel.png",
				"/Users/xun/Documents/java_workspace/gprom-gui/test3.png", 300, 280);
		File file3 = new File("/Users/xun/Documents/java_workspace/gprom-gui/test3.png");

		BufferedImage image1;
		BufferedImage image2;
		BufferedImage image3;
		try
		{
			image1 = ImageIO.read(file1);
			image2 = ImageIO.read(file2);
			image3 = ImageIO.read(file3);
			JLabel label1 = new JLabel(new ImageIcon(image1));
			JLabel label2 = new JLabel(new ImageIcon(image2));
			JLabel label3 = new JLabel(new ImageIcon(image3));

			// jp7.add(label1);
			// jp8.add(label2);
			// jp9.add(label3);

			// label1.setBounds(0, 10, 5, 10);

		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	*/
		//12.1
		this.add(jp7);
		this.add(jp8);
		this.add(jp9);

		// first column
		JPanel jp11 = new JPanel();
		JPanel jp12 = new JPanel();
		JPanel jp13 = new JPanel();
		jp11.setBounds(35, 35, 100, 95);
		jp11.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		// jp12.setBounds(35, 100, 100, 65);
		// jp12.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		jp13.setBounds(35, 130, 100, 70);
		jp13.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		this.add(jp11);
		this.add(jp12);
		this.add(jp13);
		JLabel jl1 = new JLabel("SQL");
		JLabel jl2 = new JLabel("TABLE");
		JLabel jl3 = new JLabel("GRAPH");
		jp11.add(jl1);
		jp12.add(jl2);
		jp13.add(jl3);

		// first label line
		// first column
		JPanel jpl1 = new JPanel();
		JPanel jpl2 = new JPanel();
		JPanel jpl3 = new JPanel();
		JPanel jpl4 = new JPanel();
		jpl1.setBounds(35, 5, 100, 30);
		jpl1.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jpl2.setBounds(135, 5, 260, 30);
		jpl2.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jpl3.setBounds(390, 5, 260, 30);
		jpl3.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		jpl4.setBounds(648, 5, 260, 30);
		jpl4.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		this.add(jpl1);
		this.add(jpl2);
		this.add(jpl3);
		this.add(jpl4);
		JLabel jll1 = new JLabel("Methods");
		JLabel jll2 = new JLabel("Before Optimization");
		JLabel jll3 = new JLabel("Selection Move Around");
		JLabel jll4 = new JLabel("Merge");
		jpl1.add(jll1);
		jpl2.add(jll2);
		jpl3.add(jll3);
		jpl4.add(jll4);

		// //get Graph
		g1 = new JButton("Graph");
		g1.setBounds(100, 100, 100, 100);
		jp7.add(g1);
		g2 = new JButton("Graph");
		g2.setBounds(100, 100, 100, 100);
		jp8.add(g2);
		g3 = new JButton("Graph");
		g3.setBounds(100, 100, 100, 100);
		jp9.add(g3);

		// Right buttons
		// Button jb1 = new Button("Refresh");
		// JButton jb2 = new JButton("Optimizer");
		// jb1.setBounds(920, 30, 100, 40);
		// jb2.setBounds(920, 70, 100, 40);
		// //jb1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// //jb2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// this.add(jb1);
		// this.add(jb2);
		this.setVisible(true);
	}

	private void setupListeners()
	{
		g1.addActionListener(this);
		g2.addActionListener(this);
		g3.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == g1)
		{
			showGraph(1);

		} else if (e.getSource() == g2)
		{
			showGraph(2);
		} else if (e.getSource() == g3)
		{
			showGraph(3);
		}

	}

	public void showGraph(int level)
	{
		// System.setProperty("org.graphstream.ui.renderer",
		// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		//
		// Graph graph = new DefaultGraph("Tutorial1");
		// graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box;
		// fill-color: grey;}"); // size-mode: fit
		// ArrayList<Node> nodes = new ArrayList<Node>();
		// int attribute = 0;
		// for(int i = 0; i <= level; i++) {
		// String nodeName = "t[1]" + String.valueOf(i);
		// Node tempNode = graph.addNode(nodeName);
		// tempNode.setAttribute("xy", attribute, 0);
		// attribute++;
		// tempNode.addAttribute("label", nodeName);
		// nodes.add(tempNode);
		// }
		//// System.out.println(nodes.size());
		//
		// for(int i = 0; i < level; i++) {
		// graph.addEdge("u" + String.valueOf(i), nodes.get(i), nodes.get(i +
		// 1), true);
		// }
		//
		// graph.display(false);
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		Graph graph = new DefaultGraph("Tutorial1");
//		graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: white}"); // size-mode:
		// fit
		
		
		graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: grey;}");
		if (level == 1)
		{
			Node r = graph.addNode("Overdraft");
			r.setAttribute("xy", 0, 0);
			r.addAttribute("label", "OD");

			Node leftPi1 = graph.addNode("leftPi1");
			leftPi1.setAttribute("xy", 1.3, 0);
			leftPi1.addAttribute("ui.color"	, Color.blue);
			leftPi1.addAttribute("label", "Π");
			graph.addEdge("u1", leftPi1, r, true);

			Node leftPi2 = graph.addNode("leftPi2");
			leftPi2.setAttribute("xy", 2.6, 0);
			leftPi2.addAttribute("label", "Π");
			graph.addEdge("u2", leftPi2, leftPi1, true);

			Node union = graph.addNode("union");
			union.setAttribute("xy", 3.9, 0);
			union.addAttribute("label", "∪");
			graph.addEdge("u3", union, leftPi2, true);

			Node topPi = graph.addNode("toppi");
			topPi.setAttribute("xy", 5.4, 0);
			topPi.addAttribute("label", "Π");
			graph.addEdge("u4", topPi, union, true);

			Node s = graph.addNode("account");
			s.setAttribute("xy", 0, 1);
			s.addAttribute("label", "ACC");

			Node rightPi1 = graph.addNode("rightPi1");
			rightPi1.setAttribute("xy", 0.5, 1);
			rightPi1.addAttribute("label", "Π");
			graph.addEdge("u10", rightPi1, s, true);

			Node rightPi2 = graph.addNode("rightPi2");
			rightPi2.setAttribute("xy", 1, 1);
			rightPi2.addAttribute("label", "Π");
			graph.addEdge("u11", rightPi2, rightPi1, true);

			Node rightPi3 = graph.addNode("rightPi3");
			rightPi3.setAttribute("xy", 1.5, 1);
			rightPi3.addAttribute("label", "Π");
			graph.addEdge("u12", rightPi3, rightPi2, true);

			Node x = graph.addNode("x");
			x.setAttribute("xy", 2, 1);
			x.addAttribute("label", "|x|");
			graph.addEdge("u13", x, rightPi3, true);

			Node rightPi4 = graph.addNode("rightPi4");
			rightPi4.setAttribute("xy", 2.5, 1);
			rightPi4.addAttribute("label", "Π");
			graph.addEdge("u15", rightPi4, x, true);

			Node delta = graph.addNode("delta");
			delta.setAttribute("xy", 3, 1);
			delta.addAttribute("label", "σ");
			graph.addEdge("u16", delta, rightPi4, true);

			Node rightPi5 = graph.addNode("rightPi5");
			rightPi5.setAttribute("xy", 3.5, 1);
			rightPi5.addAttribute("label", "Π");
			graph.addEdge("u17", rightPi5, delta, true);

			Node rightPi6 = graph.addNode("rightPi6");
			rightPi6.setAttribute("xy", 3.5, 1);
			rightPi6.addAttribute("label", "Π");
			graph.addEdge("u18", rightPi6, rightPi5, true);

			graph.addEdge("u01", union, rightPi6, true);

			graph.display(false);
		}
		if (level == 2)
		{
			Node r = graph.addNode("Overdraft");
			r.setAttribute("xy", 0, 0);
			r.addAttribute("label", "OD");

			Node leftPi1 = graph.addNode("leftPi1");
			leftPi1.setAttribute("xy", 1.5, 0);
			leftPi1.addAttribute("label", "Π");
			graph.addEdge("u1", leftPi1, r, true);

			Node leftPi2 = graph.addNode("leftPi2");
			leftPi2.setAttribute("xy", 3, 0);
			leftPi2.addAttribute("label", "Π");
			graph.addEdge("u2", leftPi2, leftPi1, true);

			Node union = graph.addNode("union");
			union.setAttribute("xy", 4.5, 0);
			union.addAttribute("label", "∪");
			graph.addEdge("u3", union, leftPi2, true);

			Node topPi = graph.addNode("toppi");
			topPi.setAttribute("xy", 6, 0);
			topPi.addAttribute("label", "Π");
			graph.addEdge("u4", topPi, union, true);

			// acc part
			Node s = graph.addNode("account");
			s.setAttribute("xy", 0, 1);
			s.addAttribute("label", "ACC");

			Node rightPi1 = graph.addNode("rightPi1");
			rightPi1.setAttribute("xy", 0.4, 1);
			rightPi1.addAttribute("label", "Π");
			graph.addEdge("u10", rightPi1, s, true);

			Node rightPi2 = graph.addNode("rightPi2");
			rightPi2.setAttribute("xy", 0.8, 1);
			rightPi2.addAttribute("label", "Π");
			graph.addEdge("u11", rightPi2, rightPi1, true);

			Node rightPi3 = graph.addNode("rightPi3");
			rightPi3.setAttribute("xy", 1.2, 1);
			rightPi3.addAttribute("label", "Π");
			graph.addEdge("u12", rightPi3, rightPi2, true);

			Node delta = graph.addNode("delta");
			delta.setAttribute("xy", 1.6, 1);
			delta.addAttribute("label", "σ");
			graph.addEdge("u22", delta, rightPi3, true ); 
			

			Node x = graph.addNode("x");
			x.setAttribute("xy", 2, 1);
			x.addAttribute("label", "|x|");
			graph.addEdge("u13", x, delta, true);

			Node delta2 = graph.addNode("delta2");
			delta2.setAttribute("xy", 2.4, 1);
			delta2.addAttribute("label", "σ");
			graph.addEdge("u14", delta2, x, true);

			Node rightPi4 = graph.addNode("rightPi4");
			rightPi4.setAttribute("xy", 2.8, 1);
			rightPi4.addAttribute("label", "Π");
			graph.addEdge("u15", rightPi4, delta2, true);
			
			Node delta3 = graph.addNode("delta3");
			delta3.setAttribute("xy", 3.2, 1);
			delta3.addAttribute("label", "σ");
			graph.addEdge("u16", delta3, rightPi4, true);
			
			Node rightPi5 = graph.addNode("rightPi5");
			rightPi5.setAttribute("xy", 3.6, 1);
			rightPi5.addAttribute("label", "Π");
			graph.addEdge("u17", rightPi5, delta3, true);
			
			Node delta4 = graph.addNode("delta4");
			delta4.setAttribute("xy", 4, 1);
			delta4.addAttribute("label", "σ");
			graph.addEdge("u18", delta4, rightPi5, true);
			
			Node rightPi6 = graph.addNode("rightPi6");
			rightPi6.setAttribute("xy", 4.4, 1);
			rightPi6.addAttribute("label", "Π");
			graph.addEdge("u19", rightPi6, delta4, true);
			
			graph.addEdge("combin", union, rightPi6, true);
			
			graph.display(false);
		}
		if (level == 3)
		{
			Node r = graph.addNode("Overdraft");
			r.setAttribute("xy", 0, 0);
			r.addAttribute("label", "OD");

			Node leftPi1 = graph.addNode("leftPi1");
			leftPi1.setAttribute("xy", 2, 0);
			leftPi1.addAttribute("label", "Π");
			graph.addEdge("u1", leftPi1, r, true);


			Node union = graph.addNode("union");
			union.setAttribute("xy", 4, 0);
			union.addAttribute("label", "∪");
			graph.addEdge("u3", union, leftPi1, true);

			Node topPi = graph.addNode("toppi");
			topPi.setAttribute("xy", 6, 0);
			topPi.addAttribute("label", "Π");
			graph.addEdge("u4", topPi, union, true);
			
			
			//acc part
			Node s = graph.addNode("account");
			s.setAttribute("xy", 0, 1);
			s.addAttribute("label", "ACC");

			Node rightPi1 = graph.addNode("rightPi1");
			rightPi1.setAttribute("xy", 0.4, 1);
			rightPi1.addAttribute("label", "Π");
			graph.addEdge("u10", rightPi1, s, true);

//			Node rightPi2 = graph.addNode("rightPi2");
//			rightPi2.setAttribute("xy", 0.8, 1);
//			rightPi2.addAttribute("label", "Π");
//			graph.addEdge("u11", rightPi2, rightPi1, true);
//
//			Node rightPi3 = graph.addNode("rightPi3");
//			rightPi3.setAttribute("xy", 1.2, 1);
//			rightPi3.addAttribute("label", "Π");
//			graph.addEdge("u12", rightPi3, rightPi2, true);

			Node delta = graph.addNode("delta");
			delta.setAttribute("xy", 0.8, 1);
			delta.addAttribute("label", "σ");
			graph.addEdge("u22", delta, rightPi1, true ); 
			

			Node x = graph.addNode("x");
			x.setAttribute("xy", 1.2, 1);
			x.addAttribute("label", "|x|");
			graph.addEdge("u13", x, delta, true);

			Node delta2 = graph.addNode("delta2");
			delta2.setAttribute("xy", 1.6, 1);
			delta2.addAttribute("label", "σ");
			graph.addEdge("u14", delta2, x, true);

			Node rightPi4 = graph.addNode("rightPi4");
			rightPi4.setAttribute("xy", 2, 1);
			rightPi4.addAttribute("label", "Π");
			graph.addEdge("u15", rightPi4, delta2, true);
			
			Node delta3 = graph.addNode("delta3");
			delta3.setAttribute("xy", 2.4, 1);
			delta3.addAttribute("label", "σ");
			graph.addEdge("u16", delta3, rightPi4, true);
			
			Node rightPi5 = graph.addNode("rightPi5");
			rightPi5.setAttribute("xy", 2.8, 1);
			rightPi5.addAttribute("label", "Π");
			graph.addEdge("u17", rightPi5, delta3, true);
			
			Node delta4 = graph.addNode("delta4");
			delta4.setAttribute("xy", 3.2, 1);
			delta4.addAttribute("label", "σ");
			graph.addEdge("u18", delta4, rightPi5, true);
			
			Node rightPi6 = graph.addNode("rightPi6");
			rightPi6.setAttribute("xy", 3.6, 1);
			rightPi6.addAttribute("label", "Π");
			graph.addEdge("u19", rightPi6, delta4, true);
			
			graph.addEdge("combin", union, rightPi6, true);
			
			graph.display(false);
		}

	}
}
