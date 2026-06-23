package de.visualdigits.common.demo.form

import androidx.compose.runtime.Immutable
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.ColorPickerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.PasswordFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import de.visualdigits.common.domain.model.configuration.keyfactory.StringListKeyFactory
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.domain.model.ui.UiText

@Immutable
class DemoConfiguration(
    values: Map<DC, Any?> = mapOf(),
): AbstractConfiguration<DemoConfiguration, DC>(values, DESCRIPTORS) {

    companion object {
        val DESCRIPTORS = listOf(
            StringFieldDescriptor(
                group = UiText.DynamicString("credentials"),
                key = DC.username,
                label = UiText.DynamicString("Username"),
                toolTip = UiText.DynamicString("Username"),
                valid = { _, value -> if (value != null) { Severity.Info } else { Severity.Error } },
            ),

            PasswordFieldDescriptor(
                group = UiText.DynamicString("credentials"),
                key = DC.password,
                label = UiText.DynamicString("Password"),
                toolTip = UiText.DynamicString("Password"),
                valid = { _, value -> if (value != null) { Severity.Info } else { Severity.Error } },
            ),

            StringFieldDescriptor(
                key = DC.text,
                label = UiText.DynamicString("Text"),
                toolTip = UiText.DynamicString("Some text"),
                valid = { _, value -> if (value != null) { Severity.Info } else { Severity.Error } },
            ),

            EnumFieldDescriptor(
                fieldClass = Language::class,
                key = DC.language,
                label = UiText.DynamicString("Language"),
                toolTip =  UiText.DynamicString("Language"),
                options = { _, _ -> Language.options },
                keyFactory = Language
            ),

            EnumFieldDescriptor(
                fieldClass = BooleanEnum::class,
                key = DC.switch,
                label =  UiText.DynamicString("Switch"),
                toolTip =  UiText.DynamicString("Switch"),
                options = { _, _ -> BooleanEnum.options },
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
                options = { _, _ -> listOf(Triple("txt", null, null)) }
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
