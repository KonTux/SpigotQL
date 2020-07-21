# SpigotQL

**SpigotQL is a library that allows you to use MySQL within your Spigot plugins very easily.**
We all hate to type out SQL query commands in Strings in a Java prgramme, don't we?

SpigotQL simply wraps basic MySQL commands with Java objects, which enables us to interact with databases in our favourite style - object oriented programming :P
Instead of having Strings that are extrememly hard to read, barely tell what they are doing and very likely to contain mistakes which are hard to find, you can
simply use an object called ```Select``` and pass it the table and the comlumn names via constructor. The result of this query will be passed back using callbacks
in a conveniently wrapped object called ```QueryResult```, which doesn't require you to handle SQLExceptions everytime you want to do something with it.

**Features:**

- Very easy to use

- Easily create your own MySQL objects

- All queries run asynchronously

- Callbacks to provide thread safety

- Utility methods to easily interact with the database with vbery little code

