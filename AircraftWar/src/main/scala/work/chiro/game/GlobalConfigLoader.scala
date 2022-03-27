package work.chiro.game

import java.io.FileNotFoundException
import scala.collection.mutable
import scala.io.{BufferedSource, Source}
import scala.language.dynamics


/**
 * 全局项目配置。每次编译的时候从磁盘读取。<br/>
 * 读取位置顺序：<br/>
 * 1. /build/.config: 当在 CI 模式下运行，将会把简化的配置文件 (.console.config) 复制到 /build/。<br/>
 * 2. /../.config:    本地调试的时候使用的文件，sbt。保证是最新配置。<br/>
 * 3. /.config:       本地调试的时候使用的文件，保证是最新配置。<br/>
 */
object GlobalConfigLoader {
  val debugConfig = false

  // val debug = true
  // Scala 的动态类型，可以动态设置成员内容
  class GlobalConfig extends Dynamic {
    // private data: store Any type.
    private val data = mutable.HashMap.empty[String, Any].withDefault({ key => throw new NoSuchFieldError(key) })

    // get content
    def selectDynamic(key: String): Any = data(key)

    // set content
    def updateDynamic(key: String)(args: Any): Unit = {
      // Remove CONFIG_ header
      val keyUse: String = if (key.startsWith("CONFIG_")) key.substring("CONFIG_".length) else key
      if (debugConfig) println(s"Setting $keyUse = $args")
      data(keyUse) = args
    }

    // 用这样的方式把储存的 `Any` 类型转换为指定类型。
    class ModuleOption(moduleConfigName: String) {
      def enabled: Boolean = data.contains(moduleConfigName)

      def getValue[T](use: () => T, default: T): T = {
        if (enabled) {
          try {
            use()
          } catch {
            case _: Throwable =>
              println("Warning: get value failed...")
              default
          }
        } else
          default
      }

      def getFullOptionName(optionName: String) = moduleConfigName + "_" + optionName

      def d[T](optionName: String): () => T = () => data(getFullOptionName(optionName)).asInstanceOf[T]

      def contains(optionName: String): Boolean = if (enabled) data.contains(getFullOptionName(optionName)) else false
    }

    object window extends ModuleOption("M_WINDOW") {
      def width: Int = getValue(d("WIDTH"), 512)

      def height: Int = getValue(d("HEIGHT"), 768)
    }

    object running extends ModuleOption("M_RUNNING") {
      def showFps: Boolean = getValue(d("SHOW_FPS"), false)
    }

    object control extends ModuleOption("M_CONTROL") {
      def moveSpeed: Double = getValue(d("MOVE_SPEED"), 1000).toDouble / 1000

      // WARNING: Keycode will not loaded from .config
      val keyUp: Int = 38
      val keyDown: Int = 40
      val keyLeft: Int = 37
      val keyRight: Int = 39
      val keySlow: Int = 16
      val keyBuff: Int = 90
      val keyQuit: Int = 81
      def updateFromData() = {
        // keyUp = getValue(d("KEYCODE_UP"), 38)
        // keyDown = getValue(d("KEYCODE_DOWN"), 40)
        // keyLeft = getValue(d("KEYCODE_LEFT"), 37)
        // keySlow = getValue(d("KEYCODE_RIGHT"), 39)
        // keySlow = getValue(d("KEYCODE_SLOW"), 16)
        // keyBuff = getValue(d("KEYCODE_BUFF"), 90)
        // keyQuit = getValue(d("KEYCODE_QUIT"), 81)
      }
    }

    def isDebug: Boolean = if (data.contains("DEBUG")) data("DEBUG").asInstanceOf[Boolean] else true
    def updateFromData() = control.updateFromData()
  }

  def generate: GlobalConfig = {
    // val resourceURI = ClassLoader.getSystemResource(".")
    val resourceURI = this.getClass.getClassLoader.getResource("/")
    // val resourceURI = new URL("file://./AircraftWar/target/.")
    // println(s"# resourceURI.toString = ${resourceURI}")
    val root = try {
      val resourcePath = resourceURI.getFile
      // /out/... 是 Mill 指定的编译目录，读取到的 resourcePath 在这下面；
      // /AircraftWar/target/... 是 SBT 的编译目录，两者需要区别对待。
      val rootSplit = resourcePath.split(if (resourcePath.contains("/out/")) "/out/" else "/AircraftWar/")
      assert(rootSplit.length == 2, s"Cannot locate root dir! resourcePath = $resourcePath")
      rootSplit(0)
    } catch {
      case _: NullPointerException =>
        // println(s"resourceURI = $resourceURI")
        "."
    }
    println(s"config root dir: $root")

    def getConfigFile: Option[BufferedSource] = {
      try {
        Some(Source.fromFile(f"$root/build/.config"))
      } catch {
        case _: FileNotFoundException =>
          try {
            // may in .bloop
            Some(Source.fromFile(f"$root/../.config"))
          } catch {
            case _: FileNotFoundException => try
              Some(Source.fromFile(f"$root/.config"))
            catch {
              case _: FileNotFoundException => try {
                Some(Source.fromFile(f"$root/.console.config"))
              } catch {
                case _: FileNotFoundException =>
                  println("Warning: No config file found! The default configuration value will be used!")
                  None
              }
            }
          }
      }
    }

    // 非空、非注释行
    val configFileOption = getConfigFile
    val c = new GlobalConfig
    if (configFileOption.nonEmpty) {
      val configFile = configFileOption.get
      val configLines = configFile.getLines().filter(_.nonEmpty).filter(!_.startsWith("#"))
      assert(configLines.nonEmpty, "No config data!")
      configLines.foreach(line => {
        val parsed = line.split("=")
        assert(parsed.length == 2, s"Config Error format: $line")
        val key = parsed(0)
        val valueString = parsed(1)
        if (valueString.startsWith("\"") && valueString.endsWith("\""))
          c.updateDynamic(key)(valueString.substring(1, valueString.length - 1))
        //  y 表示 true，false 则未定义。
        else if (valueString == "y") c.updateDynamic(key)(true)
        else try {
          // 优先转换 HEX
          if (valueString.startsWith("0x")) {
            c.updateDynamic(key)(Integer.parseInt(valueString.replace("0x", ""), 16))
          } else {
            val valueInt = valueString.toInt
            c.updateDynamic(key)(valueInt)
          }
        } catch {
          case _: NumberFormatException => c.updateDynamic(key)(valueString)
        }
      })
      c.updateFromData()
    }
    c
  }

  implicit val config: GlobalConfig = generate
}
