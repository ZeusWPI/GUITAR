package gent.zeus.guitar

import org.slf4j.Logger
import org.slf4j.LoggerFactory


val logger: Logger = LoggerFactory.getLogger("guitar log")

fun logModuleEnabledStatus(
    moduleName: String,
    envVar: EnvVarProperty,
    enabled: Boolean,
    warnDisabled: Boolean = false,
    warnEnabled: Boolean = false
) {
    val warn = if (enabled) warnEnabled else warnDisabled

    val message = if (enabled)
        "$moduleName is ENABLED (${envVar.name} is set)"
    else
        "$moduleName is DISABLED (${envVar.name} is not set)"

    if (warn)
        logger.warn(message)
    else
        logger.info(message)
}
