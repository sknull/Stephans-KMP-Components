package de.visualdigits.common.domain.service

import co.touchlab.kermit.LogWriter

expect fun getPlatformLogWriters(homeDirectoryPath: String, logFileName: String): List<LogWriter>
