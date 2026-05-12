package de.visualdigits.common.domain.model.configuration

import androidx.compose.runtime.Immutable
import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class FieldState<K : FieldKey<K>> (
    val fieldDescriptor: AbstractFieldDescriptor<*,*,K,*>,
    val options: List<Triple<*, UiText?, DrawableResource?>>,
    val currentValue: Any?,
    val currentOption: Triple<*, UiText?, DrawableResource?>?,
    val currentOptionUIText: UiText,
    val valid: Boolean
)
