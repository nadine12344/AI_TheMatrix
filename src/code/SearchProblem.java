package code;

import java.util.*;

public abstract class SearchProblem {
    String[] operators;
    String initialState;
    HashSet<String> states;
    static long expandedNodes = 0;
    static int numHostagesOriginal = 0;
    static int n = 0;
    static int m = 0;
    static int c = 0;
    static int telX = -1;
    static int telY = -1;

    //LESA MSH 3ARFEEEEEEEN
    abstract boolean isGoal(Node n);

    abstract boolean isLeaf(Node n);

    //BARDO MSH 3ARFEEEEEEEEEN
    abstract ArrayList<Node> Expand(Node n);

    abstract Node Search(String initialState, String strategy);

    abstract costTriplet pathCost(Node n);


    Node BFS(Node root) {
        Node res = null;
        expandedNodes = 0;
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.pollFirst();
            expandedNodes++;

//            System.out.println(curr.operation + " op-------------");
//            System.out.println(curr.gridState + " op-------------");
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
//            if (curr.gridState.contains("3,1,80; ; ;2,4,3,4,3,4,2,4;2,2,100,4,2,100;2,0,"))
//            {
//                System.err.println("I am "+ curr.gridState);
//               // System.err.println("problem "+children.size());
//
//            }
            for (Node child : children) {
                queue.addLast(child);
//                if (curr.gridState.equals("3,0,60; ; ;2,4,3,4,3,4,2,4;2,2,100,4,2,100;2,0,13;5;8"))
//                {
//
//                    System.err.println("hellooo  " + child.operation);
////                    if(child.operation.equals("takePill")){
////                        System.err.println("child "+ isLeaf(child)+" ---- "+child.gridState);
////                    }
//
//                }
            }


        }
        return res;
    }

    Node DFS(Node root) {
        Node res = null;
        expandedNodes = 0;
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node curr = queue.pollFirst();
            expandedNodes++;
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);

            for (Node child : children) {
                queue.addFirst(child);
            }


        }
        return res;
    }

    Matrix.NodeWithMaxDepth DFSTillDepth(Node root, int maxDepth) {
        Node res = null;

        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        int currDepth = 0;
        while (!queue.isEmpty()) {
            Node curr = queue.pollFirst();
            expandedNodes++;
            currDepth = Math.max(currDepth, curr.depth);


            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
//                System.err.println(curr.depth +" "+ curr.parent.depth);

                return new Matrix.NodeWithMaxDepth(curr, curr.depth);
            }
            if (isLeaf(curr) || curr.depth == maxDepth) {

                continue;
            }


            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
            for (Node child : children) {
                queue.addFirst(child);
            }


        }

        return new Matrix.NodeWithMaxDepth(res, currDepth);
    }

    Node IterativeDeepeningSearch(Node root) {
        Node res = null;
        Matrix.NodeWithMaxDepth last = new Matrix.NodeWithMaxDepth(null, -1);
        int currDepth = 0;
        expandedNodes = 0;
        while (true) {
            // System.err.println("hello "+ currDepth);
            states = new HashSet<>();
            Matrix.NodeWithMaxDepth now = DFSTillDepth(root, currDepth++);
            if (now.node != null && isGoal(now.node)) {
                return now.node;
            }
            //If didn't use the previous currDepth because i'm using curDepth++ not ++curDepth
            if (now.depth < currDepth - 1) {
                break;
            }
//            if (now.depth == last.depth) {
//                System.err.println(now.depth);
//                last=now;
//                break;
//            }
//            last = now;
            // System.err.println(last.depth);
        }
        return null;
    }

    Node UC(Node root) {
        Node res = null;
        expandedNodes = 0;
        //COMPARATOR COMPARES NUMBER OF HOSTAGES STILL IN GRID THEN
        //NUMBER OF DEATHS OF HOSTAGES
        // THEN NUMBER OF KILLS AND ORDER ASCENDINGLY
        PriorityQueue<Node> pq = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {

//                //NUMBER OF HOSTAGES NON-CARRIED + CARRIED = STILL IN GRID

                costTriplet c1 = o1.pathCost;
                costTriplet c2 = o2.pathCost;
                if (c1.deaths != c2.deaths)
                    return c1.deaths - c2.deaths;
                //INCREASE THE NUMBER OF HOSTAGES THAT ARE DROPPED OR KILLED => 5ALAS HOSTAGES AKTAR

                if (c1.kills != c2.kills)
                    return c1.kills - c2.kills;

                return c2.hostagesEnded - c1.hostagesEnded;
            }
        });
        pq.add(root);
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            expandedNodes++;
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
            pq.addAll(children);


        }
        return res;
    }

    Node GREEDY1(Node root) {
        Node res = null;
        expandedNodes = 0;
        //COMPARATOR COMPARES NUMBER OF HOSTAGES ALIVE STILL IN GRID ONLY AND ORDER ASCENDINGLY

        PriorityQueue<Node> pq = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                costTriplet c1 = o1.pathCost;
                costTriplet c2 = o2.pathCost;

                String[] grid1 = o1.gridState.split(";");
                String[] grid2 = o2.gridState.split(";");
                int minDistanceToDrop1 = 0;
                int minDistanceToDrop2 = 0;
                boolean atPill1 = false;
                boolean atPill2 = false;
                String[] neo = grid1[0].split(",");
                String[] neo2 = grid2[0].split(",");
                String[] pills1 = grid1[2].split(",");
                String[] pills2 = grid2[2].split(",");
                if (pills1.length > 1) {
                    for (int i = 0; i < pills1.length; i += 2) {
                        if (Integer.parseInt(neo[0]) == Integer.parseInt(pills1[i]) &&
                                Integer.parseInt(neo[1]) == Integer.parseInt(pills1[i + 1])) {
                            atPill1 = true;
                            break;
                        }
                    }
                }
                if (telX != Integer.parseInt(neo[0]) || telY != Integer.parseInt(neo[1])) {
//                    if ((telX == Integer.parseInt(neo[0]) && (telY == Integer.parseInt(neo[1]) + 1 || telY == Integer.parseInt(neo[1]) - 1))
//                    ||(telY == Integer.parseInt(neo[1]) && (telX == Integer.parseInt(neo[0]) + 1 || telX == Integer.parseInt(neo[0]) - 1))) {
//                        minDistanceToDrop1 = 1;
//                    } else minDistanceToDrop1 = 2;
                    minDistanceToDrop1 = 1;
                }

                if (telX != Integer.parseInt(neo2[0]) || telY != Integer.parseInt(neo2[1])) {
//                    if ((telX == Integer.parseInt(neo2[0]) && (telY == Integer.parseInt(neo2[1]) + 1 || telY == Integer.parseInt(neo2[1]) - 1))
//                        ||(telY == Integer.parseInt(neo2[1]) && (telX == Integer.parseInt(neo2[0]) + 1 || telX == Integer.parseInt(neo2[0]) - 1))) {
//                        minDistanceToDrop2 = 1;
//                    } else minDistanceToDrop2 = 2;
                    minDistanceToDrop2 = 1;
                }
                if (pills2.length > 1) {
                    for (int i = 0; i < pills2.length; i += 2) {
                        if (Integer.parseInt(neo2[0]) == Integer.parseInt(pills2[i]) &&
                                Integer.parseInt(neo2[1]) == Integer.parseInt(pills2[i + 1])) {
                            atPill2 = true;
                            break;
                        }
                    }
                }
                String[] carried1 = grid1[5].split(",");
                String[] carried2 = grid2[5].split(",");
                int mustDead1 = 0;
                int mustDead2 = 0;
                if (carried1.length > 1) {
                    for (int i = 0; i < carried1.length; i += 3) {
                        int damage = Integer.parseInt(carried1[i + 2]);
//                        if (damage < 100) {
//                            if (damage + (2 * minDistanceToDrop1) >= 100) {
//                                mustDead1++;
//                            }
//                        }
                        if (damage >= 98 && damage < 100 && !atPill1 && minDistanceToDrop1 != 0) {
                            mustDead1++;
                        }
                    }
                }
                if (carried2.length > 1) {
                    for (int i = 0; i < carried2.length; i += 3) {
                        int damage = Integer.parseInt(carried2[i + 2]);
//                        if (damage < 100) {
//                            if (damage + (2 * minDistanceToDrop2) >= 100) {
//                                mustDead2++;
//                            }
//                        }
                        if (damage >= 98 && damage < 100 && !atPill2 && minDistanceToDrop2 != 0) {
                            mustDead2++;
                        }
                    }
                }
                return mustDead1 - mustDead2;
            }
        });
        pq.add(root);
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            expandedNodes++;
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
            pq.addAll(children);


        }
        return res;
    }

    Node GREEDY2(Node root) {
        Node res = null;
        expandedNodes = 0;
        //COMPARATOR COMPARES NUMBER OF HOSTAGES STILL IN GRID THEN
        //NUMBER OF DEATHS OF HOSTAGES
        // THEN NUMBER OF KILLS AND ORDER ASCENDINGLY
        PriorityQueue<Node> pq = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                String[] grid1 = o1.gridState.split(";");
                String[] grid2 = o2.gridState.split(";");

                //NUMBER OF HOSTAGES THAT MUST BE KILLED
                int mustKillAgents1 = 0;
                int mustKillAgents2 = 0;
                String[] hostages1 = grid1[4].split(",");
                if (hostages1.length > 1) {
                    for (int i = 0; i < hostages1.length; i += 3) {
                        if (Integer.parseInt(hostages1[i + 2]) >= 100) {
                            mustKillAgents1++;
                        }

                    }
                }
                String[] hostages2 = grid2[4].split(",");
                if (hostages2.length > 1) {
                    for (int i = 0; i < hostages2.length; i += 3) {
                        if (Integer.parseInt(hostages2[i + 2]) >= 100) {
                            mustKillAgents2++;
                        }

                    }
                }
                return mustKillAgents1 - mustKillAgents2;
            }
        });
        pq.add(root);
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            expandedNodes++;
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
            pq.addAll(children);


        }
        return res;
    }

    Node AS1(Node root) {
        Node res = null;
        expandedNodes = 0;
        //COMPARATOR COMPARES NUMBER OF HOSTAGES STILL IN GRID THEN
        //NUMBER OF DEATHS OF HOSTAGES
        // THEN NUMBER OF KILLS AND ORDER ASCENDINGLY
        PriorityQueue<Node> pq = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                costTriplet c1 = o1.pathCost;
                costTriplet c2 = o2.pathCost;
                int hostages5lso1 = c1.hostagesEnded;
                int hostages5lso2 = c2.hostagesEnded;
                int deaths1 = c1.deaths;
                int deaths2 = c2.deaths;
                int kills1 = c1.kills;
                int kills2 = c2.kills;
                //NUMBER OF HOSTAGES THAT MUST BE KILLED
                int mustKillAgents1 = 0;
                int mustKillAgents2 = 0;
                String[] hostages1 = o1.gridState.split(";")[4].split(",");
                if (hostages1.length > 1) {
                    for (int i = 0; i < hostages1.length; i += 3) {
                        if (Integer.parseInt(hostages1[i + 2]) >= 100) {
                            mustKillAgents1++;
                        }

                    }
                }
                String[] hostages2 = o2.gridState.split(";")[4].split(",");
                if (hostages2.length > 1) {
                    for (int i = 0; i < hostages2.length; i += 3) {
                        if (Integer.parseInt(hostages2[i + 2]) >= 100) {
                            mustKillAgents2++;
                        }

                    }
                }
                if (deaths1 != deaths2)
                    return deaths1 - deaths2;
                if ((kills1 + mustKillAgents1) != (kills2 + mustKillAgents2))
                    return (kills1 + mustKillAgents1) - (kills2 + mustKillAgents2);

                return hostages5lso2 - hostages5lso1;

            }
        });
        pq.add(root);
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            expandedNodes++;
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
            pq.addAll(children);


        }
        return res;
    }

    Node AS2(Node root) {
        Node res = null;
        expandedNodes = 0;
        //COMPARATOR COMPARES NUMBER OF HOSTAGES STILL IN GRID THEN
        //NUMBER OF DEATHS OF HOSTAGES
        // THEN NUMBER OF KILLS AND ORDER ASCENDINGLY
        PriorityQueue<Node> pq = new PriorityQueue<>(10, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                //HOSTAGES THAT WERE DROPPED OR KILLED => EL HOSTAGES ELY 5ALASO
                costTriplet c1 = o1.pathCost;
                costTriplet c2 = o2.pathCost;
                int hostages5lso1 = c1.hostagesEnded;
                int hostages5lso2 = c2.hostagesEnded;
                int deaths1 = c1.deaths;
                int deaths2 = c2.deaths;
                int kills1 = c1.kills;
                int kills2 = c2.kills;
                String[] grid1 = o1.gridState.split(";");
                String[] grid2 = o2.gridState.split(";");
                int minDistanceToDrop1 = 0;
                int minDistanceToDrop2 = 0;
                boolean atPill1 = false;
                boolean atPill2 = false;
                String[] neo = grid1[0].split(",");
                String[] neo2 = grid2[0].split(",");
                String[] pills1 = grid1[2].split(",");
                String[] pills2 = grid2[2].split(",");
                if (pills1.length > 1) {
                    for (int i = 0; i < pills1.length; i += 2) {
                        if (Integer.parseInt(neo[0]) == Integer.parseInt(pills1[i]) &&
                                Integer.parseInt(neo[1]) == Integer.parseInt(pills1[i + 1])) {
                            atPill1 = true;
                            break;
                        }
                    }
                }
                if (telX != Integer.parseInt(neo[0]) || telY != Integer.parseInt(neo[1])) {
//                    if ((telX == Integer.parseInt(neo[0]) && (telY == Integer.parseInt(neo[1]) + 1 || telY == Integer.parseInt(neo[1]) - 1))
//                    ||(telY == Integer.parseInt(neo[1]) && (telX == Integer.parseInt(neo[0]) + 1 || telX == Integer.parseInt(neo[0]) - 1))) {
//                        minDistanceToDrop1 = 1;
//                    } else minDistanceToDrop1 = 2;
                    minDistanceToDrop1 = 1;
                }

                if (telX != Integer.parseInt(neo2[0]) || telY != Integer.parseInt(neo2[1])) {
//                    if ((telX == Integer.parseInt(neo2[0]) && (telY == Integer.parseInt(neo2[1]) + 1 || telY == Integer.parseInt(neo2[1]) - 1))
//                        ||(telY == Integer.parseInt(neo2[1]) && (telX == Integer.parseInt(neo2[0]) + 1 || telX == Integer.parseInt(neo2[0]) - 1))) {
//                        minDistanceToDrop2 = 1;
//                    } else minDistanceToDrop2 = 2;
                    minDistanceToDrop2 = 1;
                }
                if (pills2.length > 1) {
                    for (int i = 0; i < pills2.length; i += 2) {
                        if (Integer.parseInt(neo2[0]) == Integer.parseInt(pills2[i]) &&
                                Integer.parseInt(neo2[1]) == Integer.parseInt(pills2[i + 1])) {
                            atPill2 = true;
                            break;
                        }
                    }
                }
                String[] carried1 = grid1[5].split(",");
                String[] carried2 = grid2[5].split(",");
                int mustDead1 = 0;
                int mustDead2 = 0;
                if (carried1.length > 1) {
                    for (int i = 0; i < carried1.length; i += 3) {
                        int damage = Integer.parseInt(carried1[i + 2]);
//                        if (damage < 100) {
//                            if (damage + (2 * minDistanceToDrop1) >= 100) {
//                                mustDead1++;
//                            }
//                        }
                        if (damage >= 98 && damage < 100 && !atPill1 && minDistanceToDrop1 != 0) {
                            mustDead1++;
                        }
                    }
                }
                if (carried2.length > 1) {
                    for (int i = 0; i < carried2.length; i += 3) {
                        int damage = Integer.parseInt(carried2[i + 2]);
//                        if (damage < 100) {
//                            if (damage + (2 * minDistanceToDrop2) >= 100) {
//                                mustDead2++;
//                            }
//                        }
                        if (damage >= 98 && damage < 100 && !atPill2 && minDistanceToDrop2 != 0) {
                            mustDead2++;
                        }
                    }
                }


                if (deaths1 + mustDead1 != deaths2 + mustDead2)
                    return (deaths1 + mustDead1) - (deaths2 + mustDead2);
                if (kills1 != kills2)
                    return kills1 - kills2;
                return hostages5lso2 - hostages5lso1;


            }
        });
        pq.add(root);
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            expandedNodes++;
            if (isGoal(curr)) {
                //FEEH 7AGA EL MAFROOD TT3ML HNA
                return curr;
            }
            if (isLeaf(curr)) {
                continue;
            }

            // ADD IN QUEUE IF NOT REPEATED STATE
            ArrayList<Node> children = Expand(curr);
            pq.addAll(children);


        }
        return res;
    }


}
