package entity.object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DSU {
    private int[] parent;
    private int NumberOfDSU;

    public DSU(int n) {
        parent = new int[n];
        NumberOfDSU = n;
        for (int i = 0; i < n; i++) {
            make(i);
        }
    }

    public void make(int i) {
        parent[i] = i;
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    public boolean set_union(int i, int j) {
        if (find(i) == find(j)) {
            return false;
        } else {
            parent[j] = i;
            NumberOfDSU--;
            return true;
        }
    }

    public Set<Integer> get_roots() {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) {
                set.add(i);
            }
        }
        return set;
    }

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
