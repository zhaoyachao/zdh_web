package com.zyc.zdh.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class DAGTest {

    @Test
    public void addEdge() {
    }

    @Test
    public void addVertex() {

        DAG dag=new DAG();
        dag.addVertex(1);
        dag.addVertex(2);
        dag.addVertex(3);
        dag.addVertex(4);
        dag.addVertex(5);
        dag.addVertex(6);
        dag.addEdge(3,4);
        dag.addEdge(3,5);
        dag.addEdge(1,3);
        dag.addEdge(2,3);
        dag.addEdge(5,6);
        System.out.println(dag.toString());
        System.out.println(dag.getChildren(3));
        System.out.println(dag.getSources());
        System.out.println(dag.getSinks());
    }

    @Test
    public void getChildren() {
    }
}