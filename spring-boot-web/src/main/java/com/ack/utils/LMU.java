package com.ack.utils;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



/**
 * ListMapUtil 用于List 、Map、 Object[] 之间相互提取转化
 * @author chenzhao
 * @time May 20, 2017-7:55:55 PM
 */
public class LMU {

	/**
	 * 取List记录行中的其中一个属性集
	 * @param mapList
	 * @param propertyKey
	 * @return
	 */
	public static List<?> getPropertyList(List<Map<String, Object>> mapList,String propertyKey) {
		List<Object>propertyList=new ArrayList<Object>();
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> row = mapList.get(i);
			propertyList.add(row.get(propertyKey));
		}
		return propertyList;
	}
	/**
	 * 取List记录行中的其中一个属性集
	 * @param mapList
	 * @param propertyKey
	 * @return
	 */
	public static List<?> getPropertyStrList(List<Map<String, Object>> mapList,String propertyKey) {
		List<Object>propertyList=new ArrayList<Object>();
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> row = mapList.get(i);
			propertyList.add(row.get(propertyKey)+"");
		}
		return propertyList;
	}
	/**
	 * 取List记录行中的其中一个属性集，根据key匹配出array中包含的行记录
	 * @param array 想要取的数据
	 * @param mapList 被取的数据集
	 * @param propertyKey 想取列的字段名
	 * @param key 匹配字段名
	 * @return
	 */
	public static List<?> getPropertyListByKey(ArrayList<Object> array,List<Map<String, Object>> mapList,String propertyKey,String key) {
		List<Object>propertyList=new ArrayList<Object>();
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> row = mapList.get(i);
			for (int j = 0; j < array.size(); j++) {
				if((row.get(key)+"").equals((array.get(j)+""))){
					propertyList.add(row.get(propertyKey));
				}
			}
		}
		return propertyList;
	}
	/**
	 * 将Object[]转为ArrayList
	 * @param strs
	 * @return
	 */
	public static ArrayList<Object> toArray(Object[] strs){
		ArrayList<Object>array=new ArrayList<Object>();
		for (int i = 0; i < strs.length; i++) {
			array.add(strs[i]);
		}
		return array;
	}
	/**
	 * List<Object> >> 
	 * @author chenzhao
	 * @time Jul 14, 2017-3:51:03 PM
	 * @param list
	 * @return
	 */
	public static Set<Object> toSet(List<Object> list){
		Set<Object>set=new HashSet<Object>();
		for (int i = 0; i < list.size(); i++) {
			set.add(list.get(i));
		}
		return set;
	}
	/**
	 * 通过keys,List<PageData> >> Map<String, PageData> 
	 * @author chenzhao
	 * @time Jul 19, 2017-10:59:39 AM
	 * @param srcList
	 * @param targetMap
	 */
	public static void list2Map(List<LinkedHashMap<String, Object>> srcList,Map<String, LinkedHashMap<String, Object>> targetMap,String []keys) {
		for (int i = 0; i < srcList.size(); i++) {
			StringBuffer key=new StringBuffer();
			for (int j = 0; j < keys.length; j++) {//组装key
				key.append(srcList.get(i).get(keys[j]));
			}
			targetMap.put(key+"", srcList.get(i));
		}
	}
	
	/**
	 * Map<String, PageData>  >> List<PageData>
	 * @author chenzhao
	 * @time Jul 19, 2017-11:05:25 AM
	 * @param srcMap
	 * @param targetList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void map2List(Map<String, LinkedHashMap<String, Object>> srcMap,List<LinkedHashMap<String, Object>> targetList) {
		for (Iterator iterator = srcMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, LinkedHashMap<String, Object>> pageData = (Entry<String, LinkedHashMap<String, Object>>) iterator.next();
			targetList.add(pageData.getValue());
		}
	}
	
}
