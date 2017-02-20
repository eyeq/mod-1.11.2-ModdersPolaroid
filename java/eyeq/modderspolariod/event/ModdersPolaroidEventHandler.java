package eyeq.modderspolariod.event;

import eyeq.modderspolariod.ModdersPolaroid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ModdersPolaroidEventHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if(minecraft.currentScreen != null || minecraft.ingameGUI.getChatGUI().getChatOpen()) {
            return;
        }
        if(ModdersPolaroid.autoKey.isPressed()) {
            EntityPlayerSP player = minecraft.player;
            player.openGui(ModdersPolaroid.instance, ModdersPolaroid.GUI_ID, minecraft.world, MathHelper.ceil(player.posX), MathHelper.ceil(player.posY), MathHelper.ceil(player.posZ));
        }
    }
}
