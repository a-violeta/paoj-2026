package com.pao.project.bank.model;

public final class ImmutableIdentifier {

    private final String value;

    public ImmutableIdentifier(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableIdentifier that = (ImmutableIdentifier) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
