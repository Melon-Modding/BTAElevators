package melonmojito.elevatorsmod;

import melonmojito.elevatorsmod.server.ElevatorBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.sound.block.BlockSoundDispatcher;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;


public class ElevatorsMod implements ModInitializer {
    public static final String MOD_ID = "elevatorsmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Block elevator;
	public static int elevatorCooldown;
	public static boolean allowObstructions;
	public static TomlConfigHandler config;
	static{

		Toml properties = new Toml("Elevator Configuration: ");
			properties.addEntry("allowObstructions", "↓ When set to true, blocks can be placed in-between two elevators", false);
			properties.addEntry("elevatorCooldown", "↓ Cooldown between teleportation between elevators (ticks)", 8);

		config = new TomlConfigHandler(null, MOD_ID, properties);
			allowObstructions = config.getBoolean("allowObstructions");
			elevatorCooldown = config.getInt("elevatorCooldown");

	}
    @Override
    public void onInitialize() {
        LOGGER.info("ElevatorsMod initialized.");
		if (!Global.isServer){
			BlockSoundDispatcher.getInstance();
		}
		Block.blocksList[437] = null;
		Block.blocksList[437] = elevator = new ElevatorBlock("block.steel", 437, Material.metal)
			.withTexCoords(19, 4, 19, 6, 19, 5)
			.withHardness(5.0F).withBlastResistance(2000.0F)
			.withTags(BlockTags.MINEABLE_BY_PICKAXE);
		if (!Global.isServer){
			BlockSoundDispatcher.getInstance().addDispatch(ElevatorsMod.elevator, BlockSounds.METAL);
		}
    }
}
