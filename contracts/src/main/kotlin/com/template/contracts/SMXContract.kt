package com.template.contracts

import com.template.states.SMXState
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.Requirements.using
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction

// ************
// * Contract *
// ************
class SMXContract : Contract {
    companion object {
        // Used to identify our contract when building a transaction.
        const val ID = "com.template.contracts.SMXContract"
    }

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        when (command.value) {
            is Commands.UpdateLedger -> requireThat {
                "The inputted JSON cannot be an empty string" using (tx.outputsOfType<SMXState>().first().data.isNotEmpty())
            }
        }
    }

    // Used to indicate the transaction's intent.
    interface Commands : CommandData {
        class UpdateLedger: Commands
    }
}