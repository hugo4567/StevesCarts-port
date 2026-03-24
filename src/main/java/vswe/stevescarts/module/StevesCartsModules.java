package vswe.stevescarts.module;

import java.util.EnumSet;
import java.util.function.BiFunction;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import vswe.stevescarts.util.FluidValue;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.addon.BrakeModule;
import vswe.stevescarts.module.addon.InvisibilityModule;
import vswe.stevescarts.module.attachment.BridgeBuilderModule;
import vswe.stevescarts.module.attachment.FireworkDisplayModule;
import vswe.stevescarts.module.attachment.HydratorModule;
import vswe.stevescarts.module.attachment.RailerModule;
import vswe.stevescarts.module.attachment.SeatModule;
import vswe.stevescarts.module.attachment.TorchPlacerModule;
import vswe.stevescarts.module.attachment.TrackRemoverModule;
import vswe.stevescarts.module.realtimer.LawnMowerModule;
import vswe.stevescarts.module.realtimer.ExperienceModule;
import vswe.stevescarts.module.realtimer.MilkerModule;
import vswe.stevescarts.module.realtimer.CleanerModule;
import vswe.stevescarts.module.realtimer.CageModule;
import vswe.stevescarts.module.realtimer.CakeServerModule;
import vswe.stevescarts.module.realtimer.DynamiteModule;
import vswe.stevescarts.module.addon.HeightControllerModule;
import vswe.stevescarts.module.addon.FreezerModule;
import vswe.stevescarts.module.addon.IncineratorModule;
import vswe.stevescarts.module.addon.PowerObserverModule;
import vswe.stevescarts.module.addon.ColorizerModule;
import vswe.stevescarts.module.addon.ColorRandomizerModule;
import vswe.stevescarts.module.addon.ChunkLoaderModule;
import vswe.stevescarts.module.addon.FertilizerModule;
import vswe.stevescarts.module.addon.DivineShieldModule;
import vswe.stevescarts.module.addon.MelterModule;
import vswe.stevescarts.module.addon.MelterExtremeModule;
import vswe.stevescarts.module.realtimer.NoteSequencerModule;
import vswe.stevescarts.module.engine.AdvancedThermalEngineModule;
import vswe.stevescarts.module.engine.CoalEngineModule;
import vswe.stevescarts.module.engine.CompactSolarEngineModule;
import vswe.stevescarts.module.engine.CreativeEngineModule;
import vswe.stevescarts.module.engine.SolarEngineModule;
import vswe.stevescarts.module.engine.ThermalEngineModule;
import vswe.stevescarts.module.hull.HullData;
import vswe.stevescarts.module.hull.HullModule;
import vswe.stevescarts.module.hull.HullModuleType;
import vswe.stevescarts.module.storage.ChestModule;
import vswe.stevescarts.module.storage.CreativeTankModule;
import vswe.stevescarts.module.storage.ExtractingChestsModule;
import vswe.stevescarts.module.storage.TankModule;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class StevesCartsModules {
	public static final HullModuleType<HullModule> WOODEN_HULL = registerHull("wooden_hull", HullModule::new, new HullData(50, 1, 0, 15));
	public static final HullModuleType<HullModule> STANDARD_HULL = registerHull("standard_hull", HullModule::new, new HullData(200, 3, 6, 50));
	public static final HullModuleType<HullModule> REINFORCED_HULL = registerHull("reinforced_hull", HullModule::new, new HullData(500, 5, 12, 150));
	public static final HullModuleType<HullModule> MECHANICAL_PIG = registerHull("mechanical_pig", HullModule::new, EnumSet.of(ModuleSide.FRONT), new HullData(150, 4, 2, 50));
	public static final HullModuleType<HullModule> CREATIVE_HULL = registerHull("creative_hull", HullModule::new, new HullData(10000, 5, 12, 150));
	public static final HullModuleType<HullModule> GALGADORIAN_HULL = registerHull("galgadorian_hull", HullModule::new, new HullData(1000, 5, 12, 150));
	public static final HullModuleType<HullModule> PUMPKIN_CHARIOT = registerHull("pumpkin_chariot", HullModule::new, new HullData(40, 1, 0, 15));

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
	public static final ModuleType<CreativeTankModule> CREATIVE_TANK = register("creative_tank", new ModuleType<>(CreativeTankModule::new, StevesCarts.id("creative_tank"), 0, EnumSet.noneOf(ModuleSide.class), ModuleGroup.STORAGE, false, false, false, null, null));

	public static final ModuleType<CoalEngineModule> TINY_COAL_ENGINE = registerCoalEngine("tiny_coal_engine", 1, 0.5f, 2, ModuleTags.INCOMPATIBLE_WITH_TINY_COAL_ENGINE);
	public static final ModuleType<CoalEngineModule> COAL_ENGINE = registerCoalEngine("coal_engine", 3, 2.25f, 15, ModuleTags.INCOMPATIBLE_WITH_COAL_ENGINE);
	public static final ModuleType<SolarEngineModule> SOLAR_ENGINE = registerSolarEngine("solar_engine", 12, 100000L);
	public static final ModuleType<CompactSolarEngineModule> COMPACT_SOLAR_ENGINE = register(
		"compact_solar_engine",
		new ModuleType<>(CompactSolarEngineModule::new, StevesCarts.id("compact_solar_engine"), 40, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ENGINE, false, false, false, null, null)
	);
	public static final ModuleType<CreativeEngineModule> CREATIVE_ENGINE = registerCreativeEngine("creative_engine", 0);
	public static final ModuleType<SolarEngineModule> ADVANCED_SOLAR_ENGINE = registerSolarEngine("advanced_solar_engine", 20, 200000L);
	public static final ModuleType<ThermalEngineModule> THERMAL_ENGINE = registerThermalEngine("thermal_engine", ThermalEngineModule::new, 28, 1, ModuleTags.INCOMPATIBLE_WITH_THERMAL_ENGINE);
	public static final ModuleType<ThermalEngineModule> ADVANCED_THERMAL_ENGINE = registerThermalEngine("advanced_thermal_engine", AdvancedThermalEngineModule::new, 58, 2, ModuleTags.INCOMPATIBLE_WITH_ADVANCED_THERMAL_ENGINE);

	public static final ModuleType<SeatModule> SEAT = register("seat", new ModuleType<>(SeatModule::new, StevesCarts.id("seat"), 3, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ATTACHMENT, true, false, true, null, null));
	public static final ModuleType<FireworkDisplayModule> FIREWORK_DISPLAY = register("firework_display", new ModuleType<>(FireworkDisplayModule::new, StevesCarts.id("firework_display"), 45, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<TorchPlacerModule> TORCH_PLACER = register("torch_placer", new ModuleType<>(TorchPlacerModule::new, StevesCarts.id("torch_placer"), 14, EnumSet.of(ModuleSide.LEFT, ModuleSide.RIGHT), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<RailerModule> RAILER = register("railer", new ModuleType<>((minecart, type) -> new RailerModule(minecart, type, 1), StevesCarts.id("railer"), 3, EnumSet.of(ModuleSide.TOP), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<RailerModule> LARGE_RAILER = register("large_railer", new ModuleType<>((minecart, type) -> new RailerModule(minecart, type, 2), StevesCarts.id("large_railer"), 9, EnumSet.of(ModuleSide.TOP), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<BridgeBuilderModule> BRIDGE_BUILDER = register("bridge_builder", new ModuleType<>(BridgeBuilderModule::new, StevesCarts.id("bridge_builder"), 12, EnumSet.of(ModuleSide.FRONT), ModuleGroup.ATTACHMENT, true, false, false, null, null));
	public static final ModuleType<HydratorModule> HYDRATOR = register("hydrator", new ModuleType<>(HydratorModule::new, StevesCarts.id("hydrator"), 6, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, Util.make(new Object2IntOpenHashMap<>(), m -> m.put(ModuleTags.TANKS, 1))));
	public static final ModuleType<LawnMowerModule> LAWN_MOWER = register("lawn_mower", new ModuleType<>(LawnMowerModule::new, StevesCarts.id("lawn_mower"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<ExperienceModule> EXPERIENCE = register("experience", new ModuleType<>(ExperienceModule::new, StevesCarts.id("experience"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<TrackRemoverModule> TRACK_REMOVER = register("track_remover", new ModuleType<>(TrackRemoverModule::new, StevesCarts.id("track_remover"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<MilkerModule> MILKER = register("milker", new ModuleType<>(MilkerModule::new, StevesCarts.id("milker"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<CleanerModule> CLEANER = register("cleaner", new ModuleType<>(CleanerModule::new, StevesCarts.id("cleaner"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<CageModule> CAGE = register("cage", new ModuleType<>(CageModule::new, StevesCarts.id("cage"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<CakeServerModule> CAKE_SERVER = register("cake_server", new ModuleType<>(CakeServerModule::new, StevesCarts.id("cake_server"), 10, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<DynamiteModule> DYNAMITE = register("dynamite_carrier", new ModuleType<>(DynamiteModule::new, StevesCarts.id("dynamite_carrier"), 30, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));
	public static final ModuleType<NoteSequencerModule> NOTE_SEQUENCER = register("note_sequencer", new ModuleType<>(NoteSequencerModule::new, StevesCarts.id("note_sequencer"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ATTACHMENT, false, false, false, null, null));

	public static final ModuleType<BrakeModule> BRAKE = register("brake", new ModuleType<>(BrakeModule::new, StevesCarts.id("brake"), 12, EnumSet.of(ModuleSide.RIGHT), ModuleGroup.ADDON, true, false, false, null, null));
	public static final ModuleType<InvisibilityModule> INVISIBILITY_CORE = register("invisibility_core", new ModuleType<>(InvisibilityModule::new, StevesCarts.id("invisibility_core"), 21, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<HeightControllerModule> HEIGHT_CONTROLLER = register("height_controller", new ModuleType<>(HeightControllerModule::new, StevesCarts.id("height_controller"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<FreezerModule> FREEZER = register("freezer", new ModuleType<>(FreezerModule::new, StevesCarts.id("freezer"), 18, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<IncineratorModule> INCINERATOR = register("incinerator", new ModuleType<>(IncineratorModule::new, StevesCarts.id("incinerator"), 20, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<PowerObserverModule> POWER_OBSERVER = register("power_observer", new ModuleType<>(PowerObserverModule::new, StevesCarts.id("power_observer"), 15, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ColorizerModule> COLORIZER = register("colorizer", new ModuleType<>(ColorizerModule::new, StevesCarts.id("colorizer"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ColorRandomizerModule> COLOR_RANDOMIZER = register("color_randomizer", new ModuleType<>(ColorRandomizerModule::new, StevesCarts.id("color_randomizer"), 8, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<ChunkLoaderModule> CHUNK_LOADER = register("chunk_loader", new ModuleType<>(ChunkLoaderModule::new, StevesCarts.id("chunk_loader"), 45, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<FertilizerModule> FERTILIZER = register("fertilizer", new ModuleType<>(FertilizerModule::new, StevesCarts.id("fertilizer"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<DivineShieldModule> DIVINE_SHIELD = register("divine_shield", new ModuleType<>(DivineShieldModule::new, StevesCarts.id("divine_shield"), 40, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<MelterModule> MELTER = register("melter", new ModuleType<>(MelterModule::new, StevesCarts.id("melter"), 12, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<MelterExtremeModule> MELTER_EXTREME = register("melter_extreme", new ModuleType<>(MelterExtremeModule::new, StevesCarts.id("melter_extreme"), 22, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));

	public static final ModuleType<AdvancedFarmerModule> ADVANCED_FARMER = register(
		"advanced_farmer",
		new ModuleType<>(AdvancedFarmerModule::new, StevesCarts.id("advanced_farmer"), 30, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null)
	);
	public static final ModuleType<LiquidCleanerModule> LIQUID_CLEANER = register("liquid_cleaner", new ModuleType<>(LiquidCleanerModule::new, StevesCarts.id("liquid_cleaner"), 35, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<AdvancedShooterModule> ADVANCED_SHOOTER = register("advanced_shooter", new ModuleType<>(AdvancedShooterModule::new, StevesCarts.id("advanced_shooter"), 40, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));
	public static final ModuleType<TreeTapModule> TREE_TAP = register("tree_tap", new ModuleType<>(TreeTapModule::new, StevesCarts.id("tree_tap"), 25, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ADDON, false, false, false, null, null));

	public static void init() {
		ModuleTags.init();
	}

	private static <T extends CartModule> ModuleType<T> register(String name, ModuleType<T> module) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, module);
	}

	private static ModuleType<ThermalEngineModule> registerThermalEngine(String name, BiFunction<CartEntity, ModuleType<ThermalEngineModule>, ThermalEngineModule> factory, int cost, int tanks, TagKey<ModuleType<?>> incompat) {
		Object2IntMap<TagKey<ModuleType<?>>> reqs = new Object2IntOpenHashMap<>();
		reqs.put(ModuleTags.TANKS, tanks);
		return register(name, new ModuleType<>(factory, StevesCarts.id(name), cost, EnumSet.noneOf(ModuleSide.class), ModuleGroup.ENGINE, false, false, false, incompat, reqs));
	}

	private static ModuleType<CoalEngineModule> registerCoalEngine(String name, int fuelSlots, float multiplier, int cost, TagKey<ModuleType<?>> incompatibilities) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(((cartEntity, moduleType) -> new CoalEngineModule(cartEntity, moduleType, fuelSlots, multiplier)), id, cost, EnumSet.of(ModuleSide.BACK), ModuleGroup.ENGINE, true, false, false, incompatibilities, null));
	}

	private static ModuleType<SolarEngineModule> registerSolarEngine(String name, int cost, long maxPower) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(((cartEntity, moduleType) -> new SolarEngineModule(cartEntity, moduleType, maxPower)), id, cost, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ENGINE, true, false, true, null, null));
	}

	private static ModuleType<CompactSolarEngineModule> registerCompactSolarEngine(String name, int cost) {
		Identifier id = StevesCarts.id(name);
		return Registry.register(ModuleType.REGISTRY, id, new ModuleType<>(CompactSolarEngineModule::new, id, cost, EnumSet.of(ModuleSide.CENTER, ModuleSide.TOP), ModuleGroup.ENGINE, true, false, true, null, null));
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
