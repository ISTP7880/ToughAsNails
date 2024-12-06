/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.forge.datagen.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentModel;
import net.minecraft.world.item.equipment.EquipmentModels;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.EquipmentModel.LayerType;
import toughasnails.core.ToughAsNails;
import toughasnails.init.ModEquipmentModels;

public class TANItemModelGenerators
{
    public static final ResourceLocation TRIM_TYPE_PREDICATE_ID = ResourceLocation.withDefaultNamespace("trim_type");
    public static final List<TrimModelData> GENERATED_TRIM_MODELS = List.of(
            new TrimModelData("quartz", 0.1F, Map.of()),
            new TrimModelData("iron", 0.2F, Map.of(EquipmentModels.IRON, "iron_darker")),
            new TrimModelData("netherite", 0.3F, Map.of(EquipmentModels.NETHERITE, "netherite_darker")),
            new TrimModelData("redstone", 0.4F, Map.of()),
            new TrimModelData("copper", 0.5F, Map.of()),
            new TrimModelData("gold", 0.6F, Map.of(EquipmentModels.GOLD, "gold_darker")),
            new TrimModelData("emerald", 0.7F, Map.of()),
            new TrimModelData("diamond", 0.8F, Map.of(EquipmentModels.DIAMOND, "diamond_darker")),
            new TrimModelData("lapis", 0.9F, Map.of()),
            new TrimModelData("amethyst", 1.0F, Map.of())
    );
    private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;

    public TANItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> p_125082_) {
        this.output = p_125082_;
    }

    private void generateLayeredItem(ResourceLocation p_267272_, ResourceLocation p_266738_, ResourceLocation p_267328_) {
        ModelTemplates.TWO_LAYERED_ITEM.create(p_267272_, TextureMapping.layered(p_266738_, p_267328_), this.output);
    }

    private void generateLayeredItem(ResourceLocation p_268353_, ResourceLocation p_268162_, ResourceLocation p_268173_, ResourceLocation p_268312_) {
        ModelTemplates.THREE_LAYERED_ITEM.create(p_268353_, TextureMapping.layered(p_268162_, p_268173_, p_268312_), this.output);
    }

    private ResourceLocation getItemModelForTrimMaterial(ResourceLocation p_266817_, String p_267030_) {
        return p_266817_.withSuffix("_" + p_267030_ + "_trim");
    }

    private JsonObject generateBaseArmorTrimTemplate(ResourceLocation p_266939_, Map<TextureSlot, ResourceLocation> p_267324_, ResourceLocation p_371397_) {
        JsonObject jsonobject = ModelTemplates.TWO_LAYERED_ITEM.createBaseTemplate(p_266939_, p_267324_);
        JsonArray jsonarray = new JsonArray();

        for (TrimModelData itemmodelgenerators$trimmodeldata : GENERATED_TRIM_MODELS) {
            JsonObject jsonobject1 = new JsonObject();
            JsonObject jsonobject2 = new JsonObject();
            jsonobject2.addProperty(TRIM_TYPE_PREDICATE_ID.getPath(), itemmodelgenerators$trimmodeldata.itemModelIndex());
            jsonobject1.add("predicate", jsonobject2);
            jsonobject1.addProperty("model", this.getItemModelForTrimMaterial(p_266939_, itemmodelgenerators$trimmodeldata.name(p_371397_)).toString());
            jsonarray.add(jsonobject1);
        }

        jsonobject.add("overrides", jsonarray);
        return jsonobject;
    }

    private void generateArmorTrims(Item p_371707_, ResourceLocation p_371849_, EquipmentModel equipmentModel, EquipmentSlot p_371847_) {
        List<EquipmentModel.Layer> list = equipmentModel.getLayers(EquipmentModel.LayerType.HUMANOID);
        if (!list.isEmpty()) {
            boolean flag = list.size() == 2 && list.getFirst().dyeable().isPresent();
            ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(p_371707_);
            ResourceLocation resourcelocation1 = TextureMapping.getItemTexture(p_371707_);
            ResourceLocation resourcelocation2 = TextureMapping.getItemTexture(p_371707_, "_overlay");
            if (flag) {
                ModelTemplates.TWO_LAYERED_ITEM
                        .create(
                                resourcelocation,
                                TextureMapping.layered(resourcelocation1, resourcelocation2),
                                this.output,
                                (p_370385_, p_370386_) -> this.generateBaseArmorTrimTemplate(p_370385_, p_370386_, p_371849_)
                        );
            } else {
                ModelTemplates.FLAT_ITEM
                        .create(
                                resourcelocation,
                                TextureMapping.layer0(resourcelocation1),
                                this.output,
                                (p_370382_, p_370383_) -> this.generateBaseArmorTrimTemplate(p_370382_, p_370383_, p_371849_)
                        );
            }
            String s = switch (p_371847_) {
                case HEAD -> "helmet";
                case CHEST -> "chestplate";
                case LEGS -> "leggings";
                case FEET -> "boots";
                default -> throw new UnsupportedOperationException();
            };

            for (TrimModelData itemmodelgenerators$trimmodeldata : GENERATED_TRIM_MODELS) {
                String s1 = itemmodelgenerators$trimmodeldata.name(p_371849_);
                ResourceLocation resourcelocation3 = this.getItemModelForTrimMaterial(resourcelocation, s1);
                String s2 = s + "_trim_" + s1;
                ResourceLocation resourcelocation4 = ResourceLocation.withDefaultNamespace(s2).withPrefix("trims/items/");
                if (flag) {
                    this.generateLayeredItem(resourcelocation3, resourcelocation1, resourcelocation2, resourcelocation4);
                } else {
                    this.generateLayeredItem(resourcelocation3, resourcelocation1, resourcelocation4);
                }
            }
        }
    }

    public void run()
    {
        Map<ResourceLocation, EquipmentModel> map = new HashMap<>();
        ModEquipmentModels.bootstrap(map::put);

        for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
            // We only want to generate models for our armour
            if (!entry.getKey().location().getNamespace().equals(ToughAsNails.MOD_ID))
                continue;

            Item item = entry.getValue();

            Equippable equippable = item.components().get(DataComponents.EQUIPPABLE);
            if (equippable != null && equippable.slot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR && equippable.model().isPresent()) {
                ResourceLocation resourcelocation = equippable.model().get();
                EquipmentModel equipmentModel = map.get(resourcelocation);
                if (equipmentModel == null) {
                    throw new IllegalStateException("Referenced equipment model does not exist: " + resourcelocation);
                }

                this.generateArmorTrims(item, resourcelocation, equipmentModel, equippable.slot());
            }
        }
    }

    record TrimModelData(String name, float itemModelIndex, Map<ResourceLocation, String> overrideArmorMaterials) {

        public String name(ResourceLocation p_363786_) {
            return (String)this.overrideArmorMaterials.getOrDefault(p_363786_, this.name);
        }

        public float itemModelIndex() {
            return this.itemModelIndex;
        }
    }
}
