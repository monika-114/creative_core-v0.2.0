## build-fix-15 - Mine Craft generalized hardness / Silk Touch drop resolver

- Mine Craft now treats every block hardness outside the inclusive `[0, 50]` range as hardness `50`; the old hardness `-1`-only special case was removed.
- Removed the hard-coded glass/light-block pickaxe bonus tag. If no registered vanilla or modded item accelerates a block above bare-hand speed, Mine Craft treats that block as pickaxe-efficient at Netherite speed.
- Mine Craft drop resolution now first evaluates the block loot table with a synthetic Silk Touch Netherite pickaxe. Only when that produces no item does the legacy one-block self-drop fallback run.
- Added `#creationcore:mine_craft_drop_fallback_blacklist` (dragon egg, Nether portal, End portal, End gateway) to suppress the self-drop fallback for excluded blocks.
- Vault drops preserve `ominous` and normalize non-`active` vault states to `inactive`; active remains active. Trial spawners do the equivalent for `ominous` and `trial_spawner_state`. Sculk shriekers preserve `can_summon`.
- The generic player break transaction and delayed multiblock duplicate-drop suppression remain in place.

## build-fix-7 — startup crash

- Fixed LightBlockMixin accessing `creationcore:mine_craft` before the deferred item registry was bound during vanilla block bootstrap.
- No gameplay behavior changed.

# Changelog

## v0.1.0 build-fix-14 — biome egg items + model/classification refresh
- Updated the user-supplied Blockbench model/texture for Base Matter and Mine Craft.
- Added nine biome-themed spawn-egg-named plain items: cave, arid, ocean, plains, forest, mountain, wetland, Nether and End.
- Creative Crafting Table hardness is now 2.5 and the block is included in `minecraft:mineable/axe`.
- Mine Craft is now an actual `SwordItem` and is included in `minecraft:swords`, while retaining universal mining, the generic multi-block drop transaction, axe/shovel right-click actions and zero durability consumption.
- Mine Craft is shown in the Combat creative tab; the nine biome egg items are shown in Spawn Eggs.


## v0.1.0 build-fix-11 — Mine Craft single-form rollback + texture refresh
- Removed Mine Craft's R-key dual-mode system, mode networking, mode-2 model override and placeholder texture.
- Restored Mine Craft to the build-fix-6 combat values: 10 attack damage / 1.6 attack speed.
- Restored the original universal high-speed mining behavior while limiting right-click block actions to axe strip/scrape/wax-off and shovel flatten only. Brush, hoe tilling, shears harvest/carve and other second-form interactions are no longer exposed.
- Preserved the build-fix-9/10 generic multi-block break transaction, including the short delayed companion-drop suppression window.
- Preserved Base Matter's generic `PICKAXE_DIG` acceleration and `minecraft:mineable/pickaxe` compatibility.
- Replaced the user-supplied 16×16 textures for Mine Craft, Void Bucket, Bottled Nothing, Blank Matter and Base Matter. Base Matter uses the supplied Base Matter texture on all six faces.

## v0.1.0 build-fix-10 — NeoForge 1.21.1 compile fix
- Fixed `CoreGameplayEvents` calling `ServerPlayer#preventsBlockDrops()`, which is unavailable in the project's Minecraft 1.21.1 mappings and caused `compileJava` to fail.
- Creative-mode suppression for Mine Craft transaction drops now uses the available `ServerPlayer#isCreative()` check.
- Added a static validation guard preventing the unavailable call from being reintroduced.

## v0.1.0 build-fix-9 — generic Mine Craft break transactions
- Replaced the bed/door/double-plant hard-coded duplicate-drop guard with one server-side Mine Craft break transaction per `ServerPlayerGameMode#destroyBlock` operation.
- The transaction wraps the full `ServerPlayerGameMode#destroyBlock` call, starts before `BreakEvent`, suppresses companion `BlockDropsEvent` output during nested/multiblock teardown, and emits exactly one item for the player-selected primary block after a successful removal.
- Transaction cleanup uses `try/finally`, so another mod canceling/short-circuiting block destruction cannot leave stale Mine Craft drop state behind.
- Added a two-tick delayed companion-drop window for modded multiblocks that remove/drop slave parts shortly after the original destroy call, including breaker-less drop events when they still match the same Mine Craft/self-item operation.
- Infested blocks and the Dragon Egg remain excluded from Mine Craft's forced self-drop behavior.
- Confirmed Base Matter uses the standard `minecraft:mineable/pickaxe` tag plus NeoForge `PICKAXE_DIG`, and derives acceleration from the held tool's normal player destroy speed. This keeps vanilla and modded pickaxe-like tools generic instead of class-whitelisting specific pickaxes.

