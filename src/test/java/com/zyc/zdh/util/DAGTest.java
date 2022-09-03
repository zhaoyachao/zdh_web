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
//        dag.addVertex(1);
//        dag.addVertex(2);
//        dag.addVertex(3);
//        dag.addVertex(4);
//        dag.addVertex(5);
//        dag.addVertex(6);
//        dag.addEdge(3,4);
//        dag.addEdge(3,5);
//        dag.addEdge(0,1);
//        dag.addEdge(1,3);
//        dag.addEdge(2,3);
//        dag.addEdge(5,6);
//        System.out.println(dag.toString());
//        System.out.println(dag.getChildren(3));
//        System.out.println(dag.getSources());
//        System.out.println(dag.getSinks());
//        System.out.println(dag.getParent(3));
//        System.out.println(dag.getAllParent(3));

        dag.addEdge(823139834674548736L,823139834678743041L);
        dag.addEdge(823139834678743041L,823139834678743040L);
        dag.addEdge(823139834678743040L,823139834682937344L);

        System.out.println(dag.getAllParent(823139834682937344L));
    }

    @Test
    public void getChildren() {
    }

    @Test
    public void getSources() {
        DAG dag=new DAG();
        dag.addEdge(823139834678743047L,823139834682937344L);
        dag.addEdge(823139834674548736L,823139834678743041L);
        dag.addEdge(823139834678743041L,823139834678743040L);
        dag.addEdge(823139834678743040L,823139834682937344L);
        System.out.println(dag.getSources());

    }

}