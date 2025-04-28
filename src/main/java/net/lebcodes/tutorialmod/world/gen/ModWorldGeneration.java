package net.lebcodes.tutorialmod.world.gen;

import net.lebcodes.tutorialmod.world.ModBushGeneration;

public class ModWorldGeneration {
    public static void generateModWorldGen() {
        //keep it in specific order (might be important)
        ModOreGeneration.generateOres();
        ModTreeGeneration.generateTrees();
        ModBushGeneration.generateBushes();
    }
}
