import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SparseMatrix[]M = new SparseMatrix[7];
        M[0] = new SparseMatrix();
        long t1 = System.currentTimeMillis();
        M[0].loadMatrix("src/test_data.txt");
        for (int i=1;i<7;i++){
            M[i] = M[i-1].multiply(M[0]);
        }
        System.out.println("Time Cost > " +( System.currentTimeMillis()-t1));
        ArrayList<PriorityQueue<int[]>> ls = new ArrayList<>();
        int lines = 0;
        for (int i=2;i<7;i++){
            HashSet<String> hs = new HashSet<>();
            PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    for(int i=0;i<o1.length;i++){
                        if (o1[i]==o2[i])continue;
                        return o1[i]<o2[i]?-1:1;
                    }
                    return 0;
                }
            });
            for (Element e:M[i].elem){
                if (e.row==e.column){
                    for (int []arr:e.routes){
                        rotate(arr);
                        String str = encoding(arr);
                        if (!hs.contains(str)){
                            hs.add(str);
                            pq.add(arr);
                        }
                    }
                }
            }
            ls.add(pq);
            lines+=pq.size();
        }
        PrintStream ps = new PrintStream("src/myResult.txt");
        System.setOut(ps);
        System.out.println(lines);
        for(int i=0;i<ls.size();i++){
            while (!ls.get(i).isEmpty()){
                System.out.println(encoding(Objects.requireNonNull(ls.get(i).poll())));
            }
        }
    }
    public static String encoding(int []arr){
        if (arr.length==0)return "";
        StringBuilder sb = new StringBuilder();
        sb.append(arr[0]);
        for (int i=1;i<arr.length;i++){
            sb.append(",").append(arr[i]);
        }
        return sb.toString();
    }
    public static void rotate(int []rt){
        if (rt.length==0)return;
        int pos =0,min = rt[0];
        for (int i=1;i<rt.length;i++){
            if (rt[i]<min){
                pos = i;
                min = rt[i];
            }
        }
        for (int i=0,cnt=0;i<rt.length&&cnt<rt.length;i++){
            int last = i,next = (last+pos)%rt.length;
            int tmp = rt[last];
            while (next!=i){
                rt[last] = rt[next];
                last = next;
                next = (last+pos)%rt.length;
                cnt++;
            }
            rt[last] = tmp;
            cnt++;
        }
    }
}
