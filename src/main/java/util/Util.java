package util;

import util.geo.GeoHash;
import util.hilbert.*;

public class Util{
    public static String getGeoHash(double longitude,double latitude){
        GeoHash g = new GeoHash(latitude,longitude);
		return g.getGeoHashBase32();
    }
    public static int getHibertCode(double longitude,double latitude){
        int hilbertcode = Hilbert.xy2d(20000000,(int)(longitude*100000), (int)(latitude*100000));
        return hilbertcode;
    }
}