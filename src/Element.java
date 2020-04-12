import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Element {
    public Element(int row,int column){
        this.routes = new ArrayList<>();
        this.row=row;
        this.column=column;
    }
    public void prepareRoute(){
        routes.add(new int[]{row});
        edges.add(new HashMap<Integer, Integer>());

        HashSet<Integer> hs = new HashSet<>();
        hs.add(row);
        vertices.add(hs);
    }
    ArrayList<int[]> routes;
    ArrayList<HashMap<Integer,Integer>> edges = new ArrayList<>();

    ArrayList<HashSet<Integer>> vertices = new ArrayList<>();

    int row;
    int column;

    public ArrayList<int[]> multiply(@NotNull Element e){
        ArrayList<int[]> r = new ArrayList<>(this.routes.size()*e.routes.size());
        for (int cnt=0;cnt<routes.size();cnt++) {
            int []front = routes.get(cnt);
            HashMap<Integer,Integer> hm1 = edges.get(cnt);

            HashSet<Integer> hm2 = vertices.get(cnt);

            out:
            for (int ct = 0;ct<e.routes.size();ct++) {
                int []tail = e.routes.get(ct);
                int []rt = new int[front.length+tail.length];
                int lastFrom = front[front.length-1];
                for (int i=0;i<tail.length;i++){
                    if (hm1.containsKey(lastFrom)&&hm1.get(lastFrom)==tail[i]||hm2.contains(tail[i])){
                        break out;
                    }
                    lastFrom = tail[i];
                }
                System.arraycopy(front, 0, rt, 0, front.length);
                System.arraycopy(tail, 0, rt, front.length, tail.length);
                r.add(rt);
            }
        }
        return r;
    }
    public void add(Element e){
        routes.addAll(e.routes);
        edges.addAll(e.edges);
    }
    public void add(ArrayList<int[]> e){
        routes.addAll(e);
        for (int []arr:e) {
            HashMap<Integer, Integer> hm = new HashMap<>();
            for (int i = 0; i < arr.length - 1; i++) {
                hm.put(arr[i],arr[i+1]);
            }

            HashSet<Integer> vx = new HashSet<>();
            for (int i=0;i < arr.length;i++){
                vx.add(arr[i]);
            }
            vertices.add(vx);

            edges.add(hm);
        }
    }

    @Override
    public String toString() {
        StringBuilder sbr = new StringBuilder();
        sbr.append("{");
        for (int []arr:routes){
            sbr.append("[");
            for (int i=0;i<arr.length-1;i++){
                sbr.append(arr[i]+",");
            }
            if (arr.length!=0)sbr.append(arr[arr.length-1]+"]");
        }
        sbr.append("}");
        return row+" "+column+" > "+sbr.toString();
    }
}
