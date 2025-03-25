package net.pinto.mythandmetal.block.customEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModDungeonPortalDoorBlockEntity extends BlockEntity {
    private boolean notaccessed = true;
    private int accessnumber=0;

    public ModDungeonPortalDoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MyBlockEntityTypes.LAVA_MOD_PORTAL.get(), pPos, pBlockState);
    }

    public boolean isNotaccessed() {
        return notaccessed;
    }

    public void   setAccessNumber() {
        accessnumber = 0;
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

    public boolean shouldRenderFace(Direction pFace) {
        return pFace.getAxis() == Direction.Axis.Y;
    }

}