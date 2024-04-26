package me.gopro336.zenith.api.config;

import com.google.gson.*;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.FeatureManager;
import me.gopro336.zenith.feature.hudElement.Element;
import me.gopro336.zenith.property.Property;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Gopro336
 * @since 5/12/21
 * Todo: remove paste
 */
public class ConfigManagerJSON {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createDirectory() throws IOException {
        if (!Files.exists(Paths.get("zenith/")))
            Files.createDirectories(Paths.get("zenith/"));

        if (!Files.exists(Paths.get("zenith/features")))
            Files.createDirectories(Paths.get("zenith/features"));

        if (!Files.exists(Paths.get("zenith/gui")))
            Files.createDirectories(Paths.get("zenith/gui"));

        if (!Files.exists(Paths.get("zenith/social")))
            Files.createDirectories(Paths.get("zenith/social"));
    }

    public static void registerFiles(String name, String path) throws IOException {
        if (!Files.exists(Paths.get("zenith/" + path + "/" + name + ".json")))
            Files.createFile(Paths.get("zenith/" + path + "/" + name + ".json"));

        else {
            File file = new File("zenith/" + path + "/" + name + ".json");
            file.delete();
            Files.createFile(Paths.get("zenith/" + path + "/" + name + ".json"));
        }
    }

    public static void saveConfig() {
        try {
            saveModules();
            saveHUD();
        } catch (IOException ignored) {

        }
    }

    public static void loadConfig() {
        try {
            createDirectory();
            loadModules();
            loadHUD();
        } catch (IOException ignored) {

        }
    }

    public static void saveModules() throws IOException {
        for (Feature feature : FeatureManager.getModulesStatic()) {
            registerFiles(feature.getName(), "features");
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("zenith/features/" + feature.getName() + ".json"), StandardCharsets.UTF_8);

            JsonObject featureObject = new JsonObject();
            JsonObject propertyObject = new JsonObject();
            JsonObject subPropertyObject = new JsonObject();

            featureObject.add("Name", new JsonPrimitive(feature.getName()));
            featureObject.add("Enabled", new JsonPrimitive(feature.isEnabled()));
            featureObject.add("Bind", new JsonPrimitive(feature.getKey()));

            for (Property<?> property : FeatureManager.getOldPropertiesFromFeature(feature)) {
                if (property.getValue() instanceof Boolean) {
                    propertyObject.add((property).getName(), new JsonPrimitive((Boolean) (property).getValue()));
                }

                if (property.getValue() instanceof Number) {
                    propertyObject.add((property).getName(), new JsonPrimitive((Number) (property).getValue()));
                }

                if (property.getValue() instanceof Enum) {
                    propertyObject.add((property).getName(), new JsonPrimitive(String.valueOf(property.getValue())));
                }
            }

            propertyObject.add("SubProperties", subPropertyObject);
            featureObject.add("Properties", propertyObject);
            String jsonString = gson.toJson(new JsonParser().parse(featureObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public static void loadModules() {
        for (Feature feature : FeatureManager.getModulesStatic()) {
            if (!Files.exists(Paths.get("zenith/features/" + feature.getName() + ".json")))
                continue;

            JsonObject featureObject = null;

            try {
                InputStream inputStream = Files.newInputStream(Paths.get("zenith/features/" + feature.getName() + ".json"));
                featureObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
            } catch (Exception ignored) {

            }

            assert featureObject != null;
            if (featureObject.get("Name") == null || featureObject.get("Enabled") == null || featureObject.get("Bind") == null)
                continue;

            JsonObject propertyObject = featureObject.get("Properties").getAsJsonObject();
            JsonObject subPropertyObject = propertyObject.get("SubProperties").getAsJsonObject();

            for (Property property : FeatureManager.getOldPropertiesFromFeature(feature)) {
                JsonElement propertyValueObject = null;

                if (property.getValue() instanceof Boolean) {
                    propertyValueObject = propertyObject.get(property.getName());

                }

                if (property.getValue() instanceof Number) {
                    propertyValueObject = propertyObject.get(property.getName());

                }

                if (property.getValue() instanceof Enum) {
                    propertyValueObject = propertyObject.get(property.getName());

                }

                if (propertyValueObject != null) {
                    if (property.getValue() instanceof Boolean)
                        property.setValue(propertyValueObject.getAsBoolean());

                    if (property.getValue() instanceof Number)
                        property.setValue(propertyValueObject.getAsNumber());

                    if (property.getValue() instanceof Enum)
                        property.setValue(propertyValueObject.getAsInt());
                }
            }

            feature.setEnabled(featureObject.get("Enabled").getAsBoolean());
            feature.setKey(featureObject.get("Bind").getAsInt());
        }
    }

    public static void saveHUD() throws IOException {
        registerFiles("HUD", "gui");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("zenith/gui/HUD.json"), StandardCharsets.UTF_8);
        JsonObject guiObject = new JsonObject();
        JsonObject hudObject = new JsonObject();

        for (Element component : FeatureManager.getHudElements()) {
            JsonObject positionObject = new JsonObject();

            positionObject.add("x", new JsonPrimitive(component.x));
            positionObject.add("y", new JsonPrimitive(component.y));
            positionObject.add("enabled", new JsonPrimitive(component.isEnabled()));

            hudObject.add(component.getName(), positionObject);
        }

        guiObject.add("Components", hudObject);
        String jsonString = gson.toJson(new JsonParser().parse(guiObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadHUD() throws IOException {
        if (!Files.exists(Paths.get("zenith/gui/HUD.json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("zenith/gui/HUD.json"));
        JsonObject guiObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (guiObject.get("Components") == null)
            return;

        JsonObject windowObject = guiObject.get("Components").getAsJsonObject();
        for (Element component : FeatureManager.getHudElements()) {
            if (windowObject.get(component.getName()) == null)
                return;

            JsonObject categoryObject = windowObject.get(component.getName()).getAsJsonObject();

            JsonElement hudXObject = categoryObject.get("x");
            if (hudXObject != null && hudXObject.isJsonPrimitive())
                component.x = hudXObject.getAsInt();

            JsonElement hudYObject = categoryObject.get("y");
            if (hudYObject != null && hudYObject.isJsonPrimitive())
                component.y = hudYObject.getAsInt();

            JsonElement hudEnabledObject = categoryObject.get("enabled");
            if (hudEnabledObject != null && hudEnabledObject.isJsonPrimitive())
                if (hudEnabledObject.getAsBoolean())
                    component.toggle();
        }

        inputStream.close();
    }

}