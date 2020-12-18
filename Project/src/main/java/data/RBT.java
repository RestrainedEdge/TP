package data;

import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

public class RBT<K extends Comparable, V> implements Iterable<V> {

    private static final String[] term = {"\u2500", "\u2534", "\u252C", "\u253C"};
    private static final String rightEdge = "\u250C";
    private static final String leftEdge = "\u2514";
    private static final String endEdge = "\u25CF";
    private static final String vertical = "\u2502  ";
    private String horizontal;
    public String toVisualizedString(String dataCodeDelimiter) {
        horizontal = term[0] + term[0];
        return root == null ? ">" + horizontal
                : toTreeDraw(root, ">", "", dataCodeDelimiter);
    }
    private String toTreeDraw(RBTNode<K, V> node, String edge, String indent, String dataCodeDelimiter) {
        if (node == null) {
            return "";
        }
        String step = (edge.equals(leftEdge)) ? vertical : "   ";
        StringBuilder sb = new StringBuilder();
        sb.append(toTreeDraw(node.right, rightEdge, indent + step, dataCodeDelimiter));
        int t = (node.right != null) ? 1 : 0;
        t = (node.left != null) ? t + 2 : t;
        sb.append(indent).append(edge).append(horizontal).append(term[t]).append(endEdge)
                .append(split(node.toString(), dataCodeDelimiter)).append(System.lineSeparator());
        step = (edge.equals(rightEdge)) ? vertical : "   ";
        sb.append(toTreeDraw(node.left, leftEdge, indent + step, dataCodeDelimiter));
        return sb.toString();
    }
    private String split(String s, String dataCodeDelimiter) {
        int k = s.indexOf(dataCodeDelimiter);
        if (k <= 0) {
            return s;
        }
        return s.substring(0, k);
    }

