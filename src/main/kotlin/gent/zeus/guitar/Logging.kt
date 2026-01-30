package gent.zeus.guitar

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator

val logger: Logger = Configurator.setLevel(LogManager.getLogger("GuitarLog"), Level.DEBUG)
