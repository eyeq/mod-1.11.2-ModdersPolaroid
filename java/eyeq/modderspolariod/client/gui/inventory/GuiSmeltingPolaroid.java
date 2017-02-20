package eyeq.modderspolariod.client.gui.inventory;

import java.util.Map;
import java.util.Map.Entry;

import eyeq.modderspolariod.ModdersPolaroid;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.input.Keyboard;

public class GuiSmeltingPolaroid extends GuiFurnace implements IGuiPolaroid {
    public GuiSmeltingPolaroid(InventoryPlayer playerInv, IInventory furnaceInv) {
        super(playerInv, furnaceInv);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if(GuiPolaroid.isShow) {
            drawGuiInformation();
        }
    }

    public Map<ItemStack, ItemStack> getRecipes() {
        return FurnaceRecipes.instance().getSmeltingList();
    }

    public Entry<ItemStack, ItemStack> getEntry(int index) {
        int i = 0;
        for(Entry<ItemStack, ItemStack> entry : getRecipes().entrySet()) {
            if(i == index) {
                return entry;
            }
            i++;
        }
        return null;
    }

    @Override
    public int getSize() {
        return getRecipes().size();
    }

    @Override
    public ItemStack getRecipeOutput(int index) {
        return getEntry(index).getValue();
    }

    @Override
    public boolean setRecipe() {
        Entry<ItemStack, ItemStack> entry = getEntry(VARIABLE.index);
        ItemStack src = entry.getKey().copy();
        if(src.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            src.setItemDamage(0);
        }
        this.inventorySlots.inventorySlots.get(0).putStack(src);
        this.inventorySlots.inventorySlots.get(2).putStack((ItemStack) entry.getValue());
        return true;
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
        // no type
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

        drawString(String.valueOf(VARIABLE.index) + "/" + String.valueOf(getSize() - 1), this.height - 10);
    }

    @Override
    public void next() {
        VARIABLE.index = (VARIABLE.index + 1) % getSize();
        VARIABLE.dicIndex = 0;
    }

    @Override
    public void prev() {
        int size = getSize();
        VARIABLE.index = (size + VARIABLE.index - 1) % size;
        VARIABLE.dicIndex = 0;
    }

    @Override
    public String getName() {
        return "smelting";
    }

    @Override
    public Container getInventorySlots() {
        return inventorySlots;
    }
}