## 0.1.0
- 初始核心流程。
- 新增空白物质、基底物质、创造物质、创造核心、创造工作台。
- 新增凋灵、末影龙、末地返回传送门三段世界交互仪式。
- 新增“空”桶与瓶装“　”的虚空打捞与装瓶机制。
- 新增创造工作台专属占位配方：牛刷怪蛋。

## v0.1.0 build-fix-1
- Fixed the 1.21.1 `Container` import used by `CreativeCraftingResultSlot`.
- Replaced the unavailable `CustomRecipe.Serializer` with 1.21.1 `SimpleCraftingRecipeSerializer` for void bottling.
- Made dragon ritual `SavedData` use an explicit `DataFixTypes` entry for safer reload persistence on 1.21.1.

## v0.1.0 build-fix-2
- Creative Matter now spawns weightlessly above the centre of the End exit fountain, preventing it from falling through the return portal into the Overworld.
- End void-bucket trigger changed from Y -40 +/- 5 to Y -10 +/- 5.
- Void-bucket ascent speed doubled from 0.075 to 0.15 blocks/tick.
- Void-bucket network update interval reduced from 10 ticks to 1 tick and client-side velocity is kept constant for smoother ascent.
- Returning void buckets can now be picked up during ascent.
- On reaching the recorded return height, a void bucket now remains hovering with no gravity instead of falling again.

## v0.1.0 build-fix-3
- Creative Core entity is no longer pickable/attackable and cannot be hit by projectiles.
- Creative Core entity now always uses the glowing outline flag.
- When a non-spectator player comes within 5 blocks, the entity form converts into a normal Creative Core ItemEntity with no pickup delay.
- Removed direct right-click collection from the persistent entity form.

## v0.1.0 build-fix-4
- Void Bucket return motion was changed to an Executive-inspired one-shot upward launch with no gravity and vanilla ItemEntity drag/deceleration.
- Existing Creation Core rituals and crafting systems were otherwise left unchanged.

## v0.1.0 build-fix-5
- Fixed a client crash when picking up the returning Void Bucket.
- The custom void-return entity now uses vanilla ItemEntityRenderer, avoiding the pickup particle casting a temporary ItemEntity to VoidBucketEntity.

## v0.1.0 build-fix-6 — Mine Craft
- Added `creationcore:mine_craft` (挖掘工艺) with no survival recipe yet.
- Added universal Netherite pickaxe/axe/shovel/hoe/shears mining-speed behavior plus explicit pickaxe-speed support for glass, sea lantern, glowstone, and redstone lamp.
- Added special progress for vanilla hardness -1 blocks; Efficiency V is calibrated to about 40 ticks (2 seconds) under normal mining conditions.
- Added fixed self-block drops, excluding infested blocks and the Dragon Egg; blocks with no item form drop nothing.
- Added axe strip/scrape/wax-off, shovel flatten, shears harvest/carve, and brush abilities.
- Added sword/mining enchantment compatibility while rejecting Unbreaking, Mending, Silk Touch, and Fortune. Forced Silk Touch/Fortune do not participate in Mine Craft's special drop path.
- Added Survival Barrier/Light marker visibility while holding the corresponding item or Mine Craft, and Light Block targeting while Mine Craft is held.
- Added a first purple/black double-ended Mine Craft texture.
- Changed the mod-list logo to a Creative Crafting Table-style icon.

## v0.1.0 build-fix-8
- Fixed duplicate Mine Craft self-drops for beds, doors and double-height plants.
- Added two Mine Craft modes, switchable with R only while held.
  - Mode 1: original universal mining/tool functions, 10 attack damage / 1.0 attack speed.
  - Mode 2: empty-hand-like block breaking, 10 attack damage / 1.6 attack speed, brush + hoe tilling.
- Added independent placeholder purple/black model/texture for Mine Craft mode 2.
- Base Matter can now be accelerated by any tool exposing the pickaxe-dig ability.
# v0.2.0

- Replaced placeholders with the confirmed 262-recipe catalog, including 140 biome egg stonecutting mappings.
- Added exclusive shaped and shapeless Creative Crafting recipes and optional JEI integration.
- Base Matter now emits light level 6 and resists fluid replacement and piston movement.
- Changed the displayed mod source name to ASCII `Creation Core` for tooltip font compatibility; localized item names remain unchanged.
