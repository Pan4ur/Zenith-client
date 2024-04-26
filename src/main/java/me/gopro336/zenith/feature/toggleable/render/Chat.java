package me.gopro336.zenith.feature.toggleable.render;

import me.gopro336.zenith.api.util.color.ColorTextUtils;
import me.gopro336.zenith.api.util.newUtil.ChatUtils;
import me.gopro336.zenith.event.network.PacketReceiveEvent;
import me.gopro336.zenith.feature.AnnotationHelper;
import me.gopro336.zenith.feature.Category;
import me.gopro336.zenith.feature.Feature;
import me.gopro336.zenith.feature.command.Command;
import me.gopro336.zenith.property.Property;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.text.SimpleDateFormat;
import java.util.Date;

@AnnotationHelper(name = "Chat", description = "Tampers with chat", category = Category.RENDER)
public class Chat extends Feature {
    public final Property<Boolean> customFont = new Property<>(this, "CustomFont", "", false);
    public final Property<Boolean> ncb = new Property<>(this, "NoBackground", "", false);
    public static Property<Boolean> nochatshadow;
    private final Property<Boolean> namehighlight = new Property<>(this, "NameHighlight", "", false);
    private final Property<highlightMode> namemode = new Property<>(this, namehighlight, "HighlightMode", "", highlightMode.Highlight);
    private final Property<playerNameMode> playername = new Property<>(this, "PlayerTag", "", playerNameMode.ankleBr);
    private final Property<ColorTextUtils.colors> playerColor = new Property<>(this, "PlayerColor", "", ColorTextUtils.colors.White);
    private final Property<Boolean> timestamps = new Property<>(this, "TimeStamps", "", false);
    private final Property<Boolean> mode = new Property<>(this, "24HTime", "", false);
    private final Property<bracketmodes> bracketmode = new Property<>(this, "BracketType", "", bracketmodes.ankleBr);

    private final Property<ColorTextUtils.colors> color = new Property<>(this, "Color", "", ColorTextUtils.colors.LightGray);

    public enum playerNameMode{
        ankleBr, Brackets, None, Arrow
    }

    public enum bracketmodes{
        ankleBr, Parenth, Brackets, Brace
    }

    public enum highlightMode{
        Highlight, Hide
    }


    public static Chat INSTANCE;

    public Chat() {
        nochatshadow = new Property<>(this, "No Chat Shadow", "", false);
        INSTANCE = this;
    }

    public static TextComponentString componentStringOld;

    @Listener
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat) event.getPacket();
            if (packet.getType() != ChatType.GAME_INFO) {
                if (tryProcessChat(packet.getChatComponent().getFormattedText(), packet.getChatComponent().getUnformattedText())) {
                    event.setCanceled(true);
                }
            }
        }
    }

    private boolean tryProcessChat(String message, String unformatted) {
        String out = message;
        String[] parts = out.split(" ");
        String[] partsUnformatted = unformatted.split(" ");
        parts[0] = partsUnformatted[0];
        if (parts[0].startsWith("<") && parts[0].endsWith(">")) {
            parts[0] = parts[0].replaceAll("<", "");
            parts[0] = parts[0].replaceAll(">", "");
            parts[0] = Command.SECTIONSIGN() + ColorTextUtils.getColor(playerColor.getValue()).substring(1) + parts[0] + Command.SECTIONSIGN() + "r";
            if (playername.getValue().equals(playerNameMode.ankleBr)) {
                String temp;
                temp = "<" + parts[0] + ">";
                for (int i = 1; i < parts.length; i++) {
                    temp += " " + parts[i];
                }
                message = temp;
            } else if (playername.getValue().equals(playerNameMode.Brackets)) {
                String temp;
                temp = "[" + parts[0] + "]:";
                for (int i = 1; i < parts.length; i++) {
                    temp += " " + parts[i];
                }
                message = temp;
            } else if (playername.getValue().equals(playerNameMode.None)) {
                String temp;
                temp = parts[0] + ":";
                for (int i = 1; i < parts.length; i++) {
                    temp += " " + parts[i];
                }
                message = temp;
            } else if (playername.getValue().equals(playerNameMode.Arrow)) {
                String temp;
                temp = parts[0] + " ->";
                for (int i = 1; i < parts.length; i++) {
                    temp += " " + parts[i];
                }
                message = temp;
            } else {
                String temp;
                temp = "<" + parts[0] + ">";
                for (int i = 1; i < parts.length; i++) {
                    temp += " " + parts[i];
                }
                message = temp;
            }
        }
        out = message;
        if (this.timestamps.getValue()) {
            String date = "";
            if (this.mode.getValue()) {
                date = new SimpleDateFormat("k:mm").format(new Date());
            }else{
                date = new SimpleDateFormat("h:mm a").format(new Date());
            }
            if (this.bracketmode.getValue().equals(bracketmodes.ankleBr)) {
                out = "\247" + ColorTextUtils.getColor(color.getValue()).substring(1) + "<" + date + ">\247r " + message;
            }
            else if (this.bracketmode.getValue().equals(bracketmodes.Parenth)) {
                out = "\247" + ColorTextUtils.getColor(color.getValue()).substring(1) + "(" + date + ")\247r " + message;
            }
            else if (this.bracketmode.getValue().equals(bracketmodes.Brackets)) {
                out = "\247" + ColorTextUtils.getColor(color.getValue()).substring(1) + "[" + date + "]\247r " + message;
            }
            else if (this.bracketmode.getValue().equals(bracketmodes.Brace)) {
                out = "\247" + ColorTextUtils.getColor(color.getValue()).substring(1) + "{" + date + "}\247r " + message;
            }

        }
        if (this.namehighlight.getValue()) {
            if (mc.player == null) return false;
            if (this.namemode.getValue().equals(highlightMode.Hide)) {
                out = out.replace(mc.player.getName(), "HIDDEN");
            } else {
                out = out.replace(mc.player.getName(), "\247b" + mc.player.getName() + "\247r");
            }
        }
        ChatUtils.message(out);
        return true;
    }
}
