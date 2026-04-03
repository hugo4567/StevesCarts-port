package vswe.stevescarts.module;

import java.util.EnumSet;
import java.util.function.BiFunction;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.addon.BrakeModule;
import vswe.stevescarts.module.addon.ChunkLoaderModule;
import vswe.stevescarts.module.addon.ColorizerModule;
import vswe.stevescarts.module.addon.CreativeSuppliesModule;
import vswe.stevescarts.module.addon.DrillIntelligenceModule;
import vswe.stevescarts.module.addon.EnchanterModule;
import vswe.stevescarts.module.addon.ExperienceBankModule;
import vswe.stevescarts.module.addon.FertilizerModule;
import vswe.stevescarts.module.addon.InvisibilityModule;
import vswe.stevescarts.module.attachment.BridgeBuilderModule;
import vswe.stevescarts.module.attachment.FireworkDisplayModule;
import vswe.stevescarts.module.attachment.HydratorModule;
import vswe.stevescarts.module.attachment.LargeRailerModule;
import vswe.stevescarts.module.attachment.LiquidDrainerModule;
import vswe.stevescarts.module.attachment.RailerModule;
import vswe.stevescarts.module.attachment.SeatModule;
import vswe.stevescarts.module.attachment.TorchPlacerModule;
import vswe.stevescarts.module.realtimer.LawnMowerModule;
import vswe.stevescarts.module.realtimer.ExperienceModule;
import vswe.stevescarts.module.tool.CutterModule;
import vswe.stevescarts.module.tool.DrillModule;
import vswe.stevescarts.module.tool.FarmerModule;
import vswe.stevescarts.module.engine.AdvancedThermalEngineModule;
import vswe.stevescarts.module.engine.CoalEngineModule;
import vswe.stevescarts.module.engine.CompactSolarEngineModule;
import vswe.stevescarts.module.engine.CreativeEngineModule;
import vswe.stevescarts.module.engine.SolarEngineModule;
import vswe.stevescarts.module.engine.ThermalEngineModule;
import vswe.stevescarts.module.hull.HullData;
import vswe.stevescarts.module.hull.HullModule;
import vswe.stevescarts.module.hull.HullModuleType;
import vswe.stevescarts.module.hull.CheatHullModule;
import vswe.stevescarts.module.hull.GalgadorianHullModule;
import vswe.stevescarts.module.hull.PumpkinHullModule;
import vswe.stevescarts.module.hull.ReinforcedHullModule;
import vswe.stevescarts.module.hull.StandardHullModule;
import vswe.stevescarts.module.hull.WoodHullModule;
import vswe.stevescarts.module.storage.ChestModule;
import vswe.stevescarts.module.storage.CreativeTankModule;
import vswe.stevescarts.module.storage.ExtractingChestsModule;
import vswe.stevescarts.module.storage.TankModule;

