gradlew :pc:jar
java -Dfile.encoding=UTF-8 -Duser.country=CN -Duser.language=zh -Duser.variant -cp core\build\libs\core.jar;pc\build\libs\pc.jar work.chiro.game.application.Main