package com.github.kmpk.democrud.model;

public interface HasId {
    Long getId();

    default boolean isNew() {
        return getId() == null;
    }
}
