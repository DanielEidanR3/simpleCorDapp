package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.contracts.SMXContract
import com.template.states.SMXState
import net.corda.core.contracts.Command
import net.corda.core.contracts.Requirements.using
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import javax.annotation.Signed

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class Initiator(val jsonData: String) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): SignedTransaction {
        // Step 1. Get a reference to the notary service on our network and our key pair.
        val notary = serviceHub.networkMapCache.notaryIdentities.first()

        // Step 2. Create a state from the data that is passed in via the post request.
        val state = SMXState(jsonData, listOf(ourIdentity))

        // Step 3. Create a new SMX update ledger command.
        val updateLedgerCommand = Command(SMXContract.Commands.UpdateLedger(), state.participants.map { it.owningKey })

        // Step 4. Create a new TransactionBuilder object.
        val builder = TransactionBuilder(notary = notary)

        // Step 5. Add the SMX state as an output state, as well as a command to the transaction builder.
        builder.addOutputState(state, SMXContract.ID)
        builder.addCommand(updateLedgerCommand)

        // Step 5. Verify and sign it with our KeyPair.
        builder.verify(serviceHub)
        val ptx = serviceHub.signInitialTransaction(builder)

        // Collect signatures from noone (we would collect signatures from our counterparties but there aren't any)
        val sessions = (state.participants - ourIdentity).map { initiateFlow(it) }.toSet()
        // Step 6. Collect the other party's signature using the SignTransactionFlow.
        val stx = subFlow(CollectSignaturesFlow(ptx, sessions))

        // Step 7. Assuming no exceptions, we can now finalise the transaction.
        return subFlow(FinalityFlow(stx, sessions))

    }
}

@InitiatedBy(Initiator::class)
class Responder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransactionFlow = object : SignTransactionFlow(flowSession) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat {
                val output = stx.tx.outputs.single().data
                "This must be an SMX transaction" using (output is SMXState)
            }
        }

        val txWeJustSignedId = subFlow(signedTransactionFlow)

        return subFlow(ReceiveFinalityFlow(otherSideSession = flowSession, expectedTxId = txWeJustSignedId.id))
    }
}
