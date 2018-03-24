package com.dmytrod.githubsearch.model

/**
 * Created by Dmytro Denysenko on 12.02.18.
 */
data class Repository (val id: Long, val name: String, val starts: Long, var seen: Boolean)