    private RBTNode<K, V> root;
    public RBT() {
        root = null;
    }
    private RBTNode<K, V> GetParent(RBTNode<K, V> node) {
        return node == null ? null : node.parent;
    }
    private RBTNode<K, V> GetGrandParent(RBTNode<K, V> node) {
        return GetParent(GetParent(node));
    }
    private RBTNode<K, V> GetSibling(RBTNode<K, V> node) {
        RBTNode<K, V> parent = GetParent(node);
        if (parent == null) {
            return null;
        }
        if(node == parent.left) {
            return parent.right;
        }
        else {
            return parent.left;
        }
    }
    private RBTNode<K, V> GetUncle(RBTNode<K, V> node) {
        RBTNode<K, V> parent = GetParent(node);
        if(parent == null)
            return null;
        return GetSibling(parent);
    }
    private Color GetColor(RBTNode<K, V> node) {
        if(node == null)
            return Color.BLACK;
        return node.color;
    }
    private void RotateLeft(RBTNode<K, V> node) {
        RBTNode<K, V> temp = node.right;
        RBTNode<K, V> parent = GetParent(node);

        node.right = temp.left;
        temp.left = node;
        node.parent = temp;

        if(node.right != null) {
            node.right.parent = node;
        }
        if(parent != null) {
            if(node == parent.left) {
                parent.left = temp;
            }
            else if(node == parent.right) {
                parent.right = temp;
            }
        }
        else {
            root = temp;
        }
        temp.parent = parent;

    }
    private void RotateRight(RBTNode<K, V> node) {
        RBTNode<K, V> temp = node.left;
        RBTNode<K, V> parent = GetParent(node);

        node.left = temp.right;
        temp.right = node;
        node.parent = temp;

        if(node.left != null) {
            node.left.parent = node;
        }
        if(parent != null) {
            if(node == parent.left) {
                parent.left = temp;
            }
            else if(node == parent.right) {
                parent.right = temp;
            }
        }
        else {
            root = temp;
        }
        temp.parent = parent;
    }
    public void Add(K key, V value) {
        RBTNode<K, V> node = new RBTNode<K, V>(key, value);
        InsertRecursive(root, node);
        InsertRepairTree(node);
        root = node;
        while(GetParent(root) != null) {
            root = GetParent(root);
        }
    }
    private void InsertRecursive(RBTNode<K, V> root, RBTNode<K, V> node) {
        if(root != null) {
            if(node.key.compareTo(root.key) < 0) {
                if(root.left != null) {
                    InsertRecursive(root.left, node);
                    return;
                }
                else {
                    root.left = node;
                }
            }
            else {
                if(root.right != null) {
                    InsertRecursive(root.right, node);
                    return;
                }
                else {
                    root.right = node;
                }
            }
        }
        node.parent = root;
        node.left = null;
        node.right = null;
        node.color = Color.RED;
    }
    private void InsertRepairTree(RBTNode<K, V> node) {
        if(GetParent(node) == null) {
            InsertCase1(node);
        }
        else if(GetParent(node).color == Color.BLACK) {
            InsertCase2(node);
        }
        else if(GetUncle(node) != null && GetUncle(node).color == Color.RED) {
            InsertCase3(node);
        }
        else {
            InsertCase4(node);
        }
    }
    private void InsertCase1(RBTNode<K, V> node) {
        node.color = Color.BLACK;
    }
    private void InsertCase2(RBTNode<K, V> node) {
        return;
    }
    private void InsertCase3(RBTNode<K, V> node) {
        GetParent(node).color = Color.BLACK;
        GetUncle(node).color = Color.BLACK;
        GetGrandParent(node).color = Color.RED;
        InsertRepairTree(GetGrandParent(node));
    }
    private void InsertCase4(RBTNode<K, V> node) {
        RBTNode<K, V> parent = GetParent(node);
        RBTNode<K, V> grandparent = GetGrandParent(node);

        if(node == parent.right && parent == grandparent.left) {
            RotateLeft(parent);
            node = node.left;
        }
        else if(node == parent.left && parent == grandparent.right) {
            RotateRight(parent);
            node = node.right;
        }
        InsertCase4Step2(node);
    }
    private void InsertCase4Step2(RBTNode<K, V> node) {
        RBTNode<K, V> parent = GetParent(node);
        RBTNode<K, V> grandparent = GetGrandParent(node);

        if(node == parent.left) {
            RotateRight(grandparent);
        }
        else {
            RotateLeft(grandparent);
        }
        parent.color = Color.BLACK;
        grandparent.color = Color.RED;
    }
    private RBTNode<K, V> FindNode(K key) {
        RBTNode<K, V> node = root;
        while(node != null) {
            if(node.key.compareTo(key) < 0) {
                node = node.right;
            }
            else if (node.key.compareTo(key) > 0) {
                node = node.left;
            }
            else {
                return node;
            }
        }
        return null;
    }
    public boolean IsBalanced() {
        if(root == null)
            return true;
        if(Height(root.left) == Height(root.right))
            return true;
        return false;
    }
    private int Height(RBTNode<K, V> node) {
        if(node == null) {
            return 0;
        }
        int a = node.color == Color.BLACK ? 1 : 0;
        return Math.max(Height(node.left), Height(node.right)) + a;
    }
    public V Get(K key) {
        return FindNode(key).value;
    }
    public void Remove(K key) {
        RBTNode<K, V> node = FindNode(key);
        Remove(node);
    }
    private void Remove(RBTNode<K, V> node) {
        // Remove(K key) had correct key i.e. Node Exists
        if(node != null) {
            RBTNode<K, V> parent = GetParent(node);
            // Node IS NOT root
            if(parent != null) {
                // Node has 2 children
                if (node.left != null && node.right != null) {
                    RBTNode<K, V> max = FindMaximum(node.left);

                    node.key = max.key;
                    node.value = max.value;

                    Remove(max);
                }
                // Node IS Leaf
                else if (node.left == null && node.right == null) {
                    // Node is red. It has no weight, so deleting it DOES NOT need balancing
                    if(node.color == Color.RED) {
                        if(parent.left == node) {
                            parent.left = null;
                        }
                        else {
                            parent.right = null;
                        }
                    }
                    // Node is BLACK, so it will need RE-balancing
                    else {
                        System.out.println(node);

                        DeleteCase1(node);

                        if(node.parent.left == node) {
                            node.parent.left = null;
                        }
                        else {
                            node.parent.right = null;
                        }


                    }
                }
                // Node has 1 child
                //
                // The only possible subtree
                //
                //      ?
                //      |
                //      ■ B
                //      |
                //      □ R
                //     / \
                //    n   n
                //
                else {
                    // Replacing data to node and deleting copy
                    if(node.left != null) {
                        node.value = node.left.value;
                        node.key = node.left.key;
                        node.left = null;
                    }
                    else {
                        node.value = node.right.value;
                        node.key = node.right.key;
                        node.right = null;
                    }
                }
            }
            // Node IS root
            else {
                // Node has 2 children
                if (node.left != null && node.right != null) {
                    RBTNode<K, V> max = FindMaximum(node.left);

                    node.key = max.key;
                    node.value = max.value;

                    Remove(max);
                }
                // Root IS Leaf
                else if (node.left == null && node.right == null) {
                    root = null;
                }
                // Root has 1 child
                else {
                    root = node.left != null ? node.left : node.right;
                    root.parent = null;
                    root.color = Color.BLACK;
                }
            }
        }
    }
    private RBTNode<K, V> FindMaximum(RBTNode<K, V> node) {
        RBTNode<K, V> temp = node;
        while(temp.right != null) {
            temp = temp.right;
        }
        return temp;
    }

