package eyeq.modderspolariod.client.gui.inventory;

import java.util.List;

import eyeq.modderspolariod.ModdersPolaroid;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.input.Keyboard;

public class GuiCraftingPolaroid extends GuiCrafting implements IGuiPolaroid {
    private static String[] heightName;
    private static String[] widthName;

    public GuiCraftingPolaroid(InventoryPlayer playerInventory, World world) {
        this(playerInventory, world, BlockPos.ORIGIN);
    }

    public GuiCraftingPolaroid(InventoryPlayer playerInventory, World world, BlockPos pos) {
        super(playerInventory, world, pos);
        heightName = ObfuscationReflectionHelper.remapFieldNames(ShapedOreRecipe.class.getName(), "height");
        widthName = ObfuscationReflectionHelper.remapFieldNames(ShapedOreRecipe.class.getName(), "width");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if(GuiPolaroid.isShow) {
            drawGuiInformation();
        }
    }

    public List<IRecipe> getRecipes() {
        return CraftingManager.getInstance().getRecipeList();
    }

    public IRecipe getRecipe(int index) {
        return getRecipes().get(index);
    }

    @Override
    public int getSize() {
        return getRecipes().size();
    }

    @Override
    public ItemStack getRecipeOutput(int index) {
        return getRecipe(index).getRecipeOutput();
    }

    @Override
    public boolean setRecipe() {
        IRecipe recipe = getRecipe(VARIABLE.index);
        if(recipe instanceof ShapedRecipes) {
            setShapedRecipe((ShapedRecipes) recipe);
            return true;
        }
        if(recipe instanceof ShapelessRecipes) {
            setShapelessRecipe((ShapelessRecipes) recipe);
            return true;
        }
        if(recipe instanceof ShapedOreRecipe) {
            return setShapedOreRecipe((ShapedOreRecipe) recipe);
        }
        if(recipe instanceof ShapelessOreRecipe) {
            return setShapelessOreRecipe((ShapelessOreRecipe) recipe);
        }
        return true;
    }

