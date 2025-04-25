# Kroki Collections

Immutable implementations of java collections.

## Why?

 - Java collections implementations are mutable. 
 - One can wrap them to be immutable, but the backing data-structure 
   will still be mutable.
 - While accessing the data, users also pay the price of mutability.

And what is more desirable for applications written on top of immutable 
data structures:

 - data structure built once, never changed
 - since no change needed, the code to access the data can be faster

