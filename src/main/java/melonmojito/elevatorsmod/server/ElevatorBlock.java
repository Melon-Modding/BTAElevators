package melonmojito.elevatorsmod.server;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;

public class ElevatorBlock extends Block{
	public ElevatorBlock(String key, int id, Material material) {
		super(key, id, material);
	}
	public AABB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		float f = 0.075F;
		return AABB.getBoundingBoxFromPool(x, y, z, x + 1, (float)(y + 1) - f, z + 1);
	}
	public boolean renderAsNormalBlock() {
		return false;
	}

	public static void jump(World world, int x, int y, int z, EntityPlayer player){
		for(int y2 = y+1; y2 < 255; y2++){
			if(world.getBlock(x, y2, z) instanceof ElevatorBlock){
				teleport(x+0.5, y2+1, z+0.5, player);
				break;
			}
			else if (world.getBlockId(x, y2, z) != 0) {
				break;
			}
		}
	}
	public static void sneak(World world, int x, int y, int z, EntityPlayer player){
		for(int y2 = y-1; y2 > 0; y2--){
			if(world.getBlock(x, y2, z) instanceof ElevatorBlock){
				teleport(x+0.5, y2+1, z+0.5, player);
				break;
			}
			else if (world.getBlockId(x, y2, z) != 0) {
				break;
			}
		}
	}

	public static void teleport(double x, double y, double z, EntityPlayer player){
		if (player instanceof EntityPlayerMP){
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			playerMP.playerNetServerHandler.teleportTo(x, y, z, player.yRot, player.xRot);
		} else if (player instanceof EntityPlayerSP) {
			EntityPlayerSP playerSP = (EntityPlayerSP)player;
			playerSP.setPos(x, y + playerSP.bbHeight, z);
		}
	}
}
