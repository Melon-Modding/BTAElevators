package watermelonmojito.elevatorsmod.mixin;

import watermelonmojito.elevatorsmod.ElevatorsMod;
import watermelonmojito.elevatorsmod.server.ElevatorBlock;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = EntityPlayer.class, remap = false)
public abstract class EntityPlayerMixin extends EntityLiving {

	@Unique
	protected int elevatorBlockX;
	@Unique
	protected int elevatorBlockY;
	@Unique
	protected int elevatorBlockZ;
	@Unique
	protected boolean stoodOnElevator;
	@Unique
	protected double py = 0;
	@Unique
	protected int cooldown = 0;
	@Unique
	protected EntityPlayer thisAs = (EntityPlayer)(Object)this;
	public EntityPlayerMixin(World world) {
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
				Block blockUnderFeet = world.getBlock(blockX, blockY, blockZ);

				if(blockUnderFeet instanceof ElevatorBlock) {
					stoodOnElevator = true;
					elevatorBlockX = blockX;
					elevatorBlockY = blockY;
					elevatorBlockZ = blockZ;
				} else if (blockUnderFeet != null || world.getBlockId(blockX, blockY, blockZ) == 0) {
					stoodOnElevator = false;
					cooldown += 1;
				}

				if(isSneaking() && cooldown <= 0 && blockUnderFeet instanceof ElevatorBlock && stoodOnElevator){
					ElevatorBlock.sneak(world, blockX, blockY, blockZ, thisAs);
					cooldown = ElevatorsMod.elevatorCooldown;
					stoodOnElevator = false;
					return;
				}
			}
		}


		if(dy > 0.075 &&  cooldown <= 0  && stoodOnElevator && Math.abs(this.x - (elevatorBlockX+0.5f)) < 0.5f && Math.abs(this.z - (elevatorBlockZ+0.5f)) < 0.5f && this.y - elevatorBlockY > 0){
			ElevatorBlock.jump(world, elevatorBlockX, elevatorBlockY, elevatorBlockZ, thisAs);
			cooldown = ElevatorsMod.elevatorCooldown;
			stoodOnElevator = false;
			return;
		}
	}
}
