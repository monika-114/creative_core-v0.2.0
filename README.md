# 创造核心 / Creation Core — v0.2.0

> Minecraft 1.21.1 · NeoForge 21.1.248 · Java 21

物品名称保留中文翻译；模组来源标签显示为 **Creation Core**，避免部分第三方提示字体把中文来源名渲染为乱码。内部 Mod ID 是 `creationcore`。

## v0.2 配方与 JEI

`docs/recipe_spec.docx` 是本次使用的合成表。262 个配方文件包含 140 条群系蛋切石映射；原版工作台、锻造台、切石机配方由 JEI 原生展示，创造工作台专属有序及无序配方在独立的 JEI 分类展示。JEI 为可选依赖。高版本物品配方使用 `neoforge:item_exists` 条件。旧纸合成空白物质和牛生成蛋占位配方已删除。

基底物质不会被流体替换，不可被活塞推动，发光等级为 6。完整配方的 JSON 文件位于 `src/main/resources/data/creationcore/recipe/`。

## v0.1 已实现的核心流程

1. **空白物质**：创造工作台使用遮光玻璃、木棍及瓶装“　”合成。
2. **凋灵仪式**：凋灵出生爆炸会吞掉本次爆炸影响到的全部空白物质；该凋灵获得持久标记。死亡后移除下界之星，并固定生成 `1×基底物质`。
3. **基底物质**：可放置为中心 8×8×8 的漂浮立方体；无碰撞、可选中、空手约 4.5 秒破坏，任何正确声明镐挖掘能力的原版/模组工具都可按自身效率加速；免疫爆炸和末影龙冲撞破坏。
4. **末影龙仪式**：末影龙死亡时，消耗 Dragon Fight Origin 中心纵轴上的全部基底物质；记录一个可持久化的待完成仪式。检测到出口传送门完成后，仅生成 `1×创造物质`。
5. **创造核心仪式**：只含 `1×创造物质` 的合法潜影盒掉落物进入末地主岛返回传送门后被完全消耗，在主世界世界出生点生成悬浮且高亮的创造核心实体；实体不可选中、不可攻击，任意非旁观玩家进入 5 格范围后会原地转化为普通创造核心掉落物。
6. **创造工作台**：锻造台使用 `创造核心（模板槽） + 工作台（基础槽） + 下界合金锭（材料槽）` 制作。
7. **创造工作台配方系统**：3×3 GUI 与原版工作台一致；先匹配 `creationcore:creative_crafting` 专属配方，再回退到全部普通 `minecraft:crafting` 配方。
8. **专属配方**：包括 9 种群系生成蛋、特殊方块、原矿、唱片复制等，见配方文档。
9. **虚空打捞**：原版空桶可在末地、主世界、下界掉入虚空后转化为“空”桶；末地阈值为 `Y=-10±5`，主世界/下界为 `minBuildHeight-10±5`。返回阶段采用 Executive 风格的一次性向上抛升：无重力、正常方块碰撞、可在途中拾取，并在记录的返回 Y 附近停止悬浮。
10. **“空”桶**：是普通自定义 Item，不继承 BucketItem，因此没有舀取、倒出液体或炼药锅桶交互能力。
11. **瓶装“　”**：四个玻璃呈十字包围“空”桶，输出 `3×瓶装“　”`，同时返还 `1×普通桶`。

## 兼容性预留

- `#creationcore:creative_core_containers`：默认包含全部 17 种原版潜影盒。其他模组/数据包未来可向该标签加入兼容容器。
- 独立 `creative_crafting` RecipeType 及有序、无序 Serializer；已添加可选 JEI 分类。
- 世界交互逻辑集中在 event/data 层，没有把 Create、JEI 等可选模组类引用进核心代码。

## build-fix-6：挖掘工艺

物品 **挖掘工艺**（`creationcore:mine_craft`）新增下界合金工具合成配方；现作为剑类武器加入战斗创造栏用于测试，同时保留通用挖掘能力。

- 无耐久组件，无法损坏。
- Tooltip 目标属性：攻击伤害 10、攻击速度 1.6。
- 对方块取原版下界合金镐/斧/铲/锄/剑与剪刀中的最高破坏速度；若注册表中没有任何其他物品能比空手更快地挖掘该方块，则挖掘工艺把它视为可被下界合金镐效率加速，不再维护玻璃等硬编码特例。
- 方块硬度位于 `[0,50]` 时使用原硬度；小于 0 或大于 50 时统一按硬度 50 计算。这样包括基岩在内的异常硬度方块仍可被挖掘。
- 掉落首先用“精准采集 I 的下界合金镐”模拟原方块 loot table；若完全没有物品掉落，才检查 `#creationcore:mine_craft_drop_fallback_blacklist`，未命中黑名单时回退为 `1×方块自身`。宝库/试炼刷怪笼会保留 ominous 并规范化其活动状态，幽匿尖啸体会保留 can_summon。
- 右键仅保留斧子/铲子类功能：去皮、铜除锈、去蜡、土径。
- 支持剑与采掘工具体系附魔，但明确拒绝耐久、经验修补、精准采集、时运；即使通过命令强制附上精准采集/时运，特殊掉落也不会读取它们。
- 生存模式下，手持屏障/亮度方块本身或挖掘工艺时可显示对应不可见方块的标记；挖掘工艺还可选中亮度方块。
- 当前使用新的 16×16 紫黑配色挖掘工艺材质。
- 模组列表图标改为创造工作台风格。

## 后续目标

EMI、REI、Ponder、Create 联动、Fabric 版、1.20.1 版，以及完整粒子/旋转动画。

## 构建

见 [BUILDING.md](BUILDING.md)。工程自带 `.github/workflows/build.yml`，推送到 GitHub 后可直接通过 Actions 使用 Java 21 + Gradle 9.2.1 构建。

## 静态验证

执行：

```bash
python3 tools/validate_project.py
```

会检查 JSON、16×16 材质、模型引用、关键配方和兼容标签。当前交付版本已通过该检查。

真实 NeoForge 编译状态请见 [VALIDATION.md](VALIDATION.md)。


## v0.1.0 build-fix-2 test changes

- Creative Matter now appears weightlessly above the centre of the End exit fountain, preventing accidental travel through the return portal.
- End void-fishing threshold: Y = -10 +/- 5.
- Overworld/Nether thresholds remain min build height - 10 +/- 5.
- Void Bucket return speed: 0.15 blocks/tick.
- Returning Void Buckets are pickable while rising and hover at their recorded return height.
- Void Bucket entity network update interval is 1 tick for smoother motion.


## build-fix-14：群系生成蛋物品与分类调整

- 更新基底物质与挖掘工艺的 Blockbench 模型/材质。
- 新增 9 个群系主题生成蛋物品（当前为普通物品，暂无生成逻辑）：洞穴、干旱、海洋、平原、森林、山地、湿地、下界、末地。
- 创造工作台硬度调整为 2.5，并加入 `minecraft:mineable/axe`，可被斧类工具正常加速。
- 挖掘工艺改为实际继承 `SwordItem`，并加入 `minecraft:swords`；仍保留无限耐久、原有挖掘/掉落事务与斧/铲右键能力。
