package com.derek.morecells;


/**
 * Created by Rocker545 on 8/11/2016.
 */
public class ItemDefinition implements IItemDefinition{
    public static final ItemDefinition instance = new ItemDefinition();

    @Override
    public appeng.api.definitions.IItemDefinition physCell16k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 1);
    }

    @Override
    public appeng.api.definitions.IItemDefinition physCell64k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 3);
    }

    // Physical Cells
    @Override
    public appeng.api.definitions.IItemDefinition physCell1k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem());
    }

    @Override
    public appeng.api.definitions.IItemDefinition physCell4k() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 2);
    }

    @Override
    public appeng.api.definitions.IItemDefinition physCellContainer() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 4);
    }
    
    @Override
    public appeng.api.definitions.IItemDefinition physCell16M() {
        return new ItemItemDefinitions(ItemEnum.BIGCELL.getItem(), 5);
    }
}
