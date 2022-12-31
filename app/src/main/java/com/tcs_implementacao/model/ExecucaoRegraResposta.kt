package com.tcs_implementacao.model

data class ExecucaoRegraResposta(
    val idExecucaoRegraResposta: Int?,
    val regraItem: RegraItem?,
    val execucaoRegra: ExecucaoRegra?,
    var resposta: VariavelValor?,
    val acertou: Boolean?
) {

    constructor(): this(null,
        null, null, null, null)
}