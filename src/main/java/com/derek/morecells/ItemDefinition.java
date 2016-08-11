package com.derek.morecells;

import appeng.api.util.AEItemDefinition;

/**
 * Created by Rocker545 on 8/11/2016.
 */
public class ItemDefinition implements IItemDefinition{
    public static final ItemDefinition instance = new ItemDefinition();

    @Override
    public AEItemDefinition physCell1024k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 1);
    }

    @Override
    public AEItemDefinition physCell16384k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 3);
    }

    // Physical Cells
    @Override
    public AEItemDefinition physCell256k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem());
    }

    @Override
    public AEItemDefinition physCell4096k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 2);
    }

    @Override
    public AEItemDefinition physCellContainer() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 4);
    }
}
