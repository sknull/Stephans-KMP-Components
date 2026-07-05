package de.visualdigits.common.presentation.components.container

import androidx.compose.foundation.v2.ScrollbarAdapter
import de.visualdigits.common.presentation.components.PlatformScrollbarAdapter

class DesktopScrollbarAdapter(val nativeAdapter: ScrollbarAdapter): PlatformScrollbarAdapter
