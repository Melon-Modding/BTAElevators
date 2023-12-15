package melonmojito.elevatorsmod.server;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
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

	public static void jump(World world, int x, int y, int z, EntityPlayerMP player){
		for(int y2 = y+1; y2 < 255; y2++){
			if(world.getBlock(x, y2, z) instanceof ElevatorBlock){
				player.playerNetServerHandler.teleportTo(x+0.5, y2+1, z+0.5, player.yRot, player.xRot);
				break;
			}
			else if (world.getBlockId(x, y2, z) != 0) {
				break;
			}


		}
	}
	public static void sneak(World world, int x, int y, int z, EntityPlayerMP player){
		for(int y2 = y-1; y2 > 0; y2--){
			if(world.getBlock(x, y2, z) instanceof ElevatorBlock){
				player.playerNetServerHandler.teleportTo(x+0.5, y2+1, z+0.5, player.yRot, player.xRot);
				break;
			}
			else if (world.getBlockId(x, y2, z) != 0) {
				break;
			}
		}
	}
}
