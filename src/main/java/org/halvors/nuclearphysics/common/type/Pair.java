package org.halvors.nuclearphysics.common.type;

public class Pair<L, R> {
    private final L left;
    private final R right;

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

    @Override
    public int hashCode() {
        if (left != null && right != null) {
            return left.hashCode() ^ right.hashCode();
        }

        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Pair) {
            Pair pair = (Pair) object;

            return left.equals(pair.getLeft()) && right.equals(pair.getRight());
        }

        return false;
    }
}