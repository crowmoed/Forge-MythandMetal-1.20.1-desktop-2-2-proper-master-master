package net.pinto.mythandmetal.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

public class SavePortalData extends SavedData {
    private static final String DATA_NAME = "mythandmetal_global_data";
    private int myVariable = 0;
    private int dungeonlava = 0; // Default value

    // Method to load data from an NBT tag
    public static SavePortalData load(CompoundTag tag) {
        SavePortalData data = new SavePortalData();
        data.myVariable = tag.getInt("myVariable");
        data.dungeonlava = tag.getInt("dungeonlava");
        return data;
    }

    // Method to save data to an NBT tag
    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("myVariable", myVariable);
        tag.putInt("dungeonlava", dungeonlava);
        return tag;
    }

    // Get the saved data instance from the server
    public static SavePortalData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(SavePortalData::load, SavePortalData::new, DATA_NAME);
    }

    // Setter method for your variable
    public void setMyVariable(int value) {
        this.myVariable = value;
        setDirty(); // Marks the data as changed, ensuring it saves
    }

    public void setDungeonlava(int value) {
        this.dungeonlava = value;
        setDirty(); // Marks the data as changed, ensuring it saves
    }

    // Getter method for your variable
    public int getMyVariable() {
        return this.myVariable;
    }

    public int getDungeonlava() {
        return this.dungeonlava;
    }
}