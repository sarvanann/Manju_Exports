package com.manju_exports.Model;

public class Model {
    private String name;
    private String mistake_piece;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMistake_piece() {
        return mistake_piece;
    }

    public void setMistake_piece(String mistake_piece) {
        this.mistake_piece = mistake_piece;
    }

    public Model(String name,String mistake_piece) {
        this.name = name;
        this.mistake_piece = mistake_piece;
    }
}
