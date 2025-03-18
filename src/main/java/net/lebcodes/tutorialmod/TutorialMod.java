package net.lebcodes.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.lebcodes.tutorialmod.block.ModBlocks;
import net.lebcodes.tutorialmod.component.ModDataComponentTypes;
import net.lebcodes.tutorialmod.item.ModItemGroups;
import net.lebcodes.tutorialmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID = "tutorialmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModDataComponentTypes.registerDataComponentTypes();

		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);


		//for all vanilla fuel burn times, press shift twice to search for AbstractFurnaceBlockEntity
		//for all vanilla recipes, go to External Libraries -> minecraft-merged -> data -> minecraft -> recipe
		//for all vanilla loot tables, go to External Libraries -> minecraft-merged -> data -> minecraft -> loot_table
	}
}