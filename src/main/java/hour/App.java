package hour;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lib.DataBlock;
import lib.Node;
import lib.NodeEngine;
import util.Util;

public final class App {

    public static List<DataBlock> getData() throws FileNotFoundException,IOException,ParseException{
        BufferedReader bf = new BufferedReader(new FileReader("./res/data.csv"));
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
        /*此处N是文档要求预留的节点数 */
        int N = 10; 
        List<DataBlock> dataBlocks = getData();
        //Util.printList(dataBlocks);
        
        NodeEngine nodeEngine = new NodeEngine(dataBlocks, N);
        Map<Long,Node> nodes = nodeEngine.getNodes();
        for(Map.Entry<Long,Node> nodeentry:nodes.entrySet()){
            Long nodeid = nodeentry.getKey();
            Node node = nodeentry.getValue();
        }
        
    }
      
}
