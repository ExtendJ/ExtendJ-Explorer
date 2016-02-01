package drast.model.nodeinfo;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Class that holds the terminal Attributes
 * A Attribute holds their computed values.
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private boolean parametrized;
    private String aspect;
    private String declaredAt;
    private boolean isCircular;
    private boolean isNTA;
    private String kind;
    private HashMap<String, Object> computedValues;
    private HashMap<String, Object[]> usedParameters;
    private String lastComputedkey;
    public Attribute(String name, Object value, Method m) {
        super(name, value, m);
        computedValues = new HashMap<>();
        usedParameters = new HashMap<>();
    }


    public Attribute(String name, Object value, Method m, String kind) {
        super(name, value, m);
        computedValues = new HashMap<>();
        usedParameters = new HashMap<>();
        this.kind = kind;
    }

    @Override
    protected void setChildInfo(ArrayList<NodeInfoHolder> al) {
        al.add(new NodeInfoHolder("Is circular", isCircular));
        al.add(new NodeInfoHolder("Is NTA", isNTA));
        al.add(new NodeInfoHolder("Aspect", aspect));
        al.add(new NodeInfoHolder("Declared at", declaredAt, true));
        al.add(new NodeInfoHolder("Kind", kind));
    }

    @Override
    public String getKind() { return kind; }


    public void setKind(String kind) { this.kind = kind; }

    @Override
    public String print(){ return getName(method, null); }

    @Override
    public boolean isParametrized() { return parametrized; }
    public void setParametrized(boolean parametrized) { this.parametrized = parametrized; }

    /**
     * Returns if the Attribute isCircular
     * @return
     */
    public boolean isCircular() { return isCircular; }
    public void setCircular(boolean isCircular) { this.isCircular = isCircular; }

    /**
     * Check if the Attribute is a non-terminal attribute
     */
    public boolean isNTA() { return isNTA; }
    public void setNTA(boolean NTA) { this.isNTA = NTA; }

    /**
     * Returns the name of the Aspect in which the Attribute was declared.
     * @return
     */
    public String getAspect() { return aspect; }
    public void setAspect(String aspect) { this.aspect = aspect; }

    /**
     * Check if a attribute is parametrized
     * @return
     */
    public boolean isAttribute(){ return true; }

    /**
     * Returns the file path to where the Attribute was declared
     */
    public String getDeclaredAt() { return declaredAt; }
    public void setDeclaredAt(String declaredAt) { this.declaredAt = declaredAt; }

    /**
     * Adds a computed value to its List of computed values.
     * If the attribute is non-parametrized it will write to the attributes main value
     * @param params
     * @param value
     */
    public void addComputedValue(Object[] params, Object value){
        if(!isParametrized() && this.value == null){
            this.value = value;
        }
        if(computedValues == null)
            computedValues = new HashMap<>();
        String key = getKey(params);
        lastComputedkey = key;
        computedValues.put(key, value);
        usedParameters.put(key, params);
    }

    public boolean containsValue(Object[] params){
        return  computedValues.containsKey(getKey(params));
    }

    public Object getComputedValue(Object[] params){
        return computedValues.get(getKey(params));
    }

    public Set<Map.Entry<String, Object>> getComputedEntry(){ return computedValues.entrySet(); }

    public Collection<Object> getComputedValues(){
        return computedValues.values();
    }

    public HashMap<String, Object[]> getUsedParameters(){
        return usedParameters;
    }

    public String getLastComputedKey(){return lastComputedkey;}
    private String getKey(Object[] params){
        if(params.length == 0)
            return  "no-params";
        String key = "";
        for (Object obj : params)
            key += (obj == null ? null : obj.hashCode()) + " : ";
        return key;
    }

}
