package work.chiro.game.libraries

import org.luaj.vm2.LuaValue
import org.luaj.vm2.LuaValue.tableOf
import org.luaj.vm2.lib._

class LuaGame extends TwoArgFunction {
  override def call(moduleName: LuaValue, env: LuaValue) = {
    val library: LuaValue = tableOf()
    library.set("config", new Config)
    env.set("LuaGame", library)
    env.get("package").get("loaded").set("LuaGame", library)
    library
  }

  class Config extends OneArgFunction {
    override def call(name: LuaValue) = {
      println(name.checkstring())
      LuaValue.valueOf("OK")
    }
  }
}
