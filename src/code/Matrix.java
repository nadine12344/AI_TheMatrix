package code;

import java.io.PrintWriter;
import java.util.*;

public class Matrix extends SearchProblem {

    static class pair {
        int i;
        int j;

        public pair(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    static class NodeWithMaxDepth {
        Node node;
        int depth;

        public NodeWithMaxDepth(Node n, int d) {
            node = n;
            depth = d;
        }
    }

    public Matrix() {
        //changed
        this.operators = new String[]{"up", "down", "left", "right", "carry",
                "drop", "takePill", "kill", "fly"};
    }


//    static HashSet<String> states;


    static ArrayList<Position> generateRandomPositions(int dimensionX, int dimensionY) {
        ArrayList<Position> allPositions = new ArrayList<>();
        int pos = 0;
        for (int i = 0; i < dimensionX; i++) {
            for (int j = 0; j < dimensionY; j++) {
                allPositions.add(new Position(i, j));
            }
        }
        Collections.shuffle(allPositions);
        return allPositions;
    }

    public static String genGrid() {
        String res = "";
        // dimesions mn 5-> 15
        int dimensionX = (int) ((Math.random() * 11) + 5);//0<= num< 11 -> +5 -> 5 <=num<16
        int dimensionY = (int) ((Math.random() * 11) + 5);
        n = dimensionX;
        m = dimensionY;
        ArrayList<Position> randomPositions = generateRandomPositions(dimensionX, dimensionY);
        // NEO'S POSITION
        Position neo = randomPositions.remove(0);
        //TELEPHON BOOTH POS
        Position telephoneBooth = randomPositions.remove(0);
        telX = telephoneBooth.i;
        telY = telephoneBooth.j;

        //CARRY
        int carry = (int) ((Math.random() * 4) + 1);
        c = carry;
        //NUM HOSTAGES
        int numPosLeft = dimensionX * dimensionY;
        numPosLeft -= 2;
        int numHostages = (int) ((Math.random() * 8) + 3);
        numHostages = Math.min(numHostages, numPosLeft);
        numPosLeft -= numHostages;
        StringBuilder sb = new StringBuilder();

        //HOSTAGES
        ArrayList<Hostage> hostages = new ArrayList<>();

        for (int i = 0; i < numHostages; i++) {
            hostages.add(new Hostage(randomPositions.remove(0), (int) ((Math.random() * 99) + 1)));
        }
        //Pills
        int numPills = (int) ((Math.random() * (numHostages + 1)));
        numPills = Math.min(numPills, numPosLeft);
        numPosLeft -= numPills;
        ArrayList<Pill> pills = new ArrayList<>();
        for (int i = 0; i < numPills; i++) {
            pills.add(new Pill(randomPositions.remove(0)));
        }
        //PADS
        int numPads = (int) ((Math.random() * (numPosLeft / 2 + 1)));
        numPosLeft -= (numPads * 2);
        ArrayList<Pad> pads = new ArrayList<>();
        for (int i = 0; i < numPads; i++) {
            pads.add(new Pad(randomPositions.remove(0), randomPositions.remove(0)));
        }
        //AGENTS
        int numAgents = (int) ((Math.random() * (numPosLeft + 1)));
        numPosLeft -= numAgents;
        ArrayList<Agent> agents = new ArrayList<>();
        for (int i = 0; i < numAgents; i++) {
            agents.add(new Agent(randomPositions.remove(0)));
        }
        //DIMENSIONS
        res += dimensionX + "," + dimensionY + ";";
        //CARRY
        res += carry + ";";
        // NEO POS
        res += neo.i + "," + neo.j;
        res += ";";
        //TELEPHONE
        res += telephoneBooth.i + "," + telephoneBooth.j + ";";
        //AGENTS
        for (int i = 0; i < numAgents; i++) {
            res += agents.get(i).pos.i + "," + agents.get(i).pos.j;
            if (i < numAgents - 1)
                res += ",";
        }
        if (agents.size() == 0) {
            res += " ";
        }
        res += ";";

        //PILLS
        for (int i = 0; i < numPills; i++) {
            res += pills.get(i).pos.i + "," + pills.get(i).pos.j;
            if (i < numPills - 1)
                res += ",";
        }
        if (pills.size() == 0) {
            res += " ";
        }
        res += ";";
        //PADS
        for (int i = 0; i < numPads; i++) {
            res += pads.get(i).posBeg.i + "," + pads.get(i).posBeg.j + "," + pads.get(i).posEnd.i + "," + pads.get(i).posEnd.j;
            if (i < numPads - 1)
                res += ",";
        }
        if (pads.size() == 0) {
            res += " ";
        }
        res += ";";
        //HOSTAGES
        for (int i = 0; i < numHostages; i++) {
            res += hostages.get(i).pos.i + "," + hostages.get(i).pos.j + hostages.get(i).damage;
            if (i < numHostages - 1)
                res += ",";
        }
        //res=swapRowsAndColumns(res);
        return res;
    }

    static String swapRowsAndColumns(String grid) {
        StringBuilder res = new StringBuilder();
        String[] gridSplit = grid.split(";");
        //SWAP ROWS AND COLUMNS IN INPUT
        String[] current = gridSplit[0].split(",");
        res.append(current[1]).append(",").append(current[0]).append(";");
        //ADD THE CARRY
        res.append(gridSplit[1]).append(";");
        //SWAP NEO POSITION
        current = gridSplit[2].split(",");
        res.append(current[1]).append(",").append(current[0]).append(";");
        //SWAP TELEPHONE POSITIONS
        current = gridSplit[3].split(",");
        res.append(current[1]).append(",").append(current[0]).append(";");
        //AGENTS
        current = gridSplit[4].split(",");
        if (current.length > 1) {
            for (int i = 0; i < current.length; i += 2) {
                res.append(current[i + 1]).append(",").append(current[i]);
                if (i + 1 < current.length - 1) {
                    res.append(",");
                }
            }
        } else {
            res.append(" ");
        }
        res.append(";");
        //Pills
        current = gridSplit[5].split(",");
        if (current.length > 1) {
            for (int i = 0; i < current.length; i += 2) {
                res.append(current[i + 1]).append(",").append(current[i]);
                if (i + 1 < current.length - 1) {
                    res.append(",");
                }
            }
        } else {
            res.append(" ");
        }
        res.append(";");
        //PADS
        current = gridSplit[6].split(",");
        if (current.length > 1) {
            for (int i = 0; i < current.length; i += 2) {
                res.append(current[i + 1]).append(",").append(current[i]);
                if (i + 1 < current.length - 1) {
                    res.append(",");
                }
            }
        } else {
            res.append(" ");
        }
        res.append(";");
        //HOSTAGES
        current = gridSplit[7].split(",");
        if (current.length > 1) {
            for (int i = 0; i < current.length; i += 3) {
                res.append(current[i + 1]).append(",").append(current[i]).append(",").append(current[i + 2]);
                if (i + 2 < current.length - 1) {
                    res.append(",");
                }
            }
        } else {
            res.append(" ");
        }


        return res.toString();
    }
    costTriplet pathCost(Node n){
        String[]grid=n.gridState.split(";");
        return new costTriplet(numHostagesOriginal-(grid[4].split(",").length / 3)
                + (grid[5].split(",").length / 3),
                Integer.parseInt(grid[6]),Integer.parseInt(grid[7]));
    }
    Node Search(String grid,String strategy){
        Node result = null;
        initialState = "";
        expandedNodes=0;
        //grid=swapRowsAndColumns(grid);
        String[] gridSplit = grid.split(";");
        m = Integer.parseInt(gridSplit[0].split(",")[0]);
        n = Integer.parseInt(gridSplit[0].split(",")[1]);
        c = Integer.parseInt(gridSplit[1]);
        telX = Integer.parseInt(gridSplit[3].split(",")[0]);
        telY = Integer.parseInt(gridSplit[3].split(",")[1]);
        numHostagesOriginal = gridSplit[7].split(",").length / 3;
        initialState += gridSplit[2] + ",0;";//NEO'S POSITION AND HEALTH
        initialState += gridSplit[4] + ";";//AGENTS POSITIONS
        initialState += gridSplit[5] + ";";//PILLS
        initialState += gridSplit[6] + ";";//PADS
        initialState += gridSplit[7] + ";";//HOSTAGES THAT ARE NOT CARRIED
        initialState += " ;"; //CARRIED NOW HOSTAGES ALONG WITH THEIR DATA
        initialState += "0;";//DEAD HOSTAGES
        initialState += "0";//KILLED AGENTS
        Node root = new Node(initialState, null, "", 0, null);
        root.pathCost=pathCost(root);
        states=new HashSet<>();
        //states.add(getSmallState(initialState.split(";")));
        states.add(initialState);
        if (strategy.equals("BF")) {

            result = BFS(root);
        }
        if (strategy.equals("DF")) {

            result = DFS(root);
        }
        if (strategy.equals("ID")) {

            result = IterativeDeepeningSearch(root);
        }
        if (strategy.equals("UC")) {
            result =UC(root);
        }
        if (strategy.equals("GR1")) {
            result = GREEDY1(root);
        }
        if (strategy.equals("GR2")) {
            result = GREEDY2(root);
        }
        if (strategy.equals("AS1")) {
            result = AS1(root);
        }
        if (strategy.equals("AS2")) {
            result = AS2(root);
        }
       return result;
    }
    public static String solve(String grid, String strategy, boolean visualize) {

        String res = "";

        Matrix matrix = new Matrix();
        Node result=matrix.Search(grid,strategy);
        if(result==null){
            return "No Solution";
        }
        if (visualize) {
            StringBuilder sb = new StringBuilder();
            Node currentNode = result;
            //  System.out.println(currentNode);
            while (currentNode != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append((currentNode.gridState));

                sb2.append('\n');
//                sb2.append(getSmallState(currentNode.gridState.split(";")));
//                sb2.append('\n');

//                System.out.println(currentNode.gridState);
                sb2.append("-----------------------------------------------------");
                sb2.append('\n');
                sb = sb2.append(sb);
//                System.out.println(currentNode.gridState);
                currentNode = currentNode.parent;
            }
            res = sb.toString();
            System.out.println(res.toString());

        }

        StringBuilder solution = new StringBuilder();
        Node currentNode = result;

        int nodes = 0;
        while (currentNode != null) {
            if (currentNode.operation.length() > 0) {
                if (solution.length() == 0) {
                    solution = new StringBuilder(currentNode.operation);
                } else {
                    solution = new StringBuilder(currentNode.operation).append(",").append(solution);
                }
            }
            currentNode = currentNode.parent;
            nodes++;
        }
        String[] splitResult = result.gridState.split(";");
        solution.append(";").append(splitResult[6]).append(";").append(splitResult[7]).append(";").append(expandedNodes);

        return solution.toString();
//        return "up,kill;0;0;0";

    }

    @Override
     boolean isGoal(Node n) {
        String[] splitStates = n.gridState.split(";");
        String[] neo = splitStates[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        //CHECK IF NO HOSTAGES IN THE GRID AND IN THE TEL BOOTH AND NO ONE CARRIED
        return (splitStates[4].compareTo(" ") == 0)
                && (telX == neoNow.i && telY == neoNow.j) && splitStates[5].compareTo(" ") == 0 && (Integer.parseInt(neo[2]) < 100);
    }


    boolean isLeaf(Node n) {
        String[] splitStates = n.gridState.split(";");
        int neoDamage = Integer.parseInt(splitStates[0].split(",")[2]);
        return neoDamage >= 100;
    }

//

     String getSmallState(String[] splitStates) {
        StringBuilder res = new StringBuilder();
        String[] neo = splitStates[0].split(",");
        res.append(splitStates[0]).append(";");
        res.append(splitStates[1]).append(";").append(splitStates[2]).append(";");
        //res.append(splitStates[3]).append(";");
        String[] hostages = splitStates[4].split(",");
        if (hostages.length > 1) {
            for (int i = 0; i < hostages.length; i += 3) {
                if (i != 0) {
                    res.append(",");
                }
                res.append(hostages[i]).append(",").append(hostages[i + 1]);
            }
        } else {
            res.append(" ");
        }
        res.append(";");
        hostages = splitStates[5].split(",");
        if (hostages.length > 1) {
            for (int i = 0; i < hostages.length; i += 3) {
                if (i != 0) {
                    res.append(",");
                }
                res.append(hostages[i]).append(",").append(hostages[i + 1]);
            }
        } else {
            res.append(" ");
        }
        res.append(";");
        res.append(splitStates[6]).append(";").append(splitStates[7]);

        return res.toString();
    }

     String CHANGEDAMAGE(String[] splitStates, int change) {
        //                splitStates[5] + ";" +
        int deaths = 0;
        StringBuilder newState = new StringBuilder();
        //System.out.println("change damage");
        //STATE AFTER SPLITING FOR EASY MANIPULATION
//        String[] splitStates = gridState.split(";");
        //ADD EVERYTHING THAT DIDN'T CHANGE
        newState.append(splitStates[0]).append(";").append(splitStates[1]).append(";").append(splitStates[2]).append(";").append(splitStates[3]).append(";");
        //HOSTAGES AND THEIR DAMAGE
        //INCREASE DAMAGE OF NON CARRIED HOSTAGES
        String[] positionsAndProperties = splitStates[4].split(",");
        //INCREASE ALL DAMAGES BY CHANGE
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                newState.append(positionsAndProperties[i]).append(",").append(positionsAndProperties[i + 1]).append(",");
                if (Integer.parseInt(positionsAndProperties[i + 2]) == 100) {
                    newState.append(positionsAndProperties[i + 2]);
                } else {
                    int newDamage = Math.max(0, Math.min(100, Integer.parseInt(positionsAndProperties[i + 2]) + change));
                    newState.append(newDamage);
                    if (newDamage == 100) {
                        deaths++;
                    }
                }
                if (i + 2 < positionsAndProperties.length - 1)
                    newState.append(",");
                else {
                    newState.append(";");
                }
            }
        } else {
            newState.append(splitStates[4]).append(";");
        }
        //INCREASE DAMAGE OF CARRIED HOSTAGES
        positionsAndProperties = splitStates[5].split(",");
        //INCREASE ALL DAMAGES BY 2
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                newState.append(positionsAndProperties[i]).append(",").append(positionsAndProperties[i + 1]).append(",");
                if (Integer.parseInt(positionsAndProperties[i + 2]) == 100) {
                    newState.append(positionsAndProperties[i + 2]);
                } else {
                    int newDamage = Math.max(0, Math.min(100, Integer.parseInt(positionsAndProperties[i + 2]) + change));
                    newState.append(newDamage);
                    if (newDamage == 100) {
                        deaths++;
                    }

                }
                if (i + 2 < positionsAndProperties.length - 1)
                    newState.append(",");
                else {
                    newState.append(";");
                }
            }
        } else {
            newState.append(splitStates[5]).append(";");
        }

