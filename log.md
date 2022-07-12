

## 卑微的测试工具qwq

### 7.6

1. **ShogunateSoldier**

   1. 函数startMoving没有用
   2. ShogunateSolider是构造函数，若是没有的话不会出现敌人，但是没有的话最后的背景图也不会出现，不是很理解。

2. **新增ElementalBuff的Enum**，（虽然里面只有一个Firing灼烧）

3. **将ElementalBuff添加进了ElementUtils**

4. **DrawColor新增Firing** 

   ```ElementalBuff.Firing,new CoupleColor(0xFFff0000,0xFFcc3333)```

5.  
    ```Java
    public class AbstractBuff extends AbstractThing<BasicAttackAttributes, AbstractAction>
    ```
    

改了Buff 的继承后，```FiringBuff```的在继承一直不对

6.  ```XView```里面没有继承```AbstractObject```里面的 ```getSize、getHeight、getWidth```的函数，但是增添继承后，还是无法改变字体大小，原本还以为是没有继承的问题，结果发现不是，测试后发现，也确实把数据传进了....完了，当时没做记录，找不到了，不过输出测试确实是有了的



### 7.7

1. ```BattleActivity```里面```getGame().addEnemyThing(shogunateSolder)```是添加敌人进入组，若是注释掉的话，不会创造敌人（没有显示，实际也没有）

2. ```Game```中```addEnemyThing```中```instanceof```其实就是判断左边的是不是右边的子类，即```A instanceof B```如果A是B的子类就为```true```,那这里其实是一个大类，对于进来的```EnemyThing```分类判断，进入```Character Attack UnderAttack```

   1. ```Enemy```就是```enemies.add```
   2. ```Attack```就是```enemiesAttacks.add```
   3. ```UnderAttack```就是```enemiesUnderAttacks.add```这个地方不是很理解，记录的应该是地方能被攻击的对象，那其实所有的enemy应该都需要加入这个类才对，但是我看```ShogunateSoldier```好像没有继承```UnderAttack```的接口，那什么东西会通过```UnderAttack```进去呢？不是很理解qwq

