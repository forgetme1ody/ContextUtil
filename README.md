模组信息
=======

该模组灵感来自附魔、冠军、精英怪等等诸如此类的原版游戏机制或模组，并从中提取通用部分。

基于LootContext的实用工具，目前有2种主要功能：

1. 修改值，对于基本类型或其他不可变值，它返回一个新值，例：(LootContext, int) -> int；对于可变类型值，如实体，它修改值本身，例：(LootContext, Entity) -> {...do anything to entity}。
2. 提供值，从LootContext解析特定类型的值，如原版的NumberProvider，可视作LootContext -> Number。
