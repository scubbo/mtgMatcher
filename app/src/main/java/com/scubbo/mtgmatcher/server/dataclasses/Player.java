package com.scubbo.mtgmatcher.server.dataclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class Player {
    private final String mDciNumber;
    private final String mName;

    public Player(String dciNumber, String name) {
        mDciNumber = dciNumber;
        mName = name;
    }

    public Player(JSONObject jsonObject) throws JSONException {
        mDciNumber = (String) jsonObject.get("dciNumber");
        mName = (String) jsonObject.get("name");
    }

    public Player(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public String getDciNumber() {
        return mDciNumber;
    }

    public String getName() {
        return mName;
    }
}
