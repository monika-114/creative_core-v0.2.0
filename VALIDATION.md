# v0.2 validation status

## v0.2.0 buildfix3

- 251 recipes; 288 JSON/resource files and 20 textures passed static validation.
- All seven infestation-potion recipes, farmland, dirt_path, vault_copper and trial_spawner_copper are removed (11 recipes total). Silverfish-egg routes remain.
- Ominous vault/trial spawner output names now use JSON text strings, while block_state.ominous stays "true". Runtime verification is still required with different ominous bottle levels and ingredient positions.
- Replace the checkout's src/main/resources/data/creationcore/recipe folder with this release's folder; merging folders alone leaves obsolete recipes behind. Keep a backup of local custom recipes, if any.
- No Java changes in this revision. Java 21/Gradle are unavailable in this workspace, so no compiled JAR or in-game verification is claimed.

## v0.2.0 buildfix2

Resource checks cover 262 recipes, including 140 valid ASCII stonecutting paths and 19 separate disc recipes. Each disc recipe outputs one copy and returns the original (two total) to avoid an invalid count-2 unstackable result. The documented top-middle Blank Matter position is required. Mine Craft now requires a Creative Core. The revised specification is included in docs/recipe_spec.docx.

Java 21 and Gradle are still unavailable here; this is a source release, not a compiled or game-tested JAR. Priority runtime checks: all nine biome eggs in a stonecutter; a disc copy with normal click and Shift-click; a small recipe placed at each corner; JEI empty-slot borders; Mine Craft with/without Creative Core.

When updating an existing checkout, remove the old JSON files with Chinese biome names from src/main/resources/data/creationcore/recipe/stonecutting, then copy in this release's ASCII-named replacements. All 140 mappings are preserved.

## v0.2.0 buildfix1

- Fixed the reported ModRecipes.java compilation error by obtaining the vanilla shaped/shapeless codecs through RecipeSerializer's public codec() and streamCodec() methods instead of accessing serializer implementation fields.
- Recipe JSON, serializer IDs, JEI integration, gameplay behavior and mod version (0.2.0) are unchanged.
- This revision has not been compiled or launched here: only Java 17 is installed and Gradle is unavailable. GitHub Actions must confirm the build and produce the installable JAR.

## Completed here

- `python3 tools/generate_recipes_v02.py`: 262 recipes generated from `docs/recipe_spec.docx`; 140 stonecutting mappings and 27 separately crafted spawn eggs.
- `python3 tools/validate_project.py`: 299 JSON/resource files parsed and 20 textures checked; no static errors.
- JEI integration uses the optional 1.21.1 API at version 19.21.1.248; the dedicated category is separate from normal vanilla crafting.

## Build and game verification still required

This workspace does not include Java 21 or Gradle, and its artifact download endpoints timed out. Run the included GitHub Actions workflow or `gradle build` with Java 21 to compile and produce `build/libs/creationcore-0.2.0.jar`. The compiled mod has not been launched here.

In a 1.21.1 NeoForge 21.1.248 client, check the following representative cases:

1. JEI installed and absent: dedicated Creative Crafting category exists only when JEI is present; ordinary recipes remain visible in JEI's vanilla categories.
2. Creative Crafting Table: the blank matter and mine craft recipes work; old paper and cow spawn egg placeholders do not.
3. Stonecutter: one biome egg yields one selected spawn egg. Verify several choices from the same source egg, including a conditional high-version result with and without the backport mod.
4. Bottle/glass and tool remainders: the barrier returns eight glass bottles, path/farmland return the input tool, and the void bottling recipe returns an ordinary bucket.
5. Components: the light item places level-1 light; ominous vault/trial spawner retain ominous state after placement.
6. Base Matter: flowing water leaves it in place, a piston cannot push it, and emitted light is level 6.
7. Tooltip: the source name appears as `Creation Core`. The gray `creationcore:bottled_nothing` line is Minecraft's advanced item ID and can be hidden with F3+H.
