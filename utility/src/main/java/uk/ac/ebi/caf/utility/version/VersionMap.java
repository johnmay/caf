package uk.ac.ebi.caf.utility.version;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * VersionMap - 08.03.2012 <br/>
 * <p/>
 * Store and access versionned objects.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class VersionMap<O> {

    private TreeMap<Version, O> map = new TreeMap<Version, O>();

    public void put(Version v, O object) {
        map.put(v, object);
    }

    /**
     * Retrieve an object for the given version. This assumes that
     * the if the version is higher then the provided version the returned
     * object is compatible.
     * <p/>
     * i.e if the values v1,v2,v3 and v4 are stored, if i call for v5 and v2 i will
     * get v4 and v2 as no v5 is available.
     *
     * @param v
     *
     * @return
     */
    public O get(Version v) {

        for (Map.Entry<Version, O> e : map.entrySet()) {
            if (v.getIndex() >= e.getKey().getIndex()) {
                return e.getValue();
            }
        }

        throw new InvalidParameterException("No such version for " + map.values() + ", available version are: " + map.keySet());
    }
    
    public Collection<O> values(){
        return map.values();
    }
    

    public boolean has(Version v){

        if(v == null) {
            throw new NullPointerException("Provided version is null");
        }

        for (Map.Entry<Version, O> e : map.entrySet()) {
            if (v.getIndex() >= e.getKey().getIndex()) {
                return true;
            }
        }
        return false;
    }

}
