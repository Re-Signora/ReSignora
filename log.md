

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







