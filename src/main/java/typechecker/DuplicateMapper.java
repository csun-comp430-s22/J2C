package typechecker;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


public class DuplicateMapper<K, V> {
    public final Map<K, ArrayList<V>> map = new HashMap<K, ArrayList<V>>();

    public void put(K key, V value) {
       if(map.containsKey(key)) {
           map.get(key).add(value);
       } else {
           ArrayList<V> list = new ArrayList<V>();
           list.add(value);
           map.put(key, list);
       }
    }

    public ArrayList<V> get(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public V get(K key, int index) {
        return map.get(key).size() - 1 < index ? null : map.get(key).get(index);
    }

    public boolean equals (Object o) {
        if (o instanceof DuplicateMapper) {
            DuplicateMapper other = (DuplicateMapper) o;
            return map.equals(other.map);
        } else {
            return false;
        }
    }

}