        newState.append(Integer.parseInt(splitStates[6]) + deaths).append(";").append(splitStates[7]);//ADD THE CARRY
//        System.out.println("change damage"+newState.split(";").length);
        return newState.toString();
    }

     String[] UP(String[] splitState) {
        String[] newState = new String[splitState.length];
        String[] neo = splitState[0].split(",");

        // System.out.println("UP");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        int neoDamage = Integer.parseInt(neo[2]);
        //CHECK IF CELL EXISTS
        if (neoNow.i == 0) {
            //EMPTY STRING AS INVALID ACTION
            return new String[]{};
        }
        //CHECK IF CONTAINS AGENT
        String[] positionsAndProperties = splitState[1].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 2) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                if (row == neoNow.i - 1 && col == neoNow.j) {
                    return new String[]{};
                }
            }
        }
        //CHECK IF CONTAINS HOSTAGES THAT BECAME AGENTS
        positionsAndProperties = splitState[4].split(",");

        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                int damage = Integer.parseInt(positionsAndProperties[i + 2]);
                if (row == neoNow.i - 1 && col == neoNow.j && damage >= 98) {
                    return new String[]{};
                }
            }
        }

        //CHANGE CURRENT POSITION ONLY AND KEEP ALL OTHER PROPERTIES
        newState[0] = (neoNow.i - 1) + "," + (neoNow.j) + "," + (neoDamage);
        newState[1] = splitState[1];
        newState[2] = splitState[2];
        newState[3] = splitState[3];
        newState[4] = splitState[4];
        newState[5] = splitState[5];
        newState[6] = splitState[6];
        newState[7] = splitState[7];
