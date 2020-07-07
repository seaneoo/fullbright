package dev.seano.fullbright;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.MessageType;
import net.minecraft.text.TranslatableText;

public class FullbrightMod implements ClientModInitializer {

    private MinecraftClient minecraftClient;
    private final String KEYBIND_CATEGORY = "key.fullbright.category";
    private final KeyBinding TOGGLE_FULLBRIGHT_KEYBIND = new KeyBinding("key.fullbright.toggle_fullbright",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, KEYBIND_CATEGORY);
    private final double modGamma = 10.0D;
    private boolean enabled = false;

    @SuppressWarnings("deprecation")
    @Override
    public void onInitializeClient() {
        ModConfig.getConfig().load();

        minecraftClient = MinecraftClient.getInstance();
        KeyBindingHelper.registerKeyBinding(TOGGLE_FULLBRIGHT_KEYBIND);

        ClientTickCallback.EVENT.register(client -> {
            while (TOGGLE_FULLBRIGHT_KEYBIND.wasPressed()) {
                enabled = !enabled;
                minecraftClient.options.gamma = (enabled ? modGamma : ModConfig.getConfig().getDefaultGamma());

                client.inGameHud.addChatMessage(MessageType.SYSTEM,
                        new TranslatableText("chat.toggle_fullbright", (enabled ? "On" : "Off")), null);
            }
        });
    }
}