# Re Signora

[toc]

## 说明

本代码为客户端代码。

本代码可以同时编译出同一游戏逻辑的 Swing 版本和 Android 版本。

## 运行

### 注意事项

1. 本项目使用的 Gradle DSL 为 Kotlin DSL，Android Studio 并不完全支持，每次添加/删除控件等的时候请查看 `build.gradle.kts` 是否有不合法修改。

#### 电脑

1. `W/S/A/D` 移动角色
2. `Shift` 冲刺
3. `E` 元素战技
4. `Q` 元素爆发
5. `1/2/3/4` 切换角色
6. ……大致与原神电脑版一致

#### 手机

1. 左手使用虚拟摇杆控制角色方向
2. 右手触摸按钮
3. ……大致与原神手机版一致

### 编译运行

1. 电脑
   1. `gradlew :pc:run` 或者运行 `run_jar.bat`
2. 手机
   1. `adb connect # ip_of_android:port`
   2. `gradlew :app:assembleDebug`
   3. `adb install /path/to/apk`

## 资源

请从 [Re-Signora/resources (github.com)](https://github.com/Re-Signora/resources) 下载资源。

1. `genshin.ttf` => `app/src/main/res/font`, `core/src/main/resources/fonts`

## ToDo List

- [x] 从原代码适配图像显示、声音播放、记录储存、窗口逻辑
- [ ] 更改桌面端和移动端框架……？
  - [ ] SDL？
    - [ ] 需要 NDK，多架构支持堪忧...
    - [ ] 好处是绘图、事件等代码统一
  - [ ] LibGDX？
    - [ ] 开源，非常受欢迎，支持多个平台，支持Tiled，Box2D等，良好的文档资料
    - [ ] based on OpenGL (ES) that works on Windows, Linux, macOS, Android, your browser and iOS.[Get started](https://libgdx.com/dev/)
- [ ] 修改为横版
  - [x] 横屏
    - [x] Android 横屏
    - [x] pc 横屏+缩放
  - [ ] 物体横向移动
- [ ] XSytem
  - [x] XLayout
  - [x] XLayoutManager
  - [x] XActivity
  - [x] XActivityManager
  - [ ] XView
    - [x] XButton
    - [ ] XJoySticks
    - [ ] XMediaPlayer

## Roadmap

- [ ] 作战功能
  - [ ] 基础图标更改（英雄机--女士，敌机--野伏众（3种）/幕府士兵，子弹--蝴蝶/刀光/弩箭）
  - [ ] 动画更改（蝴蝶飞呀飞qwq）
  - [ ] 操作面板更改（召唤/技能/普攻图标，道具使用图标/暂停页面）
  - [ ] 女士技能更新（被动灼烧标记/大招的蝴蝶）
  - [ ] 追踪自动索敌更改（加权求取攻击目标）
  - [ ] 操作方式更改（改成上下左右，而非触屏）

## Notes

1. 左侧虚拟摇杆：https://github.com/kongqw/AndroidRocker