//        newState.append(neoNow.i - 1).append(",").append(neoNow.j).append(",").append(neoDamage).append(";");
//        newState.append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(splitState[4]).append(";").append(splitState[5]).append(";").append(splitState[6]).append(";").append(splitState[7]);
//        // System.out.println("Up state"+newState.split(";").length);
        return newState;
    }

     String[] DOWN(String[] splitState) {
        //System.out.println("grid"+gridState);
        String[] newState = new String[splitState.length];
        // System.out.println("Down");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        int neoDamage = Integer.parseInt(neo[2]);
        //CHECK IF CELL EXISTS
        // System.out.println(neoNow.i+" i "+neoNow.j+" j");
        if (neoNow.i == n - 1) {
            //EMPTY STRING AS INVALID ACTION
            return new String[]{};
        }
        //CHECK IF CONTAINS AGENT
        String[] positionsAndProperties = splitState[1].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 2) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                if (row == neoNow.i + 1 && col == neoNow.j) {
                    return new String[]{};
                }
            }
        }
        //CHECK IF CONTAINS HOSTAGES THAT BECAME AGENTS
        positionsAndProperties = splitState[4].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                int damage = Integer.parseInt(positionsAndProperties[i + 2]);
                if (row == neoNow.i + 1 && col == neoNow.j && damage >= 98) {
                    return new String[]{};
                }
            }
        }
        newState[0] = (neoNow.i + 1) + "," + (neoNow.j) + "," + (neoDamage);
        newState[1] = splitState[1];
        newState[2] = splitState[2];
        newState[3] = splitState[3];
        newState[4] = splitState[4];
        newState[5] = splitState[5];
        newState[6] = splitState[6];
        newState[7] = splitState[7];
        //CHANGE CURRENT POSITION ONLY AND KEEP ALL OTHER PROPERTIES
