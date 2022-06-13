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
2. `main.ttf` => `app/src/main/res/font`, `core/src/main/resources/fonts`

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
  - [ ] 基础图标更改
    - [x] 女士
    - [ ] 野伏众（3种）
    - [ ] 幕府士兵
    - [ ] 子弹
      - [x] 蝴蝶
      - [ ] 刀光
      - [ ] 弩箭
  - [x] 动画更改（蝴蝶飞呀飞qwq）
    - [x] 设计图片轮播动画
    - [ ] 使用状态机设计技能图片切换
    - [x] 将蝴蝶（一个Attack对象）放在女士手上
  - [ ] 操作面板更改
    - [x] 设计战斗界面
    - [ ] 召唤/技能/普攻图标/道具使用图标
    - [ ] 暂停页面
    - [x] 添加摇杆
    - [x] 适配多点触控
  - [ ] 女士技能更新
    - [ ] 普攻：发射蝴蝶
    - [ ] 被动灼烧标记
    - [ ] 大招的蝴蝶
  - [ ] 追踪自动索敌更改（加权求取攻击目标）
  - [ ] 操作方式更改
    - [ ] 电脑：W/S/A/D+E+Q+(shift)1234
    - [ ] 手机
      - [x] 左边使用摇杆控制
        - [x] 摇杆控制速度：触控点与中心距离，两个等级
        - [x] 摇杆控制方向
      - [ ] 右边控制出招
- [ ] 游戏逻辑
  - [ ] 伤害计算
    - [ ] Attack 对象 + UnderAttack 接口
    - [x] 元素种类
    - [ ] 元素反应
    - [ ] 暴击计算
  - [ ] 碰撞检测
  - [ ] 面板计算
    - [ ] 当前 Buff/Debuff 列表
    - [ ] Buff/Debuf 延迟
  - [ ] 特殊状态
    - [ ] 特殊状态记录：Hasmap
  - [ ] 

## Notes

- [x] 左侧虚拟摇杆：https://github.com/kongqw/AndroidRocker
- [ ] 战斗场地尺寸
  - [ ] 屏幕尺寸：2400x1080
  - [ ] 每个格子：120x120
  - [ ] 总共格子：20x9
  - [ ] 人物大小：平均2格左右，240x240