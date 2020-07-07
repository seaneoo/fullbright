package dev.seano.fullbright;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ModMenuImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> {
            ConfigBuilder builder = ConfigBuilder.create();
            builder.setTitle(new TranslatableText("key.fullbright.category"));
            builder.setSavingRunnable(() -> ModConfig.getConfig().save());

            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();
            ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("config.fullbright.category"));
            category.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.fullbright.entry.defaultGamma"), ModConfig.getConfig().getDefaultGamma())
                    .setSaveConsumer(d -> ModConfig.getConfig().setDefaultGamma(d))
                    .setDefaultValue(1.0D)
                    .setMin(0.0D)
                    .setMax(1.0D)
                    .setTooltip(new TranslatableText("config.fullbright.tooltip.defaultGamma"))
                    .build());

            return builder.build();
        };
    }
}