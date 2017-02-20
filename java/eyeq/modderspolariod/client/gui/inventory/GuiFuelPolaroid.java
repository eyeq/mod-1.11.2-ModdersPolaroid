package eyeq.modderspolariod.client.gui.inventory;

import java.util.List;

import eyeq.modderspolariod.ModdersPolaroid;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

public class GuiFuelPolaroid extends GuiFurnace implements IGuiPolaroid {
    public static final List<ItemStack> fuelList = Lists.newArrayList();

    static {
        for(Item obj : Item.REGISTRY) {
            ItemStack itemStack = new ItemStack(obj);
            if(TileEntityFurnace.isItemFuel(itemStack)) {
                fuelList.add(itemStack);
            }
        }
    }

    public GuiFuelPolaroid(InventoryPlayer playerInv, IInventory furnaceInv) {
        super(playerInv, furnaceInv);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if(GuiPolaroid.isShow) {
            drawGuiInformation();
        }
    }

    @Override
    public int getSize() {
        return fuelList.size();
    }

    @Override
    public ItemStack getRecipeOutput(int index) {
        return fuelList.get(index);
    }

    @Override
    public boolean setRecipe() {
        ItemStack src = getRecipeOutput(VARIABLE.index);
        this.inventorySlots.inventorySlots.get(1).putStack(src);
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
        drawString("Efficiency Output", y);
        y += 10;
        ItemStack input = inventorySlots.inventorySlots.get(1).getStack();
        drawString("BurnTime / 10" + ":" + (input.isEmpty() ? "----" : String.valueOf((double) TileEntityFurnace.getItemBurnTime(input) / 10)), y);
        y += 10;
        drawString("Fuel Input", y);
        y += 10;
        drawString(String.valueOf(1) + ":" + (input.isEmpty() ? "----" : input.getDisplayName()), y);
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
        return "fuel";
    }

    @Override
    public Container getInventorySlots() {
        return inventorySlots;
    }
}
