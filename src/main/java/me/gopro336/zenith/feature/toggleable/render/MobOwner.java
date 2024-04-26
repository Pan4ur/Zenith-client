package me.gopro336.zenith.feature.toggleable.render;

import java.util.Scanner;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.UUID;
import com.google.gson.JsonParser;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import net.minecraft.entity.Entity;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.entity.passive.AbstractHorse;
import java.util.List;

@AnnotationHelper(name = "MobOwner", description = "displays the owner of mobs", category = Category.RENDER)
public class MobOwner extends Feature {
    private List<AbstractHorse> mobs;
    private Map<String, String> uuidToName;

    public MobOwner() {
        this.mobs = new ArrayList<AbstractHorse>();
        this.uuidToName = new HashMap<String, String>();
    }

    @Override
    public void onUpdate() {
        if (MobOwner.mc.world == null) {
            return;
        }
        for (final Entity e : MobOwner.mc.world.loadedEntityList) {
            if (!(e instanceof AbstractHorse)) {
                continue;
            }
            final AbstractHorse horse = (AbstractHorse)e;
            if (this.mobs.contains(horse)) {
                continue;
            }
            this.mobs.add(horse);
            final UUID uuid = horse.getOwnerUniqueId();
            if (uuid == null) {
                horse.setCustomNameTag("Not tamed");
            }
            else {
                final String uuidString = uuid.toString().replace("-", "");
                String name = "";
                if (this.uuidToName.get(name) != null) {
                    name = this.uuidToName.get(name);
                }
                else {
                    try {
                        final String s = requestName(uuidString);
                        final JsonElement element = new JsonParser().parse(s);
                        final JsonArray array = element.getAsJsonArray();
                        if (array.size() == 0) {
                            //Command.sendChatMessage("Couldn't find player name. (1)");
                            continue;
                        }
                        name = array.get(array.size() - 1).getAsJsonObject().get("name").getAsString();
                        this.uuidToName.put(uuidString, name);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        //Command.sendChatMessage("Couldn't find player name. (2)");
                        continue;
                    }
                }
                horse.setCustomNameTag("Owner: " + name);
            }
        }
    }

    private static String requestName(final String uuid) {
        try {
            final String query = "https://api.mojang.com/user/profiles/" + uuid + "/names";
            final URL url = new URL(query);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            final InputStream in = new BufferedInputStream(conn.getInputStream());
            final String res = convertStreamToString(in);
            in.close();
            conn.disconnect();
            return res;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertStreamToString(final InputStream is) {
        final Scanner s = new Scanner(is).useDelimiter("\\A");
        final String r = s.hasNext() ? s.next() : "/";
        return r;
    }
}
