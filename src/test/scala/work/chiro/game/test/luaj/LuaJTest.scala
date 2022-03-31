package work.chiro.game.test.luaj

import org.luaj.vm2.lib.jse.JsePlatform
import org.scalatest.flatspec.AnyFlatSpec
import work.chiro.game.libraries.LibrariesLoader
import work.chiro.game.{GlobalConfigLoader, logger}
import work.chiro.game.utils.tryGetFile

import java.awt.Robot

class LuaJTest extends AnyFlatSpec {
  "Lua test" should "pass the test" in {
    val script = "luaj-test.lua"
    val fileLines = tryGetFile(script).getLines()
    val fileContent = fileLines.mkString("\n")
    println("file content:")
    println(fileContent)
    println("execute:")
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.load(fileContent)
    chunk.call
    logger.info("test done.")
  }

  "Lua swing test" should "pass the test" in {
    val script = "luaj-swing-test.lua"
    println(s"execute file $script:")
    val globals = JsePlatform.standardGlobals()
    val chunk = globals.loadfile(script)
    chunk.call()
    val r = new Robot
    r.delay(5000)
    logger.info("test done.")
  }

  "Lua libraries test" should "pass the test" in {
    val script = "luaj-libs-test.lua"
    println(s"execute file $script:")
    val globals = JsePlatform.standardGlobals()
    LibrariesLoader.loadAllLibraries(globals = globals)
    val chunk = globals.loadfile(script)
    chunk.call()
    logger.info("test done.")
  }

  "Game config test" should "pass the test" in {
    GlobalConfigLoader.init
    val script = "game-config-test.lua"
    println(s"execute file $script:")
    val globals = JsePlatform.standardGlobals()
    LibrariesLoader.loadAllLibraries(globals = globals)
    val chunk = globals.loadfile(script)
    chunk.call()
    logger.info("test done.")
  }
}

