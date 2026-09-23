<?php

$base = __DIR__ . DIRECTORY_SEPARATOR . "src/main/resources/assets/stevescarts";
$tex = $base . DIRECTORY_SEPARATOR . "textures";

function texExists(string $ref, string $tex): bool
{
    $parts = explode(":", $ref, 2);
    $path = count($parts) > 1 ? $parts[1] : $ref;
    return file_exists($tex . DIRECTORY_SEPARATOR . $path . ".png");
}

// which of these relative texture paths exists?
function firstExisting(array $paths, string $tex): ?string
{
    foreach ($paths as $path) {
        if (file_exists($tex . DIRECTORY_SEPARATOR . $path . ".png")) {
            return $path;
        }
    }
    return null;
}

$mdir = $base . DIRECTORY_SEPARATOR . "models/item";
$models = array_values(array_filter(scandir($mdir), static function (string $file): bool {
    return str_ends_with($file, ".json");
}));

$resolvable = [];   // {model, key, oldRef, newRef}
$unresolvable = []; // {model, oldRef}

foreach ($models as $m) {
    try {
        $contents = file_get_contents($mdir . DIRECTORY_SEPARATOR . $m);
        $d = json_decode($contents, true, 512, JSON_THROW_ON_ERROR);
    } catch (Throwable $e) {
        echo "PARSE ERR ", $m, " ", $e->getMessage(), PHP_EOL;
        continue;
    }

    $txs = $d["textures"] ?? [];
    foreach ($txs as $k => $v) {
        if (is_string($v) && str_starts_with($v, "stevescarts:") && !texExists($v, $tex)) {
            $refParts = explode(":", $v, 2);
            $name = basename($refParts[1]);
            // priority: items/<name>_icon, items/<name>, item/<name>_icon
            $cand = firstExisting([
                "items/" . $name . "_icon",
                "items/" . $name,
                "item/" . $name . "_icon",
            ], $tex);
            if ($cand !== null) {
                $resolvable[] = ["m" => $m, "k" => $k, "oldRef" => $v, "newRef" => "stevescarts:" . $cand];
            } else {
                $unresolvable[] = ["m" => $m, "oldRef" => $v];
            }
        }
    }
}

echo "RESOLVABLE: ", count($resolvable), PHP_EOL;
foreach ($resolvable as $r) {
    echo "  ", $r["m"], " : ", $r["oldRef"], " -> ", $r["newRef"], PHP_EOL;
}
echo PHP_EOL, "UNRESOLVABLE: ", count($unresolvable), PHP_EOL;
foreach ($unresolvable as $u) {
    echo "  ", $u["m"], " : ", $u["oldRef"], PHP_EOL;
}