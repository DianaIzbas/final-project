package com.siit.finalproject.entity;

import lombok.Data;

@Data
public class P_Entity {

    int rows;
    int columns;
    int [][] p_Matrix = new int[rows][columns];

    public void setP_Matrix(int row, int column, int e) {
        this.p_Matrix[row][column] = e;
    }

    public int getP_MatrixElement(int row, int column) {
        return p_Matrix[row][column];
    }
}

