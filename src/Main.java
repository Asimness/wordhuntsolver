import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static HashSet<String> words = new HashSet<String>();
    public static HashSet<String> resultSet = new HashSet<String>();
    static HashMap<String, List<Pair>> wordMap = new HashMap<>();
    static HashMap<Pair, Integer> pairMap = new HashMap<>();
    static List<Pair> path = new ArrayList<>();
    static char[][] board = new char[4][4];
    //static HashMap<List<Pair>, Set<String>> pathReduce = new HashMap<>();


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        buildBoard(board, s);
        generatePairMap();
        makeDict(words);
        System.out.println(words.size());

        wordSearch(board, s);
        System.out.println(resultSet);
        ArrayList<String> list = new ArrayList<>(resultSet);
        list.sort((a,b) -> b.length() - a.length());
        List<String> words =
                list.stream().filter(word -> word.contains("a") || word.contains("e") ||
                        word.contains("i") || word.contains("o") || word.contains("u") || word.contains("y"))
                        .filter(word -> word.length() > 3).collect(Collectors.toList());

        for(int i = words.size() - 1; i >= 0; i--) {
            String k = words.get(i);
            if (k.length() > 3) {
                System.out.println(k);
                pairsToPath(wordMap.get(k));
            }
        }
        System.out.println(words);
        //printBoard();
        //ebkgrcoaeraytnts
        long end = System.currentTimeMillis();
        group();
        System.out.println("Total time: " + (end - start));
        //System.out.println(pathReduce);

        // PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        /*
        for (Set<String> stringSet : pathReduce.values()) {
            if(stringSet.size() >= 3) {
                System.out.println(stringSet);
            }
        }
         */



    }

    public static void wordSearch(char[][] board, String s) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                dfs(""+  board[i][j], i, j);
                System.out.println(i + " " + j);
                buildBoard(board, s);
                printBoard();
                path.clear();
            }
        }
    }

    public static void dfs(String s, int i, int j) {
        char t = board[i][j];
        board[i][j] = '*';
        Pair p = new Pair(i, j);
        path.add(p);
        if (s.length() > 1) {
            if(words.contains(s)) {
                resultSet.add(s);
                printBoard();
                List<Pair> pairList = new ArrayList<>(path);
                wordMap.put(s, pairList);

                /*
                Set<String> curSet = new HashSet<>(pathReduce.getOrDefault(pairList.subList(0, pairList.size() - 2), Collections.emptySortedSet()));
                pathReduce.remove(pairList.subList(0, pairList.size() - 2));
                curSet.addAll(pathReduce.getOrDefault(pairList.subList(0, pairList.size() - 1), Collections.emptySortedSet()));
                curSet.add(s);
                pathReduce.put(pairList,curSet);
                pathReduce.remove(pairList.subList(0, pairList.size() - 1));
                 */
            }
        }

        if(s.length() > 10) {
            board[i][j] = t;
            path.remove(p);
            return;
        }


        // check top
        if (i - 1 > -1 && board[i-1][j] != '*') {
            dfs(s + board[i-1][j], i-1, j);
        }

        // check right
        if (j + 1 < 4 && board[i][j+1] != '*') {
            dfs(s + board[i][j+1], i, j+1);
        }

        // check bottom
        if (i + 1 < 4 && board[i+1][j] != '*') {
            dfs(s + board[i+1][j], i+1, j);
        }

        // check left
        if (j - 1 > -1 && board[i][j-1] != '*') {
            dfs(s + board[i][j-1], i, j-1);
        }

        // check top left
        if (i - 1 > -1 && j - 1 > -1 && board[i-1][j-1] != '*') {
            dfs(s + board[i-1][j-1], i-1, j-1);
        }

        // check top right
        if (i - 1 > -1 && j + 1 < 4 && board[i-1][j+1] != '*') {
            dfs(s + board[i-1][j+1], i-1, j+1);
        }

        // check bottom right
        if (i + 1 < 4 && j + 1 < 4 && board[i+1][j+1] != '*') {
            dfs(s + board[i+1][j+1], i+1, j+1);
        }

        // check bottom left
        if (i + 1 < 4 && j - 1 > -1 && board[i+1][j-1] != '*') {
            dfs(s + board[i+1][j-1], i+1, j-1);
        }
        board[i][j] = t;
        path.remove(p);
    }




    public static void buildBoard(char[][] board, String s) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = s.charAt(count);
                count++;
            }
        }
    }

    public static void makeDict(Set<String> set) {
        try {
            File myObj = new File("words2.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                set.add(myReader.nextLine().trim().toLowerCase());

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void printBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void pairsToPath(List<Pair> pairs) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int cur = 1;
        for (int i = 0; i < pairs.size(); i++) {
            map.put(pairMap.get(pairs.get(i)), cur);
            cur++;
        }
        int[][] grid = new int[4][4];
        int count = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = map.getOrDefault(count, 0);
                count++;
            }
        }

        printPath(grid);
    }

    public static void printPath(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                String s;
                if(grid[i][j] != 0) {
                    s = "" + grid[i][j];
                } else {
                    s = " ";
                }
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public static void generatePairMap() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Pair pair = new Pair(i, j);
                pairMap.put(pair, count);
                count++;
            }
        }
    }

    public static void group() {
        List<String> bigWords = new ArrayList<>();
        for (String s : wordMap.keySet()) {
            if (s.length() >= 5) {
                bigWords.add(s);
            }
        }
        //System.out.println(bigWords);
        HashMap<List<Pair>, Set<String>> groupWordSet = new HashMap<>();

        for (String s : bigWords) {
            Set<String> curWordSet = new HashSet<>();
            curWordSet.add(s);

            if (wordMap.getOrDefault(s.substring(0, s.length() - 1), null) != null) {
                curWordSet.add(s.substring(0, s.length() - 1));
            }

            if (wordMap.getOrDefault(s.substring(0, s.length() - 2), null) != null) {
                curWordSet.add(s.substring(0, s.length() - 2));
            }

            if (wordMap.getOrDefault(s.substring(0, s.length() - 3), null) != null) {
                curWordSet.add(s.substring(0, s.length() - 3));
            }
            groupWordSet.put(wordMap.get(s), curWordSet);
        }

        List<Set<String>> allSets = new ArrayList<>(groupWordSet.values());

        List<Set<String>> mergeSets = new ArrayList<>();

        for(int i = 0; i < allSets.size(); i++) {
            for (int j = i+1; j < allSets.size(); j++) {
                Set<String> firstSet = allSets.get(i);
                Set<String> secondSet = allSets.get(j);
                for(String s : firstSet) {
                    if (secondSet.contains(s)) {
                        Set<String> mergedSet = new HashSet<>();
                        mergedSet.addAll(firstSet);
                        mergedSet.addAll(secondSet);
                        mergeSets.add(mergedSet);
                        allSets.remove(firstSet);
                        allSets.remove(secondSet);
                    }
                }

            }
        }

        HashSet<Set<String>> temp = new HashSet<>();

        temp.addAll(allSets);
        temp.addAll(mergeSets);
        HashMap<Integer, Integer> pointMap = new HashMap<>();
        pointMap.put(3, 100);
        pointMap.put(4, 400);
        pointMap.put(5, 800);
        pointMap.put(6, 1400);
        pointMap.put(7, 1700);
        pointMap.put(8, 2000);
        pointMap.put(9, 2400);
        pointMap.put(10, 2700);
        pointMap.put(11, 3000);
        pointMap.put(12, 3300);
        pointMap.put(13, 3700);
        pointMap.put(14, 4000);
        PriorityQueue<Set<String>> pq = new PriorityQueue<>((a, b) -> {
            int points1 = 0;
            int points2 = 0;
            for (String s: a){
                points1 += pointMap.getOrDefault(s.length(), 0);
            }

            for (String s: b){
                points2 += pointMap.getOrDefault(s.length(), 0);
            }

            return points1 - points2;
        });
        pq.addAll(temp);
        while(!pq.isEmpty()) {
            ArrayList<String> words = new ArrayList<>(pq.poll());
            words.sort((a, b) -> b.length() - a.length());
            System.out.println("subWords: " + words);
            pairsToPath(wordMap.get(words.get(0)));
        }
    }





    static class Pair {

        int r;
        int c;

        public Pair(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            Pair temp = (Pair) o;
            return this.r == temp.r && this.c == temp.c;
        }

        public String toString(){
            return "row: " + r + " column: " + c;
        }


        @Override
        public int hashCode() {
            int count = 0;
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    count++;
                }
            }
            return count;
        }
    }

}
