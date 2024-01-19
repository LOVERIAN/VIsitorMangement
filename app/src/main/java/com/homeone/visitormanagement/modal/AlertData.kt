package com.homeone.visitormanagement.modal

data class AlertData(val name: String, val fire: Boolean, val other: Boolean) {
    constructor():this("", false, false)
}