// Add-ons - Missing ones
import vswe.stevescarts.module.addon.AdvancedFarmerModule;
import vswe.stevescarts.module.addon.AdvancedShooterModule;
import vswe.stevescarts.module.addon.BaitFencerModule;
import vswe.stevescarts.module.addon.ChunkLoaderModule;
import vswe.stevescarts.module.addon.ColorRandomizerModule;
import vswe.stevescarts.module.addon.DivineShieldModule;
import vswe.stevescarts.module.addon.FreezerModule;
import vswe.stevescarts.module.addon.HeightControllerModule;
import vswe.stevescarts.module.addon.InformationProviderModule;
import vswe.stevescarts.module.addon.MelterExtremeModule;
import vswe.stevescarts.module.addon.ModuleTogglerModule;
import vswe.stevescarts.module.addon.NoteSequencerModule;
import vswe.stevescarts.module.addon.OreExtractorModule;
import vswe.stevescarts.module.addon.PowerObserverModule;
import vswe.stevescarts.module.addon.StevesArcadeModule;
import vswe.stevescarts.module.addon.TrickOrTreatCakeServerModule;
// Attachments - Missing ones
import vswe.stevescarts.module.attachment.TrackRemoverModule;
// Detectors - All modules
import vswe.stevescarts.module.detector.DetectorJunctionModule;
import vswe.stevescarts.module.detector.DetectorRedstoneUnitModule;
import vswe.stevescarts.module.detector.DetectorStationModule;
import vswe.stevescarts.module.detector.DetectorUnitModule;
import vswe.stevescarts.module.detector.EntityDetectorAnimalModule;
import vswe.stevescarts.module.detector.EntityDetectorBatModule;
import vswe.stevescarts.module.detector.EntityDetectorMonsterModule;
import vswe.stevescarts.module.detector.EntityDetectorPlayerModule;
import vswe.stevescarts.module.detector.EntityDetectorVillagerModule;
// Farming - Missing ones
import vswe.stevescarts.module.farming.BasicFarmerModule;
import vswe.stevescarts.module.farming.BasicWoodCutterModule;
import vswe.stevescarts.module.farming.GalgadorianFarmerModule;
import vswe.stevescarts.module.farming.GalgadorianWoodCutterModule;
import vswe.stevescarts.module.farming.HardenedWoodCutterModule;
import vswe.stevescarts.module.farming.PlanterRangeExtenderModule;
import vswe.stevescarts.module.farming.SilkTouchFarmerModule;
// Managers - All modules
import vswe.stevescarts.module.manager.CargoManagerModule;
import vswe.stevescarts.module.manager.DetectorManagerModule;
import vswe.stevescarts.module.manager.FluidManagerModule;
import vswe.stevescarts.module.manager.InventoryEvalizerModule;
// Processors - All modules
import vswe.stevescarts.module.processor.AdvancedCrafterModule;
import vswe.stevescarts.module.processor.AdvancedSmelterModule;
import vswe.stevescarts.module.processor.BasicSmelterModule;
import vswe.stevescarts.module.processor.CleaningMachineModule;
import vswe.stevescarts.module.processor.CrafterModule;
import vswe.stevescarts.module.processor.CreativeIncineratorModule;
import vswe.stevescarts.module.processor.ExtremeMelterModule;
import vswe.stevescarts.module.processor.IncineratorModule;
import vswe.stevescarts.module.processor.LiquidCleanerModule;
import vswe.stevescarts.module.processor.MelterModule;
import vswe.stevescarts.module.processor.StoneCutterModule;
import vswe.stevescarts.module.processor.TreeTapModule;
// Realtimers - Missing ones
import vswe.stevescarts.module.realtimer.CageModule;
import vswe.stevescarts.module.realtimer.CakeServerModule;
import vswe.stevescarts.module.realtimer.CleanerModule;
import vswe.stevescarts.module.realtimer.DynamiteModule;
import vswe.stevescarts.module.realtimer.MilkerModule;
// Storage - Missing ones
import vswe.stevescarts.module.storage.InternalTankModule;
// Tools - Missing ones
import vswe.stevescarts.module.tool.BasicDrillModule;
import vswe.stevescarts.module.tool.DiamondDrillModule;
import vswe.stevescarts.module.tool.ExternalDistributorModule;
import vswe.stevescarts.module.tool.GalgadorianDrillModule;
import vswe.stevescarts.module.tool.HardenedDrillModule;
import vswe.stevescarts.module.tool.IronDrillModule;
// Weapons - All modules
import vswe.stevescarts.module.weapon.BasicShooterModule;
import vswe.stevescarts.module.weapon.DynamiteCarrierModule;
import vswe.stevescarts.module.weapon.ProjectileCakeModule;
import vswe.stevescarts.module.weapon.ProjectileEggModule;
import vswe.stevescarts.module.weapon.ProjectileFireChargeModule;
import vswe.stevescarts.module.weapon.ProjectilePotionModule;
import vswe.stevescarts.module.weapon.ProjectileSnowballModule;
import vswe.stevescarts.module.weapon.TNTLauncherModule;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class StevesCartsModules {
	public static final HullModuleType<WoodHullModule> WOODEN_HULL = registerHull("wooden_hull", WoodHullModule::new, new HullData(50, 1, 0, 15));
	public static final HullModuleType<StandardHullModule> STANDARD_HULL = registerHull("standard_hull", StandardHullModule::new, new HullData(200, 3, 6, 50));
	public static final HullModuleType<ReinforcedHullModule> REINFORCED_HULL = registerHull("reinforced_hull", ReinforcedHullModule::new, new HullData(500, 5, 12, 150));
	public static final HullModuleType<StandardHullModule> MECHANICAL_PIG = registerHull("mechanical_pig", StandardHullModule::new, EnumSet.of(ModuleSide.FRONT), new HullData(150, 4, 2, 50));
	public static final HullModuleType<CheatHullModule> CREATIVE_HULL = registerHull("creative_hull", CheatHullModule::new, new HullData(10000, 5, 12, 150));
	public static final HullModuleType<GalgadorianHullModule> GALGADORIAN_HULL = registerHull("galgadorian_hull", GalgadorianHullModule::new, new HullData(1000, 5, 12, 150));
	public static final HullModuleType<PumpkinHullModule> PUMPKIN_CHARIOT = registerHull("pumpkin_chariot", PumpkinHullModule::new, new HullData(40, 1, 0, 15));

	public static final ModuleType<ChestModule> FRONT_CHEST = registerRegularChest("front_chest", (entity, type) -> new ChestModule(entity, type, 4, 3), EnumSet.of(ModuleSide.FRONT), 5);
	public static final ModuleType<ChestModule> TOP_CHEST = registerRegularChest("top_chest", (entity, type) -> new ChestModule(entity, type, 6, 3), EnumSet.of(ModuleSide.TOP), 5);
	public static final ModuleType<ChestModule> SIDE_CHESTS = registerRegularChest("side_chests", (entity, type) -> new ChestModule(entity, type, 5, 3), EnumSet.of(ModuleSide.LEFT, ModuleSide.RIGHT), 3);
	public static final ModuleType<ChestModule> EXTRACTING_CHESTS = registerRegularChest("extracting_chests", ExtractingChestsModule::new, EnumSet.of(ModuleSide.LEFT, ModuleSide.RIGHT, ModuleSide.CENTER), 75);
	public static final ModuleType<ChestModule> INTERNAL_STORAGE = registerRegularChest("internal_storage", (entity, type) -> new ChestModule(entity, type, 3, 3), EnumSet.noneOf(ModuleSide.class), 25);
	public static final ModuleType<ChestModule> EGG_BASKET = registerRegularChest("egg_basket", (entity, type) -> new ChestModule(entity, type, 6, 4), EnumSet.of(ModuleSide.TOP), 12);
	public static final ModuleType<ChestModule> GIFT_STORAGE = registerRegularChest("gift_storage", (entity, type) -> new ChestModule(entity, type, 9, 4), EnumSet.of(ModuleSide.TOP), 20);
	public static final ModuleType<TankModule> TOP_TANK = registerRegularTank("top_tank", 14, EnumSet.of(ModuleSide.TOP), 22, false);
	public static final ModuleType<TankModule> FRONT_TANK = registerRegularTank("front_tank", 8, EnumSet.of(ModuleSide.FRONT), 15, false);
	public static final ModuleType<TankModule> SIDE_TANKS = registerRegularTank("side_tanks", 8, EnumSet.of(ModuleSide.LEFT, ModuleSide.RIGHT), 10, false);
	public static final ModuleType<TankModule> OPEN_TANK = registerRegularTank("open_tank", 14, EnumSet.of(ModuleSide.TOP), 22, false);
	public static final ModuleType<TankModule> ADVANCED_TANK = registerRegularTank("advanced_tank", 32, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), 54, true);
	public static final ModuleType<CreativeTankModule> CREATIVE_TANK = register(
		"creative_tank",
		new ModuleType<>(CreativeTankModule::new, StevesCarts.id("creative_tank"), 0, EnumSet.noneOf(ModuleSide.class), ModuleGroup.STORAGE, false, false, false, null, null)
	);

	public static final ModuleType<CoalEngineModule> TINY_COAL_ENGINE = registerCoalEngine("tiny_coal_engine", 1, 0.5f, 2, ModuleTags.INCOMPATIBLE_WITH_TINY_COAL_ENGINE);
	public static final ModuleType<CoalEngineModule> COAL_ENGINE = registerCoalEngine("coal_engine", 3, 2.25f, 15, ModuleTags.INCOMPATIBLE_WITH_COAL_ENGINE);
	public static final ModuleType<SolarEngineModule> SOLAR_ENGINE = registerSolarEngine("solar_engine", 12, 100000L);
	public static final ModuleType<CompactSolarEngineModule> COMPACT_SOLAR_ENGINE = registerCompactSolarEngine("compact_solar_engine", 18, 150000L);
	public static final ModuleType<CreativeEngineModule> CREATIVE_ENGINE = register("creative_engine", (cart, type) -> new CreativeEngineModule(cart, type));
	public static final ModuleType<SolarEngineModule> ADVANCED_SOLAR_ENGINE = registerSolarEngine("advanced_solar_engine", 20, 200000L);
	public static final ModuleType<ThermalEngineModule> THERMAL_ENGINE = registerThermalEngine("thermal_engine", (cart, type) -> new ThermalEngineModule(cart, type), 28, 1, ModuleTags.INCOMPATIBLE_WITH_THERMAL_ENGINE);
	public static final ModuleType<ThermalEngineModule> ADVANCED_THERMAL_ENGINE = registerThermalEngine("advanced_thermal_engine", (cart, type) -> new AdvancedThermalEngineModule(cart, type), 58, 2, ModuleTags.INCOMPATIBLE_WITH_ADVANCED_THERMAL_ENGINE);

	public static final ModuleType<SeatModule> SEAT = register("seat", new ModuleType<>(SeatModule::new, StevesCarts.id("seat"), 3, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ATTACHMENT, true, false, true, null, null));
	public static final ModuleType<FireworkDisplayModule> FIREWORK_DISPLAY = register("firework_display", new ModuleType<>(FireworkDisplayModule::new, StevesCarts.id("firework_display"), 45, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<TorchPlacerModule> TORCH_PLACER = register("torch_placer", new ModuleType<>(TorchPlacerModule::new, StevesCarts.id("torch_placer"), 14, EnumSet.of(ModuleSide.LEFT, ModuleSide.RIGHT), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<RailerModule> RAILER = register("railer", new ModuleType<>((minecart, type) -> new RailerModule(minecart, type, 1), StevesCarts.id("railer"), 3, EnumSet.of(ModuleSide.TOP), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<LargeRailerModule> LARGE_RAILER = register("large_railer", new ModuleType<>((minecart, type) -> new LargeRailerModule(minecart, type), StevesCarts.id("large_railer"), 9, EnumSet.of(ModuleSide.TOP), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<BridgeBuilderModule> BRIDGE_BUILDER = register("bridge_builder", new ModuleType<>(BridgeBuilderModule::new, StevesCarts.id("bridge_builder"), 12, EnumSet.of(ModuleSide.FRONT), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<HydratorModule> HYDRATOR = register("hydrator", new ModuleType<>(HydratorModule::new, StevesCarts.id("hydrator"), 6, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, Util.make(new Object2IntOpenHashMap<>(), m -> m.put(ModuleTags.TANKS, 1))));
	public static final ModuleType<LiquidDrainerModule> LIQUID_DRAINER = register("liquid_drainer", new ModuleType<>(LiquidDrainerModule::new, StevesCarts.id("liquid_drainer"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<LawnMowerModule> LAWN_MOWER = register("lawn_mower", new ModuleType<>(LawnMowerModule::new, StevesCarts.id("lawn_mower"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<ExperienceModule> EXPERIENCE = register("experience", new ModuleType<>(ExperienceModule::new, StevesCarts.id("experience"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<BrakeModule> BRAKE = register("brake", new ModuleType<>(BrakeModule::new, StevesCarts.id("brake"), 3, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<InvisibilityModule> INVISIBILITY_CORE = register("invisibility_core", new ModuleType<>(InvisibilityModule::new, StevesCarts.id("invisibility_core"), 4, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<CreativeSuppliesModule> CREATIVE_SUPPLIES = register("creative_supplies", new ModuleType<>(CreativeSuppliesModule::new, StevesCarts.id("creative_supplies"), 2, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<EnchanterModule> ENCHANTER = register("enchanter", new ModuleType<>(EnchanterModule::new, StevesCarts.id("enchanter"), 5, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<DrillIntelligenceModule> DRILL_INTELLIGENCE = register("drill_intelligence", new ModuleType<>(DrillIntelligenceModule::new, StevesCarts.id("drill_intelligence"), 3, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	
	
	// Addon modules - Missing ones
	public static final ModuleType<AdvancedFarmerModule> ADVANCED_FARMER = register("advanced_farmer", new ModuleType<>(AdvancedFarmerModule::new, StevesCarts.id("advanced_farmer"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<AdvancedShooterModule> ADVANCED_SHOOTER = register("advanced_shooter", new ModuleType<>(AdvancedShooterModule::new, StevesCarts.id("advanced_shooter"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<BaitFencerModule> BAIT_FENCER = register("bait_fencer", new ModuleType<>(BaitFencerModule::new, StevesCarts.id("bait_fencer"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ChunkLoaderModule> CHUNK_LOADER = register("chunk_loader", new ModuleType<>(ChunkLoaderModule::new, StevesCarts.id("chunk_loader"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ColorizerModule> COLORIZER = register("colorizer", new ModuleType<>(ColorizerModule::new, StevesCarts.id("colorizer"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ColorRandomizerModule> COLOR_RANDOMIZER = register("color_randomizer", new ModuleType<>(ColorRandomizerModule::new, StevesCarts.id("color_randomizer"), 6, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<DivineShieldModule> DIVINE_SHIELD = register("divine_shield", new ModuleType<>(DivineShieldModule::new, StevesCarts.id("divine_shield"), 30, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<FreezerModule> FREEZER = register("freezer", new ModuleType<>(FreezerModule::new, StevesCarts.id("freezer"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<HeightControllerModule> HEIGHT_CONTROLLER = register("height_controller", new ModuleType<>(HeightControllerModule::new, StevesCarts.id("height_controller"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<InformationProviderModule> INFORMATION_PROVIDER = register("information_provider", new ModuleType<>(InformationProviderModule::new, StevesCarts.id("information_provider"), 5, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<MelterExtremeModule> MELTER_EXTREME = register("melter_extreme", new ModuleType<>(MelterExtremeModule::new, StevesCarts.id("melter_extreme"), 45, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ModuleTogglerModule> MODULE_TOGGLER = register("module_toggler", new ModuleType<>(ModuleTogglerModule::new, StevesCarts.id("module_toggler"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<NoteSequencerModule> NOTE_SEQUENCER = register("note_sequencer", new ModuleType<>(NoteSequencerModule::new, StevesCarts.id("note_sequencer"), 30, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<OreExtractorModule> ORE_EXTRACTOR = register("ore_extractor", new ModuleType<>(OreExtractorModule::new, StevesCarts.id("ore_extractor"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<PowerObserverModule> POWER_OBSERVER = register("power_observer", new ModuleType<>(PowerObserverModule::new, StevesCarts.id("power_observer"), 7, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<StevesArcadeModule> STEVES_ARCADE = register("steves_arcade", new ModuleType<>(StevesArcadeModule::new, StevesCarts.id("steves_arcade"), 50, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<TrickOrTreatCakeServerModule> TRICK_OR_TREAT_CAKE_SERVER = register("trick_or_treat_cake_server", new ModuleType<>(TrickOrTreatCakeServerModule::new, StevesCarts.id("trick_or_treat_cake_server"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));

	// Attachment modules - Missing ones
	public static final ModuleType<TrackRemoverModule> TRACK_REMOVER = register("track_remover", new ModuleType<>(TrackRemoverModule::new, StevesCarts.id("track_remover"), 8, EnumSet.of(ModuleSide.FRONT), ModuleGroup.ATTACHMENT, true, false, false, null, null));

	// Detector modules - All
	public static final ModuleType<DetectorJunctionModule> DETECTOR_JUNCTION = register("detector_junction", new ModuleType<>(DetectorJunctionModule::new, StevesCarts.id("detector_junction"), 5, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<DetectorRedstoneUnitModule> DETECTOR_REDSTONE_UNIT = register("detector_redstone_unit", new ModuleType<>(DetectorRedstoneUnitModule::new, StevesCarts.id("detector_redstone_unit"), 3, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<DetectorStationModule> DETECTOR_STATION = register("detector_station", new ModuleType<>(DetectorStationModule::new, StevesCarts.id("detector_station"), 4, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<DetectorUnitModule> DETECTOR_UNIT = register("detector_unit", new ModuleType<>(DetectorUnitModule::new, StevesCarts.id("detector_unit"), 3, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<EntityDetectorAnimalModule> ENTITY_DETECTOR_ANIMAL = register("entity_detector_animal", new ModuleType<>(EntityDetectorAnimalModule::new, StevesCarts.id("entity_detector_animal"), 4, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<EntityDetectorBatModule> ENTITY_DETECTOR_BAT = register("entity_detector_bat", new ModuleType<>(EntityDetectorBatModule::new, StevesCarts.id("entity_detector_bat"), 3, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<EntityDetectorMonsterModule> ENTITY_DETECTOR_MONSTER = register("entity_detector_monster", new ModuleType<>(EntityDetectorMonsterModule::new, StevesCarts.id("entity_detector_monster"), 5, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<EntityDetectorPlayerModule> ENTITY_DETECTOR_PLAYER = register("entity_detector_player", new ModuleType<>(EntityDetectorPlayerModule::new, StevesCarts.id("entity_detector_player"), 4, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));
	public static final ModuleType<EntityDetectorVillagerModule> ENTITY_DETECTOR_VILLAGER = register("entity_detector_villager", new ModuleType<>(EntityDetectorVillagerModule::new, StevesCarts.id("entity_detector_villager"), 4, EnumSet.noneOf(ModuleSide.class), ModuleGroup.DETECTOR, false, false, false, null, null));

	// Farming modules - Missing ones
	public static final ModuleType<BasicFarmerModule> BASIC_FARMER = register("basic_farmer", new ModuleType<>(BasicFarmerModule::new, StevesCarts.id("basic_farmer"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));
	public static final ModuleType<BasicWoodCutterModule> BASIC_WOOD_CUTTER = register("basic_wood_cutter", new ModuleType<>(BasicWoodCutterModule::new, StevesCarts.id("basic_wood_cutter"), 9, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));
	public static final ModuleType<GalgadorianFarmerModule> GALGADORIAN_FARMER = register("galgadorian_farmer", new ModuleType<>(GalgadorianFarmerModule::new, StevesCarts.id("galgadorian_farmer"), 30, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));
	public static final ModuleType<GalgadorianWoodCutterModule> GALGADORIAN_WOOD_CUTTER = register("galgadorian_wood_cutter", new ModuleType<>(GalgadorianWoodCutterModule::new, StevesCarts.id("galgadorian_wood_cutter"), 28, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));
	public static final ModuleType<HardenedWoodCutterModule> HARDENED_WOOD_CUTTER = register("hardened_wood_cutter", new ModuleType<>(HardenedWoodCutterModule::new, StevesCarts.id("hardened_wood_cutter"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));
	public static final ModuleType<PlanterRangeExtenderModule> PLANTER_RANGE_EXTENDER = register("planter_range_extender", new ModuleType<>(PlanterRangeExtenderModule::new, StevesCarts.id("planter_range_extender"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));
	public static final ModuleType<SilkTouchFarmerModule> SILK_TOUCH_FARMER = register("silk_touch_farmer", new ModuleType<>(SilkTouchFarmerModule::new, StevesCarts.id("silk_touch_farmer"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.FARMING, false, false, false, null, null));

	// Manager modules - All
	public static final ModuleType<CargoManagerModule> CARGO_MANAGER = register("cargo_manager", new ModuleType<>(CargoManagerModule::new, StevesCarts.id("cargo_manager"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.MANAGER, false, false, false, null, null));
	public static final ModuleType<DetectorManagerModule> DETECTOR_MANAGER = register("detector_manager", new ModuleType<>(DetectorManagerModule::new, StevesCarts.id("detector_manager"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.MANAGER, false, false, false, null, null));
	public static final ModuleType<FluidManagerModule> FLUID_MANAGER = register("fluid_manager", new ModuleType<>(FluidManagerModule::new, StevesCarts.id("fluid_manager"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.MANAGER, false, false, false, null, null));
	public static final ModuleType<InventoryEvalizerModule> INVENTORY_EVALIZER = register("inventory_evalizer", new ModuleType<>(InventoryEvalizerModule::new, StevesCarts.id("inventory_evalizer"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.MANAGER, false, false, false, null, null));

	// Processor modules - All
	public static final ModuleType<AdvancedCrafterModule> ADVANCED_CRAFTER = register("advanced_crafter", new ModuleType<>(AdvancedCrafterModule::new, StevesCarts.id("advanced_crafter"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<AdvancedSmelterModule> ADVANCED_SMELTER = register("advanced_smelter", new ModuleType<>(AdvancedSmelterModule::new, StevesCarts.id("advanced_smelter"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<BasicSmelterModule> BASIC_SMELTER = register("basic_smelter", new ModuleType<>(BasicSmelterModule::new, StevesCarts.id("basic_smelter"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<CleaningMachineModule> CLEANING_MACHINE = register("cleaning_machine", new ModuleType<>(CleaningMachineModule::new, StevesCarts.id("cleaning_machine"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<CrafterModule> CRAFTER = register("crafter", new ModuleType<>(CrafterModule::new, StevesCarts.id("crafter"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<CreativeIncineratorModule> CREATIVE_INCINERATOR = register("creative_incinerator", new ModuleType<>(CreativeIncineratorModule::new, StevesCarts.id("creative_incinerator"), 35, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<ExtremeMelterModule> EXTREME_MELTER = register("extreme_melter", new ModuleType<>(ExtremeMelterModule::new, StevesCarts.id("extreme_melter"), 40, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<IncineratorModule> INCINERATOR = register("incinerator", new ModuleType<>(IncineratorModule::new, StevesCarts.id("incinerator"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<LiquidCleanerModule> LIQUID_CLEANER = register("liquid_cleaner", new ModuleType<>(LiquidCleanerModule::new, StevesCarts.id("liquid_cleaner"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<MelterModule> MELTER = register("melter", new ModuleType<>(MelterModule::new, StevesCarts.id("melter"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<StoneCutterModule> STONE_CUTTER = register("stone_cutter", new ModuleType<>(StoneCutterModule::new, StevesCarts.id("stone_cutter"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));
	public static final ModuleType<TreeTapModule> TREE_TAP = register("tree_tap", new ModuleType<>(TreeTapModule::new, StevesCarts.id("tree_tap"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.PROCESSOR, false, false, false, null, null));

	// Realtimer modules - Missing ones
	public static final ModuleType<CageModule> CAGE_REALTIMER = register("cage_realtimer", new ModuleType<>(CageModule::new, StevesCarts.id("cage_realtimer"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.REALTIMER, false, false, false, null, null));
	public static final ModuleType<CakeServerModule> CAKE_SERVER_REALTIMER = register("cake_server_realtimer", new ModuleType<>(CakeServerModule::new, StevesCarts.id("cake_server_realtimer"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.REALTIMER, false, false, false, null, null));
	public static final ModuleType<CleanerModule> CLEANER = register("cleaner", new ModuleType<>(CleanerModule::new, StevesCarts.id("cleaner"), 6, EnumSet.noneOf(ModuleSide.class), ModuleGroup.REALTIMER, false, false, false, null, null));
	public static final ModuleType<DynamiteModule> DYNAMITE = register("dynamite", new ModuleType<>(DynamiteModule::new, StevesCarts.id("dynamite"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.REALTIMER, false, false, false, null, null));
	public static final ModuleType<MilkerModule> MILKER_REALTIMER = register("milker_realtimer", new ModuleType<>(MilkerModule::new, StevesCarts.id("milker_realtimer"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.REALTIMER, false, false, false, null, null));

	// Storage modules - InternalTankModule only (others already registered)
	public static final ModuleType<InternalTankModule> INTERNAL_TANK = register("internal_tank", new ModuleType<>(InternalTankModule::new, StevesCarts.id("internal_tank"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.STORAGE, false, false, false, null, null));

	// Tool modules - Missing ones
	public static final ModuleType<BasicDrillModule> BASIC_DRILL = register("basic_drill", new ModuleType<>(BasicDrillModule::new, StevesCarts.id("basic_drill"), 5, EnumSet.noneOf(ModuleSide.class), ModuleGroup.TOOL, true, false, false, null, null));
	public static final ModuleType<DiamondDrillModule> DIAMOND_DRILL = register("diamond_drill", new ModuleType<>(DiamondDrillModule::new, StevesCarts.id("diamond_drill"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.TOOL, true, false, false, null, null));
	public static final ModuleType<ExternalDistributorModule> EXTERNAL_DISTRIBUTOR = register("external_distributor", new ModuleType<>(ExternalDistributorModule::new, StevesCarts.id("external_distributor"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.TOOL, true, false, false, null, null));
	public static final ModuleType<GalgadorianDrillModule> GALGADORIAN_DRILL = register("galgadorian_drill", new ModuleType<>(GalgadorianDrillModule::new, StevesCarts.id("galgadorian_drill"), 30, EnumSet.noneOf(ModuleSide.class), ModuleGroup.TOOL, true, false, false, null, null));
	public static final ModuleType<HardenedDrillModule> HARDENED_DRILL = register("hardened_drill", new ModuleType<>(HardenedDrillModule::new, StevesCarts.id("hardened_drill"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.TOOL, true, false, false, null, null));
	public static final ModuleType<IronDrillModule> IRON_DRILL = register("iron_drill", new ModuleType<>(IronDrillModule::new, StevesCarts.id("iron_drill"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.TOOL, true, false, false, null, null));

	// Weapon modules - All
	public static final ModuleType<BasicShooterModule> BASIC_SHOOTER = register("basic_shooter", new ModuleType<>(BasicShooterModule::new, StevesCarts.id("basic_shooter"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<DynamiteCarrierModule> DYNAMITE_CARRIER = register("dynamite_carrier", new ModuleType<>(DynamiteCarrierModule::new, StevesCarts.id("dynamite_carrier"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<ProjectileCakeModule> PROJECTILE_CAKE = register("projectile_cake", new ModuleType<>(ProjectileCakeModule::new, StevesCarts.id("projectile_cake"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<ProjectileEggModule> PROJECTILE_EGG = register("projectile_egg", new ModuleType<>(ProjectileEggModule::new, StevesCarts.id("projectile_egg"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<ProjectileFireChargeModule> PROJECTILE_FIRE_CHARGE = register("projectile_fire_charge", new ModuleType<>(ProjectileFireChargeModule::new, StevesCarts.id("projectile_fire_charge"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<ProjectilePotionModule> PROJECTILE_POTION = register("projectile_potion", new ModuleType<>(ProjectilePotionModule::new, StevesCarts.id("projectile_potion"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<ProjectileSnowballModule> PROJECTILE_SNOWBALL = register("projectile_snowball", new ModuleType<>(ProjectileSnowballModule::new, StevesCarts.id("projectile_snowball"), 6, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));
	public static final ModuleType<TNTLauncherModule> TNT_LAUNCHER = register("tnt_launcher", new ModuleType<>(TNTLauncherModule::new, StevesCarts.id("tnt_launcher"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.WEAPON, false, false, false, null, null));

	static {
	}

	public static void init() {
		ModuleTags.init();
	}

	private static <T extends CartModule> ModuleType<T> register(String name, BiFunction<CartEntity, ModuleType<T>, T> factory) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(factory, id, 0, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	}

	@SuppressWarnings("unchecked")
	private static <T extends CartModule> ModuleType<T> register(String name, ModuleType<?> moduleType) {
		return (ModuleType<T>) Registry.register(ModuleType.REGISTRY, StevesCarts.id(name), moduleType);
	}

	private static ModuleType<ThermalEngineModule> registerThermalEngine(String name, BiFunction<CartEntity, ModuleType<ThermalEngineModule>, ThermalEngineModule> factory, int cost, int tanks, TagKey<ModuleType<?>> incompat) {
		Object2IntMap<TagKey<ModuleType<?>>> reqs = new Object2IntOpenHashMap<>();
		reqs.put(ModuleTags.TANKS, tanks);
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(factory, id, cost, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ENGINE, false, false, false, incompat, reqs));
	}

	private static ModuleType<CoalEngineModule> registerCoalEngine(String name, int fuelSlots, float multiplier, int cost, TagKey<ModuleType<?>> incompatibilities) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(((cartEntity, moduleType) -> new CoalEngineModule(cartEntity, moduleType, fuelSlots, multiplier)), id, cost, EnumSet.of(ModuleSide.BACK), ModuleGroup.ENGINE, true, false, false, incompatibilities, null));
	}

	private static ModuleType<SolarEngineModule> registerSolarEngine(String name, int cost, long maxPower) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(((cartEntity, moduleType) -> new SolarEngineModule(cartEntity, moduleType, maxPower)), id, cost, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ENGINE, true, false, true, null, null));
	}

	private static ModuleType<CompactSolarEngineModule> registerCompactSolarEngine(String name, int cost, long maxPower) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(((cartEntity, moduleType) -> new CompactSolarEngineModule(cartEntity, moduleType, maxPower)), id, cost, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ENGINE, true, false, true, null, null));
	}

	private static ModuleType<CreativeEngineModule> registerCreativeEngine(String name, int cost) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(CreativeEngineModule::new, id, cost, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ENGINE, true, false, true, null, null));
	}

	private static <T extends HullModule> HullModuleType<T> registerHull(String name, BiFunction<CartEntity, ModuleType<T>, T> factory, EnumSet<ModuleSide> sides, HullData hullData) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new HullModuleType<>(factory, id, sides, hullData));
	}

	private static <T extends HullModule> HullModuleType<T> registerHull(String name, BiFunction<CartEntity, ModuleType<T>, T> factory, HullData hullData) {
		return registerHull(name, factory, EnumSet.noneOf(ModuleSide.class), hullData);
	}

	private static <T extends ChestModule> ModuleType<T> registerRegularChest(String name, BiFunction<CartEntity, ModuleType<T>, T> factory, EnumSet<ModuleSide> sides, int cost) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(factory, id, cost, sides, ModuleGroup.STORAGE, true, false, false, null, null));
	}
	private static ModuleType<TankModule> registerRegularTank(String name, int buckets, EnumSet<ModuleSide> sides, int moduleCost, boolean noHullTop) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>((entity, type) -> new TankModule(entity, type, buckets * 1000), id, moduleCost, sides, ModuleGroup.STORAGE, true, false, noHullTop, null, null));
	}
}