    private void setShapedRecipe(ShapedRecipes recipe) {
        for(int i = 0; i < recipe.recipeHeight; i++) {
            for(int j = 0; j < recipe.recipeWidth; j++) {
                ItemStack obj = recipe.recipeItems[i * recipe.recipeWidth + j];
                if(obj == null) {
                    continue;
                }

                ItemStack src = obj.copy();
                if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    src.setItemDamage(0);
                }
                src.setCount(1);
                this.inventorySlots.inventorySlots.get(i * 3 + j + 1).putStack(src);
            }
        }
        this.inventorySlots.inventorySlots.get(0).putStack(recipe.getRecipeOutput());
    }

    private void setShapelessRecipe(ShapelessRecipes recipe) {
        for(int i = 0; i < recipe.recipeItems.size(); i++) {
            ItemStack src = recipe.recipeItems.get(i).copy();
            if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                src.setItemDamage(0);
            }
            this.inventorySlots.inventorySlots.get(i + 1).putStack(src);
        }
        this.inventorySlots.inventorySlots.get(0).putStack(recipe.getRecipeOutput());
    }

    private boolean setShapedOreRecipe(ShapedOreRecipe recipe) {
        int recipeHeight = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, heightName);
        int recipeWidth = ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, widthName);

        int max = 0;
        for(int i = 0; i < recipeHeight; i++) {
            for(int j = 0; j < recipeWidth; j++) {
                Object input = recipe.getInput()[i * recipeWidth + j];
                if(input == null) {
                    continue;
                }

                if(input instanceof List) {
                    List list = (List) input;
                    if(max < list.size()) {
                        max = list.size();
                    }
                    if(list.size() > 0) {
                        ItemStack src = ((ItemStack) ((VARIABLE.dicIndex < list.size()) ? list.get(VARIABLE.dicIndex) : list.get(list.size() - 1))).copy();
                        if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                            src.setItemDamage(0);
                        }
                        this.inventorySlots.inventorySlots.get(i * 3 + j + 1).putStack(src);
                    }
                } else if(input instanceof ItemStack) {
                    ItemStack src = ((ItemStack) input).copy();
                    if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        src.setItemDamage(0);
                    }
                    this.inventorySlots.inventorySlots.get(i * 3 + j + 1).putStack(src);
                }
            }
        }
        this.inventorySlots.inventorySlots.get(0).putStack(recipe.getRecipeOutput());

        return VARIABLE.dicIndex >= max - 1;
    }

    private boolean setShapelessOreRecipe(ShapelessOreRecipe recipe) {
        int max = 0;
        for(int i = 0; i < recipe.getInput().size(); i++) {
            Object input = recipe.getInput().get(i);
            if(input instanceof List) {
                List list = (List) input;
                if(max < list.size()) {
                    max = list.size();
                }
                if(list.size() > 0) {
                    ItemStack src = ((ItemStack) ((VARIABLE.dicIndex < list.size()) ? list.get(VARIABLE.dicIndex) : list.get(list.size() - 1))).copy();
                    if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        src.setItemDamage(0);
                    }
                    this.inventorySlots.inventorySlots.get(i + 1).putStack(src);
                }
            } else if(input instanceof ItemStack) {
                ItemStack src = ((ItemStack) input).copy();
                if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    src.setItemDamage(0);
                }
                this.inventorySlots.inventorySlots.get(i + 1).putStack(src);
            }
        }
        this.inventorySlots.inventorySlots.get(0).putStack(recipe.getRecipeOutput());

        return VARIABLE.dicIndex >= max - 1;
    }

    public void drawString(String str, int y, int color) {
        fontRendererObj.drawString(str, this.width - fontRendererObj.getStringWidth(str), y, color);
    }

    public void drawString(String str, int y) {
        drawString(str, y, 0xFFFFFF);
    }

    @Override
    public void drawGuiInformation() {
        int y = 0;
        drawString("Key Binding", y);
        y += 10;
        drawString("Change:" + Keyboard.getKeyName(ModdersPolaroid.changeKey.getKeyCode()), y);
        y += 10;
        drawString("Auto:" + Keyboard.getKeyName(ModdersPolaroid.autoKey.getKeyCode()), y);
        y += 10;
        drawString("Next:" + Keyboard.getKeyName(ModdersPolaroid.nextKey.getKeyCode()), y);
        y += 10;
        drawString("Prev:" + Keyboard.getKeyName(ModdersPolaroid.prevKey.getKeyCode()), y);
        y += 10;
        drawString("Shot:" + Keyboard.getKeyName(ModdersPolaroid.shotKey.getKeyCode()), y);
        y += 10;
        drawString("Show/Hide:" + Keyboard.getKeyName(ModdersPolaroid.showKey.getKeyCode()), y);

        y += 20;
        IRecipe recipe = getRecipe(VARIABLE.index);
        drawString("Type:" + recipe.getClass().getSimpleName(), y);
        y += 10;
        drawString("Recipe Output", y);
        y += 10;
        ItemStack output = inventorySlots.inventorySlots.get(0).getStack();
        drawString(String.valueOf(0) + ":" + (output.isEmpty() ? "----" : output.getDisplayName()), y);
        y += 10;
        drawString("Recipe Input", y);
        y += 10;
        for(int i = 1; i < 10; i++) {
            ItemStack input = inventorySlots.inventorySlots.get(i).getStack();
            drawString(String.valueOf(i) + ":" + (input.isEmpty() ? "----" : input.getDisplayName()), y);
            y += 10;
        }

        drawString(String.valueOf(VARIABLE.index) + "/" + String.valueOf(getSize() - 1), this.height - 10);
    }

    private int getDicSize(IRecipe recipe) {
        if(recipe instanceof ShapedRecipes) {
            return 1;
        }
        if(recipe instanceof ShapelessRecipes) {
            return 1;
        }
        if(recipe instanceof ShapedOreRecipe) {
            int max = 0;
            int length = ((ShapedOreRecipe) recipe).getInput().length;
            for(int i = 0; i < length; i++) {
                Object input = ((ShapedOreRecipe) recipe).getInput()[i];
                if(input == null) {
                    continue;
                }
                if(input instanceof List) {
                    int size = ((List) input).size();
                    if(max < size) {
                        max = size;
                    }
                }
            }
            return max;
        }
        if(recipe instanceof ShapelessOreRecipe) {
            int max = 0;
            int length = ((ShapelessOreRecipe) recipe).getInput().size();
            for(int i = 0; i < length; i++) {
                Object input = ((ShapelessOreRecipe) recipe).getInput().get(i);
                if(input instanceof List) {
                    int size = ((List) input).size();
                    if(max < size) {
                        max = size;
                    }
                }
            }
            return max;
        }
        return 1;
    }

    @Override
    public void next() {
        if(VARIABLE.dicIndex < getDicSize(getRecipe(VARIABLE.index)) - 1) {
            VARIABLE.dicIndex++;
            return;
        }
        VARIABLE.index = (VARIABLE.index + 1) % getSize();
        VARIABLE.dicIndex = 0;
    }

    @Override
    public void prev() {
        if(VARIABLE.dicIndex > 0) {
            VARIABLE.dicIndex--;
            return;
        }
        int size = getSize();
        VARIABLE.index = (size + VARIABLE.index - 1) % size;
        VARIABLE.dicIndex = getDicSize(getRecipe(VARIABLE.index)) - 1;
    }

    @Override
    public String getName() {
        return "crafting";
    }

    @Override
    public Container getInventorySlots() {
        return inventorySlots;
    }
}
