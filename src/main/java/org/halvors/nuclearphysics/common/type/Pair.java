package org.halvors.nuclearphysics.common.type;

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

    @Override
    public int hashCode() {
        if (left != null || right != null) {
            return left.hashCode() ^ right.hashCode();
        }

        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair pair = (Pair) o;

            return left.equals(pair.getLeft()) && right.equals(pair.getRight());
        }

        return false;
    }
}