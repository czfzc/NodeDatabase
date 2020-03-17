package lib;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import hour.App;
import util.Util;

public class NodeEngine {

    private List<DataBlock> dataBlocks;

    private Map<Long,Node> nodes;

    private int nodeNum = 0;

    private double totalDatasize = 0;

    private NodeEngine(){

    }

    public NodeEngine(List<DataBlock> dataBlocks,int nodeNum){
        this.totalDatasize = DataBlock.getSumBlockSizeFromList(dataBlocks);
        this.nodeNum = nodeNum+1;
        this.nodes = new LinkedHashMap<>();
        /*
            创建nodeNum+1个节点 防止因block缝隙导致nodeNum不够用
            若仍然不够用会动态追加node
        */
        System.out.println("totalsize: "+this.totalDatasize);
        for(int i=0;i<this.nodeNum;i++){ 
            nodes.put((long)i,new Node(i,this.totalDatasize/nodeNum));
        }
        this.dataBlocks = dataBlocks;
        putBlocksInNodes(nodes);
    }

    public static void main(String[] args)throws Exception{
        List<DataBlock> list = App.getData();
        Map<String,List<DataBlock>> map = groupByGeohash(list);
        Node node = new Node(1,DataBlock.getSumBlockSizeFromList(list)/20);
        System.out.println(DataBlock.getSumBlockSizeFromList(list)/20);
     //   putIntoNodeAndRemove(map, node);
      //  System.out.println(node.addBlocks(map.get("")))
        System.out.println("stored size "+node.getDatablocks().size());

        List<DataBlock> rest = mergeMapToList(map);
        System.out.println("rest size "+rest.size());
        //Util.printList(node.getDatablocks());
    }
/*
    private void putBlocksInNodes(Map<Long,Node> nodes,List<DataBlock> blocks){
        List<DataBlock> current = blocks;
        for(Iterator<Map.Entry<Long,Node>> nodeite = nodes.entrySet().iterator();nodeite.hasNext();){
            Map.Entry<Long,Node> nodeentry = nodeite.next();
            Long id = nodeentry.getKey();
            Node node = nodeentry.getValue();

            Map<String,List<DataBlock>> geohashgroup = groupByGeohash(current);
            if(putIntoNodeAndRemove((Map<String,List<DataBlock>>)geohashgroup, node))
                continue;
            current = mergeMapToList(geohashgroup);
            System.out.println(current.size());

            Map<Integer,List<DataBlock>> hibertgroup = groupByHibert(current);
            if(putIntoNodeAndRemove((Map<Integer,List<DataBlock>>)hibertgroup, node))
                continue;
            current = mergeMapToList(hibertgroup);
            System.out.println(current.size());

            Map<Long,List<DataBlock>> timegroup = groupByTimecurrent(current);
            if(putIntoNodeAndRemove((Map<Long,List<DataBlock>>)timegroup, node))
                continue;
            current = mergeMapToList(timegroup);
            System.out.println(current.size());

            Map<String,List<DataBlock>> semanticgroup = groupBySemantic(current);
            if(putIntoNodeAndRemove((Map<String,List<DataBlock>>)semanticgroup, node))
                continue;
            current = mergeMapToList(semanticgroup);
            if(!nodeite.hasNext()){
                this.addNode(this.nodeNum++,this.totalDatasize/nodeNum);
            }
            System.out.println("=====");
        }
        
    }
*/

    private void putBlocksInNodes(Map<Long,Node> nodes){
        for(Iterator<Map.Entry<Long,Node>> nodeite = nodes.entrySet().iterator();nodeite.hasNext();){
            Map.Entry<Long,Node> nodeentry = nodeite.next();
            Long nodeid = nodeentry.getKey();
            Node node = nodeentry.getValue();
            List<DataBlock> rest = geohashDivide(node, this.dataBlocks);
            System.out.printf("节点id:%d\t节点存储量:%f\n",nodeid,node.getSumBlockSize());
            if(rest == null)
                continue;
            else this.dataBlocks.addAll(rest);
        }
        
    }

    private List<DataBlock> geohashDivide(Node node,List<DataBlock> list){
        Map<String,List<DataBlock>> geohashgroup = groupByGeohash(list);
        List<DataBlock> rest = putIntoNodeAndRemove(geohashgroup, node);
        if(rest == null){
            return null;
        }else return hibertDivide(node, rest);
    }

    private List<DataBlock> hibertDivide(Node node,List<DataBlock> list){
        Map<Integer,List<DataBlock>> hibertgroup = groupByHibert(list);
        List<DataBlock> rest = putIntoNodeAndRemove(hibertgroup, node);
        if(rest == null){
            return null;
        }else return timeDivide(node, rest);
    }

