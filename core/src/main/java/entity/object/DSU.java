package entity.object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Disjoint Set
 */
public class DSU {
    private final int[] parent;
    /**
     * Constructs a DSU structure with a specified number of elements.
     * Initializes each element as its own separate set.
     *
     * @param n The total number of elements, indexed from 0 to n-1.
     */
    public DSU(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            make(i);
        }
    }

    /**
     * Initializes a new set containing only the element i.
     * Sets the element as its own parent.
     *
     * @param i The element to initialize.
     */
    public void make(int i) {
        parent[i] = i;
    }

    /**
     * Finds the representative (root) of the set containing element i.
     * Implements path compression for optimization.
     *
     * @param i The element to find the root for.
     * @return The representative (root) of the set.
     */
    public int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    /**
     * Merges the sets containing elements i and j.
     * If i and j are already in the same set, this operation does nothing.
     * The root of second's set is pointed to the root of first's set.
     *
     * @param i An element in the first set.
     * @param j An element in the second set.
     * @return true if the sets were different and a union was performed,
     * false if i and j were already in the same set.
     */
    public boolean set_union(int i, int j) {
        if (find(i) == find(j)) {
            return false;
        } else {
            parent[j] = i;
            return true;
        }
    }

    /**
     * Gets all unique root elements.
     * Each root represents a distinct disjoint set.
     *
     * @return a set
     */
    public Set<Integer> get_roots() {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) {
                set.add(i);
            }
        }
        return set;
    }

    /**
     * Retrieves all elements that belong to the set represented by the given root.
     *
     * @param root The representative element of the set.
     * @return array root
     */
    public List<Integer> get_elements(int root) {
        List<Integer> set = new ArrayList<>();
        for (int i = 0; i < parent.length; i++) {
            if (find(i) == root) {
                set.add(i);
            }
        }
        return set;
    }
}
