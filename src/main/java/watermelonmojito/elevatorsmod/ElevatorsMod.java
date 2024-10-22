package watermelonmojito.elevatorsmod;

import net.minecraft.core.sound.BlockSoundDispatcher;
import net.minecraft.core.sound.BlockSounds;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import watermelonmojito.elevatorsmod.server.ElevatorBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;


public class ElevatorsMod implements ModInitializer, GameStartEntrypoint {
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

		Block.blocksList[437] = null;
		Block.blocksList[437] = elevator = new BlockBuilder(MOD_ID)
			.setTopTexture("minecraft:block/block_steel_top")
			.setBottomTexture("minecraft:block/block_steel_bottom")
			.setSideTextures("minecraft:block/block_steel_side")
			.setHardness(5.0F)
			.setResistance(2000.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build(new ElevatorBlock("block.steel", 437, Material.metal));
		elevator.setKey("block.steel");
		BlockSoundDispatcher.getInstance().addDispatch(ElevatorsMod.elevator, BlockSounds.METAL);
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}
}
