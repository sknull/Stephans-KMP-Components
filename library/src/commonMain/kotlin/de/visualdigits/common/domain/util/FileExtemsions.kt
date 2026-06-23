package de.visualdigits.common.domain.util

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.logMessage
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.io.IOException

fun Path.copyToIfNotExists(
    targetFile: Path,
    logger: (LogMessage) -> Unit,
) {
    if (!SystemFileSystem.exists(targetFile)) {
        logger(logMessage(Severity.Info, "Copy file '${SystemFileSystem.resolve(this)}' to '$targetFile'..."))
        this.copyTo(targetFile)
    }
}

fun Path.createDirectoryIfNotExists(
    logger: (LogMessage) -> Unit,
): Path {
    if (!SystemFileSystem.exists(this)) {
        logger(logMessage(Severity.Info, "Creating target directory '${SystemFileSystem.resolve(this)}'"))
        if (!this.mkdirs()) {
            logger(logMessage(Severity.Error, "Could not create target directory"))
        }
    }

    return this
}

fun Path.mkdirs(): Boolean = try {
    SystemFileSystem.createDirectories(this)
    true
} catch (_: IOException) {
    false
}

fun Path.copyTo(target: Path) {
    // make sure the target path exists
    target.parent?.let { parentPath ->
        if (!SystemFileSystem.exists(parentPath)) {
            SystemFileSystem.createDirectories(parentPath)
        }
    }

    // work with stream to copy block data over
    SystemFileSystem.source(this).buffered().use { source ->
        SystemFileSystem.sink(target).buffered().use { sink ->
            source.transferTo(sink)
        }
    }
}
