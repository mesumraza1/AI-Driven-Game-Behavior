package pathfind;
import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class demopanel extends JPanel
{
	 int maxrow;
	 int maxcol;
	 int nodesize;
	 int row1,row2,column1,column2;
	 int number;
	 Node startnode,goalnode,currentnode;
	 int[][] array;
	 ArrayList<Node> walkableList=new ArrayList<>();
	 ArrayList<Node> checkedList=new ArrayList<>();
		
	    //node
		Node[][] node=new Node[0][0];
		
		//goal reached
		boolean goalReached=false;
		int step=0;

	public void demopanel1()
	{
		
		//Getting Details as Input 
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter no.of.Rows : ");
		 maxrow=sc.nextInt();
		System.out.println("Enter no.of.Columns : ");
		 maxcol=sc.nextInt();
		System.out.println("Enter the Node Size : ");
		 nodesize=sc.nextInt();
	
		//node
		 node=new Node[maxrow][maxcol];
			
		System.out.println("Enter the Position of Start Node : ");
		System.out.print("Row: ");
		 row1=sc.nextInt();
		System.out.print("Column: ");
		column1=sc.nextInt();
		
		System.out.println("Enter the Position of Goal Node : ");
		System.out.print("Row: ");
		 row2=sc.nextInt();
		System.out.print("Column: ");
		 column2=sc.nextInt();
		 
		 System.out.println("Enter the no.of.Obstacle Nodes : ");
		 number=sc.nextInt();
			
		 array=new int[number][2];
			int k=0;
			while(number-- !=0)
			{
				System.out.print("Row: ");
				array[k][0]=sc.nextInt();
				System.out.print("Column: ");
				array[k][1]=sc.nextInt();
				k++;
			}	
	}
	
	
	public demopanel()
	{
		demopanel1();
		
		final int screenwidth=nodesize*maxcol;
		final int screenheight=nodesize*maxrow;
		this.setPreferredSize(new Dimension(screenwidth,screenheight));
		this.setBackground(Color.BLACK);
		this.setLayout(new GridLayout(maxrow,maxcol));
		this.addKeyListener(new keyhandler(this));
		this.setFocusable(true);
		
		//place nodes and construct a grid
		int col=0,row=0;
		
		while(row<maxrow && col<maxcol)
		{
			node[row][col]=new Node(row,col);
			this.add(node[row][col]);
			col++;
			if(col==maxcol)
			{
				col=0;
				row++;
			}
		}
		//set start and stop node
		setStartNode(row1,column1);
		setGoalNode(row2,column2);
		
		//set obstacle nodes
		for(int i=0;i<array.length;i++)
		{
			setObstacleNode(array[i][0],array[i][1]);
			
		}
		
		//set the cost of every node	
		setCostOnNodes();
		
	}
	

	//Choosing start node
	 void setStartNode(int row,int col)
	{
		node[row][col].setAsStart();
		startnode=node[row][col];
		currentnode=startnode;
	}
	
	//choosing goal node
	void setGoalNode(int row,int col)
	{
		node[row][col].setAsGoal();
		goalnode=node[row][col];
	}
	
	//choosing obstacle nodes
	void setObstacleNode(int row,int col)
	{
		node[row][col].setAsObstacle();	
	}
	
	//setting costs
	private void setCostOnNodes()
	{
		int row=0,col=0;
		while(col < maxcol && row < maxrow)
		{
			getCost(node[row][col]);
			col++;
			if(col==maxcol)
			{
				col=0;
				row++;
			}
		}
	}
	
	private void getCost(Node node)
	{
		//get Gcost = distance from current node from start node
		int xDistance=Math.abs(node.row-startnode.row);
		int yDistance=Math.abs(node.col-startnode.col);
		node.gcost=xDistance + yDistance;
		
		//get hcost = distance from current node to goal node
		xDistance=Math.abs(node.row-goalnode.row);
		yDistance=Math.abs(node.col-goalnode.col);		
		node.hcost=xDistance + yDistance;
		
		//get fcost = hcost+gcost
		node.fcost=node.gcost+node.hcost;
		
		//display the cost in the node
		if(node != startnode && node!=goalnode )
		{
			node.setText("<html>F:" +node.fcost+ "<br>G:" + node.gcost + "<br>H:" + node.hcost +"</html>");
		}
	}
	
	//checking whether the node is walkable or not
	private void walkablenode(Node node)
	{
		if(node.walkable==false && node.checked==false && node.obstacle == false)
		{
			//if node is not opened ,add it to walkable list
			node.setAsWalkable();
			node.parent=currentnode;
			walkableList.add(node);
		}
	}
	
	//searching for an path
	public void search()
	{
		if(goalReached==false && step<300)
		{
			int row=currentnode.row;
			int col=currentnode.col;
			
			currentnode.setAsChecked();
			checkedList.add(currentnode);
			walkableList.remove(currentnode);
			
			//walkable the up node
			if(row-1>=0)
			{
				walkablenode(node[row-1][col]);
			}
			//walkable the left node
			if(col-1>=0)
			{
				walkablenode(node[row][col-1]);
			}
			//walkable the down node
			if(row+1<maxrow)
			{
				walkablenode(node[row+1][col]);
			}
			//walkable the right node
			if(col+1<maxcol)
			{
				walkablenode(node[row][col+1]);
			}
			
			//finding best node
			int bestNodeIndex=0;
			int bestNodefcost=999;
			
			//implementation of A* algorithm
			
			for(int i=0;i<walkableList.size();i++)
			{
				//check if this nodes F cost is better
				if(walkableList.get(i).fcost < bestNodefcost)
				{
					bestNodeIndex=i;
					bestNodefcost=walkableList.get(i).fcost;
				}
				
				//if fcost is equal,check the G host
				else if(walkableList.get(i).fcost == bestNodefcost)
				{
					if(walkableList.get(i).gcost <= walkableList.get(bestNodeIndex).gcost)
					{
						bestNodeIndex=i;
					}
				}
			}
			
			//After the loop we get the best node which is our next step
			currentnode=walkableList.get(bestNodeIndex);
			if(currentnode==goalnode)
			{
				goalReached=true;
				trackThePath();
			}
		}
		else if(goalReached==false && step>=300)
		{
			System.out.println("There is no path from start node to Goal node");
		}
	}
	
	public void autoSearch()
	{
		while(goalReached==false && step<300)
		{
			int row=currentnode.row;
			int col=currentnode.col;
			
			currentnode.setAsChecked();
			checkedList.add(currentnode);
			walkableList.remove(currentnode);
			
			//walkable the up node
			if(row-1>=0)
			{
				walkablenode(node[row-1][col]);
			}
			//walkable the left node
			if(col-1>=0)
			{
				walkablenode(node[row][col-1]);
			}
			//walkable the down node
			if(row+1<maxrow)
			{
				walkablenode(node[row+1][col]);
			}
			//walkable the right node
			if(col+1<maxcol)
			{
				walkablenode(node[row][col+1]);
			}
			
			//finding best node
			int bestNodeIndex=0;
			int bestNodefcost=999;
			
			for(int i=0;i<walkableList.size();i++)
			{
				//check if this nodes F cost is better
				if(walkableList.get(i).fcost < bestNodefcost)
				{
					bestNodeIndex=i;
					bestNodefcost=walkableList.get(i).fcost;
				}
				
				//if fcost is equal,check the G host
				else if(walkableList.get(i).fcost == bestNodefcost)
				{
					if(walkableList.get(i).hcost < walkableList.get(bestNodeIndex).gcost)
					{
						bestNodeIndex=i;
						System.out.println(bestNodeIndex+"\n");
					}
				}
			}
			//After the loop we get the best node which is our next step
			currentnode=walkableList.get(bestNodeIndex);
			if(currentnode==goalnode)
			{
				goalReached=true;
				trackThePath();
			}
			int temp=step;
			step++;
			if(temp==step)
				System.out.println("There is no path from start node to Goal node");
		}
		 
	}
	
	
	private void trackThePath()
	{
		Node current=goalnode;
		while(current!=startnode)
		{
			current=current.parent;
			
			if(current!=startnode) 
			{
				current.setAsPath();
			}
		}
	}
	
}






