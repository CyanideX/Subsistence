package subsistence.items.testNew.prefab;

import javax.annotation.Nullable;

public enum EnumResources implements IItemData{
    ITEM(0, "hi", (player, world, pos, hand, facing, hitX, hitY, hitZ) -> {
        System.out.println("magic");
        return false;
    });

    int meta;
    String name;
    IItemUseAction iItemUseAction;

    EnumResources(int meta, String name, @Nullable IItemUseAction iItemUseAction){
        this.meta = meta;
        this.name = name;
        this.iItemUseAction = iItemUseAction;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public int getMeta() {
        return meta;
    }

    @Nullable
    @Override
    public IItemUseAction getItemUseAction() {
        return iItemUseAction;
    }
}
