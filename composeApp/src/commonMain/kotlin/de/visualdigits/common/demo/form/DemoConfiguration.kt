package de.visualdigits.common.demo.form

import androidx.compose.runtime.Immutable
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.ColorPickerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import de.visualdigits.common.domain.model.configuration.keyfactory.StringListKeyFactory

@Immutable
class DemoConfiguration(
    values: Map<DC, Any?> = mapOf(),
): AbstractConfiguration<DemoConfiguration, DC>(values, DESCRIPTORS) {

    companion object {
        val DESCRIPTORS = listOf(
            StringFieldDescriptor(
                key = DC.text,
                label = UiText.DynamicString("Text"),
                toolTip = UiText.DynamicString("Some text"),
                valid = { _, value -> value != null },
            ),

            EnumFieldDescriptor(
                fieldClass = Language::class,
                key = DC.language,
                label = UiText.DynamicString("Language"),
                toolTip =  UiText.DynamicString("Language"),
                options = { _ -> Language.options },
                keyFactory = Language
            ),

            EnumFieldDescriptor(
                fieldClass = BooleanEnum::class,
                key = DC.switch,
                label =  UiText.DynamicString("Switch"),
                toolTip =  UiText.DynamicString("Switch"),
                options = { _ -> BooleanEnum.options },
                keyFactory = BooleanEnum
            ),

            ListFieldDescriptor(
                fieldClass = String::class,
                key = DC.list,
                label =  UiText.DynamicString("List"),
                toolTip =  UiText.DynamicString("List"),
                keyFactory = StringListKeyFactory,
            ),

            FileFieldDescriptor(
                key = DC.file,
                label = UiText.DynamicString("File"),
                fileMode = FileMode.FILES_ONLY,
                options = { _ -> listOf(Triple("txt", null, null)) }
            ),

            ColorPickerFieldDescriptor(
                key = DC.color,
                label = UiText.DynamicString("Color"),
            ),
        )

        fun instance(): DemoConfiguration = DemoConfiguration(values = mapOf())
    }

    override fun createInstance(newValues: Map<DC, Any?>): DemoConfiguration {
        return DemoConfiguration(newValues)
    }
}
