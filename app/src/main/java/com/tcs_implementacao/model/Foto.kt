package com.tcs_implementacao.model

data class Foto(
    val idFoto: Int?,
    val nome: String?,
    val extensao: String?,
    val contornos: Int?,
    val manchas: Int?,
    val image: String?,
    val imageProc: String?
) {

    constructor():this( null, null, null, null, null, null, null)


}