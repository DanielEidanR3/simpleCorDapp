package com.template.webserver

import com.template.flows.Initiator
import com.template.states.SMXState
import net.corda.core.messaging.startFlow
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.utilities.getOrThrow
import nonapi.io.github.classgraph.json.JSONSerializer
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/api") // The paths for HTTP requests are relative to this base path.
class Controller(rpc: NodeRPCConnection) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    private val proxy = rpc.proxy

    @PostMapping(value = "/ledgerupdates", produces = arrayOf("text/plain"))
    private fun postALedgerUpdate(@RequestBody jsonData: String): String {
        val tx = proxy.startFlow(::Initiator, jsonData)
        val state = tx.returnValue.toCompletableFuture().getOrThrow()
        return JSONSerializer.serializeObject(state)
    }

    @GetMapping(value = "/ledgerupdates", produces = arrayOf("text/plain"))
    private fun getAllSMXStates(): String {
        val listOfSMXStates = proxy.vaultQuery(SMXState::class.java)
        val smxStates = listOfSMXStates.states.map { it.state.data }
        return JSONSerializer.serializeObject(smxStates)
    }
}