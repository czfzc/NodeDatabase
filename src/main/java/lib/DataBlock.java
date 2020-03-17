package lib;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DataBlock implements Cloneable{
    private double longitude = 0D;
    private double latitude = 0D;
    private Date time = null;
    private double datasize = 0D;
    private long id = 0L;
    private String semantic = "";
    private String geoHash = "";
    private int hibertCode = -1;

    public DataBlock(double longitude,double latitude,Date time,double datasize,long id,String semantic){
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.datasize = datasize;
        this.id = id;
        this.semantic = semantic;
    }

    public int getHibertCode(){
        if(hibertCode==-1){
            this.hibertCode = util.Util.getHibertCode(longitude, latitude);   
        }
        return hibertCode;
    }

    public String getGeoHash(){
        if(geoHash.equals("")){
            this.geoHash = util.Util.getGeoHash(longitude, latitude);   
        }
        return geoHash;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public Date getTime(){
        return time;
    }

    public void setTime(Date time){
        this.time = time;
    }

    public double getDatasize(){
        return datasize;
    }

    public void setDatasize(double datasize){
        this.datasize = datasize;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getSemantic(){
        return semantic;
    }

    public void setSemantic(String semantic){
        this.semantic = semantic;
    }

    public static double getSumBlockSizeFromList(List<DataBlock> blocks){
        double size = 0;
        Iterator<DataBlock> ite = blocks.iterator();
        for(;ite.hasNext();){
            DataBlock block = ite.next();
            size += block.getDatasize();
        }
        return size;
    }
    
    @Override
    protected DataBlock clone() throws CloneNotSupportedException{
        DataBlock dataBlock = (DataBlock)super.clone();
        return dataBlock;
    }
}