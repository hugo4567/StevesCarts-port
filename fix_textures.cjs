const fs = require("fs"), path = require("path");
const base = "src/main/resources/assets/stevescarts";
const tex = path.join(base, "textures");
const mdir = path.join(base, "models/item");

const exists = rel => fs.existsSync(path.join(tex, rel + ".png"));

// Category B: block-items -> parent an existing block model (3D render in inventory)
const blockParent = {
  activator: "stevescarts:block/activator",
  detector: "stevescarts:block/detector_normal",
  distributor: "stevescarts:block/distributor",
  liquid_manager: "stevescarts:block/liquid_manager",
  upgrade: "stevescarts:block/upgrade",
  module_toggler_block: "stevescarts:block/module_toggler",
};

// Category A variants (name differs from <name>_icon) + Category C reuse fallbacks.
// All are flat item icons: layer0 -> items/<file>
const flatOverride = {
  // A variants
  melter_extreme: "items/extreme_melter_icon",
  tree_tap: "items/treetap_icon",
  creative_tank: "items/creative_sctank_icon",
  cage_realtimer: "items/cage_icon",
  cake_server_realtimer: "items/cake_server_icon",
  milker_realtimer: "items/milker_icon",
  experience: "items/experience_bank_icon",
  basic_smelter: "items/smelter_icon",
  basic_shooter: "items/shooter_icon",
  // C reuse (no dedicated texture exists)
  advanced_farmer: "items/basic_farmer_icon",
  silk_touch_farmer: "items/basic_farmer_icon",
  diamond_drill: "items/iron_drill_icon",
  stone_cutter: "items/basic_wood_cutter_icon",
  liquid_drainer: "items/liquid_cleaner_icon",
  cleaner: "items/cleaning_machine_icon",
  inventory_evalizer: "items/information_provider_icon",
  bait_fencer: "items/cage_icon",
  tnt_launcher: "items/shooter_icon",
  implemented_rail: "items/railer_icon",
};

const report = { A: [], B: [], C: [], unresolved: [] };

for (const file of fs.readdirSync(mdir).filter(f => f.endsWith(".json"))) {
  const p = path.join(mdir, file);
  let d;
  try { d = JSON.parse(fs.readFileSync(p, "utf8")); } catch (e) { report.unresolved.push(file + " (parse: " + e.message + ")"); continue; }
  const layer0 = d.textures && d.textures.layer0;
  if (typeof layer0 !== "string" || !layer0.startsWith("stevescarts:")) continue;
  const relCur = layer0.split(":")[1];
  if (exists(relCur)) continue; // already fine
  const name = relCur.split("/").pop();

  // Category B: block item
  if (blockParent[name]) {
    fs.writeFileSync(p, JSON.stringify({ parent: blockParent[name] }, null, 2) + "\n");
    report.B.push(name + " -> parent " + blockParent[name]);
    continue;
  }

  // Determine flat target
  let target = null;
  if (flatOverride[name] && exists(flatOverride[name])) {
    target = flatOverride[name];
    (name in flatOverride && ["melter_extreme","tree_tap","creative_tank","cage_realtimer","cake_server_realtimer","milker_realtimer","experience","basic_smelter","basic_shooter"].includes(name)
      ? report.A : report.C).push(name + " -> " + target);
  } else if (exists("items/" + name + "_icon")) {
    target = "items/" + name + "_icon";
    report.A.push(name + " -> " + target);
  } else if (exists("items/" + name)) {
    target = "items/" + name;
    report.A.push(name + " -> " + target);
  }

  if (!target) { report.unresolved.push(name); continue; }

  d.parent = d.parent || "item/generated";
  d.textures.layer0 = "stevescarts:" + target;
  fs.writeFileSync(p, JSON.stringify(d, null, 2) + "\n");
}

console.log("Category A (direct _icon + variants):", report.A.length);
console.log("Category B (block-item -> block model):", report.B.length);
report.B.forEach(x => console.log("   " + x));
console.log("Category C (reuse nearest icon):", report.C.length);
report.C.forEach(x => console.log("   " + x));
console.log("UNRESOLVED:", report.unresolved.length);
report.unresolved.forEach(x => console.log("   " + x));
