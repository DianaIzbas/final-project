package com.siit.finalproject.entity;

import lombok.Data;

import java.util.Arrays;

@Data
public class GeneratorMatrixEntity {

    int rows;
    int columns;
    int [][] generatorMatrix = new int[rows][columns];

    public void setGeneratorMatrix(int row, int column, int e) {
        this.generatorMatrix[row][column] = e;
    }

    public int getGeneratorMatrixElement(int row, int column) {
        return generatorMatrix[row][column];
    }
}
