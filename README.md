# Kasper Database Documentation

![Kasper Logo.png](Kasper%20Database%20Documentation%20d1b2d8be7db043c188e8ddf99760d008/Kasper_Logo.png)

************************About Kasper************************

---

> Kasper is a blazing-fast, persistent, in-memory database that supports complex, nested data structures.
> 

---

© ruff.io, 2023

---

****************************Update Roadmap****************************

<aside>
💡 Kasper has moved to phase two of the alpha release. From Kasper Alfalfa (0.0.1 - 0.4.9) version to Kasper Basalt (0.5.0 - 0.9.9).

</aside>

**************************************The Kasper Pre-Release Roadmap:**************************************

- [x]  Create and Read key-value pairs.
- [x]  Support for nested data.
- [x]  Partial reference support.
- [x]  Automatic snapshot support.
- [x]  Persistence support.
- [x]  Protocol buffer adaptation.
- [x]  Delete operations.
- [ ]  Documentation finish.
- [ ]  Query [find all with] and [find all that has] operations.

****************************Kasper Post 1.0 Roadmap****************************

- [ ]  Kasper references appending support. (Add reference to an object).
- [ ]  Kasper circular references.
- [ ]  Kasper circular reference queries.

******************************************************Here’s what’s new so far:******************************************************

- Upon reaching ************Basalt************, we have created support for updating keys, which comprehensively updates all references to said key.
- Switched to a better garbage collection module. Expect reduced memory usage.
- Switched to protocol buffers as a part of the ****Nitro Wire****. Nitro Wire is a TCP-based protocol for messaging with the Kasper Engine, up to twice the speed of the old protocol.
- Compared to the XML-based protocol, Nitro Wire is up to **2000x faster**.
- Partial support for references (Only for nested set and get).
- Lazy-loading, client-side paths. Allow for complex path computation.

---

**Drivers**

**************server Tools**************

[Beans for Java](https://www.notion.so/Beans-for-Java-b5519d4fa3f147059e1b818ab0e897bf?pvs=21)

[Monty for Python](https://www.notion.so/Monty-for-Python-d36e04c9715d42e68be4f4eab0865866?pvs=21)

[Kasper Toolkit](https://www.notion.so/Kasper-Toolkit-4f9b11f904a54949843aaee61e63fe61?pvs=21)

---

## Why Kasper?

- Faster than Redis.
- Kasper stays in the memory, making it faster than traditional databases.
- Kasper allows for nested data structures not present in other in-memory databases like Redis.
- Kasper has a robust, easy-to-use API with Kasper Drivers.
- Kasper is persistent and allows for data to persist even if shut down.

## Why Not Kasper?

- Kasper is expensive to maintain as a primary database as it runs in the memory.
- Kasper has limited caching support as of the moment.
- Databases such as Redis may be faster for simpler data models.

---

**Read More:**

[Data Structures](https://www.notion.so/Data-Structures-0d1032a9622b49feb1be96ba26615b6a?pvs=21)

[Driver Requirements](https://www.notion.so/Driver-Requirements-546cb853baae477ab6528cb196b087bc?pvs=21)

---

********Support and Contact:********

✉️rufelleemmanuel.pactol@cit.edu

(c) 2023, ruff.io
