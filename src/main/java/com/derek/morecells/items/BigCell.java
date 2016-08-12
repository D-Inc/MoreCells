package com.derek.morecells.items;

import appeng.api.AEApi;
import appeng.api.config.AccessRestriction;
import appeng.api.config.FuzzyMode;
import appeng.api.config.PowerUnits;
import appeng.api.implementations.items.IAEItemPowerStorage;
import appeng.api.implementations.items.IStorageCell;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by Rocker545 on 8/11/2016.
 */
@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI|energy")
public class BigCell extends Item implements IStorageCell, IAEItemPowerStorage, IEnergyContainerItem{

    public static final String[] suffixes = {"1k", "4k", "16k", "64k", "container"};

    public static final int[] bytes_cell = { 1024, 4096, 16384, 65536, 16777216 };
    public static final int[] types_cell = { 1024, 1024, 1024, 1024, 1 };
    private IIcon[] icons;
    private final int MAX_POWER = 32000;

    public BigCell()
    {
        setMaxStackSize(1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public double injectAEPower(ItemStack itemStack, double amt) {
        if (itemStack == null || itemStack.getItemDamage() != 4)
            return 0.0D;
        NBTTagCompound tagCompound = ensureTagCompound(itemStack);
        double currentPower = tagCompound.getDouble("power");
        double toInject = Math.min(amt, this.MAX_POWER - currentPower);
        tagCompound.setDouble("power", currentPower + toInject);
        return toInject;
    }

    @Override
    public double getAEMaxPower(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemDamage() != 4)
            return 0.0D;
        return this.MAX_POWER;
    }

    @Override
    public double getAECurrentPower(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemDamage() != 4)
            return 0.0D;
        NBTTagCompound tagCompound = ensureTagCompound(itemStack);
        return tagCompound.getDouble("power");
    }


    @Override
    public AccessRestriction getPowerFlow(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        return itemStack.getItemDamage() == 4 ? AccessRestriction.READ_WRITE
                : AccessRestriction.NO_ACCESS;
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        return bytes_cell[MathHelper.clamp_int(cellItem.getItemDamage(), 0,
                suffixes.length - 1)];
    }

    @Override
    public int BytePerType(ItemStack itemStack) {
        return getBytesPerType(itemStack);
    }

    @Override
    public int getBytesPerType(ItemStack itemStack) {
        return false ? bytes_cell[MathHelper.clamp_int(itemStack.getItemDamage(), 0, suffixes.length - 1)] / 128 : 8;
    }

    @Override
    public int getTotalTypes(ItemStack cellItem) {
        return types_cell[MathHelper.clamp_int(cellItem.getItemDamage(), 0,
                suffixes.length - 1)];
    }

    @Override
    public boolean isBlackListed(ItemStack cellItem,
                                 IAEItemStack requestedAddition) {
        return false;
    }


    @Override
    public boolean storableInStorageCell() {
        return false;
    }

    @Override
    public boolean isStorageCell(ItemStack i) {
        return true;
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public boolean isEditable(ItemStack is) {
        return false;
    }

    @Override
    public IInventory getUpgradesInventory(ItemStack is) {
        return new MoreCellInventory(is, "upgrades", 2, 1);
    }

    @Override
    public IInventory getConfigInventory(ItemStack itemStack) {
        return new MoreCellInventory(itemStack, "config", 63, 1);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack is) {
        if (!is.hasTagCompound())
            is.setTagCompound(new NBTTagCompound());
        return FuzzyMode.values()[is.getTagCompound().getInteger("fuzzyMode")];
    }

    @Override
    public void setFuzzyMode(ItemStack is, FuzzyMode fzMode) {
        if (!is.hasTagCompound())
            is.setTagCompound(new NBTTagCompound());
        is.getTagCompound().setInteger("fuzzyMode", fzMode.ordinal());
    }


    @Override
    @Optional.Method(modid = "CoFHAPI|energy")
    public int receiveEnergy(ItemStack container, int maxReceive,
                             boolean simulate) {
        if (container == null || container.getItemDamage() != 4)
            return 0;
        if (simulate) {
            double current = PowerUnits.AE.convertTo(PowerUnits.RF,
                    getAECurrentPower(container));
            double max = PowerUnits.AE.convertTo(PowerUnits.RF,
                    getAEMaxPower(container));
            if (max - current >= maxReceive)
                return maxReceive;
            else
                return (int) (max - current);
        } else {
            int notStored = (int) PowerUnits.AE
                    .convertTo(
                            PowerUnits.RF,
                            injectAEPower(container, PowerUnits.RF.convertTo(
                                    PowerUnits.AE, maxReceive)));
            return maxReceive - notStored;
        }
    }

    @Override
    @Optional.Method(modid = "CoFHAPI|energy")
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if(container == null || container.getItemDamage() != 4)
        {
            return 0;
        }
        if(simulate)
        {
            return getEnergyStored(container) >= maxExtract ? maxExtract : getEnergyStored(container);
        }
        else
            {
                return (int) PowerUnits.AE.convertTo(PowerUnits.RF, extractAEPower(container, PowerUnits.RF.convertTo(PowerUnits.AE, maxExtract)));
            }
    }

