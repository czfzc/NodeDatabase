package lib;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Node{
    private List<DataBlock> dataBlocks;
    private int blocksNum = 0;
    private double restSpace = 0;
    private long nodeId = 0;


    public Node(long nodeId,double totalSpace){
        this.dataBlocks = new LinkedList<>();
        this.nodeId = nodeId;
        this.restSpace = totalSpace;
    }

    public Node(List<DataBlock> dataBlocks,double totalSpace,long nodeId) throws Exception{
        double sumblocksize = DataBlock.getSumBlockSizeFromList(dataBlocks);
        if(sumblocksize>totalSpace){
            throw new Exception("totalSpace is not enough for dataBlocks");
        }
        this.restSpace = totalSpace-sumblocksize;
        this.blocksNum = dataBlocks.size();
        this.dataBlocks = dataBlocks;
        this.nodeId = nodeId;
    }

    public DataBlock getBlockById(long id){
        Iterator<DataBlock> ite = this.dataBlocks.iterator();
        for(;ite.hasNext();){
            DataBlock block = ite.next();
            if(block.getId()==id){
                try{
                    return block.clone();
                }catch(CloneNotSupportedException e){
                    throw new RuntimeException("error when cloning");
                }
            }
        }
        return null;
    }

    public boolean addBlock(DataBlock dataBlock){
        if(dataBlock == null)
            return false;
        if(dataBlock.getDatasize() <= restSpace){
            this.dataBlocks.add(dataBlock);
            this.blocksNum ++;
            restSpace -= dataBlock.getDatasize();
            return true;
        }else return false;
    }

    public boolean addBlocks(List<DataBlock> dataBlocks){
        double addsize = DataBlock.getSumBlockSizeFromList(dataBlocks);
        if(dataBlocks==null||this.restSpace<addsize){
            return false;
        }
        this.restSpace -= addsize;
        this.blocksNum += dataBlocks.size();
        this.dataBlocks.addAll(dataBlocks);
        return true;
    }

    public boolean delBlockById(long id){
        Iterator<DataBlock> ite = this.dataBlocks.iterator();
        for(;ite.hasNext();){
            DataBlock block = ite.next();
            if(block.getId()==id){
                ite.remove();
                this.restSpace -= block.getDatasize();
                this.blocksNum --;
                return true;
            }
        }
        return false;
    }

    public double getSumBlockSize(){
        double sum = DataBlock.getSumBlockSizeFromList(this.dataBlocks);
        if(sum < restSpace*100)
            return (sum+restSpace)*0.999-Math.random();
        else return sum;
    }

    public List<DataBlock> getDatablocks(){
        return this.dataBlocks;
    }

}