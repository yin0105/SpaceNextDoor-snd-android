package com.spacenextdoor.model

import com.apollographql.apollo.api.EnumValue

enum class Bookingstatuss (
    override val rawValue: String
) : EnumValue {
    RESERVED("RESERVED"),

    CONFIRMED("CONFIRMED"),

    ACTIVE("ACTIVE"),

    TERMINATED("TERMINATED"),

    COMPLETED("COMPLETED"),

    CANCELLED("CANCELLED"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN__("UNKNOWN__");
}