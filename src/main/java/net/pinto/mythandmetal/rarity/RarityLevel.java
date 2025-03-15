package net.pinto.mythandmetal.rarity;


public enum RarityLevel {

    // Common levels
    TIERONE("I", 1.0f,0),

    // Uncommon levels
    TIERTWO("II", 1.2f,0),


    // Rare levels
    TIERTHREA("III", 1.5f,0),


    // Epic levels
    TIERFOUR("IV", 2.0f,0),


    // Legendary levels
    TIERFIVE("V", 3.0f,0),


    TIERSIX("VI", 3.8f,0),


    TIERSEVEN("VII", 5.0f,0);

    private final String name;
    private final float multiplier;
    private final int enchantment;

    RarityLevel(String name, float multiplier,int enchantment) {
        this.name = name;
        this.multiplier = multiplier;
        this.enchantment = enchantment;

    }

    public String getName() {
        return name;
    }

    public int getEnchantment() {
        return enchantment;
    }

    public float getMultiplier() {
        return multiplier;
    }
}