//        newState.append(neoNow.i + 1).append(",").append(neoNow.j).append(",").append(neoDamage).append(";");
//        newState.append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(splitState[4]).append(";").append(splitState[5]).append(";").append(splitState[6]).append(";").append(splitState[7]);
//        //System.out.println("down state"+newState.split(";").length);
        return newState;
    }

    String[] LEFT(String[] splitState) {
        String[] newState = new String[splitState.length];
        //  System.out.println("l");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        int neoDamage = Integer.parseInt(neo[2]);
        //CHECK IF CELL EXISTS
        if (neoNow.j == 0) {
            //EMPTY STRING AS INVALID ACTION
            return new String[]{};
        }
        //CHECK IF CONTAINS AGENT
        String[] positionsAndProperties = splitState[1].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 2) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                if (row == neoNow.i && col == neoNow.j - 1) {
                    return new String[]{};
                }
            }
        }
        //CHECK IF CONTAINS HOSTAGES THAT BECAME AGENTS
        positionsAndProperties = splitState[4].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                int damage = Integer.parseInt(positionsAndProperties[i + 2]);
                if (row == neoNow.i && col == neoNow.j - 1 && damage >= 98) {
                    return new String[]{};
                }
            }
        }

        //CHANGE CURRENT POSITION ONLY AND KEEP ALL OTHER PROPERTIES
        newState[0] = (neoNow.i) + "," + (neoNow.j - 1) + "," + (neoDamage);
        newState[1] = splitState[1];
        newState[2] = splitState[2];
        newState[3] = splitState[3];
        newState[4] = splitState[4];
        newState[5] = splitState[5];
        newState[6] = splitState[6];
        newState[7] = splitState[7];
//        newState.append(neoNow.i).append(",").append(neoNow.j - 1).append(",").append(neoDamage).append(";");
//        newState.append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(splitState[4]).append(";").append(splitState[5]).append(";").append(splitState[6]).append(";").append(splitState[7]);
//        // System.out.println("left state"+newState.split(";").length);
        return newState;
    }

     String[] RIGHT(String[] splitState) {
        String[] newState = new String[splitState.length];
        // System.out.println("r");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        int neoDamage = Integer.parseInt(neo[2]);
        //CHECK IF CELL EXISTS
        if (neoNow.j == m - 1) {
            //EMPTY STRING AS INVALID ACTION
            return new String[]{};
        }
        //CHECK IF CONTAINS AGENT
        String[] positionsAndProperties = splitState[1].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 2) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                if (row == neoNow.i && col == neoNow.j + 1) {
                    return new String[]{};
                }
            }
        }
        //CHECK IF CONTAINS HOSTAGES THAT BECAME AGENTS
        positionsAndProperties = splitState[4].split(",");
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                int damage = Integer.parseInt(positionsAndProperties[i + 2]);
                if (row == neoNow.i && col == neoNow.j + 1 && damage >= 98) {
                    return new String[]{};
                }
            }
        }

        //CHANGE CURRENT POSITION ONLY AND KEEP ALL OTHER PROPERTIES
        newState[0] = (neoNow.i) + "," + (neoNow.j + 1) + "," + (neoDamage);
        newState[1] = splitState[1];
        newState[2] = splitState[2];
        newState[3] = splitState[3];
        newState[4] = splitState[4];
        newState[5] = splitState[5];
        newState[6] = splitState[6];
        newState[7] = splitState[7];