    @Override
    @Optional.Method(modid = "CoFHAPI|energy")
    public int getEnergyStored(ItemStack arg0) {
        return (int) PowerUnits.AE.convertTo(PowerUnits.RF,
                getAECurrentPower(arg0));
    }

    @Override
    @Optional.Method(modid = "CoFHAPI|energy")
    public int getMaxEnergyStored(ItemStack arg0) {
        return (int) PowerUnits.AE
                .convertTo(PowerUnits.RF, getAEMaxPower(arg0));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
    {
        ICellRegistry cellRegistry = AEApi.instance().registries().cell();
        IMEInventoryHandler<IAEItemStack> invHandler = cellRegistry.getCellInventory(itemStack, null, StorageChannel.ITEMS);
        ICellInventoryHandler inventoryHandler = (ICellInventoryHandler) invHandler;
        ICellInventory cellInv = inventoryHandler.getCellInv();
        long usedBytes = cellInv.getUsedBytes();

        list.add(String.format(StatCollector.translateToLocal("morecells.tooltip.storage.physical.bytes"), usedBytes, cellInv.getTotalBytes()));
        list.add(String.format(StatCollector.translateToLocal("morecells.tooltip.storage.physical.types"), cellInv.getStoredItemTypes(), cellInv.getTotalItemTypes()));
        if (usedBytes > 0)
        {
            list.add(String.format(
                    StatCollector.translateToLocal("morecells.tooltip.storage.physical.content"),
                    cellInv.getStoredItemCount()));
        }
    }

    private NBTTagCompound ensureTagCompound(ItemStack itemStack)
    {
        if(!itemStack.hasTagCompound())
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound();
    }

    @Override
    public double extractAEPower(ItemStack itemStack, double v) {
        if (itemStack == null || itemStack.getItemDamage() != 4)
        {
            return 0.0D;
        }
        NBTTagCompound tagCompound = ensureTagCompound(itemStack);
        double currentPower = tagCompound.getDouble("power");
        double toExtract = Math.min(v, currentPower);
        tagCompound.setDouble("power", currentPower - toExtract);
        return toExtract;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemDamage() != 4)
            return super.getDurabilityForDisplay(itemStack);
        return 1 - getAECurrentPower(itemStack) / this.MAX_POWER;
    }

    @Override
    public IIcon getIconFromDamage(int dmg) {
        return this.icons[MathHelper.clamp_int(dmg, 0, suffixes.length - 1)];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack == null)
            return super.getItemStackDisplayName(stack);
        if (stack.getItemDamage() == 4) {
            try {
                IItemList list = AEApi
                        .instance()
                        .registries()
                        .cell()
                        .getCellInventory(stack, null, StorageChannel.ITEMS)
                        .getAvailableItems(
                                AEApi.instance().storage().createItemList());
                if (list.isEmpty())
                    return super.getItemStackDisplayName(stack)
                            + " - "
                            + StatCollector
                            .translateToLocal("morecells.tooltip.empty1");
                IAEItemStack s = (IAEItemStack) list.getFirstItem();
                return super.getItemStackDisplayName(stack) + " - "
                        + s.getItemStack().getDisplayName();
            } catch (Throwable e) {}
            return super.getItemStackDisplayName(stack)
                    + " - "
                    + StatCollector
                    .translateToLocal("morecells.tooltip.empty1");
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.epic;
    }
    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList) {
        for (int i = 0; i < suffixes.length; i++) {
            itemList.add(new ItemStack(item, 1, i));
            if (i == 4) {
                ItemStack s = new ItemStack(item, 1, i);
                s.setTagCompound(new NBTTagCompound());
                s.getTagCompound().setDouble("power", this.MAX_POWER);
                itemList.add(s);
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "morecells.item.storage.physical."
                + suffixes[itemStack.getItemDamage()];
    }
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[suffixes.length];

        for (int i = 0; i < suffixes.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("morecells:"
                    + "storage.physical." + suffixes[i]);
        }
    }
    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        return itemStack.getItemDamage() == 4 ? true : false;
    }
}
