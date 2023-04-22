package xyz.blushyes.demo.guarder;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class GuarderPacking {
    // 注意这里必须用线程安全的集合，不然就给createGuarderObject()加锁，因为里面有个put操作不是线程安全的
    public static final Map<Integer, GuarderObject> packing = new Hashtable<>();

    private static int id = 0;

    private synchronized static int generateId(){
        return id++;
    }

    public static GuarderObject createGuarderObject(){
        GuarderObject guarderObject = new GuarderObject();
        packing.put(generateId(), guarderObject);
        return guarderObject;
    }

    public static GuarderObject getGuarderObject(int id){
        return packing.remove(id);
    }

    public static Set<Integer> getKeySet(){
        return packing.keySet();
    }
}
