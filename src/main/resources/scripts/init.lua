require "game"

print("Lua init...")
print("config: ", game.config)
print("config.getString: ", game.config.getString)
print("config.getString(M_HERO_POWER_STEPS): ", game.config.getString("M_HERO_POWER_STEPS"))
print("config.getBoolean(M_RUNNING_PRINT_FPS): ", game.config.getBoolean("M_RUNNING_PRINT_FPS"))
initDone = true