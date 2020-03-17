package hour;

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
    public static void main(String args[]){
        int N = 20; /*此处N是文档要求预留的节点数 */
        List<DataBlock> dataBlocks = new LinkedList<>();
        NodeEngine nodeEngine = new NodeEngine(dataBlocks, N);
        Map<Long,Node> nodes = nodeEngine.getNodes();
        for(Map.Entry<Long,Node> nodeentry:nodes.entrySet()){
            Long nodeid = nodeentry.getKey();
            Node node = nodeentry.getValue();
            System.out.printf("节点id:%d\t节点存储量:%.2f\n",nodeid,node.getSumBlockSize());
        }
    }
      
}
