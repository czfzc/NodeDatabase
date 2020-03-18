package hour;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lib.DataBlock;
import lib.Node;
import lib.NodeEngine;

/**
 * 项目运行入口类
 */

public final class App {

    public static List<DataBlock> getData(String path) throws FileNotFoundException,IOException,ParseException{
        System.out.println(System.getProperty("user.dir"));
        BufferedReader bf = new BufferedReader(new FileReader(path));
        String line = "";
        SimpleDateFormat smp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<DataBlock> list = new LinkedList<DataBlock>();
        while((line = bf.readLine())!=null){
            String[] str = line.split(",");
            DataBlock datablock = new DataBlock(Double.parseDouble(str[1]), 
                Double.parseDouble(str[0]),smp.parse(str[2]+" "+str[3]),
                Double.parseDouble(str[4]),Long.parseLong(str[5]) , str[6]);
            list.add(datablock);
        }
        return list;
    }
    public static void main(String args[])throws Exception{
        args = new String[2];
        args[0] = "res/data.csv";
        args[1] = "400";
        /*此处N是文档要求预留的节点数 */
        if(args.length<2){
            System.out.printf("format: this.jar path N(nodenum)\n");
            return;
        }
        int N = Integer.valueOf(args[1]); 
        List<DataBlock> dataBlocks = getData(args[0]);
        //Util.printList(dataBlocks);
        
        NodeEngine nodeEngine = new NodeEngine(dataBlocks, N);
     //   Map<Long,Node> nodes = nodeEngine.getNodes();
      /*  for(Map.Entry<Long,Node> nodeentry:nodes.entrySet()){
            Long nodeid = nodeentry.getKey();
            Node node = nodeentry.getValue();
        }*/
        
    }
      
}
