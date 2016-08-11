package com.derek.morecells;

import com.derek.morecells.items.BigCell;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * Created by Rocker545 on 8/11/2016.
 */
public enum ItemEnum {
    BIGCELL("storage.physical", new BigCell());

    private final String internalName;
    private Item item;

    ItemEnum(String _internalName, Item _item)
    {
        this.internalName = _internalName;
        this.item = _item;
        this.item.setUnlocalizedName("morecells." + this.internalName);
    }

    public ItemStack getDamagedStack(int damage) {
        return new ItemStack(this.item, 1, damage);
    }

    public String getInternalName() {
        return this.internalName;
    }

    public Item getItem() {
        return this.item;
    }

    public ItemStack getSizedStack(int size) {
        return new ItemStack(this.item, size);
    }

    public String getStatName() {
        return StatCollector.translateToLocal(this.item.getUnlocalizedName());
    }
}
