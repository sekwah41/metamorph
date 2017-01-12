package mchorse.metamorph.api;

import mchorse.metamorph.api.events.MorphEvent;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.capabilities.morphing.IMorphing;
import mchorse.metamorph.capabilities.morphing.Morphing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Morph API class
 * 
 * This class provides public API for morphing the player. Let me know which 
 * methods I may add to simplify your life :D
 * 
 * Use this API on the server side, please. Thanks!
 */
public class MorphAPI
{
    /**
     * Demorph given player 
     */
    public static boolean demorph(EntityPlayer player)
    {
        return morph(player, null, false);
    }

    /**
     * Morph a player into given morph with given force flag. 
     * 
     * @return true if player was morphed successfully
     */
    public static boolean morph(EntityPlayer player, AbstractMorph morph, boolean force)
    {
        IMorphing morphing = Morphing.get(player);

        if (morphing == null)
        {
            return false;
        }

        MorphEvent event = new MorphEvent(player, morph, force);

        if (!MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        morphing.setCurrentMorph(event.morph, player, event.force);

        return true;
    }

    /**
     * Make given player acquire a given morph
     * 
     * @return true if player has acquired a morph
     */
    public static boolean acquire(EntityPlayer player, AbstractMorph morph)
    {
        // TODO: Actually implement
        return false;
    }
}