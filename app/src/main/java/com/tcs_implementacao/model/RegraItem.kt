package com.tcs_implementacao.model

import java.io.Serializable

data class RegraItem(var idRegraItem: Int?,
                      var conectivo: Int,
                     var variavel: Variavel?,
                     var condicional: String,
                     var variavelValor: VariavelValor?,
                     var regra: Regra?
) : Serializable {

    constructor(): this(null, 0, null, "", null,  null)
}