package Q3;

import java.io.File;
import java.io.FileWriter;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Graph {

    static final int MAX_VERTEX = 100;  //Maximum number of vertices

    int[][] graph;              //Adjacency matrix
    int numberOfVertices;       //Number of vertex   

    int[] isVisited = new int[MAX_VERTEX];
    int[] distance = new int[MAX_VERTEX];
    int[] parent = new int[MAX_VERTEX];

    int startTraversal;         //Start vertex for BFS or DFS or Dijkstra 
    int endTraversal;           //End vertex for Dijkstra
    int countComponent = 0;     //Count the number of connected component
    String resultDFS = "";      //DFS result (one connected component)
    String resultEvenDFS = "";  //DFS result with only even vertex 
    String resultBFS = "";      //BFS result (one connected component)
    String resultAllBFS = "";   //BFS result (all connected component)
    String resultAllDFS = "";   //DFS result (all connected component)
    String resultspDijkstra = ""; //Shortest path distance between 2 vertices
    String resultbacktrackDijkstra = ""; //Tracing the shortest path
    String resultDijkstra = ""; //Shortest path distance between start to all other vertices
    int sumPrim = 0;            //weight value of minimum spanning tree
    String resultPrim = "";     //List of edges on minimum spanning tree
    int numOfCutVertex = 0;     //Number of cut vertex
    String resultCutVertex = "";    //List of cut vertex   
    String resultIsolated = "";     //List of isolated vertex   
    String resultNonIsolated = "";  //List of non-isolated vertex 
    String resultPendant = "";      //List of Pendant vertex  
    String resultDegree = "";       //List of Degrees of vertices 
    String resultBridge = "";
    String res = "";
    int numOfBridge;
    boolean k = true;

    //Constructor
    public Graph() {
        this.graph = new int[MAX_VERTEX][MAX_VERTEX];
        for (int i = 0; i < MAX_VERTEX; i++) {
            for (int j = 0; j < MAX_VERTEX; j++) {
                graph[i][j] = 0;
            }
        }
    }

    //Reset value of Visited value of vertex
    void resetIsVisited() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            isVisited[i] = 0;
        }
    }

    //Reset value of Parent value of vertex
    void resetParent() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            parent[i] = 0;
        }
    }

    //Assign two-way value
    void copy(int[][] a, int b[][]) {
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = 0; j < numberOfVertices; j++) {
                a[i][j] = b[i][j];
            }
        }
    }

    //Determine whether a edge is a bridge or not
    boolean bridge(int u, int v) {
        int[][] h = new int[numberOfVertices][numberOfVertices];
        copy(h, graph);
        allDFS();
        int a = countComponent;
        System.out.println(a);
        graph[u][v] = graph[v][u] = 0;
        allDFS();
        int b = countComponent;
        System.out.println(b);
        copy(graph, h);
        if (b > a) {
            System.out.println(u + " " + v);
            resultBridge += u + " " + v + "\n";
            return true;
        }
        return false;
    }

    //Determine the bridge list
    public void bridge() {
        numOfBridge = 0;
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = i + 1; j < numberOfVertices; j++) {
                if (bridge(i, j)) {
                    numOfBridge++;
                }
            }
        }
    }

    //Determine whether a vertex is a cut vertex or not
    boolean cutVertex(int v) {
        int[][] h = new int[numberOfVertices][numberOfVertices];
        copy(h, graph);
        allDFS();
        int a = countComponent;
        for (int j = 0; j < numberOfVertices; j++) {
            graph[v][j] = graph[j][v] = 0;
        }
        allDFS();
        int b = countComponent;
        copy(graph, h);
        if (b > (a + 1)) {
            System.out.print(v + " ");
            resultCutVertex += "," + v;
            return true;
        }
        return false;
    }

    //Determine the cut vertex list
    public void cutVertex() {
        numOfCutVertex = 0;
        for (int j = 0; j < numberOfVertices; j++) {
            if (cutVertex(j)) {
                numOfCutVertex++;
            }
        }
    }

    //DFS for one connected conponent
    void DFS(int start) {
        resetIsVisited(); // remove in case of All DFS on call
        resetParent(); // remove in case of All DFS on call
        resultDFS = "";
        Stack<Integer> s = new Stack<>();
        s.add(start);
        int u;
        while (!s.isEmpty()) {
            u = s.pop();
            if (isVisited[u] == 0) {
                resultDFS += "," + u; //System.out.println(u);
                isVisited[u] = 1;
                for (int v = numberOfVertices - 1; v >= 0; v--) {
                    if (this.graph[u][v] >= 1 && isVisited[v] == 0) {
                        s.add(v);
                        this.parent[v] = u;
                    }
                }
            }
        }
    }

    //DFS for all connected conponents
    void allDFS() {
        resetIsVisited();
        resetParent();
        this.countComponent = 0;
        for (int v = 0; v < this.numberOfVertices; v++) {
            if (this.isVisited[v] == 0) {
                DFS(v);
                this.countComponent++;
                this.resultAllDFS = this.resultAllDFS + "\n" + this.resultDFS.substring(1);
            }
        }
        this.resultAllDFS = this.countComponent + this.resultAllDFS;
    }

    //DFS with only even vertices
    void findEvenDFS(int start) {
        resultEvenDFS = "";
        resetIsVisited();
        resetParent();
        Stack<Integer> s = new Stack<>();
        s.add(start);
        int u;
        while (!s.isEmpty()) {
            u = s.pop();
            if (isVisited[u] == 0) {
                if (u % 2 == 0) {
                    resultEvenDFS += "," + u;
                }
                isVisited[u] = 1;
                for (int v = numberOfVertices - 1; v >= 0; v--) {
                    if (graph[u][v] >= 1 && isVisited[v] == 0) {
                        s.add(v);
                        parent[v] = u;
                    }
                }
            }
        }
    }

    //BFS for one connected conponent
    void BFS(int start) {
        resetIsVisited(); // remove in case of AllBFS on call
        resetParent(); // remove in case of All BFS on call
        resultBFS = "";
        Queue<Integer> q = new LinkedList<>();
        q.add(start);
        isVisited[start] = 1;
        int u;
        while (!q.isEmpty()) {
            u = q.poll();
            System.out.println(u);
//            if (u>4)
            resultBFS += "," + u;
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] >= 1 && isVisited[v] == 0) {
                    q.add(v);
                    isVisited[v] = 1;
                }
            }
        }
    }

    //BFS for all connected conponents
    void allBFS() {
        resetIsVisited();
//        resetParent();
        countComponent = 0;
        for (int v = 0; v < numberOfVertices; v++) {
            if (isVisited[v] == 0) {
                BFS(v);
                countComponent++;
                resultAllBFS = resultAllBFS + "\n" + resultBFS.substring(1);
            }
        }
        resultAllBFS = countComponent + resultAllBFS;
    }

    //List of edges
    String getEdge() {
        String str = "";
        for (int u = 0; u < numberOfVertices; u++) {
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] != 0) {
                    str += "(" + u + "," + v + ")\n";
                }
            }
        }
        return str;
    }

    //Find Isolated vertex
    void findIsolated() {
        boolean k = true;
        for (int i = 0; i < numberOfVertices; i++) {
            k = true;
            for (int j = 0; j < numberOfVertices; j++) {
                if (graph[i][j] != 0) {
                    k = false;
                    break;
                }
            }
            if (k == true) {
                resultIsolated += "," + i;
            }
        }
    }

    int degree(int i) {
        int degree = 0;
        for (int j = 0; j < numberOfVertices; j++) {
            if (graph[i][j] > 0) {
                degree++;
            }
        }
        return degree;
    }

    void findPendant() {
        for (int i = 0; i < numberOfVertices; i++) {
            if (degree(i) == 1) {
                resultPendant += "," + i;
            }
        }
    }

    void findNonIsolated() {
        for (int i = 0; i < numberOfVertices; i++) {
            if (degree(i) > 0) {
                resultNonIsolated += "," + i;
            }
        }
    }

    void resetDistance() {
        for (int i = 0; i < MAX_VERTEX; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
    }

    int findNearestint() {
        int minIndex = -1;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < numberOfVertices; i++) {
            if (distance[i] < minValue && isVisited[i] == 0) {
                minValue = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    void prim() {
        getConnectedComponents();
        if (countComponent > 1) {
            res += "Impossible";
            return;
        }
        resetIsVisited();
        resetDistance();
        resetParent();
        sumPrim = 0;
        int u;
        distance[0] = 0;
        for (int i = 0; i < numberOfVertices; i++) {
            u = findNearestint(); // 0
            if (u == -1) {
                return;
            }
            isVisited[u] = 1;
            sumPrim += distance[u];
//            System.out.println(sumPrim);
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] > 0 && isVisited[v] == 0
                        && distance[v] > graph[u][v]) {
                    distance[v] = graph[u][v];
                    parent[v] = u;
                }
            }
        }
        for (int i = 0; i < numberOfVertices; i++) {
            resultPrim += "\n" + parent[i] + " " + i + " " + distance[i];
        }
        res += sumPrim;
    }

    void spDijkstra(int start) {
        resetIsVisited();
        resetDistance();
        resetParent();
        resultDijkstra = "";
        int u;
        distance[start] = 0;
        for (int i = 0; i < numberOfVertices; i++) {
            u = findNearestint(); // 0
            if (u == -1) {
                return;
            }
            isVisited[u] = 1;
            System.out.print(u + ": " + distance[u] + "\n");
            for (int v = 0; v < numberOfVertices; v++) {
                if (graph[u][v] > 0 && isVisited[v] == 0
                        && distance[v] > distance[u] + graph[u][v]) {
                    distance[v] = distance[u] + graph[u][v];
                    parent[v] = u;
                }
            }
        }
        // In duong di ngan nhat tu start den tat ca cac dinh khac
//        for (int i = 0; i < numberOfVertices; i++) {
////            resultDijkstra += "," + distance[i];
//            resultbacktrackDijkstra = "";
//            backtrackDijkstra(start, i);
//            String temp = resultbacktrackDijkstra;
//            resultDijkstra += "\n" + i + "--" + distance[i] + ": " + temp;
//        }
    }

    void spDijkstra(int start, int des) {
        spDijkstra(start);
        resultspDijkstra = distance[des] + "\n";
    }

    void backtrackDijkstra(int start, int des) {
        int u = des;
        Stack<Integer> s = new Stack<>();
        s.add(u);
        while (u != start) {
            u = parent[u];
            s.add(u);
            System.out.println(u);
        }
        while (!s.empty()) {
            resultbacktrackDijkstra += " " + (s.pop()); //index đỉnh là 0
//            resultbacktrackDijkstra += " " + (s.pop() + 1); //index đỉnh là 1
//            System.out.println(resultbacktrackDijkstra);
        }
    }

    String getConnectedComponents() {
        countComponent = 0;
        resetIsVisited(); // Đảm bảo tất cả các đỉnh chưa được duyệt
        StringBuilder result = new StringBuilder(); // StringBuilder để xây dựng chuỗi kết quả
        // Duyệt qua tất cả các đỉnh
        for (int i = 0; i < numberOfVertices; i++) {
            if (isVisited[i] == 0) { // Nếu đỉnh chưa được duyệt
                countComponent++;
                System.out.print(countComponent);
//                BFS(i);
                DFS(i); // Thực hiện duyệt theo chiều sâu từ đỉnh này
                // Thêm đỉnh đã duyệt vào chuỗi kết quả
                result.append("\nConnected component ").append(i + 1).append(": ").append(resultDFS.substring(1)).append("\n");
                // Xóa chuỗi kết quả của DFS để sử dụng lại cho đỉnh tiếp theo
                resultDFS = "";
            }
        }
        return result.toString(); // Trả về chuỗi kết quả
    }

    String getConnectedCity() {
        countComponent = 0;
        resetIsVisited(); // Đảm bảo tất cả các đỉnh chưa được duyệt
        StringBuilder result = new StringBuilder(); // StringBuilder để xây dựng chuỗi kết quả
        // Duyệt qua tất cả các đỉnh
        for (int i = 0; i < numberOfVertices; i++) {
            if (isVisited[i] == 0) { // Nếu đỉnh chưa được duyệt
                countComponent++;
//                System.out.print(countComponent);
                DFS(i); // Thực hiện duyệt theo chiều sâu từ đỉnh này
                // Thêm đỉnh đã duyệt vào chuỗi kết quả
                if (i != 0) {
                    result.append(",").append(resultDFS.substring(1));
                }
                // Xóa chuỗi kết quả của DFS để sử dụng lại cho đỉnh tiếp theo
                resultDFS = "";
            }
        }
        return result.toString(); // Trả về chuỗi kết quả
    }

