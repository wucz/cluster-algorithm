package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
	public static void main(String[] arg){
		Float a=(float) 10;
		Float b=(float) 11.5;
		System.out.print(a-b);
		Map<String, Float> map = new HashMap<String, Float>();  
		map.put("10", (float) 281);  
		map.put("11", (float) 275);  
		map.put("1232",(float)  293);  
		map.put("1123", (float) 240);  
		hashmapsort(map);
			
		
	}
	public static void hashmapsort(Map<String, Float> map) {
		List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(
				map.entrySet());
		System.out.println(map);
		Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() { // 重写collections中的compare方法
					public int compare(Map.Entry<String, Float> o1,
							Map.Entry<String, Float> o2) {
						// return (o1.getKey() - o2.getKey());//根据key排序
						if ((o1.getValue() - o2.getValue())< 0)
							return -1;// 根据value排序
						else
							return 1;
						// return
						// (o1.getKey()).toString().compareTo(o2.getKey());
					}
				});
		map.clear();
		int size=entryList.size();
		if(size>100)			//限制关键词数量，取前1000个
			size=100;
		for (int i = 0; i < size; i++) {
			map.put(entryList.get(i).getKey(), entryList.get(i).getValue());
		}
		System.out.print(entryList);
	}
}

