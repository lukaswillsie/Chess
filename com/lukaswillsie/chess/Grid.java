package com.lukaswillsie.chess;

import com.lukaswillsie.chess.logic.Pair;

import java.lang.reflect.Array;

public class Grid<T> {
    private final T[][] grid;

    public Grid (Class<T> clazz, int x, int y) {
        grid = (T[][]) Array.newInstance(clazz, x, y);
    }

    public T get(Pair pair) {
        return grid[pair.first()][pair.second()];
    }

    public T set(Pair pair, T newVal) {
        return grid[pair.first()][pair.second()] = newVal;
    }
}
