package de.visualdigits.common.demo.form

import de.visualdigits.common.domain.model.configuration.FieldKey

enum class DC : FieldKey<DC> {

    username,
    password,
    text,
    language,
    switch,
    list,
    file,
    dateTime,
    color
}
