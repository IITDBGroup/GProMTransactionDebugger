package gprom.gui;

import java.sql.Timestamp;
import java.util.*;


public class ListNode{
	ArrayList<TransactionNode> L = new ArrayList<TransactionNode>();
	public void PrintList()
	{
		System.out.println("This is a TransactionNode List\n");
	}
	public boolean AddNode(TransactionNode n){
		return L.add(n);
	}
	public TransactionNode GetNode(int n){
		return L.get(n);
	}

	public boolean IsEmpty(){    //Returns true if this list contains no elements.
		return L.isEmpty();
	}

	public boolean Remove(TransactionNode n){
		return L.remove(n);
	}
	
	public int Size(){
		return L.size();
	}
}