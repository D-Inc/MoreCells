package com.derek.morecells;

import com.derek.morecells.items.BigCell;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * Created by Rocker545 on 8/11/2016.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class MoreCells {

    public static Item bigCell;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //Item & Block initizalitions and registering
            for (ItemEnum current : ItemEnum.values()){
                GameRegistry.registerItem(current.getItem(), current.getInternalName());
            }
        //Configs too
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Proxy, TileEntity, entity, GUI, Packets
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
