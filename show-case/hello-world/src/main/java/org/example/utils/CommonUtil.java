package org.example.utils;

import cn.hutool.http.HttpUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtil {

    public static String uuid(){

        return UUIDHelper.randomUUID() ;
    }

    public static String getParameter(String parameters, String fieldName){
        Map<String, List<String>> params = HttpUtil.decodeParams(parameters, "UTF-8");
        return Optional.ofNullable(params.get(fieldName))
            .filter(item -> !CollectionUtils.isEmpty(item))
            .map(list -> list.get(0))
            .orElse(null);
    }

    /**
     * list中的element转换为另外一个对象
     * @param list 入参list
     * @param <R> 返回值类型
     * @param <V> 入参值类型
     * @return 转换后的list
     */
    public static <V,R> List<R> listMapping(List<V> list, Function<V,R> mapper){
        if (org.springframework.util.CollectionUtils.isEmpty(list)){
            return Collections.emptyList() ;
        }
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    /**
     * list转为map
     * @param list 入参list
     * @param keyExtractor key func
     * @param <K> map的key类型
     * @param <T> map的value类型
     * @return map
     */
    public static <K,T> Map<K,T> list2Map(Collection<T> list, Function<T,K> keyExtractor){
        Map<K,T> retMap = new HashMap<>() ;
        if (org.springframework.util.CollectionUtils.isEmpty(list)){
            return retMap ;
        }
        for (T item: list){
            K key = keyExtractor.apply(item) ;
            retMap.put(key, item) ;
        }
        return retMap ;
    }

    public static <K,T> Map<K,List<T>> list2MapList(List<T> list, Function<T,K> keyExtractor){
        Map<K,List<T>> retMap = new HashMap<>() ;
        if (CollectionUtils.isEmpty(list)){
            return retMap ;
        }
        for (T item: list){
            K key = keyExtractor.apply(item) ;
            retMap.putIfAbsent(key, new ArrayList<>()) ;
            retMap.get(key).add(item) ;
        }
        return retMap ;
    }


    /**
     * list转为map
     * @param list 入参list
     * @param keyExtractor key func
     * @param <K> map的key类型
     * @param <T> map的value类型
     * @return map
     */
    public static <K,V,T> Map<K,V> list2Map(
        Collection<T> list, Function<T,K> keyExtractor, Function<T,V> valueExtractor){
        Map<K,V> retMap = new HashMap<>() ;
        if (CollectionUtils.isEmpty(list)){
            return retMap ;
        }
        for (T item: list){
            retMap.put(keyExtractor.apply(item), valueExtractor.apply(item)) ;
        }
        return retMap ;
    }

    /**
     * list转为map-list
     * @param list 入参list
     * @param keyExtractor key func
     * @param <K> map的key类型
     * @param <T> map的value类型
     * @return map
     */
    public static <K,V,T> Map<K,List<V>> list2MapList(
        List<T> list, Function<T,K> keyExtractor, Function<T,V> valueExtractor){
        Map<K,List<V>> retMap = new HashMap<>() ;
        if (CollectionUtils.isEmpty(list)){
            return retMap ;
        }
        for (T item: list){
            K key = keyExtractor.apply(item);
            retMap.putIfAbsent(key, new ArrayList<>()) ;
            V value = valueExtractor.apply(item);
            retMap.get(key).add(value) ;
        }
        return retMap ;
    }
}
