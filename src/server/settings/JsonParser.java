package server.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Working with JSON settings files
 */
public class JsonParser {

    public String getMaterialStr(String material, String param) {
        String appPath = new File("").getAbsolutePath();
        JSONParser parser = new JSONParser();
        String res = "";

        try {
            Object obj = parser.parse(new FileReader(appPath+"/settings/materials.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jo = (JSONObject) jsonObject.get(material);
            res = (String) jo.get(param);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Integer getMaterialInt(String material, String param) {
        String appPath = new File("").getAbsolutePath();
        JSONParser parser = new JSONParser();
        Integer res = null;

        try {
            Object obj = parser.parse(new FileReader(appPath+"/settings/materials.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jo = (JSONObject) jsonObject.get(material);
            Long lRes = (Long) jo.get(param);
            res = lRes != null ? lRes.intValue() : null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

}
