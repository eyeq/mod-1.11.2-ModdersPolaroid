package eyeq.modderspolariod;

import eyeq.modderspolariod.event.ModdersPolaroidEventHandler;
import eyeq.modderspolariod.network.ModdersPolaroidGuiHandler;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.File;

import static eyeq.modderspolariod.ModdersPolaroid.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class ModdersPolaroid {
    public static final String MOD_ID = "eyeq_modderspolaroid";

    @Mod.Instance(MOD_ID)
    public static ModdersPolaroid instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static final int GUI_ID = 0;

    public static KeyBinding changeKey = new KeyBinding("key.change", Keyboard.KEY_O, "Modder's Polaroid");
    public static KeyBinding autoKey = new KeyBinding("key.auto", Keyboard.KEY_P, "Modder's Polaroid");
    public static KeyBinding nextKey = new KeyBinding("key.next", Keyboard.KEY_RIGHT, "Modder's Polaroid");
    public static KeyBinding prevKey = new KeyBinding("key.prev", Keyboard.KEY_LEFT, "Modder's Polaroid");
    public static KeyBinding shotKey = new KeyBinding("key.shot", Keyboard.KEY_UP, "Modder's Polaroid");
    public static KeyBinding showKey = new KeyBinding("key.show", Keyboard.KEY_DOWN, "Modder's Polaroid");

    public static int x;
    public static int y;
    public static int width;
    public static int height;
    public static int interval;

    public static Item rightArrow;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModdersPolaroidEventHandler());
        load(new Configuration(event.getSuggestedConfigurationFile()));
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModdersPolaroidGuiHandler());
        ClientRegistry.registerKeyBinding(nextKey);
        ClientRegistry.registerKeyBinding(prevKey);
        ClientRegistry.registerKeyBinding(shotKey);
        ClientRegistry.registerKeyBinding(autoKey);
        ClientRegistry.registerKeyBinding(showKey);
    }

    public static void load(Configuration config) {
        config.load();

        String category = "Int";
        x = config.get(category, "x", 306).getInt();
        y = config.get(category, "y", 104).getInt();
        width = config.get(category, "width", 236).getInt();
        height = config.get(category, "height", 112).getInt();
        interval = config.get(category, "interval", 1).getInt();

        if(config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event){
        rightArrow = new Item();

        GameRegistry.register(rightArrow, resource.createResourceLocation("right_arrow"));
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(rightArrow);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-ModdersPolaroid4");

        UModelCreator.createItemJson(project, rightArrow, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
    }
}
