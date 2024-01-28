package com.llav3ji2019.structure;

import java.util.Objects;

public record Coordinates(int xCord, int yCord) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return xCord == that.xCord && yCord == that.yCord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCord, yCord);
    }
}
