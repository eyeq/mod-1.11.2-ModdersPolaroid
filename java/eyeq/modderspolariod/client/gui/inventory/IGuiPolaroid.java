package eyeq.modderspolariod.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public interface IGuiPolaroid {
    Variable VARIABLE = new Variable();

    int getSize();

    ItemStack getRecipeOutput(int index);

    boolean setRecipe();

    void drawGuiInformation();

    void next();

    void prev();

    String getName();

    void setWorldAndResolution(Minecraft mc, int width, int height);

    void drawScreen(int mouseX, int mouseY, float partialTicks);

    Container getInventorySlots();

    class Variable {
        public int index = 0;
        public int dicIndex = 0;
    }
}
