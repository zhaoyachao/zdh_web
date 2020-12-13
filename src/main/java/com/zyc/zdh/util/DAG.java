package com.zyc.zdh.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class DAG {
	/**
	 * Multimap, supports <code>null key, but not null values.
	 */
	private static final class MultiMap {
		private final Map fMap= new LinkedHashMap();

		/**
		 * Adds <code>val to the values mapped to by key. If
		 * <code>val is null, key is added to the key set of
		 * the multimap.
		 * 
		 * @param key the key
		 * @param val the value
		 */
		public void put(Object key, Object val) {
			Set values= (Set) fMap.get(key);
			if (values == null) {
				values= new LinkedHashSet();
				fMap.put(key, values);
			}
			if (val != null)
				values.add(val);
		}

		/**
		 * Returns all mappings for the given key, an empty set if there are no mappings.
		 * 
		 * @param key the key
		 * @return the mappings for <code>key
		 */
		public Set get(Object key) {
			Set values= (Set) fMap.get(key);
			return values == null ? Collections.EMPTY_SET : values;
		}

		public Set keySet() {
			return fMap.keySet();
		}

		/**
		 * Removes all mappings for <code>key and removes key from the key
		 * set.
		 * 
		 * @param key the key to remove
		 * @return the removed mappings
		 */
		public Set removeAll(Object key) {
			Set values= (Set) fMap.remove(key);
			return values == null ? Collections.EMPTY_SET : values;
		}

		/**
		 * Removes a mapping from the multimap, but does not remove the <code>key from the
		 * key set.
		 * 
		 * @param key the key
		 * @param val the value
		 */
		public void remove(Object key, Object val) {
			Set values= (Set) fMap.get(key);
			if (values != null)
				values.remove(val);
		}
		
		/*
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return fMap.toString();
		}
	}

	private final MultiMap fOut= new MultiMap();
	private final MultiMap fIn= new MultiMap();

	/**
	 * Adds a directed edge from <code>origin to target. The vertices are not
	 * required to exist prior to this call - if they are not currently contained by the graph, they are
	 * automatically added.
	 * 
	 * @param origin the origin vertex of the dependency
	 * @param target the target vertex of the dependency
	 * @return <code>true if the edge was added, false if the
	 *         edge was not added because it would have violated the acyclic nature of the
	 *         receiver.
	 */
	public boolean addEdge(Object origin, Object target) {
//		Assert.isLegal(origin != null);
//		Assert.isLegal(target != null);

		if (hasPath(target, origin))
			return false;

		fOut.put(origin, target);
		fOut.put(target, null);
		fIn.put(target, origin);
		fIn.put(origin, null);
		return true;
	}

	/**
	 * Adds a vertex to the graph. If the vertex does not exist prior to this call, it is added with
	 * no incoming or outgoing edges. Nothing happens if the vertex already exists.
	 * 
	 * @param vertex the new vertex
	 */
	public void addVertex(Object vertex) {
//		Assert.isLegal(vertex != null);
		fOut.put(vertex, null);
		fIn.put(vertex, null);
	}

	/**
	 * Removes a vertex and all its edges from the graph.
	 *
	 * @param vertex the vertex to remove
	 */
	public void removeVertex(Object vertex) {
		Set targets= fOut.removeAll(vertex);
		for (Iterator it= targets.iterator(); it.hasNext();)
			fIn.remove(it.next(), vertex);
		Set origins= fIn.removeAll(vertex);
		for (Iterator it= origins.iterator(); it.hasNext();)
			fOut.remove(it.next(), vertex);
	}

	/**
	 * Returns the sources of the receiver. A source is a vertex with no incoming edges. The
	 * returned set's iterator traverses the nodes in the order they were added to the graph.
	 * 
	 * @return the sources of the receiver
	 */
	public Set getSources() {
		return computeZeroEdgeVertices(fIn);
	}

	/**
	 * Returns the sinks of the receiver. A sink is a vertex with no outgoing edges. The returned
	 * set's iterator traverses the nodes in the order they were added to the graph.
	 * 
	 * @return the sinks of the receiver
	 */
	public Set getSinks() {
		return computeZeroEdgeVertices(fOut);
	}

	private Set computeZeroEdgeVertices(MultiMap map) {
		Set candidates= map.keySet();
		Set roots= new LinkedHashSet(candidates.size());
		for (Iterator it= candidates.iterator(); it.hasNext();) {
			Object candidate= it.next();
			if (map.get(candidate).isEmpty())
				roots.add(candidate);
		}
		return roots;
	}

	/**
	 * Returns the direct children of a vertex. The returned {@link Set} is unmodifiable.
	 * 
	 * @param vertex the parent vertex
	 * @return the direct children of <code>vertex
	 */
	public Set getChildren(Object vertex) {
		return Collections.unmodifiableSet(fOut.get(vertex));
	}

	public Set getParent(Object vertex){
		return Collections.unmodifiableSet(fIn.get(vertex));
	}

	private boolean hasPath(Object start, Object end) {
		// break condition
		if (start == end)
			return true;

		Set children= fOut.get(start);
		for (Iterator it= children.iterator(); it.hasNext();)
			// recursion
			if (hasPath(it.next(), end))
				return true;
		return false;
	}
	
	/*
	 * @see java.lang.Object#toString()
	 * @since 3.3
	 */
	public String toString() {
		return "Out: " + fOut.toString() + " In: " + fIn.toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}
}