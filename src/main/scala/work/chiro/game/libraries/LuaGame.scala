package work.chiro.game.libraries

import org.luaj.vm2.LuaValue
import org.luaj.vm2.LuaValue.{NONE, tableOf}
import org.luaj.vm2.lib._
import work.chiro.game.GlobalConfigLoader.config
import work.chiro.game.logger

/**
 * 以 Lua Library 的形式完成 Lua 和 Java 之间数据的连接
 */
class LuaGame extends TwoArgFunction {
  override def call(moduleName: LuaValue, env: LuaValue) = {
    val library: LuaValue = tableOf()
    library.set("config", Config.getTable)
    env.set("game", library)
    env.get("package").get("loaded").set("game", library)
    library
  }

  object Config {
    trait Get extends OneArgFunction {
      def convert(key: String): LuaValue

      override def call(name: LuaValue) = {
        val key = name.checkjstring()
        if (config.contains(key)) convert(key)
        else NONE
      }
    }

    class GetString extends Get {
      override def convert(key: String) = LuaValue.valueOf(config.selectDynamic(key).asInstanceOf[String])
    }

    class GetInt extends Get {
      override def convert(key: String) = LuaValue.valueOf(config.selectDynamic(key).asInstanceOf[Int])
    }

    class GetDouble extends Get {
      override def convert(key: String) = LuaValue.valueOf(config.selectDynamic(key).asInstanceOf[Double])
    }

    class GetBoolean extends Get {
      override def convert(key: String) = LuaValue.valueOf(config.selectDynamic(key).asInstanceOf[Boolean])
    }

    def getTable = {
      val table = tableOf()
      table.set("getString", new GetString)
      table.set("getInt", new GetInt)
      table.set("getDouble", new GetDouble)
      table.set("getBoolean", new GetBoolean)
      table
    }
  }
}
