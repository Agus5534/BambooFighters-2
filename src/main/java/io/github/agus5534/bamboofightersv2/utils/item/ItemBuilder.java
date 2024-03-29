package io.github.agus5534.bamboofightersv2.utils.item;

import io.github.agus5534.agusutils.utils.text.ChatFormatter;
import io.github.agus5534.bamboofightersv2.BambooFighters;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {

    protected ItemStack is;

    protected ItemMeta im;

    public ItemBuilder() {
        this(Material.AIR);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        this.im = this.is.getItemMeta();
        this.im.setCustomModelData(data);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean b) {
        this.im = this.is.getItemMeta();
        this.im.setUnbreakable(b);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.im = this.is.getItemMeta();
        this.im.displayName(ChatFormatter.formatString(name));
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setDisplayName(Component component) {
        this.im = this.is.getItemMeta();
        this.im.displayName(component);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.im = this.is.getItemMeta();
        if (this.im instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) this.im).addStoredEnchant(enchantment, level, true);
        } else {
            this.im.addEnchant(enchantment, level, true);
        }
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        this.im = this.is.getItemMeta();
        if (!enchantments.isEmpty())
            for (Enchantment enchantment : enchantments.keySet())
                this.im.addEnchant(enchantment, enchantments.get(enchantment), true);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setSkullSkin(OfflinePlayer player) {
        final SkullMeta skullMeta = (SkullMeta) this.is.getItemMeta();
        skullMeta.setOwningPlayer(player);
        this.is.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag... itemFlag) {
        this.im = this.is.getItemMeta();
        this.im.addItemFlags(itemFlag);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        this.im = this.is.getItemMeta();
        this.im.lore(lore);
        this.is.setItemMeta(this.im);
        return this;
    }


    public ItemBuilder setLore(String... lore) {
        this.setLore(Arrays.stream(lore).map(ChatFormatter::formatString).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder clearEnchants() {
        this.im = this.is.getItemMeta();
        this.im.getEnchants().forEach((enchantment, integer) -> this.im.removeEnchant(enchantment));
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setLore(Component... lore) {
        this.setLore(List.of(lore));
        return this;
    }

    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {

        this.im = this.is.getItemMeta();
        this.im.addAttributeModifier(attribute, attributeModifier);
        this.is.setItemMeta(this.im);
        return this;

    }

    public ItemBuilder addAttributeModifier(Attribute attribute, double value, AttributeModifier.Operation operation, EquipmentSlot slot) {
        this.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.getKey().getKey(),value, operation, slot));
        return this;
    }

    /**
     * Se activa cuando se da click derecho al aire o a un bloque.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractEvent
     */
    public ItemBuilder onConsumeRightClick(String key, Consumer<PlayerInteractEvent> consumer) {
        this.onConsume(key, consumer, Interaction.RIGHT_CLICK);
        return this;
    }

    /**
     * Se activa solo cuando se da click derecho al aire.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractEvent
     */
    public ItemBuilder onConsumeRightClickAir(String key, Consumer<PlayerInteractEvent> consumer) {
        this.onConsume(key, consumer, Interaction.RIGHT_CLICK_AIR);
        return this;
    }

    /**
     * Se activa solo cuando se da click derecho a un bloque.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractEvent
     */
    public ItemBuilder onConsumeRightClickBlock(String key, Consumer<PlayerInteractEvent> consumer) {
        this.onConsume(key, consumer, Interaction.RIGHT_CLICK_BLOCK);
        return this;
    }

    /**
     * Se activa cuando se da click izquierdo al aire o a un bloque.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractEvent
     */
    public ItemBuilder onConsumeLeftClick(String key, Consumer<PlayerInteractEvent> consumer) {
        this.onConsume(key, consumer, Interaction.LEFT_CLICK);
        return this;
    }
    /**
     * Se activa solo cuando se da click izquierdo al aire.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractEvent
     */
    public ItemBuilder onConsumeLeftClickAir(String key, Consumer<PlayerInteractEvent> consumer) {
        this.onConsume(key, consumer, Interaction.LEFT_CLICK_AIR);
        return this;
    }
    /**
     * Se activa solo cuando se da click izquierdo a un bloque.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractEvent
     */
    public ItemBuilder onConsumeLeftClickBlock(String key, Consumer<PlayerInteractEvent> consumer) {
        this.onConsume(key, consumer, Interaction.LEFT_CLICK_BLOCK);
        return this;
    }

    /**
     * Se activa solo cuando se consume el item.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerItemConsumeEvent
     */
    public ItemBuilder onConsumeEat(String key, Consumer<PlayerItemConsumeEvent> consumer) {
        this.onConsume(key, consumer, Interaction.EAT);
        return this;
    }

    /**
     * Se activa cuando se le interactúa mediante un click a una entidad.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see PlayerInteractAtEntityEvent
     */
    public ItemBuilder onConsumeClickEntity(String key, Consumer<PlayerInteractAtEntityEvent> consumer) {
        this.onConsume(key, consumer, Interaction.CLICK_ENTITY);
        return this;
    }

    /**
     * Se activa cuando se daña a una entidad.
     * @param key Key de la interacción.
     * @param consumer Consumer del evento.
     * @return {@link ItemBuilder}
     * @see EntityDamageByEntityEvent
     */
    public ItemBuilder onConsumeHitEntity(String key, Consumer<EntityDamageByEntityEvent> consumer) {
        this.onConsume(key, consumer, Interaction.HIT_ENTITY);
        return this;
    }

    private ItemBuilder onConsume(String key, Consumer<?> consumer, Interaction interaction) {
        this.addData(key, "true");
        var interactable = new InteractableItem(interaction, new NamespacedKey(BambooFighters.instance, key), consumer);
        InteractionManager.registerItem(interactable);
        return this;
    }

    public ItemBuilder addData(String key, String value) {
        this.im = this.is.getItemMeta();
        this.im.getPersistentDataContainer().set(new NamespacedKey(BambooFighters.instance, key), PersistentDataType.STRING, value);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemStack build() {
        return this.is;
    }
}
