package com.scubbo.mtgmatcher.server;

import com.scubbo.mtgmatcher.server.dataclasses.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPlayersJSONResponse extends JSONResponse {
    public final List<Player> mPlayerList;

    public GetPlayersJSONResponse(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public GetPlayersJSONResponse(JSONObject jsonObject) {
        super(jsonObject);
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonObject.get("data");
        } catch (JSONException e) {
            //Shouldn't happen - already checked in validate
            e.printStackTrace();
            throw new JSONMarshallingException(e);
        }
        List<Player> playerList = new ArrayList<Player>();
        for (int i = 0; i<jsonArray.length(); i++) {
            try {
                playerList.add(new Player((JSONObject) jsonArray.get(i)));
            } catch (JSONException e) {
                //Shouldn't happen - checked by validate
                e.printStackTrace();
                throw new JSONMarshallingException(e);
            }
        }
        mPlayerList = playerList;
    }

    @Override
    public void validate(JSONObject jsonObject) {
        try {
            Object jsonArray = jsonObject.get("data");
            if (!(jsonArray instanceof JSONArray)) {
                throw new JSONMarshallingException("jsonObject's \"data\" attribute is not an array");
            }
            for (Integer i = 0; i<((JSONArray) jsonArray).length(); i++) {
                final JSONObject o = (JSONObject) ((JSONArray) jsonArray).get(0);
                Player player = new Player(o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONMarshallingException(e);
        }
        super.validate(jsonObject);
    }

    public List<Player> getPlayerList() {
        return mPlayerList;
    }
}
