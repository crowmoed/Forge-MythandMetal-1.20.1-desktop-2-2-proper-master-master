package net.pinto.mythandmetal.block.customEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModLavaDungeonPortalDoorBlockEntity extends BlockEntity {
    private boolean notaccessed = true;
    private int accessnumber=0;

    public ModLavaDungeonPortalDoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MyBlockEntityTypes.LAVA_MOD_PORTAL.get(), pPos, pBlockState);
    }

    public boolean isNotaccessed() {
        return notaccessed;
    }
    public int getAccessNumber() {
        return accessnumber;
    }
    public void setNotaccessed(boolean notaccessed) {
        this.notaccessed = notaccessed;
    }


    public void setAccessnumber(int notaccessed) {
        this.accessnumber = notaccessed;
    }
}