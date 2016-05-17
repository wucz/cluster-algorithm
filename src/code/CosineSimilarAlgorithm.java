package code;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CosineSimilarAlgorithm {
	HashMap<String, Float> artic1 = null;
	HashMap<String, Float> artic2 = null;

	public CosineSimilarAlgorithm(HashMap<String, Float> map1,
			HashMap<String, Float> map2) {
		artic1 = map1;
		artic2 = map2;
	}

	private float pointMulti() {
		Iterator iter1 = artic1.entrySet().iterator();
		float res = 0;
		while (iter1.hasNext()) {
			Map.Entry entry = (Map.Entry) iter1.next();
			String key = (String) entry.getKey();
			float val = (Float) entry.getValue();
			if (artic2.containsKey(key)) {
				res += val * artic2.get(key);
			}
		}
		return res;
	}
	
	private float squares(HashMap<String, Float> map){
		float res=0;
		Iterator iter1 = map.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entry = (Map.Entry) iter1.next();
			float val =  (Float) entry.getValue();
			res += val*val;
		}
		return res;
	}
	
	private float sqrtMulti() {  
		float result =  0;  
        result = squares(artic1) * squares(artic2);  
        result = (float) Math.sqrt(result);  
        return result;  
    }
	
	public float sim(){
		float res=0;
		return pointMulti()/sqrtMulti();
	}
}
