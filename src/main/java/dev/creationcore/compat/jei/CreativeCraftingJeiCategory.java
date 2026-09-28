package dev.creationcore.compat.jei;

import dev.creationcore.recipe.CreativeCraftingRecipe;
import dev.creationcore.registry.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.Arrays;

public final class CreativeCraftingJeiCategory implements IRecipeCategory<CreativeCraftingRecipe> {
    private final IDrawable icon;

    public CreativeCraftingJeiCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemLike(ModItems.CREATIVE_CRAFTING_TABLE.get());
    }

    @Override
    public RecipeType<CreativeCraftingRecipe> getRecipeType() {
        return CreationCoreJeiPlugin.CRAFTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.creationcore.creative_crafting_table");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 116;
    }

    @Override
    public int getHeight() {
        return 58;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CreativeCraftingRecipe recipe, IFocusGroup focuses) {
        int width = 3;
        if (recipe.delegate() instanceof ShapedRecipe shaped) {
            width = shaped.getWidth();
        }
        int slot = 0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (!ingredient.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, 1 + (slot % width) * 18, 1 + (slot / width) * 18)
                        .setStandardSlotBackground()
                        .addItemStacks(Arrays.asList(ingredient.getItems()));
            }
            slot++;
        }
        var level = Minecraft.getInstance().level;
        if (level != null) {
            ItemStack result = recipe.getResultItem(level.registryAccess());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19)
                    .setOutputSlotBackground()
                    .addItemStack(result);
        }
    }
}
