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
        logger.info(s"call: $name")
        if (config.contains(key)) convert(key)
        NONE
      }
    }

    class GetString extends OneArgFunction {
      override def call(name: LuaValue) = {
        val key = name.checkjstring()
        logger.info(s"call: $name")
        val value = config.selectDynamic(key).asInstanceOf[String]
        logger.info(s"\tconfig.$name = $value")
        if (config.contains(key)) LuaValue.valueOf(value)
        NONE
      }
    }

    // class GetString extends Get {
    //   override def convert(key: String) = {
    //     val value = config.selectDynamic(key).asInstanceOf[String]
    //     logger.info(s"value = $value")
    //     LuaValue.valueOf(value)
    //   }
    //
    //   // override def call(name: LuaValue) = name
    // }

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
