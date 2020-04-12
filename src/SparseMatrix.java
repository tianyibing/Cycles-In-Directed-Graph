import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class SparseMatrix {
    int row;
    int column;
    ArrayList<Element> elem;
    int rpos[];

    public SparseMatrix() {
        elem = new ArrayList<>();
    }

//    public SpareMatrix Route2Matrix(Solution sol){
//        HashMap<Integer,Integer> hm = new HashMap<>();
//
//    }

    public void loadMatrix(String path){
        String str;
        int count = 0;
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bfr = new BufferedReader(fr);
            while ((str = bfr.readLine()) != null) {
                count++;
            }
            elem = new ArrayList<>(count);
            FileInputStream fis = new FileInputStream(path);
            Scanner input = new Scanner(fis);
            while (input.hasNext()) {
                String line = input.nextLine();
                String []words = line.split(",");
                int r = Integer.parseInt(words[0]);
                int c = Integer.parseInt(words[1]);
                elem.add(new Element(r, c));
                elem.get(elem.size()-1).prepareRoute();
                row = column = Math.max(Math.max(row,r+1), Math.max(column,c+1));
            }
            this.initializeRpos();
            input.close();
            bfr.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
        Collections.sort(elem, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                return Integer.compare(o1.row, o2.row)==0?Integer.compare(o1.column,o2.column)
                        :Integer.compare(o1.row, o2.row);
            }
        });
    }

    public SparseMatrix(int row, int column) {
        this.row = row;
        this.column = column;
        elem = new ArrayList<>();
    }

    public void add(Element e){
        if(e.row+1>row)row=e.row+1;
        if(e.column+1>column)column=e.column+1;
        elem.add(e);
    }

    public void initializeRpos(){
        int arr[]=new int[this.row+1];
        for(int i=0;i<this.elem.size();i++){
            arr[this.elem.get(i).row]++;
        }
        rpos=new int[this.row+1];
        rpos[0]=0;
        for(int i=1;i<this.row+1;i++){
            rpos[i]=arr[i-1]+rpos[i-1];
        }
    }

    public SparseMatrix multiply(SparseMatrix mt2){
        if(this.column!=mt2.row)return null;
        SparseMatrix reMatrix=new SparseMatrix(this.row,mt2.column);
        reMatrix.elem = new ArrayList<>(this.elem.size());
        for(int i=0;i<this.row;i++){
            Element []temp=new Element[mt2.column];
            for(int j=this.rpos[i];j<this.rpos[i+1];j++){
                Element t1=this.elem.get(j);
                for(int k=mt2.rpos[t1.column];k<mt2.rpos[t1.column+1];k++){
                    Element t2 = mt2.elem.get(k);
                    if (temp[t2.column]==null){
                        temp[t2.column] = new Element(i,t2.column);
                    }
                    temp[t2.column].add(t1.multiply(t2));
                }
            }
            for(int t=0;t<temp.length;t++){
                if(temp[t]!=null&&temp[t].routes.size()!=0)reMatrix.add(temp[t]);
            }
        }
        reMatrix.initializeRpos();
        return reMatrix;
    }

    @Override
    public String toString(){
        StringBuilder sbr=new StringBuilder("size : "+row+" * "+column+"\n");
        for (Element e:elem){
            sbr.append(e.toString()).append("\n");
        }
        return sbr.toString();
    }
}
