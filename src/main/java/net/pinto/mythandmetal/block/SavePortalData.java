package net.pinto.mythandmetal.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class SavePortalData extends SavedData {
    private static final String DATA_NAME = "my_world_data";
    private int myVariable = 0; // Default value

    // Method to load data from an NBT tag
    public static SavePortalData load(CompoundTag tag) {
        SavePortalData data = new SavePortalData();
        data.myVariable = tag.getInt("myVariable");
        return data;
    }

    // Method to save data to an NBT tag
    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("myVariable", myVariable);
        return tag;
    }

    // Get the saved data instance from the world
    public static SavePortalData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(SavePortalData::load, SavePortalData::new, DATA_NAME);
    }

    // Setter method for your variable
    public void setMyVariable(int value) {
        this.myVariable = value;
        setDirty(); // Marks the data as changed, ensuring it saves
    }

    // Getter method for your variable
    public int getMyVariable() {
        return this.myVariable;
    }
}