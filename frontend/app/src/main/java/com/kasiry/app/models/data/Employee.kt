package com.kasiry.app.models.data

data class Employee (
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val abilities: Ability,
    val companyId: String,
    val company: Company?,
)