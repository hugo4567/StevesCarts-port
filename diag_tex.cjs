const fs = require("fs"), path = require("path");
const base = "src/main/resources/assets/stevescarts";
const tex = path.join(base, "textures");

function texExists(ref) {
  let p = ref.includes(":") ? ref.split(":")[1] : ref;
  return fs.existsSync(path.join(tex, p + ".png"));
}

// which of these relative texture paths exists?
function firstExisting(paths) {
  for (const p of paths) if (fs.existsSync(path.join(tex, p + ".png"))) return p;
  return null;
}

const mdir = path.join(base, "models/item");
const models = fs.readdirSync(mdir).filter(f => f.endsWith(".json"));

const resolvable = [];   // {model, key, oldRef, newRef}
const unresolvable = []; // {model, oldRef}

for (const m of models) {
  let d;
  try { d = JSON.parse(fs.readFileSync(path.join(mdir, m), "utf8")); }
  catch (e) { console.log("PARSE ERR", m, e.message); continue; }
  const txs = d.textures || {};
  for (const [k, v] of Object.entries(txs)) {
    if (typeof v === "string" && v.startsWith("stevescarts:") && !texExists(v)) {
      const name = v.split(":")[1].split("/").pop();
      // priority: items/<name>_icon, items/<name>, item/<name>_icon
      const cand = firstExisting([
        "items/" + name + "_icon",
        "items/" + name,
        "item/" + name + "_icon",
      ]);
      if (cand) resolvable.push({ m, k, oldRef: v, newRef: "stevescarts:" + cand });
      else unresolvable.push({ m, oldRef: v });
    }
  }
}

console.log("RESOLVABLE:", resolvable.length);
for (const r of resolvable) console.log("  " + r.m + " : " + r.oldRef + " -> " + r.newRef);
console.log("\nUNRESOLVABLE:", unresolvable.length);
for (const u of unresolvable) console.log("  " + u.m + " : " + u.oldRef);
