package de.visualdigits.common.domain.model.errorhandling

import androidx.compose.runtime.Immutable
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import java.time.OffsetDateTime

@Immutable
data class LogMessage(
    val tag: String,
    val timestamp: OffsetDateTime,
    val severity: Severity,
    val message: String,
    val throwable: Throwable?
) {

    override fun toString(): String {
        val stackTrace = throwable?.let { t ->
            "\n${
                t.stackTraceToString().split("\n").joinToString("\n") { l -> "${severity}: $l" }
            }"
        }?:""

        return "${severity}: ${message}$stackTrace"
    }

    inline fun log(withTag: String = "") {
        val logger = Logger.withTag(withTag)
        when (severity) {
            Severity.Info -> logger.i(message, throwable)
            Severity.Warn -> logger.w(message, throwable)
            Severity.Error -> logger.e(message, throwable)
            Severity.Verbose -> logger.v(message, throwable)
            Severity.Debug -> logger.d(message, throwable)
            Severity.Assert -> logger.a(message, throwable)
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    companion object {

        val logs: MutableList<LogMessage> = mutableListOf()

        inline fun log(
            severity: Severity,
            message: String,
            throwable: Throwable? = null,
            withTag: String = ""
        ): LogMessage {
            val logMessage = LogMessage(
                tag = withTag,
                timestamp = OffsetDateTime.now(),
                severity = severity,
                message = message,
                throwable = throwable
            )
            logMessage.log(withTag)
            logs.add(logMessage)
            return logMessage
        }
    }
}