    private void DeleteCase1(RBTNode<K, V> node) {
        if(GetParent(node) != null) {
            DeleteCase2(node);
        }
    }
    private void DeleteCase2(RBTNode<K, V> node) {
        RBTNode<K, V> sibling = GetSibling(node);
        if(GetColor(sibling) == Color.RED) {
            node.parent.color = Color.RED;
            sibling.color = Color.BLACK;
            if(node == node.parent.left) {
                RotateLeft(node.parent);
            }
            else {
                RotateRight(node.parent);
            }
        }
        DeleteCase3(node);
    }
    private void DeleteCase3(RBTNode<K, V> node) {
        RBTNode<K, V> sibling = GetSibling(node);
        if(GetColor(GetParent(node)) == Color.BLACK &&
            GetColor(sibling) == Color.BLACK &&
            GetColor(sibling.left) == Color.BLACK &&
            GetColor(sibling.right) == Color.BLACK) {
            sibling.color = Color.RED;
            DeleteCase1(node.parent);
        }
        else {
            DeleteCase4(node);
        }
    }
    private void DeleteCase4(RBTNode<K, V> node) {
        RBTNode<K, V> sibling = GetSibling(node);
        if(GetColor(GetParent(node)) == Color.RED &&
            GetColor(sibling) == Color.BLACK &&
            GetColor(sibling.left) == Color.BLACK &&
            GetColor(sibling.right) == Color.BLACK) {
            sibling.color = Color.RED;
            node.parent.color = Color.BLACK;
        }
        else {
            DeleteCase5(node);
        }
    }
    private void DeleteCase5(RBTNode<K, V> node) {
        RBTNode<K, V> sibling = GetSibling(node);
        if(GetColor(sibling) == Color.BLACK) {
            if(node == node.parent.left &&
                GetColor(sibling.right) == Color.BLACK &&
                GetColor(sibling.left) == Color.RED) {
                sibling.color = Color.RED;
                sibling.left.color = Color.BLACK;
                RotateRight(sibling);
            }
            else if(node == node.parent.right &&
                GetColor(sibling.left) == Color.BLACK &&
                GetColor(sibling.right) == Color.RED) {
                sibling.color = Color.RED;
                sibling.right.color = Color.BLACK;
                RotateLeft(sibling);
            }
        }
        DeleteCase6(node);
    }
    private void DeleteCase6(RBTNode<K, V> node) {
        RBTNode<K, V> sibling = GetSibling(node);
        sibling.color = node.parent.color;
        node.parent.color = Color.BLACK;

        if(node == node.parent.left) {
            sibling.right.color = Color.BLACK;
            RotateLeft(node.parent);
        }
        else {
            sibling.left.color = Color.BLACK;
            RotateRight(node.parent);
        }
    }
    @Override
    public Iterator<V> iterator() {
        return new IteratorRBT();
    }
    private class IteratorRBT implements Iterator<V> {
        private Stack<RBTNode<K, V>> stack = new Stack<>();
        private RBTNode<K, V> parent = root;

        IteratorRBT() {
            this.toStack(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public V next() {
            if (!stack.empty()) {
                RBTNode<K, V> n = stack.pop();
                parent = (!stack.empty()) ? stack.peek() : root;
                RBTNode<K, V> node = n.right;
                toStack(node);
                return n.value;
            } else {
                return null;
            }
        }
        private void toStack(RBTNode<K, V> n) {
            while (n != null) {
                stack.push(n);
                n = n.left;
            }
        }
    }
}
