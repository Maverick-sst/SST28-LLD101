# Distributed Cache - Low Level Design

This document contains the UML class diagram for the Distributed Cache system, structured across various layers such as Entry, Routing, Core Logic, Eviction, and Service layers.

## UML Class Diagram

```mermaid
classDiagram
    %% Entry Layer
    class LoadBalancer {
        +routeRequest(request) CacheProxy
    }

    class CacheProxy {
        -router : CacheRouter
        -cacheService : CacheService
        +get(key) Value
        +put(key, value) void
    }

    %% Routing Layer
    class CacheRouter {
        -nodes : List~CacheNode~
        -hashFunction : HashFunction
        +getNode(key) CacheNode
    }

    class HashFunction {
        +hash(key) int
    }

    %% Cluster
    class CacheCluster {
        -nodes : List~CacheNode~
        +getNodes() List~CacheNode~
    }

    %% Cache Node
    class CacheNode {
        -cacheStore : CacheStore
        +get(key) Value
        +put(key, value) void
    }

    %% Core Cache Logic
    class CacheStore {
        -capacity : int
        -map : Map~Key, Node~
        -evictionPolicy : EvictionPolicy
        +get(key) Value
        +put(key, value) void
        +remove(key) void
    }

    %% Eviction Policy (Strategy Pattern)
    class EvictionPolicy {
        <<interface>>
        +onAccess(node) void
        +onInsert(node) void
        +evict() Node
    }

    class LRUEvictionPolicy {
        -dll : DoublyLinkedList
        +onAccess(node) void
        +onInsert(node) void
        +evict() Node
    }

    %% Doubly Linked List
    class DoublyLinkedList {
        -head : Node
        -tail : Node
        +addToHead(node) void
        +remove(node) void
        +removeTail() Node
    }

    class Node {
        -key : Key
        -value : Value
        -prev : Node
        -next : Node
    }

    %% Service Layer
    class CacheService {
        -dbAdapter : DatabaseAdapter
        +get(key) Value
        +put(key, value) void
    }

    %% DB Adapter (Adapter Pattern)
    class DatabaseAdapter {
        <<interface>>
        +fetch(key) Value
        +save(key, value) void
    }

    class PostgresAdapter {
        +fetch(key) Value
        +save(key, value) void
    }

    %% Relationships
    %% Client approaches LoadBalancer
    LoadBalancer --> CacheProxy : routes to
    
    %% Proxy handles the dual responsibility - check cache or check DB via Service
    CacheProxy --> CacheRouter : routes via
    CacheProxy --> CacheService : falls back to (miss)
    
    %% Router selects the node
    CacheRouter --> CacheNode : selects
    CacheRouter --> HashFunction : uses
    
    %% System has a cluster of nodes
    CacheCluster *-- CacheNode : contains
    
    %% Each node has a store
    CacheNode --> CacheStore : uses
    
    %% Store delegates eviction
    CacheStore --> EvictionPolicy : uses
    CacheStore --> Node : manages via Map
    
    %% Eviction implementations
    LRUEvictionPolicy ..|> EvictionPolicy : implements
    LRUEvictionPolicy --> DoublyLinkedList : uses
    
    %% DLL structure
    DoublyLinkedList *-- Node : maintains
    
    %% Service integrates with DB
    CacheService --> DatabaseAdapter : uses
    PostgresAdapter ..|> DatabaseAdapter : implements
```

## Mental Model Flow
**Client Request Path:**
1. **Client** hits **LoadBalancer**
2. **LoadBalancer** routes to **CacheProxy**
3. **CacheProxy** delegates to **CacheRouter** to find the responsible cache node
4. **CacheRouter** identifies the appropriate **CacheNode** (via hashing)
5. Request is forwarded to the **CacheNode** -> **CacheStore** (checks LRU map)
6. If **cache miss**, **CacheProxy** calls **CacheService** which queries the **DB**
