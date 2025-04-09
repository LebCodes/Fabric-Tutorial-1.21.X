package net.lebcodes.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.lebcodes.tutorialmod.block.ModBlocks;
import net.lebcodes.tutorialmod.component.ModDataComponentTypes;
import net.lebcodes.tutorialmod.effect.ModEffects;
import net.lebcodes.tutorialmod.enchantment.ModEnchantmentEffects;
import net.lebcodes.tutorialmod.item.ModItemGroups;
import net.lebcodes.tutorialmod.item.ModItems;
import net.lebcodes.tutorialmod.potion.ModPotions;
import net.lebcodes.tutorialmod.sound.ModSounds;
import net.lebcodes.tutorialmod.util.HammerUsageEvent;
import net.lebcodes.tutorialmod.world.gen.ModWorldGeneration;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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
		ModSounds.registerSounds();

		ModEffects.registerEffects();
		ModPotions.registerPotions();

		ModEnchantmentEffects.registerEnchantmentEffects();
		ModWorldGeneration.generateModWorldGen();

		FuelRegistry.INSTANCE.add(ModItems.STARLIGHT_ASHES, 600);

		//simply added new functionality to an already existing item
		PlayerBlockBreakEvents.BEFORE.register(new HammerUsageEvent());
		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if(entity instanceof SheepEntity sheepEntity && !world.isClient()) {
				if(playerEntity.getMainHandStack().getItem() == Items.END_ROD) {
					playerEntity.sendMessage(Text.literal("Don't do that you sick mofo!"));
					playerEntity.getMainHandStack().decrement(1);
					sheepEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 6));
				}

				return ActionResult.PASS;
			}

			return ActionResult.PASS;
		});

		//recipe for slimey potion
		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION);
		});

		//make cauliflower stuff compostable
		CompostingChanceRegistry.INSTANCE.add(ModItems.CAULIFLOWER, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.CAULIFLOWER_SEEDS, 0.25f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.HONEY_BERRIES, 0.15f);


		//for all vanilla fuel burn times, press shift twice to search for AbstractFurnaceBlockEntity
		//for all vanilla recipes, go to External Libraries -> minecraft-merged -> data -> minecraft -> recipe
		//for all vanilla loot tables, go to External Libraries -> minecraft-merged -> data -> minecraft -> loot_table
	}
}