package work.chiro.game.libraries

import org.luaj.vm2.LuaValue
import org.luaj.vm2.LuaValue.{NIL, tableOf}
import org.luaj.vm2.lib._
import work.chiro.game.logger

/**
 * 以 Lua Library 的形式完成 Lua 和 Java 之间数据的连接
 */
class LuaLogging extends TwoArgFunction {
  override def call(moduleName: LuaValue, env: LuaValue) = {
    val library: LuaValue = tableOf()
    library.set("setLevel", new SetLevel)
    library.set("log", new Log)
    library.set("debug", new Debug)
    library.set("info", new Info)
    library.set("warn", new Warn)
    library.set("error", new Error)
    library.set("fatal", new Fatal)
    env.set("logging", library)
    env.get("package").get("loaded").set("logging", library)
    library
  }

  class SetLevel extends OneArgFunction {
    override def call(level: LuaValue) = {
      logger.setLevel(level.checkint())
      NIL
    }
  }

  trait LogText extends TwoArgFunction {
    def luaLog(text: String, line: Int, source: String, name: String): Unit

    override def call(text: LuaValue, debugInfo: LuaValue) = {
      val debugTable = debugInfo.checktable()
      val line = debugTable.get("currentline").checkint()
      val source = debugTable.get("source").checkjstring()
      val name = debugTable.get("name").checkjstring()
      luaLog(text.checkjstring(), line, source, name)
      NIL
    }
  }

  class Log extends LogText {
    override def luaLog(text: String, line: Int, source: String, name: String) = logger.logItInfo(text, logger.LOG_LEVER_VERBOSE, line, source, name)
  }

  class Debug extends LogText {
    override def luaLog(text: String, line: Int, source: String, name: String) = logger.logItInfo(text, logger.LOG_LEVER_DEBUG, line, source, name)
  }

  class Info extends LogText {
    override def luaLog(text: String, line: Int, source: String, name: String) = logger.logItInfo(text, logger.LOG_LEVER_INFO, line, source, name)
  }

  class Warn extends LogText {
    override def luaLog(text: String, line: Int, source: String, name: String) = logger.logItInfo(text, logger.LOG_LEVER_WARN, line, source, name)
  }

  class Error extends LogText {
    override def luaLog(text: String, line: Int, source: String, name: String) = logger.logItInfo(text, logger.LOG_LEVER_ERROR, line, source, name)
  }

  class Fatal extends LogText {
    override def luaLog(text: String, line: Int, source: String, name: String) = logger.logItInfo(text, logger.LOG_LEVER_FATAL, line, source, name)
  }
}
