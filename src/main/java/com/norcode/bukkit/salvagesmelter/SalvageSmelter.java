package com.norcode.bukkit.salvagesmelter;

import java.util.HashSet;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class SalvageSmelter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        addRecipe(new ItemStack(Material.IRON_INGOT, 8), Material.IRON_CHESTPLATE);
        addRecipe(new ItemStack(Material.IRON_INGOT, 4), Material.IRON_BOOTS);
        addRecipe(new ItemStack(Material.IRON_INGOT, 5), Material.IRON_HELMET);
        addRecipe(new ItemStack(Material.IRON_INGOT, 7), Material.IRON_LEGGINGS);
        addRecipe(new ItemStack(Material.IRON_INGOT, 3), Material.IRON_AXE);
        addRecipe(new ItemStack(Material.IRON_INGOT, 3), Material.IRON_PICKAXE);
        addRecipe(new ItemStack(Material.IRON_INGOT, 1), Material.IRON_SPADE);
        addRecipe(new ItemStack(Material.IRON_INGOT, 2), Material.IRON_HOE);
        
        addRecipe(new ItemStack(Material.GOLD_INGOT, 8), Material.GOLD_CHESTPLATE);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 4), Material.GOLD_BOOTS);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 5), Material.GOLD_HELMET);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 7), Material.GOLD_LEGGINGS);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 3), Material.GOLD_AXE);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 3), Material.GOLD_PICKAXE);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 1), Material.GOLD_SPADE);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 2), Material.GOLD_HOE);
        
        addRecipe(new ItemStack(Material.DIAMOND, 8), Material.DIAMOND_CHESTPLATE);
        addRecipe(new ItemStack(Material.DIAMOND, 4), Material.DIAMOND_BOOTS);
        addRecipe(new ItemStack(Material.DIAMOND, 5), Material.DIAMOND_HELMET);
        addRecipe(new ItemStack(Material.DIAMOND, 7), Material.DIAMOND_LEGGINGS);
        addRecipe(new ItemStack(Material.DIAMOND, 3), Material.DIAMOND_AXE);
        addRecipe(new ItemStack(Material.DIAMOND, 3), Material.DIAMOND_PICKAXE);
        addRecipe(new ItemStack(Material.DIAMOND, 1), Material.DIAMOND_SPADE);
        addRecipe(new ItemStack(Material.DIAMOND, 2), Material.DIAMOND_HOE);
    }

    private HashSet<Material> smeltables = new HashSet<Material>();

    private void addRecipe(ItemStack result, Material source) {
        getServer().addRecipe(new FurnaceRecipe(result, source));
        smeltables.add(source);
    }

    @EventHandler
    public void onSmelt(FurnaceSmeltEvent event) {
        ItemStack orig = event.getSource();
        if (!smeltables.contains(orig.getType())) return;
        double percentage = (orig.getType().getMaxDurability() - orig.getDurability()) / (double) orig.getType().getMaxDurability(); 
        ItemStack result = getSalvage(orig.getType(), event.getResult().getType(), percentage);
        if (result == null || result.getAmount() == 0) {
            event.setResult(new ItemStack(Material.COAL, 1, (short)1));
        } else {
            event.setResult(result);
        }
    }

    public ItemStack getSalvage(Material product, Material raw, double damagePct) {
        for (Recipe r: getServer().getRecipesFor(new ItemStack(product))) {
            if (r instanceof ShapedRecipe) {
                int count = 0;
                getLogger().info(((ShapedRecipe) r).getIngredientMap().toString());
                for (Entry<Character, ItemStack> e: ((ShapedRecipe) r).getIngredientMap().entrySet()) {
                    if (e.getValue() != null && e.getValue().getType().equals(raw)) {
                        int q = e.getValue().getAmount();
                        char c = e.getKey();
                        for (String s: ((ShapedRecipe) r).getShape()) {
                            for (int i=0;i<s.length();i++) {
                                if (s.charAt(i) == c) {
                                    count += q;
                                }
                            }
                        }
                    }
                }
                if (raw.equals(Material.GOLD_INGOT)) {
                    int qty = (int) Math.floor(damagePct * (count * 9));
                    return new ItemStack(Material.GOLD_NUGGET, qty);
                }
                int qty = (int) Math.floor(damagePct * count);
                return new ItemStack(raw, qty);
            }
        }
        return null;
    }
}
