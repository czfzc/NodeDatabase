package util;

import java.util.List;
import java.util.Map;

import util.geo.GeoHash;
import util.hilbert.*;
import lib.DataBlock;

public class Util{
    
    public static String getGeoHash(double longitude,double latitude){
        GeoHash g = new GeoHash(latitude,longitude);
		return g.getGeoHashBase32();
    }
    
    public static int getHibertCode(double longitude,double latitude){
        int hilbertcode = Hilbert.xy2d(2000000,(int)(longitude*10000), (int)(latitude*10000));
        return -hilbertcode;
    }

    public static <T> void printMap(Map<T,List<DataBlock>> map){
        for(Map.Entry<T,List<DataBlock>> entry:map.entrySet()){
            System.out.println("当前map表： "+entry.getKey()+" "+entry.getValue().size());
        }   
    }

    public static <T> void printList(List<T> list){
        for(T t:list){
            System.out.println(t);
        }
    }
}