package mchorse.metamorph.entity;

import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.api.morphs.EntityMorph;
import mchorse.metamorph.capabilities.morphing.IMorphing;
import mchorse.metamorph.capabilities.morphing.Morphing;
import mchorse.metamorph.util.ObfuscatedName;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Replaces the sounds that players usually make when they are in morphs
 */
public class SoundHandler
{
    public static final ObfuscatedName GET_HURT_SOUND = new ObfuscatedName("getHurtSound", "func_184601_bQ");
    public static final ObfuscatedName GET_DEATH_SOUND = new ObfuscatedName("getDeathSound", "func_184615_bR");
    public static final ObfuscatedName PLAY_STEP_SOUND = new ObfuscatedName("playStepSound", "func_180429_a");
    
    public static final DamageSource GENERIC_DAMAGE = DamageSource.generic;
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPlayerHurt(LivingAttackEvent event)
    {
        if (event.isCanceled())
        {
            return;
        }
        
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }
        IMorphing morphing = Morphing.get((EntityPlayer)entity);
        if (morphing == null)
        {
            return;
        }
        
        morphing.setLastDamageSource(event.getSource());
    }
    
    @SubscribeEvent
    public void onPlaySound(PlaySoundAtEntityEvent event)
    {
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }
        EntityPlayer player = (EntityPlayer)entity;
        IMorphing morphing = Morphing.get(player);
        if (morphing == null)
        {
            return;
        }
        AbstractMorph morph = morphing.getCurrentMorph();
        if (morph == null)
        {
            return;
        }
        EntityLivingBase soundEntity = ((EntityMorph)morph).getEntity();
        
        String soundType = event.getSound().getRegistryName().getResourcePath();
        if (soundType.endsWith(".hurt"))
        {
            SoundEvent newSound = morph.getHurtSound(soundEntity, morphing.getLastDamageSource());
            if (newSound != null)
            {
                event.setSound(newSound);
            }
        }
        else if (soundType.endsWith(".death"))
        {
            SoundEvent newSound = morph.getDeathSound(soundEntity);
            if (newSound != null)
            {
                event.setSound(newSound);
            }
        }
        else if (soundType.endsWith(".step"))
        {
            if (player.width != morph.getWidth() || player.height !=  morph.getHeight())
            {
                /*
                 * Check if the player's current hitbox does not
                 * match the morph hitbox.
                 * 
                 * Each tick, the player entity attempts to revert
                 * back to the default player hitbox. If the current
                 * morph hitbox is smaller than the player hitbox,
                 * movement can be triggered, which in turn triggers
                 * a step sound.
                 */
                event.setCanceled(true);
            }
            else if (morph.hasCustomStepSound(soundEntity))
            {
                event.setCanceled(true);
                morph.playStepSound(soundEntity);
            }
        }
    }
}
