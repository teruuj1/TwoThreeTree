package twothreetree;

public class TwoThreeTree {
    Node root;

    public TwoThreeTree(int k) {
        root = new Node(k);
    }

    public void search(int k) {
        root.search(root, k);
    }

    public void insert(int k) {
        root.insert(root, k);
    }

    public void remove(int k) {
        root.remove(root, k);
    }
}

