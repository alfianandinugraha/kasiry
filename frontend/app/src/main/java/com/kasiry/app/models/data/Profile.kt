package com.kasiry.app.models.data

data class Profile(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val abilities: Ability,
    val companyId: String,
    val company: Company?,
)
