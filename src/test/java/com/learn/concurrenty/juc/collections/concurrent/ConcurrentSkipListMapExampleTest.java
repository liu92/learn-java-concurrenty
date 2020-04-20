package com.learn.concurrenty.juc.collections.concurrent;

import org.junit.Test;

import java.util.concurrent.ConcurrentSkipListMap;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ConcurrentSkipListMapExampleTest {


    @Test
    public void  testMerge(){
        ConcurrentSkipListMap<Integer, String> map = ConcurrentSkipListMapExample.create();
        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Clojure");

        String result = map.merge(1, "C++", (ov, v) -> {
            assertThat(ov, equalTo("Scala"));
            assertThat(v, equalTo("C++"));
            return ov + v;
        });
        assertThat(result, equalTo("ScalaC++"));
        assertThat(map.get(1), equalTo("ScalaC++"));
    }


    @Test
    public void  testCompute(){
        ConcurrentSkipListMap<Integer, String> map = ConcurrentSkipListMapExample.create();
        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Clojure");

        String result = map.compute(1,  (k, v) -> {
            assertThat(k, equalTo(1));
            assertThat(v, equalTo("Scala"));
            return "Hello";
        });
        assertThat(result, equalTo("Hello"));
        assertThat(map.get(1), equalTo("Hello"));
    }
}