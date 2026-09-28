package dev.creationcore.recipe;

import dev.creationcore.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;

public final class CreativeCraftingRecipe implements Recipe<CraftingInput> {
    private final CraftingRecipe delegate;

    public CreativeCraftingRecipe(CraftingRecipe delegate) {
        this.delegate = delegate;
    }

    public CraftingRecipe delegate() {
        return delegate;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return delegate.matches(input, level);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return delegate.assemble(input, registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return delegate.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return delegate.getResultItem(registries);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = delegate.getRemainingItems(input);
        boolean path = delegate.getResultItem(null).is(Items.DIRT_PATH);
        boolean farmland = delegate.getResultItem(null).is(Items.FARMLAND);
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if ((path && stack.canPerformAction(ItemAbilities.SHOVEL_FLATTEN))
                    || (farmland && stack.canPerformAction(ItemAbilities.HOE_TILL))) {
                remaining.set(slot, stack.copyWithCount(1));
            } else if (stack.is(Items.POTION)) {
                remaining.set(slot, new ItemStack(Items.GLASS_BOTTLE));
            } else if (stack.is(Items.TADPOLE_BUCKET)) {
                remaining.set(slot, new ItemStack(Items.BUCKET));
            }
        }
        return remaining;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return delegate.getIngredients();
    }

    @Override
    public boolean showNotification() {
        return delegate.showNotification();
    }

    @Override
    public String getGroup() {
        return delegate.getGroup();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return delegate instanceof ShapedRecipe
                ? ModRecipes.CREATIVE_CRAFTING_SERIALIZER.get()
                : ModRecipes.CREATIVE_CRAFTING_SHAPELESS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CREATIVE_CRAFTING_TYPE.get();
    }
}