//        newState.append(neoNow.i).append(",").append(neoNow.j + 1).append(",").append(neoDamage).append(";");
//        newState.append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(splitState[4]).append(";").append(splitState[5]).append(";").append(splitState[6]).append(";").append(splitState[7]);
//        // System.out.println("right state"+newState.split(";").length);
        return newState;
    }

    String[] KILLWITHOFFSET(String[] splitState) {
        String[] newState = new String[splitState.length];
        // System.out.println("ko");
        int kills = 0;
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        //CHECK IF WITH HOSTAGE OF DAMAGE BIGGER THAN OR EQUAL 98
        String[] hostagePositionsAndProperties = splitState[4].split(",");
        if (hostagePositionsAndProperties.length > 1) {
            for (int i = 0; i < hostagePositionsAndProperties.length; i += 3) {
                int row = Integer.parseInt(hostagePositionsAndProperties[i]);
                int col = Integer.parseInt(hostagePositionsAndProperties[i + 1]);
                int damage = Integer.parseInt(hostagePositionsAndProperties[i + 2]);
                if (row == neoNow.i && col == neoNow.j && damage >= 98) {
                    return new String[]{};
                }

            }
        }

        //CHECK IF CONTAINS AGENT
        boolean containsAgent = false;
        String[] positionsAndProperties = splitState[1].split(",");
        StringBuilder newAgents = new StringBuilder();
        if (positionsAndProperties.length > 1) {

            for (int i = 0; i < positionsAndProperties.length; i += 2) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                if ((row == neoNow.i && (col == neoNow.j + 1 || col == neoNow.j - 1)) ||
                        (col == neoNow.j && (row == neoNow.i + 1 || row == neoNow.i - 1))) {
                    containsAgent = true;
                    kills++;

                } else {
                    if (newAgents.length() != 0) {
                        newAgents.append(",");
                    }
                    newAgents.append(positionsAndProperties[i]).append(",").append(positionsAndProperties[i + 1]);
                }

            }


        }
        //CHECK IF CONTAINS HOSTAGES THAT BECAME AGENTS
        StringBuilder newHostages = new StringBuilder();
        positionsAndProperties = splitState[4].split(",");
        if (positionsAndProperties.length > 1) {

            for (int i = 0; i < positionsAndProperties.length; i += 3) {
                int row = Integer.parseInt(positionsAndProperties[i]);
                int col = Integer.parseInt(positionsAndProperties[i + 1]);
                int damage = Integer.parseInt(positionsAndProperties[i + 2]);
                if (damage >= 100 && ((row == neoNow.i && (col == neoNow.j + 1 || col == neoNow.j - 1)) ||
                        (col == neoNow.j && (row == neoNow.i + 1 || row == neoNow.i - 1)))) {
                    containsAgent = true;
                    kills++;

                } else {
                    if (newHostages.length() != 0) {
                        newHostages.append(",");
                    }
                    newHostages.append(positionsAndProperties[i]).append(",").append(positionsAndProperties[i + 1]).append(",").append(positionsAndProperties[i + 2]);
                }
            }
//            String[] neoState = splitState[0].split(",");
            //CHANGE ROW AND INCREASE DAMAGE
//            if (containsAgent) {
//                if (newHostages.length() == 0) {
//                    newHostages.append(" ");//MARK FOR NO AGENTS
//                }
////                //CHANGE POSITION AND DAMAGE
////                newState += (Integer.parseInt(neoState[0]) + offsetI)
////                        + "," + (Integer.parseInt(neoState[1]) + offsetJ)
////                        + "," + (Math.min(100, Integer.parseInt(neoState[2]) + 20)) + ";";
//                // DONOT CHANGE POSITION AND DONOT DAMAGE
//                newState += (Integer.parseInt(neoState[0]))
//                        + "," + (Integer.parseInt(neoState[1]))
//                        + "," + (Math.min(100, Integer.parseInt(neoState[2]))) + ";";
//                newState += splitState[1] + ";";
//                newState += splitState[2] + ";" + splitState[3] + ";"
//                        + newHostages + ";" + splitState[5] + ";" + (Integer.parseInt(splitState[6])) + ";" + (Integer.parseInt(splitState[7]) + kills);
//                return newState;
//            }
        }

        //CHANGE ROW AND INCREASE DAMAGE
        if (containsAgent) {
            String[] neoState = splitState[0].split(",");
            if (newAgents.length() == 0) {
                newAgents.append(" ");//MARK FOR NO AGENTS
            }
            if (newHostages.length() == 0) {
                newHostages.append(" ");//MARK FOR NO AGENTS
            }
            newState[0] = (Integer.parseInt(neoState[0])) + "," + (Integer.parseInt(neoState[1])) + "," + (Math.min(100, Integer.parseInt(neoState[2]) + 20));
            newState[1] = newAgents.toString();
            newState[2] = splitState[2];
            newState[3] = splitState[3];
            newState[4] = newHostages.toString();
            newState[5] = splitState[5];
            newState[6] = splitState[6];
            newState[7] = (Integer.parseInt(splitState[7]) + kills) + "";

//            newState.append(Integer.parseInt(neoState[0])).append(",").append(Integer.parseInt(neoState[1])).append(",").append(Math.min(100, Integer.parseInt(neoState[2]) + 20)).append(";");
//            newState.append(newAgents).append(";");
//            newState.append(splitState[2]).append(";").append(splitState[3]).append(";").append(newHostages).append(";").append(splitState[5]).append(";").append(splitState[6]).append(";").append(Integer.parseInt(splitState[7]) + kills);
//
            return newState;
        }
        //  System.out.println("kill offset"+newState.split(";").length);
        return new String[]{};
    }



    String[] CARRY(String[] splitState) {
        String[] newState = new String[splitState.length];
        // System.out.println("c");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        String[] hostages = splitState[4].split(",");
        StringBuilder newHostagesAfterCarry = new StringBuilder();
        boolean found = false;
        if (splitState[5].split(",").length / 3 == c) {

            return new String[]{};
        }
        StringBuilder newCarry = new StringBuilder(splitState[5]);
        if (hostages.length > 1) {

            for (int i = 0; i < hostages.length; i += 3) {
                int row = Integer.parseInt(hostages[i]);
                int col = Integer.parseInt(hostages[i + 1]);
                int damage = Integer.parseInt(hostages[i + 2]);
                if (row == neoNow.i && col == neoNow.j) {
                    found = true;
                    //   System.out.println("hello " + splitState[5].split(",").length);
                    //why less than or equal and not less than?
                    if (newCarry.length() <= 1) {
                        newCarry=new StringBuilder();
                        newCarry.append(row).append(",").append(col).append(",").append(damage);

                    } else {
                        // System.out.println("hello carried "+splitState[5]);
                        newCarry.append(",").append(row).append(",").append(col).append(",").append(damage);
                        //System.out.println("hello carried2 "+splitState[5]);
                    }
                } else {
                    if (newHostagesAfterCarry.length() != 0) {
                        newHostagesAfterCarry.append(",");
                    }
                    newHostagesAfterCarry.append(row).append(",").append(col).append(",").append(damage);
                }
            }
        }
        if (found) {
            if (newHostagesAfterCarry.length() == 0) {
                // System.out.println("0 hostages");
                newHostagesAfterCarry.append(" ");
            }
            newState[0] = splitState[0];
            newState[1] = splitState[1];
            newState[2] = splitState[2];
            newState[3] = splitState[3];
            newState[4] = newHostagesAfterCarry.toString();
            newState[5] = newCarry.toString();
            newState[6] = splitState[6];
            newState[7] = splitState[7];

            //newState.append(splitState[0]).append(";").append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(newHostagesAfterCarry).append(";").append(newCarry).append(";").append(splitState[6]).append(";").append(splitState[7]);
            return newState;
        }

        //System.out.println("Carry state"+newState.split(";").length);
        //   System.out.println("Hello new "+newState.length());
        return new String[]{};
    }

     String[] TAKEPILL(String[] splitState) {
        String[] newState = new String[splitState.length];
        // System.out.println("tp");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        String[] pillsPositions = splitState[2].split(",");
        boolean foundPill = false;
        StringBuilder newPills = new StringBuilder();
        //IF NO PILLS FOUND
        if (pillsPositions.length <= 1) {
            return new String[]{};
        }
        for (int i = 0; i < pillsPositions.length; i += 2) {
            if (neoNow.i == Integer.parseInt(pillsPositions[i])
                    && neoNow.j == Integer.parseInt(pillsPositions[i + 1])) {
                foundPill = true;
            } else {
                if (newPills.length() != 0) {
                    newPills.append(",");
                }
                newPills.append(pillsPositions[i]).append(",").append(pillsPositions[i + 1]);
            }
        }
        if (!foundPill) {
            return new String[]{};
        }
        if (newPills.length() == 0) {
            newPills.append(" ");
        }
        String[] stateAfterOnlyChangingDamage = CHANGEDAMAGE(splitState, -20).split(";");
        String hostagesWithNewDamages = stateAfterOnlyChangingDamage[4];
        String carriedHostagesWithNewDamages = stateAfterOnlyChangingDamage[5];
        int newDamageOfNeo = Math.max(0, Integer.parseInt(neo[2]) - 20);

        newState[0] = (neo[0]) + "," + (neo[1]) + "," + (newDamageOfNeo);
        newState[1] = splitState[1];
        newState[2] = newPills.toString();
        newState[3] = splitState[3];
        newState[4] = hostagesWithNewDamages;
        newState[5] = carriedHostagesWithNewDamages;
        newState[6] = splitState[6];
        newState[7] = splitState[7];

//        newState.append(neoState[0]).append(",").append(neoState[1]).append(",").append(newDamageOfNeo).append(";");
//        newState.append(splitState[1]).append(";").append(newPills).append(";").append(splitState[3]).append(";").append(hostagesWithNewDamages).append(";").append(carriedHostagesWithNewDamages).append(";").append(splitState[6]).append(";").append(splitState[7]);
//        // System.out.println("takepill"+newState.split(";").length);
        return newState;
    }

     String[] DROP(String[] splitState) {
        String[] newState = new String[splitState.length];
        // System.out.println("drop");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        if (neoNow.i != telX || neoNow.j != telY) { //NOT IN THE TEL BOOTH
            return new String[]{};
        }
        if (splitState[5].equals(" ")) {//NOT CARRYING ANYONE
            return new String[]{};
        }
        newState[0] = splitState[0];
        newState[1] = splitState[1];
        newState[2] = splitState[2];
        newState[3] = splitState[3];
        newState[4] = splitState[4];
        newState[5] = " ";
        newState[6] = splitState[6];
        newState[7] = splitState[7];
