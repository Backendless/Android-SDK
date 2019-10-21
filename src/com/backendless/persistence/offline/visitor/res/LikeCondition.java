package com.backendless.persistence.offline.visitor.res;

public class LikeCondition implements Condition {
    private Field left;
    private boolean isLike;
    private Field right;

    public LikeCondition(Field left, boolean isLike, Field right) {
        this.left = left;
        this.isLike = isLike;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + (isLike ? " LIKE " : " NOT LIKE ") + right;

    }
}
