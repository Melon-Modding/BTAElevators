package melonmojito.elevatorsmod.mixin;

import melonmojito.elevatorsmod.ElevatorsMod;
import melonmojito.elevatorsmod.server.ElevatorBlock;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EntityPlayerMP.class, remap = false)
public abstract class EntityPlayerMPMixin extends EntityPlayer {

	@Unique
	protected int elevatorBlockX;
	@Unique
	protected int elevatorBlockY;
	@Unique
	protected int elevatorBlockZ;
	@Unique
	protected double py = 0;
	@Unique
	protected int cooldown = 0;
	public EntityPlayerMPMixin(World world) {
		super(world);
	}

	@Inject(method= "onLivingUpdate()V", at = @At("TAIL"))
	private void elevatorTick(CallbackInfo ci){
		cooldown--;
		double dy = this.y-py;
		py = this.y;

		List<AABB> cubes = this.world.getCubes(this, this.bb.getOffsetBoundingBox(this.xd, -1.0, 0.0));
		if (!cubes.isEmpty()){
			AABB cube = cubes.get(0);
			if (cube != null){

				int blockX = (int) cube.minX;
				int blockY = (int) cube.minY;
				int blockZ = (int) cube.minZ;

				if(world.getBlock(blockX, blockY, blockZ) instanceof ElevatorBlock) {
					elevatorBlockX = blockX;
					elevatorBlockY = blockY;
					elevatorBlockZ = blockZ;
				}

				if(isSneaking() && cooldown <= 0 && world.getBlock(blockX, blockY, blockZ) instanceof ElevatorBlock){
					ElevatorBlock.sneak(world, blockX, blockY, blockZ, (EntityPlayerMP)(Object)this);
					cooldown = 2; /*ElevatorsMod.config.getInt("ElevatorCooldown");*/
					return;
				}
			}
		}


		if(dy > 0.2 && cooldown <= 0 && Math.abs(this.x - (elevatorBlockX+0.5f)) < 0.5f && Math.abs(this.z - (elevatorBlockZ+0.5f)) < 0.5f && this.y - elevatorBlockY > 0){

			ElevatorBlock.jump(world, elevatorBlockX, elevatorBlockY, elevatorBlockZ, (EntityPlayerMP)(Object)this);
			cooldown = 2; /*ElevatorsMod.config.getInt("ElevatorCooldown");*/
			return;
		}
	}
}
