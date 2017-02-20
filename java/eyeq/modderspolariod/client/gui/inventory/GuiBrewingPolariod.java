package eyeq.modderspolariod.client.gui.inventory;

import com.google.common.collect.Lists;
import eyeq.modderspolariod.ModdersPolaroid;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.*;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class GuiBrewingPolariod extends GuiBrewingStand implements IGuiPolaroid {
    private static final List<ItemStack> vanillaInputs = Lists.newArrayList();
    private static final List<ItemStack> vanillaIngredients = Lists.newArrayList();

    // PotionHelper ## public static void init()
    static {
        ItemStack nether_wart = new ItemStack(Items.NETHER_WART);
        ItemStack golden_carrot = new ItemStack(Items.GOLDEN_CARROT);
        ItemStack redstone = new ItemStack(Items.REDSTONE);
        ItemStack fermented_spider_eye = new ItemStack(Items.FERMENTED_SPIDER_EYE);
        ItemStack rabbit_foot = new ItemStack(Items.RABBIT_FOOT);
        ItemStack glowstone_dust = new ItemStack(Items.GLOWSTONE_DUST);
        ItemStack magma_cream = new ItemStack(Items.MAGMA_CREAM);
        ItemStack sugar = new ItemStack(Items.SUGAR);
        ItemStack pufferfish = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata());
        ItemStack speckled_melon = new ItemStack(Items.SPECKLED_MELON);
        ItemStack spider_eye = new ItemStack(Items.SPIDER_EYE);
        ItemStack ghast_tear = new ItemStack(Items.GHAST_TEAR);
        ItemStack blaze_powder = new ItemStack(Items.BLAZE_POWDER);

        registerPotionTypeConversion(PotionTypes.WATER, speckled_melon);
        registerPotionTypeConversion(PotionTypes.WATER, ghast_tear);
        registerPotionTypeConversion(PotionTypes.WATER, rabbit_foot);
        registerPotionTypeConversion(PotionTypes.WATER, blaze_powder);
        registerPotionTypeConversion(PotionTypes.WATER, spider_eye);
        registerPotionTypeConversion(PotionTypes.WATER, sugar);
        registerPotionTypeConversion(PotionTypes.WATER, magma_cream);
        registerPotionTypeConversion(PotionTypes.WATER, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.WATER, redstone);
        registerPotionTypeConversion(PotionTypes.WATER, nether_wart);
        registerPotionTypeConversion(PotionTypes.AWKWARD, golden_carrot);
        registerPotionTypeConversion(PotionTypes.NIGHT_VISION, redstone);
        registerPotionTypeConversion(PotionTypes.NIGHT_VISION, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.LONG_NIGHT_VISION, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.INVISIBILITY, redstone);
        registerPotionTypeConversion(PotionTypes.AWKWARD, magma_cream);
        registerPotionTypeConversion(PotionTypes.FIRE_RESISTANCE, redstone);
        registerPotionTypeConversion(PotionTypes.AWKWARD, rabbit_foot);
        registerPotionTypeConversion(PotionTypes.LEAPING, redstone);
        registerPotionTypeConversion(PotionTypes.LEAPING, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.LEAPING, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.LONG_LEAPING, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.SLOWNESS, redstone);
        registerPotionTypeConversion(PotionTypes.SWIFTNESS, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.LONG_SWIFTNESS, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.AWKWARD, sugar);
        registerPotionTypeConversion(PotionTypes.SWIFTNESS, redstone);
        registerPotionTypeConversion(PotionTypes.SWIFTNESS, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.AWKWARD, pufferfish);
        registerPotionTypeConversion(PotionTypes.WATER_BREATHING, redstone);
        registerPotionTypeConversion(PotionTypes.AWKWARD, speckled_melon);
        registerPotionTypeConversion(PotionTypes.HEALING, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.HEALING, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.STRONG_HEALING, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.HARMING, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.POISON, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.LONG_POISON, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.STRONG_POISON, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.AWKWARD, spider_eye);
        registerPotionTypeConversion(PotionTypes.POISON, redstone);
        registerPotionTypeConversion(PotionTypes.POISON, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.AWKWARD, ghast_tear);
        registerPotionTypeConversion(PotionTypes.REGENERATION, redstone);
        registerPotionTypeConversion(PotionTypes.REGENERATION, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.AWKWARD, blaze_powder);
        registerPotionTypeConversion(PotionTypes.STRENGTH, redstone);
        registerPotionTypeConversion(PotionTypes.STRENGTH, glowstone_dust);
        registerPotionTypeConversion(PotionTypes.WATER, fermented_spider_eye);
        registerPotionTypeConversion(PotionTypes.WEAKNESS, redstone);
    }

    private static void registerPotionTypeConversion(PotionType input, ItemStack ingredient) {
        vanillaInputs.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), input));
        vanillaIngredients.add(ingredient);
    }

    public GuiBrewingPolariod(InventoryPlayer playerInventory, IInventory tileBrewingStand) {
        super(playerInventory, tileBrewingStand);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if(GuiPolaroid.isShow) {
            drawGuiInformation();
        }
    }

    public List<IBrewingRecipe> getRecipes() {
        return BrewingRecipeRegistry.getRecipes();
    }

    public IBrewingRecipe getRecipe(int index) {
        if(index < vanillaInputs.size()) {
            return getRecipes().get(0);
        }
        return getRecipes().get(index - vanillaInputs.size() + 1);
    }

    @Override
    public int getSize() {
        return getRecipes().size() + vanillaInputs.size() - 1;
    }

    @Override
    public ItemStack getRecipeOutput(int index) {
        if(index < vanillaInputs.size()) {
            return getRecipe(0).getOutput(vanillaInputs.get(index), vanillaIngredients.get(index));
        }
        IBrewingRecipe recipe = getRecipe(index - vanillaInputs.size());
        if(recipe instanceof AbstractBrewingRecipe) {
            return ((AbstractBrewingRecipe) recipe).getOutput();
        }
        return null;
    }

    @Override
    public boolean setRecipe() {
        IBrewingRecipe recipe = getRecipe(VARIABLE.index);
        if(recipe instanceof BrewingRecipe) {
            getBrewingRecipe((BrewingRecipe) recipe);
            return true;
        }
        if(recipe instanceof BrewingOreRecipe) {
            return getBrewingOreRecipe((BrewingOreRecipe) recipe);
        }
        if(recipe instanceof VanillaBrewingRecipe) {
            getVanillaBrewingRecipe((VanillaBrewingRecipe) recipe);
            return true;
        }
        return true;
    }

    private void getBrewingRecipe(BrewingRecipe recipe) {
        ItemStack src = recipe.getIngredient();
        if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            src.setItemDamage(0);
        }
        this.inventorySlots.inventorySlots.get(3).putStack(src);
        this.inventorySlots.inventorySlots.get(0).putStack(recipe.getInput());
        this.inventorySlots.inventorySlots.get(2).putStack(recipe.getOutput());
    }

    private boolean getBrewingOreRecipe(BrewingOreRecipe recipe) {
        List<ItemStack> ingredients = recipe.getIngredient();
        ItemStack src = ingredients.get(VARIABLE.dicIndex);
        if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            src.setItemDamage(0);
        }
        this.inventorySlots.inventorySlots.get(3).putStack(src);
        this.inventorySlots.inventorySlots.get(0).putStack(recipe.getInput());
        this.inventorySlots.inventorySlots.get(2).putStack(recipe.getOutput());

        return VARIABLE.dicIndex >= ingredients.size() - 1;
    }

    private void getVanillaBrewingRecipe(VanillaBrewingRecipe recipe) {
        ItemStack src = vanillaIngredients.get(VARIABLE.index).copy();
        if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            src.setItemDamage(0);
        }
        ItemStack input = vanillaInputs.get(VARIABLE.index);
        this.inventorySlots.inventorySlots.get(3).putStack(src);
        this.inventorySlots.inventorySlots.get(0).putStack(input);
        this.inventorySlots.inventorySlots.get(1).putStack(new ItemStack(ModdersPolaroid.rightArrow));
        this.inventorySlots.inventorySlots.get(2).putStack(recipe.getOutput(input, src));
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
        IBrewingRecipe recipe = getRecipe(VARIABLE.index);
        drawString("Type:" + recipe.getClass().getSimpleName(), y);
        y += 10;
        drawString("Recipe Output", y);
        y += 10;
        ItemStack output = inventorySlots.inventorySlots.get(2).getStack();
        drawString(String.valueOf(2) + ":" + (output.isEmpty() ? "----" : output.getDisplayName()), y);
        y += 10;
        drawString("Recipe Input", y);
        y += 10;
        ItemStack input = inventorySlots.inventorySlots.get(0).getStack();
        drawString(String.valueOf(0) + ":" + (input.isEmpty() ? "----" : input.getDisplayName()), y);
        y += 10;
        drawString("Recipe Ingredient", y);
        y += 10;
        ItemStack ingredient = inventorySlots.inventorySlots.get(3).getStack();
        drawString(String.valueOf(3) + ":" + (ingredient.isEmpty() ? "----" : ingredient.getDisplayName()), y);
        y += 10;

        drawString(String.valueOf(VARIABLE.index) + "/" + String.valueOf(getSize() - 1), this.height - 10);
    }

    private int getDicSize(IBrewingRecipe recipe) {
        if(recipe instanceof BrewingRecipe) {
            return 1;
        }
        if(recipe instanceof BrewingOreRecipe) {
            return ((BrewingOreRecipe) recipe).getIngredient().size();
        }
        if(recipe instanceof VanillaBrewingRecipe) {
            return 1;
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
        return "brewing";
    }

    @Override
    public Container getInventorySlots() {
        return inventorySlots;
    }
}
