package com.github.sirblobman.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.plugin.IMultiVersionPlugin;
import com.github.sirblobman.api.utility.ConfigurationHelper;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class ItemLoader {
    public static @Nullable ItemStack loadItemStack(@NotNull IMultiVersionPlugin plugin,
                                                    @NotNull ConfigurationSection section, @NotNull String path) {
        if (section.isItemStack(path)) {
            return section.getItemStack(path);
        }

        ConfigurationSection subSection = section.getConfigurationSection(path);
        if (subSection == null) {
            return null;
        }

        return loadItemStack(plugin, subSection);
    }

    public static @Nullable ItemStack loadItemStack(@NotNull IMultiVersionPlugin plugin,
                                                    @NotNull ConfigurationSection section) {
        String materialName = section.getString("material");
        if (materialName == null) {
            return null;
        }

        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(materialName);
        XMaterial material = optionalMaterial.orElse(XMaterial.BARRIER);

        ItemBuilder builder = new ItemBuilder(material);
        builder = checkPotion(material, builder, section);
        builder = checkLeatherArmor(material, builder, section);
        builder = checkSkull(plugin, material, builder, section);
        checkNameAndLore(plugin, builder, section);
        builder = checkEnchantments(builder, section);

        int amount = section.getInt("quantity", 1);
        builder = builder.withAmount(amount);

        int damage = section.getInt("damage", 0);
        builder = builder.withDamage(damage);

        if (section.isSet("model")) {
            int model = section.getInt("model");
            builder = builder.withModel(model);
        }

        if (section.getBoolean("glowing", false)) {
            builder = builder.withGlowing();
        }

        return builder.build();
    }

    private static @NotNull ItemBuilder checkPotion(@NotNull XMaterial material, @NotNull ItemBuilder builder,
                                                    @NotNull ConfigurationSection section) {
        BottleType bottleType = BottleType.getByMaterial(material);
        if (bottleType == null){
            return builder;
        }

        ConfigurationSection sectionPotionData = section.getConfigurationSection("potion-data");
        if (sectionPotionData == null) {
            return builder;
        }

        PotionBuilder potionBuilder = new PotionBuilder(bottleType);
        ConfigurationSection basePotion = sectionPotionData.getConfigurationSection("base-potion");
        if (basePotion != null) {
            String potionTypeName = basePotion.getString("type");
            if (potionTypeName == null) {
                potionTypeName = "WATER";
            }

            PotionType potionType = ConfigurationHelper.parseEnum(PotionType.class, potionTypeName, PotionType.WATER);
            boolean extended = basePotion.getBoolean("extended", false);
            boolean upgraded = basePotion.getBoolean("upgraded", false);
            potionBuilder = potionBuilder.withMainEffect(potionType, extended, upgraded);
        }

        ConfigurationSection extraEffects = sectionPotionData.getConfigurationSection("extra-effects");
        if (extraEffects != null) {
            List<PotionEffect> potionEffectList = parsePotionEffects(extraEffects);
            for (PotionEffect effect : potionEffectList) {
                potionBuilder = potionBuilder.withExtraEffect(effect);
            }
        }

        Color color = parseColor(section);
        if (color != null) {
            potionBuilder = potionBuilder.withColor(color);
        }

        return  potionBuilder;
    }

    private static @NotNull ItemBuilder checkLeatherArmor(@NotNull XMaterial material, @NotNull ItemBuilder builder,
                                                          @NotNull ConfigurationSection section) {
        ArmorType armorType;
        if (material == XMaterial.LEATHER_HELMET) {
            armorType = ArmorType.HELMET;
        } else if (material == XMaterial.LEATHER_CHESTPLATE) {
            armorType = ArmorType.CHESTPLATE;
        } else if (material == XMaterial.LEATHER_LEGGINGS) {
            armorType = ArmorType.LEGGINGS;
        } else if (material == XMaterial.LEATHER_BOOTS) {
            armorType = ArmorType.BOOTS;
        } else {
            armorType = null;
        }

        if (armorType == null) {
            return builder;
        }

        LeatherArmorBuilder leatherArmorBuilder = new LeatherArmorBuilder(armorType);

        Color color = parseColor(section);
        if (color != null) {
            leatherArmorBuilder = leatherArmorBuilder.withColor(color);
        }

        return leatherArmorBuilder;
    }

    private static @NotNull ItemBuilder checkSkull(@NotNull IMultiVersionPlugin plugin,
                                                   @NotNull XMaterial material, @NotNull ItemBuilder builder,
                                                   @NotNull ConfigurationSection section) {
        if (material != XMaterial.PLAYER_HEAD) {
            return builder;
        }

        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        HeadHandler headHandler = multiVersionHandler.getHeadHandler();
        SkullBuilder skullBuilder = new SkullBuilder(headHandler);

        String texture = section.getString("texture");
        if (texture != null) {
            return skullBuilder.withTextureBase64(texture);
        }

        String textureUrl = section.getString("texture-url");
        if (textureUrl != null) {
            return skullBuilder.withTextureUrl(textureUrl);
        }

        String username = section.getString("skull-owner");
        if (username != null) {
            return skullBuilder.withOwner(username);
        }

        return builder;
    }

    private static @NotNull ItemBuilder checkNameAndLore(@NotNull IMultiVersionPlugin plugin,
                                                         @NotNull ItemBuilder builder,
                                                         @NotNull ConfigurationSection section) {
        checkDisplayName(plugin, builder, section);
        checkLore(plugin, builder, section);
        return builder;
    }

    private static @NotNull ItemBuilder checkDisplayName(@NotNull IMultiVersionPlugin plugin,
                                                         @NotNull ItemBuilder builder,
                                                         @NotNull ConfigurationSection section) {
        // TODO
        return builder;
    }

    private static @NotNull ItemBuilder checkLore(@NotNull IMultiVersionPlugin plugin, @NotNull ItemBuilder builder,
                                                  @NotNull ConfigurationSection section) {
        // TODO
        return builder;
    }

    private static @NotNull ItemBuilder checkEnchantments(@NotNull ItemBuilder builder,
                                                          @NotNull ConfigurationSection section) {
        ConfigurationSection sectionEnchantments = section.getConfigurationSection("enchantments");
        if (sectionEnchantments == null) {
            return builder;
        }

        Set<String> enchantmentNameSet = sectionEnchantments.getKeys(false);
        for (String enchantmentName : enchantmentNameSet) {
            @SuppressWarnings("deprecation")
            Enchantment enchantment = Enchantment.getByName(enchantmentName);
            if (enchantment == null) {
                continue;
            }

            int level = sectionEnchantments.getInt(enchantmentName, 1);
            builder = builder.withEnchantment(enchantment, level);
        }

        return builder;
    }

    private static @NotNull List<PotionEffect> parsePotionEffects(@NotNull ConfigurationSection section) {
        List<PotionEffect> potionEffectList = new ArrayList<>();
        Set<String> keys = section.getKeys(false);

        for (String key : keys) {
            ConfigurationSection subSection = section.getConfigurationSection(key);
            if (subSection == null) {
                continue;
            }

            PotionEffect effect = parsePotionEffect(subSection);
            if (effect != null) {
                potionEffectList.add(effect);
            }
        }

        return Collections.unmodifiableList(potionEffectList);
    }

    private static @Nullable PotionEffect parsePotionEffect(@NotNull ConfigurationSection section) {
        String typeName = section.getString("type");
        if (typeName == null) {
            return null;
        }

        PotionEffectType type = PotionEffectType.getByName(typeName);
        if (type == null) {
            return null;
        }

        int duration = section.getInt("duration", 10);
        int amplifier = section.getInt("amplifier", 0);
        boolean ambient = section.getBoolean("ambient", false);
        boolean particles = section.getBoolean("particles", true);
        boolean icon = section.getBoolean("icon", true);

        PotionEffect effect;
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            effect = new PotionEffect(type, duration, amplifier, ambient, particles);
        } else {
            effect = new PotionEffect(type, duration, amplifier, ambient, particles, icon);
        }

        return effect;
    }

    private static @Nullable Color parseColor(@NotNull ConfigurationSection section) {
        if (section.isConfigurationSection("color")) {
            ConfigurationSection color = section.getConfigurationSection("color");
            if (color == null) {
                return null;
            }

            int red = color.getInt("red", 0);
            int green = color.getInt("green", 0);
            int blue = color.getInt("blue", 0);
            return Color.fromRGB(red, green, blue);
        }

        if (section.isInt("color")) {
            int color = section.getInt("color");
            return Color.fromRGB(color);
        }

        if (section.isString("color")) {
            String colorString = section.getString("color");
            if (colorString == null || !colorString.startsWith("#") || colorString.length() != 7) {
                return null;
            }

            String sub = colorString.substring(1);
            try {
                int color = Integer.parseInt(sub, 16);
                return Color.fromRGB(color);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }
}
