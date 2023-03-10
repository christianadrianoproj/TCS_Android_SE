package com.tcs_implementacao.model

import java.io.Serializable

data class VariavelValor(
    var idVariavelValor: Int?,
    var valor: String,
    var variavel: Variavel?
): Serializable {

    constructor(): this(null,
        "", null)

    public override fun toString() : String {
        return this.valor
    }
}