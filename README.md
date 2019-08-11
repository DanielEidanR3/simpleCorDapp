<p align="center">
  <img src="https://avatars1.githubusercontent.com/u/22600631?s=200&v=4" alt="Corda" width="100">
<img width="300" height="100" src="https://www.securitymattersltd.com/wp-content/uploads/2019/02/img2-1024x341.png" class="attachment-large size-large" alt=""srcset="https://www.securitymattersltd.com/wp-content/uploads/2019/02/img2-1024x341.png 1024w, https://www.securitymattersltd.com/wp-content/uploads/2019/02/img2-300x100.png 300w, https://www.securitymattersltd.com/wp-content/uploads/2019/02/img2-768x256.png 768w" sizes="(max-width: 1024px) 100vw, 1024px">
</p>

# SMX CorDapp - Demo

# Pre-Requisites

See https://docs.corda.net/getting-set-up.html.

# Usage

## Running the nodes

Follow thw instructions here: https://docs.corda.net/tutorial-cordapp.html#running-the-example-cordapp to do the following two steps:

1. Build the CorDapp
2. Run the CorDappp

Then start the SpringBoot server as outlined below.

### Webserver

`clients/src/main/kotlin/com/template/webserver/` defines a simple Spring webserver that connects to a node via RPC and
allows you to interact with the node over HTTP.

We defined two endpoints:

	GET /ledgerupdates -> returns a JSON with all the states stored on ledger
	POST /ledgerupdates -> Takes a JSON payload as a parameter and returns the JSON with state stored on ledger

The API endpoints are defined here:

     clients/src/main/kotlin/com/template/webserver/Controller.kt


And a static webpage is defined here:

     clients/src/main/resources/static/



#### Running the webserver

##### Via the command line

Run the `runTemplateServer` Gradle task. By default, it connects to the node with RPC address `localhost:10006` with
the username `user1` and the password `test`, and serves the webserver on port `localhost:10050`.

##### Via IntelliJ

Run the `Run Template Server` run configuration. By default, it connects to the node with RPC address `localhost:10006`
with the username `user1` and the password `test`, and serves the webserver on port `localhost:10050`.

#### Interacting with the webserver

The static webpage is served on:

    http://localhost:10050




### Shell



When started via the command line, each node will display an interactive shell:

    Welcome to the Corda interactive shell.
    Useful commands include 'help' to see what is available, and 'bye' to shut down the node.

    Tue Nov 06 11:58:13 GMT 2018>>>


You can quiry the sates on the node through the shell with the command:

    run vaultQuery contractStateType: com.template.states.SMXState

You can add a state with this command:

	flow start Initiator jsonData: world

You can use this shell to interact with your node. For example, enter `run networkMapSnapshot` to see a list of
the other nodes on the network:

    Tue Nov 06 11:58:13 GMT 2018>>> run networkMapSnapshot
    [
      {
      "addresses" : [ "localhost:10003" ],
      "legalIdentitiesAndCerts" : [ "O=Notary, L=London, C=GB" ],
      "platformVersion" : 4,
      "serial" : 1541505484825
    },
      {
      "addresses" : [ "localhost:10006" ],
      "legalIdentitiesAndCerts" : [ "O=SMX,L=TelAviv,C=IL" ],
      "platformVersion" : 4,
      "serial" : 1541505382560
    }
    ]

    Tue Nov 06 12:30:11 GMT 2018>>>

You can find out more about the node shell [here](https://docs.corda.net/shell.html).
