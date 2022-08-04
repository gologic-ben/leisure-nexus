# The leisures nexus

## In a nutshell

Find new leisure ideas by your source of references.

## How it works

Connect your fries as inspiration, share your interests and find new referral sources.

## Examples 

### Recommand leisures

- Princess Leia recommend movies like Star Wars IV and Venom.

```

                   +--------------+
+---------+     +--+ Star Wars IV |
|Princess |     |  +--------------+
+  Leia   +-----+
|         |     |
+---------+     |   +-----------+
                +---+ Venom     |
                    +-----------+
```

### Discover new leisures

- Princess Leia follows the references of her friends *Tim* and *Bob*
- Princess Leia can quickly discover new sources of leisure thanks to the references of her friends.
- And so on for all of her sources of interests. 

```
                                          +-------------------+
                                     +----+ E.T.              |
                                     |    +-------------------+
                                     |    +-------------------+
                                     +----+ Independance Day  |
                    +---------+      |    +-------------------+
+---------+      +--+ Tim     +------+    +-------------------+
|Princess |      |  +---------+      +----+ Ghost Buster      |
+  Leia   +------+                        +-------------------+
|         |      |
+---------+      |   +-----------+                   +----------+
                 +---+ Bob       +--------+----------+ The Mask |
                     +-----------+        |          +----------+
                                          |
                                          |   +----------+
                                          +---+ Titanic  |
                                              +----------+
```


### Get new source !

- Princess Leia and Tim love cinema.
- Princess Leia sees that her friend Tim likes Xien's movie recommendations.
- Thanks to Xien, Princess Leia discovers Gladiator and Pulp Fiction ! 

```

                                                        +-----------+
                                                    +---+ Gladiator |
                    +---------+     +---------+     |   +-----------+
+---------+      +--+ Tim     +-----+ Xien    +-----+
|Princess |      |  +---------+     +---------+     |
+  Leia   +------+                                  |
|         |                                         |   +--------------+
+---------+                                         +---+ Pulp Fiction |
                                                        +--------------+
                                                                         
```

## How to run

* Build image: `docker build --tag leisure-nexus:latest .`
* Check image: `docker images`
* Run in Kubernetes: `kubectl run leisure-nexus --image=leisure-nexus:latest --image-pull-policy=Never --port=8080`
* Access application with port-forwarding: `kubectl port-forward pods/leisure-nexus 8080:8080`
