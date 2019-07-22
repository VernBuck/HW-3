import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Your implementation of a Binary Search Tree.
 *
 * @author Vernon Buck
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> implements BSTInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty BST.
     * YOU DO NOT NEED TO IMPLEMENT THIS CONSTRUCTOR!
     */
    public BST() {
    }

    /**
     * Initializes the BST with the data in the Collection. The data in the BST
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("null data passed");
        }
        for (T x: data) {
            add(x);
        }
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("null data passed");
        }
        if (root == null) {
            root = new BSTNode<T>(data);
            size++;
        } else {
            addHelper(data, root);
        }
    }

    /**
     * Helper method used for recursive calls in regards to the add method
     * @param data data of node to be created
     * @param current points to the root and later node pointers passed
     *
     */
    private void addHelper(T data, BSTNode<T> current) {
        if (current.getData().compareTo(data) < 0) {
            if (current.getRight() != null) {
                addHelper(data, current.getRight());
            } else {
                current.setRight(new BSTNode<>(data));
                size++;
            }
        } else {
            if (current.getLeft() != null) {
                addHelper(data, current.getLeft());
            } else {
                current.setLeft(new BSTNode<>(data));
                size++;
            }
        }
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("invalid data was passed");
        }
        size--;
        return removeHelper(data, root, root);
    }

    /**
     * Method removes passed data
     * @param data to be compared to
     * @param current node to be removed
     * @param preCurrent parent to removed node
     * @return data contained by the node to be removed
     */
    private T removeHelper(T data, BSTNode<T> current, BSTNode<T> preCurrent) {
        if (current == null) {
            throw new NoSuchElementException("element not present");
        }
        if (root.getData() == data) {
            if (root.getLeft() != null) {
                T temp = root.getData();
                root.setData(rootRem(root, root.getLeft()));
                return temp;
            } else if (root.getRight() != null) {
                T temp = root.getData();
                root.setData(rootRem(root, root.getRight()));
                return temp;
            }
            T temp = root.getData();
            root = null;
            return temp;
        }
        if (current.getData() == data) {
            if (current.getLeft() == null && current.getRight() == null) {
                if (preCurrent.getLeft() == current) {
                    T temp = current.getData();
                    preCurrent.setLeft(current.getLeft());
                    return temp;
                } else {
                    T temp = current.getData();
                    preCurrent.setRight(current.getRight());
                    return temp;
                }
            } else if (current.getLeft() != null
                    && current.getRight() == null) {
                T temp = current.getData();
                preCurrent.setLeft(current.getLeft());
                return temp;
            } else if (current.getLeft() == null
                    && current.getRight() != null) {
                T temp = current.getData();
                preCurrent.setRight(current.getRight());
                return temp;
            } else {
                T temp = current.getData();
                preCurrent.getLeft().setData(
                        predecessorHelper(current, current.getLeft()));
                return temp;
            }
        } else {
            if (current.getData().compareTo(data) < 0) {
                return removeHelper(data, current.getRight(), current);
            } else {
                return removeHelper(data, current.getLeft(), current);
            }
        }
    }

    /**
     * Finds the next Largest value smaller than the given one
     * @param current parent of the node used to find next largest
     * @param travel node of the next largest value to removed node
     * @return value of the next largest node
     */
    private T predecessorHelper(BSTNode<T> current, BSTNode<T> travel) {
        if (travel.getLeft() == null && travel.getRight() == null) {
            T temp = travel.getData();
            current.setRight(travel.getRight());
            return temp;
        } else if (travel.getLeft() != null && travel.getRight() == null) {
            T temp = travel.getData();
            current.setLeft(travel.getLeft());
            return temp;

        } else {
            return predecessorHelper(travel, travel.getRight());
        }
    }

    /**
     * Removes the root of the bst
     * @param current root
     * @param other value to left or right of root
     * @return value to replace the root
     */
    private T rootRem(BSTNode<T> current, BSTNode<T> other) {
        if (other.getLeft() == null && other.getRight() == null) {
            T temp = other.getData();
            current.setLeft(other.getLeft());
            return temp;
        } else if (other.getLeft() != null && other.getRight() == null) {
            T temp = current.getData();
            current.setLeft(current.getLeft());
            return temp;

        } else if (other.getLeft() == null && other.getRight() != null) {
            T temp = other.getData();
            current.setRight(other.getRight());
            return temp;
        } else {
            return rootRem(other, other.getRight());
        }
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("null data passed");
        }
        return getHelper(data, root);
    }

    /**
     * @param data value used to compare to data of other nodes
     * @param current node used to traverse the tree
     * @return data of the node that matches the data being searched
     */
    private T getHelper(T data, BSTNode<T> current) {
        if (current == null) {
            throw new NoSuchElementException("Element not found");
        }
        if (current.getData() == data) { //base case
            return current.getData();
        }
        if (current.getData().compareTo(data) < 0) {
            return getHelper(data, current.getRight());
        } else {
            return getHelper(data, current.getLeft());
        }
    }

    @Override
    public boolean contains(T data) {
        try {
            get(data);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    @Override
    public T nextLargest(T data) {
        if (data == null) {
            throw new IllegalArgumentException("null data passed");
        }
        if (data == getMax()) {
            return null;
        }
        return nextHelper(data, root);
    }

    /**
     * @param data data of the node passed for comparison
     * @param current node used to traverse through the tree
     * @return data of the successor
     */
    private T nextHelper(T data, BSTNode<T> current) {
        if (current == null) {
            throw new NoSuchElementException("Data not there");
        }
        if (current.getData() == data) {
            if (current.getRight() != null) {
                return successorHelp(current.getRight());
            } else {
                return levelsuccessorHelp(root, current).getData();
            }
        } else {
            if (current.getData().compareTo(data) < 0) {
                return nextHelper(data, current.getRight());
            } else {
                return nextHelper(data, current.getLeft());
            }
        }
    }

    /**
     * @param current node used to progress to the left
     * @return leftmost node can also be obtained by calling the min
     */
    private T successorHelp(BSTNode<T> current) {
        if (current.getLeft() == null) {
            return current.getData();
        }
        return successorHelp(current.getLeft());
    }

    /**
     * @param current node to be returned
     * @param oth node to find successor of
     * @return the successor
     */
    private BSTNode<T> levelsuccessorHelp(BSTNode<T> current, BSTNode<T> oth) {
        if (current.getLeft().getData().compareTo(oth.getData()) == 0) {
            return current;
        }
        current = levelsuccessorHelp(current.getLeft(), oth);
        return current;
    }

    @Override
    public String toString() {
        return helpString(root);
    }

    /**
     * Tree to string
     * @param current node used to recurse through through
     * @return string version of tree
     */
    private String helpString(BSTNode<T> current) {
        if (current != null) {
            return "{" + current.getData() + ", "
                    + helpString(current.getLeft())
                    + ", "
                    + helpString(current.getRight()) + "}";
        } else {
            return "{}";
        }
    }

    @Override
    public T getMax() {
        return maxHelper(root);
    }

    /**
     * @param current node to be compared
     * @return maximum value
     */
    private T maxHelper(BSTNode<T> current) {
        if (root == null) {
            return null;
        }
        if (current.getRight() == null) {
            return current.getData();
        }
        if (current != null) {
            return maxHelper(current.getRight());
        }
        return null;
    }

    @Override
    public T getMin() {
        return minHelper(root);
    }

    /**
     * @param current node used to traverse tree
     * @return lowest value possible
     */
    private T minHelper(BSTNode<T> current) {
        if (root == null) {
            return null;
        }
        if (current.getLeft() == null) {
            return current.getData();
        }
        if (current != null) {
            return minHelper(current.getLeft());
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override

    public List<T> preorder() {
        List<T> list = new ArrayList<>();
        return preHelper(list, root);
    }

    /**
     *
     * @param list list to put data into
     * @param current node pointer to use to move through tree
     * @return list with all of the data
     */
    private List<T> preHelper(List<T> list, BSTNode<T> current) {
        if (current != null) {
            list.add(current.getData());
            preHelper(list, current.getLeft());
            preHelper(list, current.getRight());
        }
        return list;
    }

    @Override
    public List<T> postorder() {
        List<T> list = new ArrayList<>();
        return postHelper(list, root);
    }

    /**
     *
     * @param list list to put data into
     * @param current node pointer to use to move through tree
     * @return list with all of the data
     */
    private List<T> postHelper(List<T> list, BSTNode<T> current) {
        if (current != null) {
            postHelper(list, current.getLeft());
            postHelper(list, current.getRight());
            list.add(current.getData());
        }
        return list;
    }

    @Override
    public List<T> inorder() {
        List<T> list = new ArrayList<>();
        return inHelper(list, root);
    }

    /**
     *
     * @param list list to put data into
     * @param current node pointer to use to move through tree
     * @return list with all of the data
     */
    private List<T> inHelper(List<T> list, BSTNode<T> current) {
        if (current != null) {
            inHelper(list, current.getLeft());
            list.add(current.getData());
            inHelper(list, current.getRight());
        }
        return list;
    }

    @Override
    public List<T> levelorder() {
        List<T> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        return lvHelper(root, list);
    }

    /**
     * Creats a list with nodes by level
     * @param current node used to traverse through the list
     * @param list nodes that are passed om
     * @return the complete list
     */
    private List<T> lvHelper(BSTNode<T> current, List<T> list) {
        Queue<BSTNode<T>>  q = new LinkedList<>();
        q.add(current);
        while (!(q.isEmpty())) {
            current = q.remove();
            if (current.getLeft() != null) {
                q.add(current.getLeft());
            }
            if (current.getRight() != null) {
                q.add(current.getRight());
            }
            list.add(current.getData());
        }
        return list;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int height() {
        return heightHelp(root);
    }

    /**
     *
     * @param current node used to traverse tree
     * @return returns height value
     */
    private int heightHelp(BSTNode<T> current) {
        if (current != null) {
            return Math.max(heightHelp(current.getLeft()),
                    heightHelp(current.getRight())) + 1;
        } else {
            return -1;
        }
    }

    @Override
    public BSTNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}
