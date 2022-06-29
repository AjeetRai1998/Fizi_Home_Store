package digi.coders.capsicostorepartner.model;

import com.google.gson.JsonArray;

import org.json.JSONArray;

public class ProductMenuModel {
    String id,name,isStatus;
    JSONArray jsonArray;

    public ProductMenuModel(String id, String name,String isStatus, JSONArray jsonArray) {
        this.id = id;
        this.name = name;
        this.isStatus = isStatus;
        this.jsonArray = jsonArray;
    }


    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
