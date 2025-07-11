package net.lebcodes.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.*;
import net.lebcodes.tutorialmod.block.ModBlocks;
import net.lebcodes.tutorialmod.block.entity.ModBlockEntities;
import net.lebcodes.tutorialmod.component.ModDataComponentTypes;
import net.lebcodes.tutorialmod.effect.ModEffects;
import net.lebcodes.tutorialmod.enchantment.ModEnchantmentEffects;
import net.lebcodes.tutorialmod.entity.ModEntities;
import net.lebcodes.tutorialmod.entity.custom.MantisEntity;
import net.lebcodes.tutorialmod.item.ModItemGroups;
import net.lebcodes.tutorialmod.item.ModItems;
import net.lebcodes.tutorialmod.particle.ModParticles;
import net.lebcodes.tutorialmod.potion.ModPotions;
import net.lebcodes.tutorialmod.screen.ModScreenHandlers;
import net.lebcodes.tutorialmod.sound.ModSounds;
import net.lebcodes.tutorialmod.util.HammerUsageEvent;
import net.lebcodes.tutorialmod.util.ModLootTableModifiers;
import net.lebcodes.tutorialmod.villager.ModVillagers;
import net.lebcodes.tutorialmod.world.gen.ModWorldGeneration;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
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

		ModEntities.registerModEntities();
		ModVillagers.registerVillagers();

		ModParticles.registerParticles();
		ModLootTableModifiers.modifyLootTables();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

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

		StrippableBlockRegistry.register(ModBlocks.DRIFTWOOD_LOG, ModBlocks.STRIPPED_DRIFTWOOD_LOG);
		StrippableBlockRegistry.register(ModBlocks.DRIFTWOOD_WOOD, ModBlocks.STRIPPED_DRIFTWOOD_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_DRIFTWOOD_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_DRIFTWOOD_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.DRIFTWOOD_LEAVES, 30, 60);

		FabricDefaultAttributeRegistry.register(ModEntities.MANTIS, MantisEntity.createAttributes());

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 3),
					new ItemStack(ModItems.CAULIFLOWER, 8), 7, 2, 0.04f));

			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.DIAMOND, 9),
					new ItemStack(ModItems.CAULIFLOWER_SEEDS, 2), 3, 4, 0.04f));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 3),
					new ItemStack(ModItems.HONEY_BERRIES, 5), 4, 2, 0.04f));
		});

		TradeOfferHelper.registerVillagerOffers(ModVillagers.LEBENGER, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 10),
					new ItemStack(ModItems.CHISEL, 1), 4, 2, 0.04f));

			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 16),
					new ItemStack(ModItems.RAW_PINK_GARNET, 1), 4, 2, 0.04f));
		});

		TradeOfferHelper.registerVillagerOffers(ModVillagers.LEBENGER, 2, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 10),
					new ItemStack(ModItems.CHISEL, 1), 4, 2, 0.04f));

			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(ModItems.PINK_GARNET, 16),
					new ItemStack(ModItems.TOMAHAWK, 1), 4, 12, 0.04f));
		});




		//for all vanilla fuel burn times, press shift twice to search for AbstractFurnaceBlockEntity
		//for all vanilla recipes, go to External Libraries -> minecraft-merged -> data -> minecraft -> recipe
		//for all vanilla loot tables, go to External Libraries -> minecraft-merged -> data -> minecraft -> loot_table
	}
}