    private List<DataBlock> timeDivide(Node node,List<DataBlock> list){
        Map<Long,List<DataBlock>> timegroup = groupByTimecurrent(list);
        List<DataBlock> rest = putIntoNodeAndRemove(timegroup, node);
        if(rest == null)
            return null;
        else return semanticDivide(node, rest);
    }

    private List<DataBlock> semanticDivide(Node node,List<DataBlock> list){
        Map<String,List<DataBlock>> semanticgroup = groupBySemantic(list);
        List<DataBlock> rest = putIntoNodeAndRemove(semanticgroup, node);
        return rest;
    }

    private static <T> List<DataBlock> mergeMapToList(Map<T,List<DataBlock>> map){
        List<DataBlock> list = new LinkedList<DataBlock>();
        for(T t:map.keySet()){
            list.addAll(map.get(t));
        }
        return list;
    }

    private <T> List<DataBlock> putIntoNodeAndRemove(Map<T,List<DataBlock>> map,Node node){
        for(Iterator<Map.Entry<T,List<DataBlock>>> blockite = 
                map.entrySet().iterator();blockite.hasNext();){
            Map.Entry<T,List<DataBlock>> blocksentry = blockite.next();
            T key = blocksentry.getKey();
            List<DataBlock> list = blocksentry.getValue();
            if(node.addBlocks(list)){
                blockite.remove();
                this.dataBlocks.removeAll(list);
            }else return list;
        }
        return null;
    }

    private static Map<String,List<DataBlock>> groupByGeohash(List<DataBlock> blocks){
        Set<String> geoset = new HashSet<>(blocks.stream().
            map(DataBlock::getGeoHash).
            map((String s)->s.substring(0,7)).
            collect(Collectors.toList()));

        Map<String,List<DataBlock>> geogroup = new HashMap<>();

        for(String item:geoset){
            List<DataBlock> list = new LinkedList<>();
            for(DataBlock block:blocks){
                if(block.getGeoHash().startsWith(item)){
                    list.add(block);
                }
            }
            geogroup.put(item, list);
        }
        return geogroup;
    }

    private static Map<Integer,List<DataBlock>> groupByHibert(List<DataBlock> blocks){
        Set<Integer> hibertset = new HashSet<>(blocks.stream().
        map(DataBlock::getHibertCode).
        collect(Collectors.toList()));

        Map<Integer,List<DataBlock>> hibertgroup = new HashMap<>();

        for(Integer item:hibertset){
            List<DataBlock> list = new LinkedList<>();
            for(DataBlock block:blocks){
                if(block.getHibertCode()==item){
                    list.add(block);
                }
            }
            hibertgroup.put(item, list);
        }
        return hibertgroup;
    }

    private static Map<Long,List<DataBlock>> groupByTimecurrent(List<DataBlock> blocks){
        Set<Long> timeset = new HashSet<>(blocks.stream().
        map(DataBlock::getTime).
        map((Date time)->(Long)(time.getTime()/1000/20)).
        collect(Collectors.toList()));

        Map<Long,List<DataBlock>> timegroup = new HashMap<>();

        for(Long item:timeset){
            List<DataBlock> list = new LinkedList<>();
            for(DataBlock block:blocks){
                if(block.getTime().getTime()/1000/20==item){
                    list.add(block);
                }
            }
            timegroup.put(item, list);
        }
        return timegroup;
    }

    private static Map<String,List<DataBlock>> groupBySemantic(List<DataBlock> blocks){
        Set<String> semanticset = new HashSet<>(blocks.stream().
            map(DataBlock::getSemantic).
            collect(Collectors.toList()));

        Map<String,List<DataBlock>> semanticgroup = new HashMap<>();

        for(String item:semanticset){
            List<DataBlock> list = new LinkedList<>();
            for(DataBlock block:blocks){
                if(block.getSemantic().equals(item)){
                    list.add(block);
                }
            }
            semanticgroup.put(item, list);
        }
        return semanticgroup;
    }

    public boolean addNode(long id,double totalSize){
        if(this.nodes.get(id)!=null){
            return false;
        }else{
            this.nodes.put(id, new Node(id,totalSize));
            nodeNum ++;
            return true;
        }
    }

    public boolean removeNode(long id){
        if(this.nodes.remove(id)!=null){
            nodeNum --;
            return true;
        }
        return false;
    }

    public Map<Long,Node> getNodes(){
        return this.nodes;
    }
}