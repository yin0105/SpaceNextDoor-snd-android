package com.spacenextdoor.model

import java.util.*

class SNDProviderModel {
    var providerId: Int? = null
    var taxId: String? = null
    var bankAccountNumber: String? = null
    var bankAccountHolderName: String ? = null
    var updatedBy: Int? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null

    var platformBank: BankPlatformModel? = null
}