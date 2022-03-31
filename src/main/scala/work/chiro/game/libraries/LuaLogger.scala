package work.chiro.game.libraries

import org.luaj.vm2.LuaValue
import org.luaj.vm2.LuaValue.{NIL, tableOf}
import org.luaj.vm2.lib._
import work.chiro.game.logger

/**
 * 以 Lua Library 的形式完成 Lua 和 Java 之间数据的连接
 */
class LuaLogger extends TwoArgFunction {
  override def call(moduleName: LuaValue, env: LuaValue) = {
    val library: LuaValue = tableOf()
    library.set("setLevel", new SetLevel)
    library.set("log", new Log)
    library.set("debug", new Debug)
    library.set("info", new Info)
    library.set("warn", new Warn)
    library.set("error", new Error)
    library.set("fatal", new Fatal)
    env.set("logger", library)
    env.get("package").get("loaded").set("logger", library)
    library
  }

  class SetLevel extends OneArgFunction {
    override def call(level: LuaValue) = {
      logger.setLevel(level.checkint())
      NIL
    }
  }

  trait LogText extends OneArgFunction {
    def luaLog(text: String): Unit

    override def call(text: LuaValue) = {
      luaLog(text.checkjstring())
      NIL
    }
  }

  class Log extends LogText {
    override def luaLog(text: String) = logger.log(text)
  }

  class Debug extends LogText {
    override def luaLog(text: String) = logger.debug(text)
  }

  class Info extends LogText {
    override def luaLog(text: String) = logger.info(text)
  }

  class Warn extends LogText {
    override def luaLog(text: String) = logger.warn(text)
  }

  class Error extends LogText {
    override def luaLog(text: String) = logger.error(text)
  }

  class Fatal extends LogText {
    override def luaLog(text: String) = logger.fatal(text)
  }
}
