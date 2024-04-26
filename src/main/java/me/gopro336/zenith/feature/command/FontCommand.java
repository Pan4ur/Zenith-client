package me.gopro336.zenith.feature.command;

import me.gopro336.zenith.api.util.font.FontUtil;

import static me.gopro336.zenith.api.util.newUtil.ChatUtils.message;

public class FontCommand extends Command {

    public FontCommand() {
        super("Font", new String[]{"f", "cfont"}, "Change the custom font defaultinfo | currentfont | setfont", "font setfont [fontname]");
    }

    @Override
    public void call(String[] args) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("defaultinfo")) {
                message("Font: Verdana, Size: 18");
            }
            if (args[1].equalsIgnoreCase("currentfont")) {
                message("Font: " + FontUtil.customFontRenderer.getFont().getFontName() + ", Size: " + FontUtil.customFontRenderer.getFont().getSize());
            }
            if (args[1].equalsIgnoreCase("fonts")) {
                message("Fonts:");
                String out = "";
                boolean start = true;
                for (String s : FontUtil.getFonts()) {
                    if (start)
                        out = s;
                    else
                        out = out + ", " + s;
                    start = false;
                }
                message(out);
            }
            if (args[1].equalsIgnoreCase("setfont")) {
                if (args.length < 3) {
                    message("Specify your font!");
                    return;
                }
                if (args.length < 4) {
                    message("Specify your font size!");
                    return;
                }
                if (Integer.parseInt(args[3]) == 0) return;
                FontUtil.setFontRenderer(args[2], Integer.parseInt(args[3]));
            }
        } else {
            message("Do .customfont help for options");
        }
    }
}
