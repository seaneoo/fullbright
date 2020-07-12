package dev.seano.fullbright;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.MessageType;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.Timer;
import java.util.TimerTask;

public class FullbrightMod implements ClientModInitializer {

   private MinecraftClient minecraftClient;
   private final String KEYBIND_CATEGORY = "key.fullbright.category";
   private final KeyBinding TOGGLE_FULLBRIGHT_KEYBIND = new KeyBinding("key.fullbright.toggle_fullbright",
           InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, KEYBIND_CATEGORY);
   private boolean enabled = false;
   private final double modGamma = 10.0D;

   @Override
   public void onInitializeClient() {
      ModConfig.getConfig().load();

      minecraftClient = MinecraftClient.getInstance();
      KeyBindingHelper.registerKeyBinding(TOGGLE_FULLBRIGHT_KEYBIND);

      ClientLifecycleEvents.CLIENT_STARTED.register(client -> enabled = !(minecraftClient.options.gamma <= 1.0));

      ClientTickEvents.START_CLIENT_TICK.register(client -> {
         while (TOGGLE_FULLBRIGHT_KEYBIND.wasPressed()) {
            if (!enabled) {
               enabled = true;
               increase();
               client.inGameHud.addChatMessage(MessageType.GAME_INFO,
                       new TranslatableText("chat.fullbright_enabled"), null);
            } else {
               enabled = false;
               decrease();
               client.inGameHud.addChatMessage(MessageType.GAME_INFO,
                       new TranslatableText("chat.fullbright_disabled"), null);
            }
         }
      });
   }

   private void increase() {
      Timer timer = new Timer();
      timer.scheduleAtFixedRate(new TimerTask() {
         double d = ModConfig.getConfig().getDefaultGamma();

         @Override
         public void run() {
            if (d >= modGamma || !enabled) timer.cancel();
            else {
               d += 0.5;
               minecraftClient.options.gamma = d;
            }
         }
      }, 0L, 10L);
   }

   private void decrease() {
      Timer timer = new Timer();
      timer.scheduleAtFixedRate(new TimerTask() {
         double d = modGamma;

         @Override
         public void run() {
            if (d <= ModConfig.getConfig().getDefaultGamma() || enabled) timer.cancel();
            else {
               d -= 0.5;
               minecraftClient.options.gamma = d;
            }
         }
      }, 0L, 10L);
   }
}