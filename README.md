
## Description
I developed a content distribution network (CDN) that builds and uses Minimum Spanning Trees (MST) to disseminate content to router nodes within the network. The CDN can contain at least 10 routers, and each router will be connected to N (default of 4) other routers within the CDN. Each link that connects two routers within the CDN has a weight associated with it. Links are bidirectional (i.e. if router A established a connection to router B, then the router B must use this same link to communicate with A. The objective of any router node is to route data efficiently to other router nodes. I am using an MST computed based on the weights assigned to links in the CDN. These link weights are changing at regular intervals and should result in a new MST. When a router feeds data into the CDN, it should ensure that the packet routes are based on the current MST. All communications in the CDN are based on TCP.


## Compilation and execution
- To compile the source files, you have to execute the command "make all" inside the cdn folder.
- To delete class files, from the bin folder you have to execute the command "make clean" inside the cdn folder.
- To execute the programs, you need to enter the bin folder and then execute the programs as described below:
  - To start the Discover node,
    - java node.Discovery port_number [referesh_interval].
  - To start a Router node,
    - java node.Router port_number router_ID Discovery_host Discovery_port.
