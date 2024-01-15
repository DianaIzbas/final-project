package com.siit.finalproject.entity;

import lombok.Data;

@Data
public class EncryptMessageEntity {

    int length;
    int [] encryptMessage = new int[length];

    public void setEncryptMessage(int length, int e) {
        this.encryptMessage[length] = e;
    }

    public int getEncryptMessageElement(int row) {
        return encryptMessage[row];
    }
}
