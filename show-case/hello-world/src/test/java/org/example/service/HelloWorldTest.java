package org.example.service;

import org.junit.jupiter.api.Test;

import java.util.*;

public class HelloWorldTest {

    static class MyError extends Error {
        public MyError(String message) {
            super(message);
        }
    }

    @Test
    void hello() throws ClassNotFoundException {
//        LinkedHashMap<String, String> map = new LinkedHashMap<>() ;
//        map.put("key1", "value1");
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
//        Class<?> clazz = HelloWorldTest.class;
//        Class<?> aClass = Class.forName("org.example.service.HelloWorldTest");
//        throw new MyError("This is a custom error message for testing purposes.");
    }

    @Test
    void hello2(){
//        LinkedList<String> list = new LinkedList<>();
//        list.add("Hello");

//        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        map.put("key1", "value1");
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
//        Map<String,String> map = new HashMap<>();
//        map.put(null, "value1");
//        System.out.println(map.get(null)) ;

        Hashtable <String, String> map = new Hashtable<>();
        map.put("tet", "value1");
        System.out.println(map.get(null));
    }
}
