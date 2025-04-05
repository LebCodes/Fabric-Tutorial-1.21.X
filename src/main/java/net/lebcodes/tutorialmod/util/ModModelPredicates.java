package net.lebcodes.tutorialmod.util;

import net.lebcodes.tutorialmod.TutorialMod;
import net.lebcodes.tutorialmod.component.ModDataComponentTypes;
import net.lebcodes.tutorialmod.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

//take a look at bow, very similar (bow has different stages)
public class ModModelPredicates {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.CHISEL, Identifier.of(TutorialMod.MOD_ID, "used"),
                (stack, world, entity, seed) -> stack.get(ModDataComponentTypes.COORDINATES) != null ? 1f : 0f);

    }
}
