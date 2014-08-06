package com.scubbo.mtgmatcher.server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public abstract class JSONResponse {

    private boolean mIsSuccess;

    public JSONResponse(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public JSONResponse(JSONObject jsonObject) {
        validate(jsonObject);

        try {
            this.mIsSuccess = ((String) jsonObject.get("status")).equals(POSSIBLE_STATUS.SUCCESS.toString());
        } catch (JSONException e) {
            // should not happen, since we have checked this in validate
            e.printStackTrace();
            throw new JSONMarshallingException(e);
        }
    }

    public void validate(JSONObject jsonObject) {
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
        }
    }

    public boolean isSuccess() {return mIsSuccess;}

    public enum POSSIBLE_STATUS {
        SUCCESS,
        FAILURE;

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.getDefault());
        }

        public static boolean contains(String s) {
            for (POSSIBLE_STATUS status : values()) {
                if (status.toString().equals(s)) {return true;}
            }
            return false;
        }
    }
}
