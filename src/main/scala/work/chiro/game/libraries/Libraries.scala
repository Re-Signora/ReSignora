package work.chiro.game.libraries

import org.luaj.vm2.Globals
import work.chiro.game.application.Main

object Libraries {
  def get = Array(
    () => new LuaGame,
    () => new HyperbolicTest
  )

  def loadAllLibraries(globals: Globals = Main.getLuaGlobals): Unit = get.foreach(getLib => globals.load(getLib()))
}