//        newState.append(splitState[0]).append(";").append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(splitState[4]).append(";");
//        //EMPTY CARRIED LIST
//        newState.append(" ;");
//
//        newState.append(Integer.parseInt(splitState[6])).append(";").append(splitState[7]);


        //System.out.println("drop"+newState.split(";").length);
        return newState;
    }

     String [] FLY(String[] splitState) {
        String[]newState = new String[splitState.length];
        //  System.out.println("fly");
        String[] neo = splitState[0].split(",");
        Position neoNow = new Position(Integer.parseInt(neo[0]),
                Integer.parseInt(neo[1]));
        int neoDamage = Integer.parseInt(neo[2]);
        String[] positionsAndProperties = splitState[3].split(",");//PADS
        if (positionsAndProperties.length > 1) {
            for (int i = 0; i < positionsAndProperties.length; i += 4) {
                int startRow = Integer.parseInt(positionsAndProperties[i]);
                int startCol = Integer.parseInt(positionsAndProperties[i + 1]);
                int endRow = Integer.parseInt(positionsAndProperties[i + 2]);
                int endCol = Integer.parseInt(positionsAndProperties[i + 3]);
                if (neoNow.i == startRow && neoNow.j == startCol) {
                    newState[0] = (endRow)+","+(endCol)+","+(neoDamage);
                    newState[1] = splitState[1];
                    newState[2] = splitState[2];
                    newState[3] = splitState[3];
                    newState[4] = splitState[4];
                    newState[5] = splitState[5];
                    newState[6] = splitState[6];
                    newState[7] = splitState[7];

//                    newState.append(endRow).append(",").append(endCol).append(",").append(neoDamage).append(";");
//                    newState.append(splitState[1]).append(";").append(splitState[2]).append(";").append(splitState[3]).append(";").append(splitState[4]).append(";").append(splitState[5]).append(";").append(splitState[6]).append(";").append(splitState[7]);
//
                    return newState;
                }
            }
        }
//System.out.println("fly"+newState.split(";").length);
        return new String[]{};
    }




    ArrayList<Node> Expand(Node head) {
        ArrayList<Node> nodes = new ArrayList<>();
        String[] splitState = head.gridState.split(";");

        String[] StateCarry = CARRY(splitState);
        if (StateCarry.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateCarry = CHANGEDAMAGE(StateCarry, 2);
            //String smallState = getSmallState(newStateCarry.split(";"));
            //IF NOT IN REPEATED STATES
            if (!states.contains(newStateCarry)) {


                states.add(newStateCarry);
                //need to adjust path cost
                Node carry = new Node(newStateCarry, head, "carry", head.depth + 1, null);
                carry.pathCost=pathCost(carry);
                nodes.add(carry);
            }

        }
        String []StateTakePill = TAKEPILL(splitState);
        if (StateTakePill.length > 0) {
           // String smallState = getSmallState(StateTakePill);
            StringBuilder newStateTakePill=new StringBuilder();
            newStateTakePill.append(StateTakePill[0]);
            for (int i = 1; i <StateTakePill.length ; i++) {
                newStateTakePill.append(";").append(StateTakePill[i]);
            }
            //IF NOT IN REPEATED STATES
            if (!states.contains(newStateTakePill.toString())) {
//            //INCREASE GLOBAL HOSTAGE DAMAGE -> WRONG AFTER THE NEW UPDATE
//            StateTakePill = CHANGEDAMAGE(StateTakePill, 2);

                states.add(newStateTakePill.toString());
                //need to adjust path cost
                Node takePill = new Node(newStateTakePill.toString(), head, "takePill", head.depth + 1, null);
                takePill.pathCost=pathCost(takePill);
                nodes.add(takePill);
            }

        }

        String[] StateDrop = DROP(splitState);
        if (StateDrop.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateDrop = CHANGEDAMAGE(StateDrop, 2);
            //String smallState = getSmallState(newStateDrop.split(";"));
            //IF NOT IN REPEATED STATES
            if (!states.contains(newStateDrop)) {

                states.add(newStateDrop);
                //need to adjust path cost
                Node drop = new Node(newStateDrop, head, "drop", head.depth + 1, null);
                drop.pathCost=pathCost(drop);
                nodes.add(drop);
            }

        }

        String[] StateUp = UP(splitState);
//        System.out.println(StateUp+"up");
        if (StateUp.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateUp = CHANGEDAMAGE(StateUp, 2);
            //IF NOT IN REPEATED STATES
           // String smallState = getSmallState(newStateUp.split(";"));
            if (!states.contains(newStateUp)) {



                states.add(newStateUp);
                //need to adjust path cost
                Node up = new Node(newStateUp, head, "up", head.depth + 1, null);
                up.pathCost=pathCost(up);
                nodes.add(up);
            }
        }
        String[] StateDown = DOWN(splitState);
        if (StateDown.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateDown = CHANGEDAMAGE(StateDown, 2);
            //IF NOT IN REPEATED STATES
           // String smallState = getSmallState(newStateDown.split(";"));

            if (!states.contains(newStateDown)) {


                states.add(newStateDown);
                //need to adjust path cost
                Node down = new Node(newStateDown, head, "down", head.depth + 1, null);
                down.pathCost=pathCost(down);
                nodes.add(down);
            }
        }
        String [] StateLeft = LEFT(splitState);
        if (StateLeft.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateLeft = CHANGEDAMAGE(StateLeft, 2);
            //IF NOT IN REPEATED STATES
            //String smallState = getSmallState(newStateLeft.split(";"));
            if (!states.contains(newStateLeft)) {


                states.add(newStateLeft);
                //need to adjust path cost
                Node left = new Node(newStateLeft, head, "left", head.depth + 1, null);
                left.pathCost=pathCost(left);
                nodes.add(left);
            }
        }
        String[] StateRight = RIGHT(splitState);
        if (StateRight.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateRight = CHANGEDAMAGE(StateRight, 2);
            //IF NOT IN REPEATED STATES
          //  String smallState = getSmallState(newStateRight.split(";"));
//            if (head.gridState.equals("3,0,60; ; ;2,4,3,4,3,4,2,4;2,2,100,4,2,100;2,0,13;5;8")) {
//                System.err.println("in expand " + states.contains(smallState) + " --- " + smallState);
//            }
            if (!states.contains(newStateRight)) {


                states.add(newStateRight);
                //need to adjust path cost
                Node right = new Node(newStateRight, head, "right", head.depth + 1, null);
                right.pathCost=pathCost(right);
                nodes.add(right);
            }
        }
        String[] StateFly = FLY(splitState);
        if (StateFly.length > 0) {
            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateFly = CHANGEDAMAGE(StateFly, 2);
           // String smallState = getSmallState(newStateFly.split(";"));
            //IF NOT IN REPEATED STATES
            if (!states.contains(newStateFly)) {
                states.add(newStateFly);


                //need to adjust path cost
                Node fly = new Node(newStateFly, head, "fly", head.depth + 1, null);
                fly.pathCost=pathCost(fly);
                nodes.add(fly);
            }
        }
        boolean foundAKill = false;
        String[] StateKill;



        StateKill = KILLWITHOFFSET(splitState);

        if (StateKill.length > 0) {
            foundAKill = true;


        }

        if (foundAKill) {

            //INCREASE GLOBAL HOSTAGE DAMAGE
            String newStateKill = CHANGEDAMAGE(StateKill, 2);
            //String smallState = getSmallState(newStateKill.split(";"));
            //IF NOT IN REPEATED STATES
            if (!states.contains(newStateKill)) {


                states.add(newStateKill);
                Node killInAllDirections = new Node(newStateKill, head, "kill", head.depth + 1, null);
                killInAllDirections.pathCost=pathCost(killInAllDirections);
                nodes.add(killInAllDirections);
            }
        }




        return nodes;
    }

    public static void main(String[] args) {
        PrintWriter out = new PrintWriter(System.out);
        String grid = "3,3;1;1,1;0,0; ; ; ;1,0,92,2,1,76";
        //"3,3;3;1,1;0,1;1,2; ; ;0,0,30,2,1,10";
        String grid0 = "5,5;2;3,4;1,2;0,3,1,4;2,3;4,4,0,2,0,2,4,4;2,2,91,2,4,62";
        String grid1 = "5,5;1;1,4;1,0;0,4;0,0,2,2;3,4,4,2,4,2,3,4;0,2,32,0,1,38";
        String grid2 = "5,5;2;3,2;0,1;4,1;0,3;1,2,4,2,4,2,1,2,0,4,3,0,3,0,0,4;1,1,77,3,4,34";
        String grid3 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,1";
        String grid4 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,98,1,0,98";
        String grid5 = "5,5;2;0,4;3,4;3,1,1,1;2,3;3,0,0,1,0,1,3,0;4,2,54,4,0,85,1,0,43";
        String grid6 = "5,5;2;3,0;4,3;2,1,2,2,3,1,0,0,1,1,4,2,3,3,1,3,0,1;2,4,3,2,3,4,0,4;4,4,4,0,4,0,4,4;1,4,57,2,0,46";
        String grid7 = "5,5;3;1,3;4,0;0,1,3,2,4,3,2,4,0,4;3,4,3,0,4,2;1,4,1,2,1,2,1,4,0,3,1,0,1,0,0,3;4,4,45,3,3,12,0,2,88";
        String grid8 = "5,5;2;4,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,3,3,3,3,4,4;4,0,17,1,2,54,0,0,46,4,1,22";
        String grid9 = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
        String grid10 = "5,5;4;1,1;4,1;2,4,0,4,3,2,3,0,4,2,0,1,1,3,2,1;4,0,4,4,1,0;2,0,0,2,0,2,2,0;0,0,62,4,3,45,3,3,39,2,3,40";

        HashSet<String>myStates=new HashSet<>();
//        String vision = solve(grid3, "BF", true);
//        System.out.println(expandedNodes);
        String initialState = "";
        expandedNodes=0;
        //grid=swapRowsAndColumns(grid);
        String[] gridSplit = grid0.split(";");
        m = Integer.parseInt(gridSplit[0].split(",")[0]);
        n = Integer.parseInt(gridSplit[0].split(",")[1]);
        c = Integer.parseInt(gridSplit[1]);
        telX = Integer.parseInt(gridSplit[3].split(",")[0]);
        telY = Integer.parseInt(gridSplit[3].split(",")[1]);
        numHostagesOriginal = gridSplit[7].split(",").length / 3;
        initialState += gridSplit[2] + ",0;";//NEO'S POSITION AND HEALTH
        initialState += gridSplit[4] + ";";//AGENTS POSITIONS
        initialState += gridSplit[5] + ";";//PILLS
        initialState += gridSplit[6] + ";";//PADS
        initialState += gridSplit[7] + ";";//HOSTAGES THAT ARE NOT CARRIED
        initialState += " ;"; //CARRIED NOW HOSTAGES ALONG WITH THEIR DATA
        initialState += "0;";//DEAD HOSTAGES
        initialState += "0";//KILLED AGENTS



     //   System.err.println(solve(gri0, "GR1", false));
        System.err.println(solve(grid3, "UC", false));
        System.err.println(solve(grid3, "AS1", false));
        System.err.println(solve(grid3, "AS2", false));
//        //start
//        String fP = initialState;
//        String small1 = getSmallState(fP.split(";"));
//        System.err.println(small1+" " +myStates.contains(small1));
//        myStates.add(small1);
//        System.err.println(fP);
//        //kill
//        String[] action = KILLWITHOFFSET(fP.split(";"));
//        String newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+isLeaf2(newAction));
//        myStates.add(small1);
//        //left
//        action = LEFT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //kill
//        action = KILLWITHOFFSET(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //left
//        action = LEFT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //left
//        action = LEFT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //down
//        action = DOWN(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //kill
//        action = KILLWITHOFFSET(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //down
//        action = DOWN(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //down
//        action = DOWN(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //kill
//        action = KILLWITHOFFSET(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //left
//        action = LEFT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //down
//        action = DOWN(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //takePill
//        action = TAKEPILL(newAction.split(";"));
//        //newAction=CHANGEDAMAGE(action,2);
//        small1=getSmallState(action);
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //up
//        action = UP(action);
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //up
//        action = UP(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //carry
//        action = CARRY(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //down
//        action = DOWN(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //right
//        action = RIGHT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //right
//        action = RIGHT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //kill
//        action = KILLWITHOFFSET(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //right
//        action = RIGHT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //down
//        action = DOWN(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //right
//        action = RIGHT(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        //Drop
//        action = DROP(newAction.split(";"));
//        newAction=CHANGEDAMAGE(action,2);
//        System.err.println("--------- "+newAction);
//        small1=getSmallState(newAction.split(";"));
//        System.err.println(small1 +" "+myStates.contains(small1)+" "+action[0]);
//        myStates.add(small1);
//        System.err.println(isGoal2(newAction));

//        String StateFly1=FLY(fP.split(";"));
//        StateFly1= CHANGEDAMAGE(StateFly1,2);
//        String StateFly2=FLY(StateFly1.split(";"));
//        small1=getSmallState(StateFly2.split(";"));
//        System.err.println(small1);

       // out.println(vision);
        out.flush();
//        8,0,0;0,1,0,3,1,0,1,1,1,2,0,7,1,8,3,8,6,1,6,5;0,6,2,8;8,1,4,5,4,5,8,1;0,0,95,0,2,98,0,8,94,2,5,13,2,6,39; ;0;0
//                -----------------------------------------------------
//                8,1,0;0,1,0,3,1,0,1,1,1,2,0,7,1,8,3,8,6,1,6,5;0,6,2,8;8,1,4,5,4,5,8,1;0,0,97,0,2,100,0,8,96,2,5,15,2,6,41; ;1;0
//                -----------------------------------------------------
//                4,5,0;0,1,0,3,1,0,1,1,1,2,0,7,1,8,3,8,6,1,6,5;0,6,2,8;8,1,4,5,4,5,8,1;0,0,99,0,2,100,0,8,98,2,5,17,2,6,43; ;1;0
//                -----------------------------------------------------
//                8,1,0;0,1,0,3,1,0,1,1,1,2,0,7,1,8,3,8,6,1,6,5;0,6,2,8;8,1,4,5,4,5,8,1;0,0,100,0,2,100,0,8,100,2,5,19,2,6,45; ;3;0
//                -----------------------------------------------------
//                4,5,0;0,1,0,3,1,0,1,1,1,2,0,7,1,8,3,8,6,1,6,5;0,6,2,8;8,1,4,5,4,5,8,1;0,0,100,0,2,100,0,8,100,2,5,21,2,6,47; ;3;0
    }
}
