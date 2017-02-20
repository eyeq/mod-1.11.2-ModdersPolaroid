package eyeq.modderspolariod.network;

import eyeq.modderspolariod.ModdersPolaroid;
import eyeq.modderspolariod.client.gui.inventory.GuiPolaroid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModdersPolaroidGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID != ModdersPolaroid.GUI_ID) {
            return null;
        }
        return new GuiPolaroid(player.inventoryContainer, player.inventory, world, new BlockPos(x, y, z));
    }
}
