package tech.alexnijjar.endermanoverhaul.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.endermanoverhaul.common.config.EndermanOverhaulConfig;
import tech.alexnijjar.endermanoverhaul.common.entities.base.BaseEnderman;
import tech.alexnijjar.endermanoverhaul.common.registry.ModParticleTypes;

public class SnowyEnderman extends BaseEnderman {

    public SnowyEnderman(EntityType<? extends EnderMan> entityType, Level level) {
        super(entityType, level);
        xpReward = 8;
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 35.0)
            .add(Attributes.MOVEMENT_SPEED, 0.1944)
            .add(Attributes.ATTACK_DAMAGE, 6.0)
            .add(Attributes.FOLLOW_RANGE, 42.0);
    }

    public static boolean checkMonsterSpawnRules(@NotNull EntityType<? extends Monster> type, ServerLevelAccessor level, @NotNull MobSpawnType spawnType, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!EndermanOverhaulConfig.spawnSnowyEnderman || !EndermanOverhaulConfig.allowSpawning) return false;
        return BaseEnderman.checkMonsterSpawnRules(type, level, spawnType, pos, random);
    }

    @Override
    public boolean canOpenMouth() {
        return false;
    }

    @Override
    public @Nullable ParticleOptions getCustomParticles() {
        return ModParticleTypes.SNOW.get();
    }

    @Override
    public double getVisionRange() {
        return 42.0;
    }

    @Override
    public @Nullable MobEffectInstance getHitEffect() {
        return new MobEffectInstance(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
    }

    @Override
    protected void onChangedBlock(@NotNull BlockPos pos) {
        super.onChangedBlock(pos);
        FrostWalkerEnchantment.onEntityMoved(this, this.level(), pos, 2);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity entity && !entity.isFullyFrozen()) {
                entity.setTicksFrozen(entity.getTicksFrozen() + 100);
            }

            return true;
        } else {
            return false;
        }
    }
}
