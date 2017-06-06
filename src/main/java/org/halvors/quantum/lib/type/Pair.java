package org.halvors.quantum.lib.type;

/** Container for two objects
 *
 * @author Robert Seifert */
public class Pair<L, R> {
    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public void setLeft(L l) {
        left = l;
    }

    public void setRight(R r) {
        right = r;
    }

    @Override
    public int hashCode() {
        if (left == null || right == null) {
            super.hashCode();
        }

        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Pair)) {
            return false;
        }

        Pair pairObject = (Pair) o;

        return left.equals(pairObject.getLeft()) && right.equals(pairObject.getRight());
    }

}