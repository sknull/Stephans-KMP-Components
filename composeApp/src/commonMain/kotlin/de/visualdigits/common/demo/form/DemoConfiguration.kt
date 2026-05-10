package de.visualdigits.common.demo.form

import androidx.compose.runtime.Immutable
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.ColorPickerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.domain.model.configuration.FieldsInitializer
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.StringFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import de.visualdigits.common.domain.model.configuration.keyfactory.StringListKeyFactory

@Immutable
class DemoConfiguration(
    newFields: List<Field<*,*,DC>>? = null
): AbstractConfiguration<DemoConfiguration, DC>(newFields?:setupFields()) {

    companion object : FieldsInitializer<DC> {
        override fun setupFields(): List<Field<*, *, DC>> {
            return listOf(
                Field(
                    descriptor = StringFieldDescriptor(
                        key = DC.text,
                        label = UiText.DynamicString("Text"),
                        toolTip = UiText.DynamicString("Some text"),
                    ),
                    valid = { true }
                ),

                Field(
                    descriptor = EnumFieldDescriptor(
                        fieldClass = Language::class,
                        key = DC.language,
                        label = UiText.DynamicString("Language"),
                        toolTip =  UiText.DynamicString("Language"),
                        options = { Language.entries.map { e -> Triple(e.name, e.uiText, e.drawableResourceId) } },
                        keyFactory = Language
                    ),
                    valid = { value -> value != null }
                ),

                Field(
                    descriptor = EnumFieldDescriptor(
                        fieldClass = BooleanEnum::class,
                        key = DC.switch,
                        label =  UiText.DynamicString("Switch"),
                        toolTip =  UiText.DynamicString("Switch"),
                        options = { BooleanEnum.entries.map { e -> Triple(e.name, e.uiText, e.drawableResourceId) } },
                        keyFactory = BooleanEnum
                    ),
                    valid = { value -> value != null }
                ),

                Field(
                    descriptor = ListFieldDescriptor(
                        fieldClass = String::class,
                        key = DC.list,
                        label =  UiText.DynamicString("List"),
                        toolTip =  UiText.DynamicString("List"),
                        keyFactory = StringListKeyFactory,
                    ),
                    valid = { _ -> true }
                ),

                Field(
                    descriptor = FileFieldDescriptor(
                        key = DC.file,
                        label = UiText.DynamicString("File"),
                        fileMode = FileMode.FILES_ONLY,
                        options = { configuration -> listOf(Triple("txt", null, null)) }
                    ),
                    valid = { _ -> true }
                ),

                Field(
                    descriptor = ColorPickerFieldDescriptor(
                        key = DC.color,
                        label = UiText.DynamicString("Color"),
                    ),
                    valid = { _ -> true }
                ),

                )
        }
    }

    override fun createInstance(newFields: List<Field<*,*,DC>>): DemoConfiguration {
        return DemoConfiguration(newFields)
    }
}
