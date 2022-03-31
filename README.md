# Aircraft-War

## 说明

本项目已分为 Scala、Java 两独立部分，Java 部分完成课程基本要求，Scala 部分想在完成课程基础上做点奇怪东西……

对助教的话：如果可以用 Scala 直接交的话请告诉我，不胜感激😭。

## UML

Scala 项目的 UML 图见 [AircraftWar-scala.puml](uml/AircraftWar-scala.puml)，Java 项目的 UML 图见 [AircraftWa-java.puml](uml/AircraftWar-java.puml)。

*图片不够大...*

![uml_svg](README.assets/AircraftWar-java-0.0.2.svg)

## 运行

### 注意事项

1. **加载本 SBT 项目需要 IDEA 安装 Scala 插件！**

2. **加载 SBT 项目需要稳定的互联网连接以加载外部依赖！**

   详见：[(44条消息) sbt卡住的解决办法，sbt设置代理_ArkFallen的博客-CSDN博客_sbt 代理](https://blog.csdn.net/baidu_33340703/article/details/105548180)

   如果您没有稳定的网络代理的话，删除 `build.sbt`再用 IDEA 打开也可以加载项目；或者直接运行 `.jar` 文件也可以。

3. **加载 SBT 项目的时候需要改动/添加`~/.sbt/1.0/global.sbt`（Windows上是`C:\Users\用户名称\.sbt\1.0\global.sbt`）以实现 ScalaTest！**

   在上述文件中添加此行：

   ```scala
   resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"
   ```

如果您需要从源代码运行本 Scala 项目请一定要执行上面的步骤！

### 操作说明

1. Java version: 鼠标拖动移动英雄机
2. Scala version:
   1. 鼠标拖动或者方向键（`↑/↓/←/→`）移动英雄机
   2. `z`键发射子弹
   3. 按下`shift`可以减慢移动速度（微操）

### 直接运行编译好的 Jar 文件

如果您是助教，我已经将 Scala 的程序编译打包为单个`.jar`文件，直接双击 `run_jar.bat`即可运行。

### 运行 Scala

1. 使用 IDEA 打开项目文件夹（即此 `README.md` 文件所在文件夹）

2. IDEA 安装 Scala 插件

   ![image-20220325193308865](README.assets/image-20220325193308865.png)

3. 重启 IDEA 并且重新打开文件夹，等待 IDEA 加载 sbt 完成

4. 如果运行配置中有这一项：![image-20220327174215752](README.assets/image-20220327174215752.png)

   1. 运行这一项（`Main (scala)`）即可

5. 如果运行配置中没有这一项

   1. 打开 `AircraftWar/src/main/scala/work/chiro/game/application/Main.scala`

   2. 点击三角形运行

      ![image-20220325193510570](README.assets/image-20220325193510570.png)

      ![image-20220325193520239](README.assets/image-20220325193520239.png)

### 运行 Java

1. 使用 IDEA 打开项目文件夹（即此 `README.md` 文件所在文件夹）

2. 等待 IDEA 加载 sbt 完成

3. 如果运行配置中有这一项：![image-20220327174334883](README.assets/image-20220327174334883.png)

   1. 运行这一项（`Main (java)`）即可

4. 如果运行配置中没有这一项

   1. 打开 `AircraftWar/src/main/scala/edu/hitsz/application/Main.java`

   2. 点击三角形运行

      ![image-20220327174436632](README.assets/image-20220327174436632.png)

