require "game"

print("Testing game.config...")
print("config: ", game.config)
print("config.getString: ", game.config.getString)
print("config.getString(M_HERO_POWER_STEPS): ", game.config.getString("M_HERO_POWER_STEPS"))
print("config.getBoolean(M_RUNNING_SHOW_FPS): ", game.config.getBoolean("M_RUNNING_SHOW_FPS"))
assert(game.config.getString("M_CONFIG_TEST_READ") == "OK", "Error reading config")
