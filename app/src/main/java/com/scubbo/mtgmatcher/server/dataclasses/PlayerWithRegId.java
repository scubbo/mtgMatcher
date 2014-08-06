package com.scubbo.mtgmatcher.server.dataclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerWithRegId extends Player {

    private final String mRegId;

    public PlayerWithRegId(String dciNumber, String name, String regId) {
        super(dciNumber, name);
        mRegId = regId;
    }

    public PlayerWithRegId(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        mRegId = (String) jsonObject.get("regId");
    }

    public PlayerWithRegId(String jsonString) throws JSONException {
        super(jsonString);
        mRegId = (String) (new JSONObject(jsonString)).get("regId");
    }

    public String getRegId() {
        return mRegId;
    }
}
