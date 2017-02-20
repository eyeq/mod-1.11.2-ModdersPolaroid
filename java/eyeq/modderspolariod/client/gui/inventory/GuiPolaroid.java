package eyeq.modderspolariod.client.gui.inventory;

import eyeq.modderspolariod.ModdersPolaroid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuiPolaroid extends GuiContainer {
    public IGuiPolaroid gui;
    public Container inventoryContainer;
    public InventoryPlayer playerInventory;
    public World world;
    public BlockPos blockPos;
    public IInventory tileFurnace;
    public IInventory tileBrewingStand;

    public Date date = new Date();

    public static boolean isShow = true;
    public static boolean isAuto = false;
    public static int tick = 0;

    public GuiPolaroid(Container inventoryContainer, InventoryPlayer playerInventory, World world, BlockPos blockPos) {
        super(new ContainerWorkbench(playerInventory, world, blockPos));
        this.gui = new GuiCraftingPolaroid(playerInventory, world, blockPos);
        this.inventoryContainer = inventoryContainer;
        this.playerInventory = playerInventory;
        this.world = world;
        this.blockPos = blockPos;
        this.tileFurnace = new TileEntityFurnace() {
            @Override
            public int getField(int id) {
                return 200;
            }
        };
        this.tileBrewingStand = new TileEntityBrewingStand() {
            @Override
            public int getField(int id) {
                return 1;
            }
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final int size = gui.getSize();
        if(IGuiPolaroid.VARIABLE.index < 0 || size <= IGuiPolaroid.VARIABLE.index) {
            gui.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }

        for(int i = 0; i < 11; i++) {
            gui.getInventorySlots().inventorySlots.get(i).putStack(ItemStack.EMPTY);
        }
        final boolean dicEnd = gui.setRecipe();
        gui.drawScreen(mouseX, mouseY, partialTicks);

        if(GuiPolaroid.isAuto) {
            saveScreenShot();
            tick++;
            if(tick < ModdersPolaroid.interval) {
                return;
            }
            tick = 0;

            if(!dicEnd) {
                IGuiPolaroid.VARIABLE.dicIndex++;
            } else if(IGuiPolaroid.VARIABLE.index < size - 1) {
                IGuiPolaroid.VARIABLE.dicIndex = 0;
                IGuiPolaroid.VARIABLE.index++;
            } else {
                GuiPolaroid.isAuto = false;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(keyCode == ModdersPolaroid.changeKey.getKeyCode()) {
            if(gui instanceof GuiCraftingPolaroid) {
                gui = new GuiSmeltingPolaroid(playerInventory, tileFurnace);
            } else if(gui instanceof GuiSmeltingPolaroid) {
                gui = new GuiFuelPolaroid(playerInventory, tileFurnace);
            } else if(gui instanceof GuiFuelPolaroid) {
                gui = new GuiBrewingPolariod(playerInventory, tileBrewingStand);
            } else if(gui instanceof GuiBrewingPolariod) {
                gui = new GuiCraftingPolaroid(playerInventory, world, blockPos);
            }
            gui.setWorldAndResolution(mc, width, height);
            IGuiPolaroid.VARIABLE.index = 0;
            IGuiPolaroid.VARIABLE.dicIndex = 0;
            return;
        }
        if(keyCode == ModdersPolaroid.nextKey.getKeyCode()) {
            tick = 0;
            gui.next();
            return;
        }
        if(keyCode == ModdersPolaroid.prevKey.getKeyCode()) {
            tick = 0;
            gui.prev();
            return;
        }
        if(keyCode == ModdersPolaroid.shotKey.getKeyCode()) {
            this.saveScreenShot();
            return;
        }
        if(keyCode == ModdersPolaroid.autoKey.getKeyCode()) {
            if(isAuto) {
                tick = 0;
            }
            isAuto = !isAuto;
            return;
        }
        if(keyCode == ModdersPolaroid.showKey.getKeyCode()) {
            isShow = !isShow;
            return;
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        gui.setWorldAndResolution(mc, width, height);
    }

    public void saveScreenShot() {
        this.saveScreenShot(IGuiPolaroid.VARIABLE.index, gui.getRecipeOutput(IGuiPolaroid.VARIABLE.index));
    }

    public void saveScreenShot(int index, ItemStack itemStack) {
        Minecraft mc = Minecraft.getMinecraft();
        if(!mc.world.isRemote) {
            return;
        }
        if(itemStack == null) {
            return;
        }

        final int displayWidth = mc.displayWidth;
        final int displayHeight = mc.displayHeight;
        final int size = displayWidth * displayHeight;
        IntBuffer buf = BufferUtils.createIntBuffer(size);
        int[] pixels = new int[size];

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        buf.clear();
        GL11.glReadPixels(0, 0, displayWidth, displayHeight, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buf);
        buf.get(pixels);
        arrayCopy(pixels, displayWidth, displayHeight);
        BufferedImage bufferedimage = new BufferedImage(displayWidth, displayHeight, 1);
        bufferedimage.setRGB(0, 0, displayWidth, displayHeight, pixels, 0, displayWidth);
        BufferedImage trimmingImage = bufferedimage.getSubimage(ModdersPolaroid.x, ModdersPolaroid.y, ModdersPolaroid.width, ModdersPolaroid.height);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dateStr = sdf.format(date);

        File screenshotsDir = new File(mc.mcDataDir, "screenshots");
        File dir = new File(screenshotsDir, dateStr + "/" + gui.getName());
        File tabDir = new File(dir, itemStack.getItem().getCreativeTab().getTabLabel());
        if(!tabDir.exists()) {
            if(!tabDir.mkdirs()) {
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Could not create file"));
                return;
            }
        }

        String outputItemName = itemStack.getDisplayName();
        String fileName = String.valueOf(index) + "_" + outputItemName;

        try {
            shot(trimmingImage, dir, fileName);
            shot(trimmingImage, tabDir, fileName);
        } catch(Exception exception) {
            exception.printStackTrace();
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Failed to save: " + exception));
        }
    }

    private void shot(BufferedImage trimmingImage, File dir, String fileName) throws IOException {
        File file = new File(dir, fileName + ".png");
        if(file.exists()) {
            return;
        }
        ImageIO.write(trimmingImage, "png", file);
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Modder's Polaroid save: " + file.getName()));
    }

    private void arrayCopy(int[] pixels, int displayWidth, int displayHeight) {
        int[] temp = new int[displayWidth];
        int n = displayHeight / 2;
        for(int i = 0; i < n; i++) {
            System.arraycopy(pixels, i * displayWidth, temp, 0, displayWidth);
            System.arraycopy(pixels, (displayHeight - 1 - i) * displayWidth, pixels, i * displayWidth, displayWidth);
            System.arraycopy(temp, 0, pixels, (displayHeight - 1 - i) * displayWidth, displayWidth);
        }
    }
}
