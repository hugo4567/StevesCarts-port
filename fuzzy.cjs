const fs = require("fs"), path = require("path");
const base = "src/main/resources/assets/stevescarts/textures";
const items = fs.readdirSync(path.join(base, "items")).filter(f => f.endsWith(".png")).map(f => f.slice(0, -4));

const names = ["activator","advanced_farmer","bait_fencer","basic_shooter","basic_smelter",
"cage_realtimer","cake_server_realtimer","cleaner","creative_tank","detector","diamond_drill",
"distributor","experience","implemented_rail","inventory_evalizer","liquid_drainer","liquid_manager",
"melter_extreme","milker_realtimer","module_toggler_block","silk_touch_farmer","stone_cutter",
"tnt_launcher","tree_tap","upgrade"];

for (const n of names) {
  // find items containing a significant token
  const tokens = n.split("_");
  const matches = items.filter(it => tokens.some(t => t.length >= 4 && it.includes(t)));
  console.log(n + "  =>  " + (matches.join(", ") || "(none)"));
}
