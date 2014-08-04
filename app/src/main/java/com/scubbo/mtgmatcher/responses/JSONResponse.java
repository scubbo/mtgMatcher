package com.scubbo.mtgmatcher.responses;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponse {

    private JSONObject mJsonObject;
    private boolean mIsSuccess;

    public JSONResponse(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public JSONResponse(JSONObject jsonObject) {
        if (!(jsonObject.has("status"))) {
            throw new JSONMarshallingException("JSONObject " + jsonObject + " has no status");
        }

        Object status;
        try {
            status = jsonObject.get("status");
        } catch (JSONException e) {
            throw new JSONMarshallingException("JSONException while fetching status from JSONObject " + jsonObject, e);
        }

        if (!(status instanceof String)) {
            throw new JSONMarshallingException("JSONObject " + jsonObject + " has a non-string status");
        }

        String stringStatus = (String) status;
        if (!(POSSIBLE_STATUS.contains(stringStatus))) {
            throw new JSONMarshallingException("JSONObject " + jsonObject + " contains illegal status: " + status);
        } else {
            this.mIsSuccess = stringStatus.equals(POSSIBLE_STATUS.SUCCESS.toString());
        }

        this.mJsonObject = jsonObject;
    }

    public boolean isSuccess() {return mIsSuccess;}

    public enum POSSIBLE_STATUS {
        SUCCESS,
        FAILURE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        public static boolean contains(String s) {
            for (POSSIBLE_STATUS status : values()) {
                if (status.toString().equals(s)) {return true;}
            }
            return false;
        }
    }
}
