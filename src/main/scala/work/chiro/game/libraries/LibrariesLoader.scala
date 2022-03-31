package work.chiro.game.libraries

import org.luaj.vm2.Globals
import work.chiro.game.application.Main

/**
 * 加载 Lua 中用到的所有的库
 */
object LibrariesLoader {
  def get = Array(
    () => new LuaGame,
    () => new HyperbolicTest,
    () => new LuaLogger
  )

  def loadAllLibraries(globals: Globals = Main.getLuaGlobals): Unit = get.foreach(getLib => globals.load(getLib()))
}
