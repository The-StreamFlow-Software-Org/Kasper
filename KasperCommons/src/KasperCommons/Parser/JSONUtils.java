package KasperCommons.Parser;

import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.KasperString;
import com.google.gson.stream.JsonWriter;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Map;

public class JSONUtils {

    public static String objectToJsonStream(Object obj) {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);

            writeJson(obj, jsonWriter);

            jsonWriter.close();
            return stringWriter.toString();
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }

    private static void writeJson(Object obj, JsonWriter jsonWriter) throws Exception {
        if (obj instanceof KasperMap map) {
            writeMap(map.toMap(), jsonWriter);
        } else if (obj instanceof KasperString str) {
            jsonWriter.value(str.toStr());
        } else if (obj instanceof KasperList list) {
            writeArray(list.toList(), jsonWriter);
        }
    }

    private static void writeMap(Map<?, ?> map, JsonWriter jsonWriter) throws Exception {
        jsonWriter.beginObject();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            jsonWriter.name(entry.getKey().toString());
            writeJson(entry.getValue(), jsonWriter);
        }
        jsonWriter.endObject();
    }

    private static void writeArray(LinkedList<KasperObject> array, JsonWriter jsonWriter) throws Exception {
        jsonWriter.beginArray();
        for (Object item : array) {
            writeJson(item, jsonWriter);
        }
        jsonWriter.endArray();
    }
}