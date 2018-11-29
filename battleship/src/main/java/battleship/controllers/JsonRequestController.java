package battleship.controllers;

import net.sf.json.JSONObject;

public class JsonRequestController {

    public JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
