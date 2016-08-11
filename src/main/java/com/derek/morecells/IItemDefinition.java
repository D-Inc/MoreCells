package com.derek.morecells;

import appeng.api.util.AEItemDefinition;

/**
 * Created by Rocker545 on 8/11/2016.
 */
public interface IItemDefinition {
    AEItemDefinition physCell256k();

    AEItemDefinition physCell1024k();

    AEItemDefinition physCell4096k();

    AEItemDefinition physCell16384k();

    AEItemDefinition physCellContainer();
}
