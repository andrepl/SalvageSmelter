package com.norcode.bukkit.salvagesmelter;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

public class SmeltRecipe {
    private Material smeltable;
    private ItemStack result;
    private FurnaceRecipe recipe;
    
    public SmeltRecipe(Material smeltable, ItemStack result) {
        this.smeltable = smeltable;
        this.result = result;
        this.recipe = new FurnaceRecipe(this.result.clone(), smeltable);
    }

    public ItemStack getResult() {
        return result;
    }

    public Material getSmeltable() {
        return smeltable;
    }

    public FurnaceRecipe getFurnaceRecipe() {
        return this.recipe;
    }

    public void installFurnaceRecipe(JavaPlugin plugin) {
        boolean found = false;
        FurnaceRecipe fr = null;
        for (Recipe r: plugin.getServer().getRecipesFor(result)) {
            if (r instanceof FurnaceRecipe) {
                fr = (FurnaceRecipe) r;
                if (fr.getInput().getType().equals(smeltable) && fr.getResult().equals(result)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            plugin.getServer().addRecipe(this.recipe);
        }
    }
}
