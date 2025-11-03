# AircraftWar

*关于对exp1的修改*

首先创建精英敌机类。-->src\edu\hitsz\aircraft\EliteEnemy.java

接下来创建道具相关的类。首先，创建一个道具的抽象父类，作为所有具体道具的基类。-->src\edu\hitsz\prop\AbstractProp.java

需要创建三种具体的道具类。首先，创建加血道具类。-->src\edu\hitsz\prop\BloodProp.java

接下来创建火力道具类。只需在控制台打印相应语句即可。-->src\edu\hitsz\prop\FireProp.java

现在创建炸弹道具类。炸弹道具也只需在控制台打印相应语句即可。-->src\edu\hitsz\prop\BombProp.java

修改现有代码以集成这些新功能。首先，需要修改AbstractAircraft类，添加getMaxHp()和setHp()方法，以便BloodProp能够更好的工作。-->src\edu\hitsz\aircraft\AbstractAircraft.java

现在需要修改BloodProp类，使其使用AbstractAircraft中新添加的方法来操作英雄机的生命值，而不是使用反射机制。-->src\edu\hitsz\prop\BloodProp.java

现在需要修改ImageManager类，添加精英敌机和道具的图片加载功能，以便游戏能够正确显示这些新添加的对象。-->src\edu\hitsz\application\ImageManager.java

接下来需要修改Game类，实现随机产生普通敌机或精英敌机、处理精英敌机发射的子弹、在精英敌机坠毁后产生随机道具，以及处理英雄机与道具的碰撞和生效。-->src\edu\hitsz\application\Game.java

现在需要创建一个PlantUML文件来绘制游戏中各实体类的UML图，包含英雄机、普通敌机、精英敌机、三种道具、两种子弹及其继承关系。-->uml\GameEntitiesUML.puml