// Phương thức tính bậc của các đỉnh và lưu kết quả vào biến resultDegree
    void calculateDegree() {
        for (int i = 0; i < numberOfVertices; i++) {
            int degree = 0;
            for (int j = 0; j < numberOfVertices; j++) {
                if (graph[i][j] > 0) {
                    degree++;
                }
            }
//            resultDegree += "," + degree;
            resultDegree += "\n" + i + "(" + degree + ")";
        }
    }


    void print() {
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = 0; j < numberOfVertices; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println("");
        }
    }

    void printEdgeList() {
        System.out.println(numberOfVertices);
        int c = 0;
        for (int i = 0; i < numberOfVertices; i++) {
            for (int j = 0; j < numberOfVertices; j++) {
                if (graph[i][j] != 0 && i < j) {
                    System.out.println(i + " " + j + " " + graph[i][j]);
                    c++;
                }
            }
        }
        System.out.println(c);
    }

//     In kq 
    void writeDataToFile(String fileName) {
        print();
        printEdgeList();
//        System.out.print(getEdge());
//        calculateDegree();
//        prim();
//        startTraversal = 0;
//        spDijkstra(startTraversal);
//        spDijkstra(startTraversal, endTraversal);
//        backtrackDijkstra(startTraversal, endTraversal);
//        BFS(startTraversal);
//        allBFS();
//        allDFS();
//        DFS(startTraversal);
//        findEvenDFS(startTraversal);
        findIsolated();
//        findNonIsolated();
//        findPendant();
//        cutVertex();
//        bridge();
//        String r = getConnectedComponents();

        try {
            File o = new File(fileName);
            FileWriter out = new FileWriter(o);
//            out.write(resultDegree.substring(1));
//            out.write(String.valueOf(numberOfVertices - 1) + " ");
//            out.write(String.valueOf(sumPrim));
//            out.write("\n");
//            out.write(resultPrim);
//            out.write(resultDFS.substring(1) + "\n");
//            out.write(resultEvenDFS.substring(1));
//            out.write(resultBFS.substring(1));
//            out.write(resultAllBFS); //Call for all component
//            out.write(resultAllDFS);
            //In ket qua do thi lien thong hay khong
//            if (countComponent == 1) {
//                out.write("YES");
//            } else {
//                out.write("NO");
//            }

            //In ra dinh khong connect duoc voi dinh 0
//            String r = getConnectedCity();            
//            if (countComponent == 1) {
//                out.write("Connected City");
//            } else {
//                out.write(r.substring(1));
//            }
            if (resultIsolated.length() == 0) {
                resultIsolated = " There is a connected graph";
            }
            
            out.write(resultIsolated.substring(1));
//            out.write(resultNonIsolated.substring(1));
//            out.write(resultPendant.substring(1));
//            out.write(resultDijkstra.substring(1));
            //In SP va Path giua hai dinh
//            out.write(resultspDijkstra);
//            out.write(resultbacktrackDijkstra.substring(1));
//            System.out.print(countComponent);
//            out.write(String.valueOf(countComponent));
//            out.write(numOfCutVertex + "\n");
//            out.write(resultCutVertex.substring(1));
//            out.write(numOfBridge + "\n");
//            out.write(resultBridge);
//            out.write(res);
//            out.write(String.valueOf(countComponent - 1));

            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    Adjacent Matrix
    void readDataFile(String fileName) {
        try {
            File in = new File(fileName);
            Scanner sc = new Scanner(in);
            numberOfVertices = sc.nextInt(); //số đỉnh
//            startTraversal = sc.nextInt(); //đỉnh bắt đầu tìm đường đi ngắn nhất Dijkstra
//            endTraversal = sc.nextInt(); //đỉnh dich tìm đường đi ngắn nhất Dijkstra

            for (int i = 0; i < numberOfVertices; i++) {
                for (int j = 0; j < numberOfVertices; j++) {
                    int w = sc.nextInt();
                    graph[i][j] = w;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
//
//     // Edge list 
//    void readDataFile(String fileName) {
//        try {
//            File in = new File(fileName);
//            Scanner sc = new Scanner(in);
//            numberOfVertices = sc.nextInt(); // Số đỉnh
//            int numberOfEdges = sc.nextInt(); // Số cạnh
//        startTraversal = sc.nextInt(); // Đỉnh bắt đầu 
////        endTraversal = sc.nextInt(); // Đỉnh kết thúc        
//
//            // Đọc các cạnh và cập nhật ma trận kề
//            for (int i = 0; i < numberOfEdges; i++) {
//                int from = sc.nextInt();
//                int to = sc.nextInt();
////                int w = sc.nextInt(); // Nếu có trọng số
//                graph[from][to] = 1; // Cập nhật ma trận kề
//                graph[to][from] = 1; // Vì đồ thị vô hướng. Nếu có hướng thì đóng lại
////                graph[from][to] = w; // Nếu có trọng số
////                graph[to][from] = w; // Nếu đồ thị vô hướng có trọng số. Nếu có hướng thì đóng lại 
//            }
////            startTraversal = sc.nextInt(); // Đỉnh bắt đầu duyệt
////            endTraversal = sc.nextInt(); // Đỉnh bắt đầu duyệt 
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }   
    
//    
//    
//    //In testcase
//    void readOneNumber(String fileName) {
//        try {
//            File in = new File(fileName);
//            Scanner sc = new Scanner(in);
//            numberOfVertices = sc.nextInt(); //đọc 1 số
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    void writeTestCase(String fileName) {
//        try {
//            File o = new File(fileName);
//            FileWriter out = new FileWriter(o);
//            String result = "";
//            if (numberOfVertices == 8) {
//                result = "8 9 10 11";
//            } else {
//                result = "1 2 3 4 5";
//            }
//            out.write(result);
//
//            out.close();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    void writeTestCase() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
