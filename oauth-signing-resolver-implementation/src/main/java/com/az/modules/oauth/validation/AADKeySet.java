package com.az.modules.oauth.validation;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AADKeySet {

    @JsonAlias("keys")
    private ArrayList<JsonWebKey> keys = new ArrayList<>();

    public ArrayList<JsonWebKey> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<JsonWebKey> keys) {
        this.keys = keys;
    }
}


