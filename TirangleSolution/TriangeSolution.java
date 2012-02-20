import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is quick and dirty code to solve the triangle problem.
 * @param args text file name
 */
public class TriangeSolution {
	static final int MAX_SIZE = 6000; // should be plenty big enough to hold nodes
	static int maxCount;
	static String fileName;
	static ArrayList<NodeTreeElement> nodeTree;
	static int nodeTreePointer;
	static 	int numberOfLevels;
	static int numberOfElements;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("need to supply file name!");
			System.exit(0);
		}
		fileName = args[0];
		nodeTree = new ArrayList<NodeTreeElement>(MAX_SIZE);

		// read text file into tree
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String inputLine;
			String [] splitLine;
			while ((inputLine = br.readLine()) != null) {
				// put the elements (numbers) each in a working array
				// this would normally have some sort of exception catching here 
				// if this were not a just for fun puzzle solution
				splitLine = inputLine.split(" ");
				for (int i = 0; i < splitLine.length; i++) {
					// build a node element and using the value in the work array
					// left and right child nodes will be null by default
					NodeTreeElement nte = new NodeTreeElement(Integer.parseInt(splitLine[i]));
					nte.setNodeLevel(numberOfLevels);
					nodeTree.add(nte);
					numberOfElements++;
				}
				numberOfLevels++;
			}
			br.close();
		} catch (IOException e) {
			System.out.println("something wrong with file!");
		} catch (Exception e) {
			System.out.println("an exception was caught!");
		}

		// now that all of the values are in the tree the left and right children
		// locations need to be set so the tree can be traversed
		// the last level - leaf nodes will be left null
		int thisElement = 0;
		int thisLevel = 0;
		int addChildPointersBelowTheseLevels = numberOfLevels - 1;
		while (thisLevel < addChildPointersBelowTheseLevels) { // only up to the next to the last level			
			int lcPosition = thisElement + thisLevel +1;
			int rcPosition = lcPosition + 1; 
			nodeTree.get(thisElement).setLeftChild(lcPosition) ;
			nodeTree.get(thisElement).setRightChild(rcPosition);
			thisElement++;
			thisLevel = nodeTree.get(thisElement).getNodeLevel();
		}

		// start at last node and roll up the best path for each node based on the either the left or right child
		int maxNodes = numberOfElements - 1; // use number of elements for relative subscript so decrease by 1
		for (int i = maxNodes; i >= 0; i-- ) {
			Integer lc = nodeTree.get(i).getLeftChild();
			Integer rc = nodeTree.get(i).getRightChild();
			String newBestPathDown;
			int newNodeValue = 0;
			if ((lc == null) || (rc == null)) {
				 // the bottom nodes have no children
				newNodeValue = nodeTree.get(i).getNodeValue();
				newBestPathDown = "\n" + nodeTree.get(i).getNodeValue()
				+ " at node " + i
				+ " on level " + nodeTree.get(i).getNodeLevel();
			} else {				
				if (nodeTree.get(lc).getNodeValue() > nodeTree.get(rc).getNodeValue()) {
					newNodeValue = nodeTree.get(i).getNodeValue()
						+ nodeTree.get(lc).getNodeValue();
					newBestPathDown = "\n" + nodeTree.get(i).getNodeValue()
						+ " at node " + i
						+ " on level " + nodeTree.get(i).getNodeLevel()
						+  nodeTree.get(lc).getBestPathDown();
				} else {
					newNodeValue = nodeTree.get(i).getNodeValue()
						+ nodeTree.get(rc).getNodeValue();					
					newBestPathDown = "\n" + nodeTree.get(i).getNodeValue()
						+ " at node " + i
						+ " on level " + nodeTree.get(i).getNodeLevel()
						+  nodeTree.get(rc).getBestPathDown();
				}				
			}
			nodeTree.get(i).setNodeValue(newNodeValue);
			nodeTree.get(i).setBestPathDown(newBestPathDown);
		}

		// write solution to a text file
		try {
			FileWriter fstream = new FileWriter("solution.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Max Value is " + nodeTree.get(0).getNodeValue() + "\n");
			out.write("The Best Path chosen was:"  + nodeTree.get(0).getBestPathDown());
			out.close();
		} catch (Exception e) {
			System.out.println("error writing to solution.txt: " + e.getMessage());
		}

	}
}

/**
 *  One of these for each node identified by its location. Contains the value, 
 *  level at this node, a String containing the best path so far, along with 
 *  left and right child node locations
 *  @param Integer Location of this node
 */
class NodeTreeElement {	
	private Integer nodeLevel;
	private Integer nodeValue;
	private String bestPathDown;
	private Integer leftChild;
	private Integer rightChild; 
	public NodeTreeElement (Integer nodeValue) {
		this.nodeValue = nodeValue;
	}
	public Integer getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(Integer nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	public Integer getNodeValue() {
		return nodeValue;
	}		
	public void setNodeValue(Integer nodeValue) {
		this.nodeValue = nodeValue;
	}
	public String getBestPathDown() {
		return bestPathDown;
	}
	public void setBestPathDown(String bestPathDown) {
		this.bestPathDown = bestPathDown;
	}
	public Integer getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(Integer leftChild) {
		this.leftChild = leftChild;
	}
	public Integer getRightChild() {
		return rightChild;
	}
	public void setRightChild(Integer rightChild) {
		this.rightChild = rightChild;
	}
}
