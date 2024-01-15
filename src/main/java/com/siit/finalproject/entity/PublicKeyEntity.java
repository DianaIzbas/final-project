package com.siit.finalproject.entity;

import lombok.Data;

@Data
public class PublicKeyEntity {

    int rows;
    int columns;
    int [][] publicKey = new int[rows][columns];

    public void setPublicKey(int row, int column, int e) {
        this.publicKey[row][column] = e;
    }

    public int getPublicKeyElement(int row, int column) {
        return publicKey[row][column];
    }
}
