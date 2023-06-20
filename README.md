# Kasper
Kasper is a scalable, persistent, in-memory nested key-value database designed for asynchronous operations and blazingly fast access. Built with Java.

## Where We Are Heading
Kasper's initial implementation for it's network protocols was through raw Java Sockets and the DOM. However, with XML latency taking up more than 90% of the query duration, I had to switch protocols. From here on forward, we will be using Protocol Buffers for sending data over our sockets-- as well as using Netty for sockets. 

## Kasper References: Absolute References
I'm so excited to bring to you my most ambitious feature yet: **Absolute References!**

Absolute collections allow you to save on space and make sure that your data stays consistent! No primary-foreign keys needed, no duplicate data is required, and no cascading algorithms are used. 

## Why Kasper?

- Kasper stays in the memory, making it faster than traditional databases.
- Kasper allows for nested data structures not present in other in-memory databases like Redis.
- Kasper has a robust, easy-to-use API with Kasper Drivers.
- Kasper is persistent and allows for data to persist even if shut down.


## Why Not Kasper?

- Kasper is expensive to maintain as a primary database as it runs in the memory.
- Kasper has limited caching support as of the moment.
- Databases such as Redis may be faster for simpler data models.

## The Kasper Implementation Roadmap:

## For KasperBeans (Kasper Java Driver / API):
- Build client-side logic for retrieving nested date.
- BUild client-side logic for filter operations for server-side computing.

## For the Kasper Server
- Build authentication features
- Build the Kasper Toolkit with Swing
- Create a diagram for handling server operations
- Implement a ROM storage system for persistence.
- Implement multiple queries feature.