3. 实在看的有点晕，我自己先从创建game开始理一下吧qwq

   1. 首先创建```game```的时候会有一个```characterController```传入，应该是确定可控魔偶，对所有的控制系统进行初始化

   2. ~~然后通过```ConfigFactory```这个工厂创建了一个```config```，不过唔进去看了一下，应该是选择简单困难的东西，注释掉了，没有什么用~~

   3. ```background.add``了两次？想尝试注释掉，却发现报错了

      1. 不是很理解为什么是加的```null```，进去看了一下，```AbstractBackground```这个类大概是对
      2. 我感觉可能是空占？占一下位置？不是很确定

   4. 然后创建了一个```XActivityManager```并且在这里面用```set```来更新了原本```background```里面的```null```，所以说之前的那个```background```不能注释掉，但是为什么要这样呢？为什么不能直接创建然后给呢？我试试qwq

      1. 首先这有一个如果背景图1不空的话把图1给0,我注释掉后发现没什么用，因为就以普遍理性而论，这个地方只会在创建game的时候出现一次，而这个时候刚好定义完地图，所以说肯定是不会跑到这里的

         ```java
         if (backgrounds.get(1) != null) {
                             backgrounds.set(0, backgrounds.get(1));
                         }
         ```

      2. 但是如果我把set换成add，嗯，就是把最开始的两个```backgrounds.add(null)```删掉一个后，把后面的set改为add后，背景图就不能正常显示，一个是选关界面有问题，另一个是最后的战斗界面也有问题。我怀疑可能```backgrounds```是第零张是当前界面的展示，第一张是下一个界面展示。但是如果是这样的话，那应该前面那个被我注释掉的应该有用，但是为什么没有用到呢？qwq

   5. 然后set了个游戏退出？实例销毁的东西进入```Xactivity```，应该是游戏退出时候用的，但是现在没有从正规渠道退出过qwq，也不知道怎么退，所以暂时没管

   6. 然后初始化了下```HomeActivity```？把```HomeActivity```丢进了```XActivityManager```里面，但是看里面的样子，似乎只是用来传一个布尔值？我感觉可能这个的意义是判断按钮点击的作用

   7. 最后给了个```Utils.getLogger().info("Game instance created!")```算是个标志？

4. 跳出了game 的创建了,game是由```createInstance```创建的 

5. 改到一半的时候出现了一堆报错emmm，然后发现是heroAircraft的东西，把他们注释掉了，还是能跑，觉得有点冗杂qwq，删了

6. 然后其中```createInstance```被调用过四次

   1. 一个是在```GameActivity```类的```onCreate```类里面，感觉上大概是先删除了已有的，然后再重新创建了一下，后面应该是设置销毁用。整体这个类我感觉是在设置是在andorid还是在pc上的一个类，应该就是那个中间层。
      ```java
      Game.clearInstance();
      game = Game.createInstance(heroControllerAndroid);
      game.setOnExit(this::finish);
      ```

   2. 然后是在```GamePanel```中```action```里面有调用，感觉只是像是一个保险，确认是否已经有了创建，真正跑的创建应该不再这里？

       ```java
       if (Game.getInstance() == null) {
           Game.createInstance(heroControllerImpl);
       }
       ```
       没毛病，最后注释掉这里后，还能跑，确实不是这里进行的创建。应该是类似于一个保险一样的，那么这样看来，这个的创建似乎是可以在很多地方啊。

   3. 还有一个在```GamePanel```里的```resetStates```感觉像是重置用吧，但是这个函数就没被调用过emm，有点尴尬，看来也不是这里，把这里先注释了qwq,注释完后发现选择性的能跑？跑了十次，有两次对键盘输入没有响应了，我怀疑是上面那个是不是不该注释qwq，不是很确定，这个创建难道是随机的嘛？

   4. 最后就是GamePanel里面的开始初始化程序里面了，这个才是最终的调用的地方，至少如果我把这个地方注释了的话，跑都跑不起来，那接下来就看看```gamepannel```干了些什么吧qwq

7. 来看看```GamePanel```的初始化干了些啥吧qwq

   1. 我还是没看懂第一句，不知道是在干啥，没理解到意思，感觉注释掉之后速度变快，了，但是最后战斗的背景页面却没有显示出来，而且所有人物也没有进行加载，（至少是没有显示，不知道是加载还是显示）

      ```java
      ResourceProvider.getInstance().setXGraphicsGetter(() -> new XGraphicsPC() {
                  @Override
                  protected Graphics2D getGraphics() {
                      return getGraphics2D();
                  }
      
                  @Override
                  protected GraphicsConfiguration getXGraphicsConfiguration() {
                      return getGraphicsConfiguration();
                  }
              });
      ```

   2.  然后就是```Game.createInstance(heroControllerImpl)```就是创建实例

   3. 后面有一个唔，setNextScene，是跳转到Main界面，但是好像就目前我还不知道怎么跳转回去，注释掉后对于战斗什么的没有影响就先注释掉了

   4. 后面也是一个finish的东西，里面看上去更像是进行排行榜记录的东西，想了想没用，注释掉了，不影响程序正常运行，注释太多了，一狠心删了qwq，反正有git，而且，大不了后面我自己写，哼~

   5. 然后是传了个paint给到```Game```里面```Game.getInstance().setOnPaint(this::repaint);```repaint是java内置的刷新重新绘图的函数？类 ，但是我不知道传过去是个什么意思

      1. 就是把```game```里面的```onPaint```赋值为```repaint```？他的类是```BasicCallback```类，看上去是个通用接口，但这个接口可以干啥，一头雾水....```Game```里面的注释是这是个重绘的钩子，所以每次都需要使用这个来进行重新绘制嘛？不是很理解。

   6. 这里应该是英雄控制，如果注释掉的话那英雄无法移动，但是奇怪的是，注释掉过后，qe两个技能却还是可以放，只是不能移动，不是很理解```Game.getInstance().setOnFrame(heroControllerImpl::onFrame);```

      1. 这个也是和前面一样，也是传到了```Game```里面的钩子里面，```onFrame```，是个每帧的钩子，但是这玩意儿为什么么是不能动了，不理解

   7. 然后是设置监听程序？但是这玩意儿是监听什么的啊，我注释了过后一样能正常操作，按钮什么的也能够点，除此以外里面还有个大小调整，但是我就算全部注释完后，整个大小也没有问题，不算是很理解qwq

      ```java
       addComponentListener(new ComponentAdapter() {
                  @Override
                  public void componentResized(ComponentEvent e) {
                      super.componentResized(e);
                      // if (RunningConfig.allowResize) {
                      UtilsPC.refreshWindowSize(getWidth(), getHeight());
                      justResized = true;
                      // }
                  }
              });
      ```

8. 那继续看看```GamePanel```的调用？



# 7.12

###### 哇呜呜呜，隔了三天，然后忘了之前改了写啥了qwq，代码太多太乱了，生活不易，猫猫叹气，呜呜┭┮﹏┭┮

####

1. TimerController备注写的是键盘控制emm

   1. 但是在add中的两个我注释掉过后，发现还是可以前后左右移动，qe能正常放，但是普攻没了，emm，所以这个应该是个自动普攻按钮吧.......这备注怎么标的啊>w<

   2. ```synchronized public boolean remove(Object from, Timer c)```

      这个remove，我注释掉之后没有人和子弹改变，虽然感觉上看上去是蝴蝶移动的，但我直接return false 也没事啊qwq，不理解，直接return true也没事啊，更不理解了qwq，不明白

   3. ```synchronized public void remove(Object from)```

      这个remove删了也没有什么用，感觉这个应该也不是用来移动的函数吧qwq，一个不成熟的小猜测，这个应该是在动画没有出来之前的直接移动？动画出来后这几个函数就废弃了

   4. ```synchronized public void update()```

      这个注释过后就出现大问题，首先是普攻没了，这个应该就是普攻的前移，然后敌人也只是更新了一个其他就没了，最后的话技能的时间居然出现了负数，不理解qwq，什么操作。既然如此的话那对这个好好研究一下吧qwq

      ```
       synchronized public void update() {
              if (TimeManager.isPaused()) return;
              frameTime = TimeManager.getTimeMills();
              frameCounter.add(frameTime);
              frameCounter.removeIf(t -> t < ((frameTime >= 1000) ? (frameTime - 1000) : 0));
              timers.values().forEach(timersList -> timersList.forEach(timer -> timer.update(frameTime)));
          }
      ```
      
      1. 那首先是一个return？额，回去看```TimeManager```里面是这样的，那么这个应该是暂停功能了，能理解，就是说如果有暂停的话，那么就直接返回，不用管这里面的东西了
       ```java
         public static void timePause() {
             if (paused) return;
             paused = true;
             timePauseStart = System.currentTimeMillis();
         }
       ```
      
      2. 然后就是```frameTime = TimeManager.getTimeMills();```,我感觉这个东西应该是一个用来记录时间的，其余作用并不是很明显qwq，看上去应该就是返回了一个时间
         ```java
          public static double getTimeMills() {
                if (timeStartGlobal == 0) {
                    timeStartGlobal = System.currentTimeMillis();
                }
                if (paused) {
                    return timeLastValue;
                }
                timeLastValue = (double) (System.currentTimeMillis() - timeStartGlobal - timePaused);
                return timeLastValue;
            }
         ```
      
      3. 感觉像是添加时间？```frameCounter.add(frameTime);```就是上一个是用来得到时间，这个就是把时间加进去，虽然，我不知道这个加时间进去有什么用qwq
      
      4. ```removeIf```就有点迷了，没有看太懂qwq
      
      5. 不过又再次实验了一下，后面呢都要用到frameTimer，但是把那个add给注释掉却没事qwq
      
      6. 最后一个value我怀疑是对所有的进行一个改变，但是注释掉后，整个也能够跑，所以也不知道这个的功能到低是干什么的，那除此之外的话，看起来最核心的代码一个是取得frameTime，另一个是removeIf
      
      5. 还有就是```update```的调用问题了，在game的```getMainTask```里面被调用，感觉这玩意儿其实就是真正战斗时所循环运行的东西了，虽然没有找到循环的点在哪里，但是感觉这里是没有问题的
      	```java
         protected Runnable getMainTask() {
                 return () -> {
                     try {
                         timerController.update();
                         timerController.execute();
                         if (onFrame != null) {
                             onFrame.run();
                         }
                         getActivityManager().onFrame();
                         synchronized (allThings) {
                             allThings.forEach(objList -> objList.forEach(AbstractObject::forward));
                         }
                         getTopLayout().forEach(AbstractObject::forward);
                         crashCheckAction();
                         synchronized (allThings) {
                             allThings.forEach(objList -> objList.removeIf(obj -> !obj.isValid()));
                         }
                         synchronized (allUnderAttacks) {
                             allUnderAttacks.forEach(underAttacksList -> underAttacksList.removeIf(underAttack -> !underAttack.getRelativeCharacter().isValid()));
                         }
                         if (onPaint != null) {
                             onPaint.run();
                         }
                         timerController.done();
                         Thread.sleep(1);
                     } catch (InterruptedException e) {
                         Utils.getLogger().warn("this thread will exit: " + e);
                     }
                 };
             }
         ```
      
      6. 那理论上来讲，后面针对这个来改就可以了，这个应该就是最后的东西了qwq
      
      7. 





