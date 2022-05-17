# Aircraft-War

[toc]

## 说明

本代码可以通过 Gradle 同时编译出同一游戏逻辑的 Swing 版本和 Android 版本。

## UML

所有 UML 图见 [AircraftWar-all.puml](uml/AircraftWar-all.puml)，其部分子图位于 `uml/*.puml`。

## 运行

### 注意事项

1. 本项目使用 Gradle 管理，使用 IDEA 打开项目工程时请加载 Gradle 项目。

### 操作说明

#### 电脑

1. 鼠标拖动或者方向键（`↑/↓/←/→`）移动英雄机
2. `z`键发射子弹
3. 按下`shift`可以减慢移动速度（微操）
4. 英雄机判定点不是整个飞机，而是飞机中间的小点，方便操作

#### 手机

1. 单指触摸拖拽英雄机移动
2. 判定点等如上

### 直接运行编译好的 Jar 文件

如果您是助教，我已经程序编译打包为单个`.jar`文件，直接双击 `run_jar.bat`即可运行。

编译好的 Jar 文件放在 `pc/build/libs/pc.jar;core/build/libs/core.jar`。

### 运行

1. 使用 Android Studio 打开项目文件夹（即此 `README.md` 文件所在文件夹）
2. 等待 Android Studio 加载 Gradle 完成
3. 运行 `app` 选项为 Android 程序
4. 运行 `AircraftWar[run]` 选项为电脑端 Swing 程序
