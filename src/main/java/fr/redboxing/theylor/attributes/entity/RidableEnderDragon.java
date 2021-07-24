package fr.redboxing.theylor.attributes.entity;

import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.competences.competences.LastResource;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.boss.dragon.IMixinEnderDragonEntity;
import fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.IMixinLivingEntity;
import fr.redboxing.theylor.attributes.utils.MutableVec3d;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RidableEnderDragon extends EnderDragonEntity {
    private static final int SHOOT_COOLDOWN = 5;
    private long lastShoot;
    private long lastPassengerTime;

    public RidableEnderDragon(World world) {
        super(EntityType.ENDER_DRAGON, world);

        UseEntityCallback.EVENT.register(this::onInteractEntity);
        LivingEntityDamageCallback.EVENT.register(this::onPlayerDamage);
    }

    private ActionResult onPlayerDamage(LivingEntity entity, DamageSource source, float v) {
        if(entity instanceof PlayerEntity && entity.hasVehicle() && entity.getVehicle() == this) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    private ActionResult onInteractEntity(PlayerEntity playerEntity, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        RidableEnderDragon dragon = null;

        if(entity instanceof RidableEnderDragon) {
            dragon = (RidableEnderDragon) entity;
        } else if(entity instanceof EnderDragonPart) {
            EnderDragonEntity owner = ((EnderDragonPart) entity).owner;
            if(owner instanceof RidableEnderDragon) {
                dragon = (RidableEnderDragon) owner;
            }
        }

        if(dragon != null) {
            playerEntity.startRiding(dragon, true);
            this.lastPassengerTime = 0;
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    public boolean canBeRiddenInWater() {
        return true;
    }

    @Override
    public void tickMovement() {
          IMixinEnderDragonEntity mixin = (IMixinEnderDragonEntity) this;

          this.addAirTravelEffects();
          if (this.world.isClient) {
              this.setHealth(this.getHealth());
              if (!this.isSilent() && --this.ticksUntilNextGrowl < 0) {
                  this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, this.getSoundCategory(), 2.5F, 0.8F + this.random.nextFloat() * 0.3F, false);
                  this.ticksUntilNextGrowl = 200 + this.random.nextInt(200);
              }
          }

          this.prevWingPosition = this.wingPosition;
          float i;
          if (this.isDead()) {
              float f = (this.random.nextFloat() - 0.5F) * 8.0F;
              i = (this.random.nextFloat() - 0.5F) * 4.0F;
              float h = (this.random.nextFloat() - 0.5F) * 8.0F;
              this.world.addParticle(ParticleTypes.EXPLOSION, this.getX() + (double)f, this.getY() + 2.0D + (double)i, this.getZ() + (double)h, 0.0D, 0.0D, 0.0D);
          } else {
              mixin.invokeTickWithEndCrystals();
              this.wingPosition += 0.1F;
              this.setYaw(MathHelper.wrapDegrees(this.getYaw()));

              if (this.latestSegment < 0) {
                  for(int j = 0; j < this.segmentCircularBuffer.length; ++j) {
                      this.segmentCircularBuffer[j][0] = (double)this.getYaw();
                      this.segmentCircularBuffer[j][1] = this.getY();
                  }
              }

              if (++this.latestSegment == this.segmentCircularBuffer.length) {
                  this.latestSegment = 0;
              }

              this.segmentCircularBuffer[this.latestSegment][0] = (double)this.getYaw();
              this.segmentCircularBuffer[this.latestSegment][1] = this.getY();

              this.bodyYaw = this.getYaw();
              Vec3d[] vec3ds = new Vec3d[this.getBodyParts().length];

              for(int x = 0; x < this.getBodyParts().length; ++x) {
                  vec3ds[x] = new Vec3d(this.getBodyParts()[x].getX(), this.getBodyParts()[x].getY(), this.getBodyParts()[x].getZ());
              }

              float y = (float)(this.getSegmentProperties(5, 1.0F)[1] - this.getSegmentProperties(10, 1.0F)[1]) * 10.0F * 0.017453292F;
              float z = MathHelper.cos(y);
              float aa = MathHelper.sin(y);
              float ab = this.getYaw() * 0.017453292F;
              float ac = MathHelper.sin(ab);
              float ad = MathHelper.cos(ab);

              mixin.invokeMovePart(mixin.getBody(), (double)(ac * 0.5F), 0.0D, (double)(-ad * 0.5F));
              mixin.invokeMovePart(mixin.getRightWing(), (double)(ad * 4.5F), 2.0D, (double)(ac * 4.5F));
              mixin.invokeMovePart(mixin.getLeftWing(), (double)(ad * -4.5F), 2.0D, (double)(ac * -4.5F));

              if (!this.world.isClient && this.hurtTime == 0) {
                  mixin.invokeLaunchLivingEntities(this.world.getOtherEntities(this, mixin.getRightWing().getBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
                  mixin.invokeLaunchLivingEntities(this.world.getOtherEntities(this, mixin.getLeftWing().getBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
                  mixin.invokeDamageLivingEntities(this.world.getOtherEntities(this, this.head.getBoundingBox().expand(1.0D), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
                  mixin.invokeDamageLivingEntities(this.world.getOtherEntities(this, mixin.getNeck().getBoundingBox().expand(1.0D), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
              }

              float ae = MathHelper.sin(this.getYaw() * 0.017453292F - this.yawAcceleration * 0.01F);
              float af = MathHelper.cos(this.getYaw() * 0.017453292F - this.yawAcceleration * 0.01F);
              float ag = mixin.invokeGetHeadVerticalMovement();

              mixin.invokeMovePart(this.head, (double)(ae * 6.5F * z), (double)(ag + aa * 6.5F), (double)(-af * 6.5F * z));
              mixin.invokeMovePart(mixin.getNeck(), (double)(ae * 5.5F * z), (double)(ag + aa * 5.5F), (double)(-af * 5.5F * z));
              double[] ds = this.getSegmentProperties(5, 1.0F);

              int ah;
              for(ah = 0; ah < 3; ++ah) {
                  EnderDragonPart enderDragonPart = null;
                  if (ah == 0) {
                      enderDragonPart = mixin.getTail1();
                  }

                  if (ah == 1) {
                      enderDragonPart = mixin.getTail2();
                  }

                  if (ah == 2) {
                      enderDragonPart = mixin.getTail3();
                  }

                  double[] es = this.getSegmentProperties(12 + ah * 2, 1.0F);
                  float ai = this.getYaw() * 0.017453292F + mixin.invokeWrapYawChange(es[0] - ds[0]) * 0.017453292F;
                  float aj = MathHelper.sin(ai);
                  float ak = MathHelper.cos(ai);
                  float am = (float)(ah + 1) * 2.0F;
                  mixin.invokeMovePart(enderDragonPart, (double)(-(ac * 1.5F + aj * am) * z), es[1] - ds[1] - (double)((am + 1.5F) * aa) + 1.5D, (double)((ad * 1.5F + ak * am) * z));
              }

              if (!this.world.isClient) {
                  this.slowedDownByBlock = mixin.invokeDestroyBlocks(this.head.getBoundingBox()) | mixin.invokeDestroyBlocks(mixin.getNeck().getBoundingBox()) | mixin.invokeDestroyBlocks(mixin.getBody().getBoundingBox());
              }

              for(ah = 0; ah < this.getBodyParts().length; ++ah) {
                  this.getBodyParts()[ah].prevX = vec3ds[ah].x;
                  this.getBodyParts()[ah].prevY = vec3ds[ah].y;
                  this.getBodyParts()[ah].prevZ = vec3ds[ah].z;
                  this.getBodyParts()[ah].lastRenderX = vec3ds[ah].x;
                  this.getBodyParts()[ah].lastRenderY = vec3ds[ah].y;
                  this.getBodyParts()[ah].lastRenderZ = vec3ds[ah].z;
              }

              if(!this.hasPassengers() || (!(this.getFirstPassenger() instanceof PlayerEntity))) {
                  if(this.lastPassengerTime == 0) {
                      this.lastPassengerTime = System.currentTimeMillis();
                  }

                  if(10 * 1000 <= (System.currentTimeMillis() - this.lastPassengerTime)) {
                      LastResource.getFirst().setCompetenceState(TheylorCompetences.LAST_RESSOURCE, false);
                      LastResource.getSecond().setCompetenceState(TheylorCompetences.LAST_RESSOURCE, false);
                      this.remove(RemovalReason.DISCARDED);
                      LastResource.setActive(false);
                      LastResource.setFirst(null);
                      LastResource.setSecond(null);
                  }
                  return;
              }

              if(this.lastPassengerTime != 0) this.lastPassengerTime = 0;

              PlayerEntity rider = (PlayerEntity) this.getFirstPassenger();
              Vec3d forwardDir = rider.getRotationVector();

              if(((IMixinLivingEntity)rider).getJumping() && SHOOT_COOLDOWN * 1000 <= (System.currentTimeMillis() - lastShoot)) {
                  shoot(forwardDir);
              }

              this.setYaw(180 + rider.getYaw());
              this.setPitch(rider.getPitch());

              double speedMultiplier = 2.0D;
              double fwSpeed = rider.forwardSpeed * speedMultiplier;
              double sideSpeed = -1 * rider.sidewaysSpeed * speedMultiplier;

              Vec3d sideways = forwardDir.crossProduct(new Vec3d(0, 1, 0));
              Vec3d total = forwardDir.multiply(fwSpeed).add(sideways.multiply(sideSpeed));
              this.move(MovementType.PLAYER, total);
          }
    }

    @Override
    protected void updatePostDeath() {
        ++this.ticksSinceDeath;
        if (this.ticksSinceDeath >= 180 && this.ticksSinceDeath <= 200) {
            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            float g = (this.random.nextFloat() - 0.5F) * 4.0F;
            float h = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + 2.0D + (double)g, this.getZ() + (double)h, 0.0D, 0.0D, 0.0D);
        }

        if (this.world instanceof ServerWorld) {
            if (this.ticksSinceDeath == 1 && !this.isSilent()) {
                this.world.syncGlobalEvent(1028, this.getBlockPos(), 0);
            }
        }

        this.move(MovementType.SELF, new Vec3d(0.0D, 0.10000000149011612D, 0.0D));
        this.setYaw(this.getYaw() + 20.0F);
        this.bodyYaw = this.getYaw();
        if (this.ticksSinceDeath == 200 && this.world instanceof ServerWorld) {
            this.remove(RemovalReason.KILLED);
        }
    }

    public void shoot(Vec3d forwardDir) {
        Vec3d pos = new MutableVec3d(this.getPos()).add(new MutableVec3d(forwardDir).multiply(10).setY(-1)).toVec3D();

        DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(world, this, forwardDir.x, forwardDir.y, forwardDir.z);
        dragonFireballEntity.setPosition(pos);

        world.spawnEntity(dragonFireballEntity);

        lastShoot = System.currentTimeMillis();
    }


}
