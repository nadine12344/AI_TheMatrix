package code;

import java.util.HashSet;

public class Node {
 String gridState;//DOESN'T CONTAIN GLOBAL DAMAGE OR BOOTH LOCATION OR CARRY
 Node parent;
 String operation;
 int depth;
 costTriplet pathCost;
// HashSet<String>repeatedStates;
 public Node(String gs,Node p,String op,int dep,costTriplet pCost){
  gridState=gs;parent=p;operation=op;depth=dep;pathCost=pCost;
//  repeatedStates=h;
 }


}
