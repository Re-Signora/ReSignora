require "logging"

logger = {
    log = function(value)
        logging.log(value, debug.getinfo(2, "nSl"))
    end,
    debug = function(value)
        logging.debug(value, debug.getinfo(2, "nSl"))
    end,
    info = function(value)
        logging.info(value, debug.getinfo(2, "nSl"))
    end,
    warn = function(value)
        logging.warn(value, debug.getinfo(2, "nSl"))
    end,
    error = function(value)
        logging.error(value, debug.getinfo(2, "nSl"))
    end,
    fatal = function(value)
        logging.fatal(value, debug.getinfo(2, "nSl"))
    end,

}
