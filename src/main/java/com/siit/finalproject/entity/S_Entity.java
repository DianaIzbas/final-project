package com.siit.finalproject.entity;

import lombok.Data;

@Data
public class S_Entity {

    int rows;
    int columns;
    int [][] s_Matrix = new int[rows][columns];

    public void setS_Matrix(int row, int column, int e) {
        this.s_Matrix[row][column] = e;
    }

    public int getS_MatrixElement(int row, int column) {
        return s_Matrix[row][column];
    }
}

