package KasperCommons.DataStructures;

import KasperCommons.Network.Timer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
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
        } else if (obj instanceof KasperReference ref) {
            jsonWriter.beginObject();
            jsonWriter.name("$reference");
            jsonWriter.value(ref.data.toString());
            jsonWriter.endObject();
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

    public static KasperObject parseJson(String jsonString) throws IOException {
        Timer t= new Timer();
        t.start();
        JsonReader reader = new JsonReader(new StringReader(jsonString));
        return readElement(reader, null);
    }

    private static KasperObject readElement(JsonReader reader, KasperObject parent) throws IOException {
        KasperObject element;

        switch (reader.peek()) {
            case BEGIN_ARRAY:
                element = readArray(reader, parent);
                break;

            case BEGIN_OBJECT:
                element = readMap(reader, parent);
                break;

            case STRING:
                String value = reader.nextString();
                element = new KasperString(value);
                element.setParent(parent); // Set the parent reference before object creation
                break;

            // Handle other cases like NUMBER, BOOLEAN, NULL

            default:
                throw new IllegalArgumentException("Unexpected JSON type: " + reader.peek());
        }

        return element;
    }

    private static KasperList readArray(JsonReader reader, KasperObject parent) throws IOException {
        KasperList list = new KasperList();
        list.setParent(parent);

        reader.beginArray();
        while (reader.hasNext()) {
            KasperObject element = readElement(reader, list);
            list.addToList(element);
        }
        reader.endArray();

        return list;
    }

    private static KasperMap readMap(JsonReader reader, KasperObject parent) throws IOException {
        KasperMap map = new KasperMap();
        map.setParent(parent);

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            KasperObject value = readElement(reader, map);
            map.put(name, value);
        }
        reader.endObject();

        return map;
    }


}