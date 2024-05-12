package watermelonmojito.elevatorsmod.server;

import watermelonmojito.elevatorsmod.ElevatorsMod;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class ElevatorBlock extends Block{
	public ElevatorBlock(String key, int id, Material material) {
		super(key, id, material);
	}

	// returns true if we teleported
	public static boolean jump(World world, int x, int y, int z, EntityPlayer player){
		int counter = 2;
		for(int y2 = y+1; y2 < 255; y2++){
			if(counter > 0){
				counter--;
				if (world.getBlockId(x, y2, z) == Block.blockSteel.id){
					break;
				}
			}
			if(world.getBlock(x, y2, z) instanceof ElevatorBlock){
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !ElevatorsMod.allowObstructions) {
				break;
			}
		}
		return false;
	}

	// returns true if we teleported
	public static boolean sneak(World world, int x, int y, int z, EntityPlayer player){
		int counter = 2;
		for(int y2 = y-1; y2 > 0; y2--){
			if(counter > 0){
				counter--;
				if (world.getBlockId(x, y2, z) == Block.blockSteel.id){
					break;
				}
			}
			if(world.getBlock(x, y2, z) instanceof ElevatorBlock){
				teleport(x+0.5, y2+1, z+0.5, player);
				return true;
			}
			else if (world.getBlockId(x, y2, z) != 0 && !ElevatorsMod.allowObstructions) {
				break;
			}
		}
		return false;
	}

	public static void teleport(double x, double y, double z, EntityPlayer player){
		if (player.world.isClientSide)
			return;
		if (player instanceof EntityPlayerMP){
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			playerMP.playerNetServerHandler.teleport(x, y, z);
		} else if (player instanceof EntityPlayerSP) {
			EntityPlayerSP playerSP = (EntityPlayerSP)player;
			playerSP.setPos(x, y + playerSP.bbHeight, z);
		}
		player.world.playSoundAtEntity(null, player, "mob.ghast.fireball", 1f, 100f);
	}
}
