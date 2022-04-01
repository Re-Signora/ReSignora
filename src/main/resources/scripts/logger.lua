require "logging"

logger = {
    log = function(value)
        local debugInfo = debug.getinfo(2)
        logging.log(value, debugInfo)
    end,
    debug = function(value)
        local debugInfo = debug.getinfo(2)
        logging.debug(value, debugInfo)
    end,
    info = function(value)
        local debugInfo = debug.getinfo(2)
        logging.info(value, debugInfo)
    end,
    warn = function(value)
        local debugInfo = debug.getinfo(2)
        logging.warn(value, debugInfo)
    end,
    error = function(value)
        local debugInfo = debug.getinfo(2)
        logging.error(value, debugInfo)
    end,
    fatal = function(value)
        local debugInfo = debug.getinfo(2)
        logging.fatal(value, debugInfo)
    end,

}
