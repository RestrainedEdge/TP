package data;

public class RBTNode<K, V> {
    // Key
    protected K key;
    // Value
    protected V value;
    // Color of node [Red/Black]
    protected Color color;
    // Parent
    protected RBTNode<K, V> parent;
    // Left child
    protected RBTNode<K, V> left;
    // Right child
    protected RBTNode<K, V> right;

    protected RBTNode(K key, V value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public String toString() {
        String s = color == Color.BLACK ? "B-" : "R-";
        s += value;
        return s;
    }
}