package mchorse.metamorph.config;

import mchorse.metamorph.Metamorph;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Metamorph config class
 * 
 * Intance of this class is responsible for storing configuration for Metamorph 
 * mod.
 */
public class MetamorphConfig
{
    /**
     * Prevents ghosts from spawning if the player has already a currently 
     * killed mob's morph 
     */
    public boolean prevent_ghosts;

    /**
     * Retain morphs when player died 
     */
    public boolean keep_morphs;

    private Configuration config;

    public MetamorphConfig(Configuration config)
    {
        this.config = config;
        this.reload();
    }

    /**
     * Reload config values
     */
    public void reload()
    {
        String cat = Configuration.CATEGORY_GENERAL;
        String lang = "metamorph.config.";

        this.prevent_ghosts = this.config.getBoolean("prevent_ghosts", cat, true, "Prevent ghosts from spawning if player has morph of mob already?", lang + "prevent_ghosts");
        this.keep_morphs = this.config.getBoolean("keep_morphs", cat, true, "Retain morphs when player dies?", lang + "keep_morphs");

        this.config.getCategory(cat).setComment("General configuration of Metamorph mod");

        if (this.config.hasChanged())
        {
            this.config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Metamorph.MODID) && this.config.hasChanged())
        {
            this.reload();
        }
    }
}