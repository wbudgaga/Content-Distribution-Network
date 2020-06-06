
## Description
I developed a content distribution network (CDN) that builds and uses Minimum Spanning Trees (MST) to disseminate content to router nodes within the network. The CDN can contain at least 10 routers, and each router will be connected to N (default of 4) other routers within the CDN. Each link that connects two routers within the CDN has a weight associated with it. Links are bidirectional (i.e. if router A established a connection to router B, then the router B must use this same link to communicate with A. The objective of any router node is to route data efficiently to other router nodes. I am using an MST computed based on the weights assigned to links in the CDN. These link weights are changing at regular intervals and should result in a new MST. When a router feeds data into the CDN, it should ensure that the packet routes are based on the current MST. All communications in the CDN are based on TCP.

## Implementation
This application satisfies all the requirements and it is implemented in different packages with minimum coupling and high cohesion. This kind of separation with the used design patterns gives us the ability to reuse its packages and it is very easy to maintain and extend.

### The implemented packages
- **cdn**
It is the main package in the application and contains CDN interface, Node class, Discovery class, and Router class, and DiscoverTimer class.
   - **CDN interface** defines the global configurable parameters that control the behavior of the application. These parameters include default buffer size and the number of connections per Router in the CDN network.
   - **Node class** is the superclass of the Router and Discovery classes. It contains all the shared methods that are needed in the Router and Discovery classes.
   - **Discovery class** implements all operations that are needed to manage the Discovery node.
   - **Router class** implements all operations that are needed to manage the Routernode.
   - **DiscoverTimer class** is a thread class that periodically invokes a discovery's method that updates the links' weights.


- **graph**
It contains the Network and the Edge classes that are used by Router and Discovery classes to represent the CDN network.


- **mst**
It contains the MSTInterface interface, MSTFactory class, and MSTKruskal class.
   - **MSTInterface interface** declares the name of the class that implements the MST algorithms and the methods that have to be implemented by any MST algorithm class.
   - **MSTFactory class** implements a factory method that returns the desired instance of any class that implements the MST interface. 
   - **MSTKruskal class** implements the MST Kruskal algorithm.
   
   
- **communications**
It contains Link class, LinkConnectionFactory class, ConnectionsContainer class, ConnectionManager class, ListeningThread class, and ReceivingThread class.
   - **Link class** encapsulates the connection link information. Every instance of this class represents one link. It is used to to send/receive Byte stream data.
   - **LinkConnectionFactory class** is a factory class that responsible for creating instances of link, ListeningThread, and ReceivingThread for every received and created connection.
   - **ConnectionsContainer class** is a singleton class that is responsible for managing the operations that are performed on a collection of connections.
   - **ConnectionManager class** hides all the connections details and enables performing all connections operations.
   - **ListeningThread class** is a thread class that is responsible for accepting new incoming connections.
   - **ReceivingThread class** is a thread class that is responsible for reading incoming data from the connection.


- **wireformates**
It contains MessageType interface, MessageFactory class, and collection of message classes.
   - **MessageType interface** contains constant information that represents the message types and the names of their corresponding classes. For each message type, it returns the name of the corresponding class.
   - **MessageFactory class** is responsible for creating instances of message class for a given message type.
collection of message classes, every class represents one message type and can pack and unpack its data.

- **messageHandlers**
It contains MessageHandler interface:, MessageHandlerFactory class, and collection of message classes.
   - **MessageHandler interface** contains constant information that represents the names of the handler classes, each is responsible for handling a particular message type.
   - **MessageHandlerFactory class** is responsible for creating instances of message handlers classes for a given message type.
collection of handler classes, every class is responsible for creating and handling a particular message type. For each message class in wireformates, there is one corresponding handler class in this package.

- **util**
It contains two help classes, ByteStream and Graph. 
   - **ByteStream class** implements methods that are used to make conversion between object types and byte streams. This class is used to convert the received bytes from the network links into objects and convert objects into a byte stream to be sent over the network. 
   - **Graph class** implements methods that are used by Network class for some operations, such as generating a random regular graph for the network.


## Compilation and execution
- To compile the source files, you have to execute the command "make all" inside the cdn folder.
- To delete class files, from the bin folder you have to execute the command "make clean" inside the cdn folder.
- To execute the programs, you need to enter the bin folder and then execute the programs as described below:
  - To start the Discover node,
    - java node.Discovery port_number [referesh_interval].
  - To start a Router node,
    - java node.Router port_number router_ID Discovery_host Discovery_port.


## The supported commands
The Discover node and each of the router nodes can receive command from the user and execute it.
- **For the Discovery Node**
   - **list-routers :** list all the Routers that are connected to the Discovery node.
   - **list-weights :** list all available connections with their weights.
   - **update-weights :** updates the connections with random weights. It is useful in the case where the refresh_interval has the value zero (disabled).
   - **setup-cdn [number-of-connections] :** create the CDN by giving every Router a number-of-connections. If the number-of-connections is not given, the default value from CDN interface will be used.
   - **exit-cdn :** try to deregister all router first and then exit only if all routers are deregistered.
   - **quit :** stop the Discovery node without considefring the Router nodes that will detect the failed Discovery node and exit.

- **For the Router Node**
   - **list-routers :** list all the Routers that are connected to the current Router.
   - **print-MST :** print the MST in form of Routing plan.
   - **send-data :** sends the data depending on the routing plan.
   - **exit-cdn :** deregister the router itself from the Discovery node and exit only in case of successful deregistration.
   - **quit :** quit without asking for deregistration. However, Discovery node will notice the disconnection and will deregister it.
