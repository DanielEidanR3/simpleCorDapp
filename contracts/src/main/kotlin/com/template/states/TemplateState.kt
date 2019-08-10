package com.template.states

import com.template.contracts.SMXContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party

// *********
// * State *
// *********
@BelongsToContract(SMXContract::class)
data class SMXState(
        val data: String,
        override val participants: List<Party> = listOf()
) : ContractState
