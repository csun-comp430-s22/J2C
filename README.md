![Logo](images/J2Clogo.png)


# Project Title


Design a programming language: J2C

The goal for us is to pass this class and try to learn how to make a compiler and since we are all familiar with Java it makes sense to do it in Java. We are choosing C as our target language because some of our members have some experience with C and it's not too low level for us to understand and explain to other members of the group.

## Appendix

Design this programming language with:
    • Concrete and abstract syntax
    • Statically-checked types
    • Expressions
    • Subroutines
    • Mechanisms for computation abstraction
• Implement a compiler for the designed language, complete with:
    • A parser
    • A typechecker / static semantic analyzer
    • A code generator

## Authors

    Ibtehaz Utsay
    Ziaur Chowdhury
    Noah Reidinger
    Ryan Nieto
    Leo Babakhanian
    Vardges Harutyunyan

## Documentation

Documentation Link (<https://docs.google.com/document/d/1mQWRqwIdn_nY9M3pWh7nB8k_Ht-t0xxur-jjfsBvdB4/edit>)

Language Name: J2C

Compiler Implementation Language and Reasoning: We are all comfortable working with Java and it’s needless to experiment with a new language at this time.

Planned Restrictions: We don’t know quite yet.

Target Language: C

Language Description: J2C (Java to C). The goal for us is to pass this class and try to learn how to make a compiler and since we are all familiar with Java it makes sense to do it in Java. We are choosing C as our target language because some of our members have some experience with C and it's not too low level for us to understand and explain to other members of the group.

<h3>Abstract Syntax:</h3>
var is a variable
classname is the name of a class
methodname is the name of a method
str is a string
i is an integer

```
type:: = int | bool | void | Built-in types
    classname; includes Object and String
op:: = + | - | * | / | < | > | Arithmetic operations
exp:: = var | str | i | Variables, strings, and integers are expressions
this | Refers to my instance
exp op exp | Arithmetic operations
new classname(exp*) | Creates a new instance of a class
exp. methodname(exp*) | Calls a method
(type)exp | Casts an expression as a type/Runtime exception for improper casting
vardec:: = type var | Variable declaration
stmt:: = vardec = exp; | Variable declaration
var = exp; | Assignment
while (exp) stmt | while loops
break; | break
{ stmt* } | block
if (exp) stmt else stmt | if/else
return exp; | return an expression
return; | return Void
println(exp) | Prints something to the terminal
exp. methodname(exp*) | Calls a method
access ::= public | private | protected
methoddef ::= access type methodname(vardec*) stmt vardecs are comma-separated 
instancedec ::= access vardec; instance variable declaration classdef ::= class classname extends classname {
instancedec* 
constructor(vardec*) stmt vardecs are comma-separated 
methoddef*
} 
program ::= classdef* exp exp is the entry point

```

# Features

Computation Abstraction Non-Trivial Feature: Class based inheritance + Methods

Non-Trivial Feature #2: Subtyping

Non-Trivial Feature #3: Type Casting

Work Planned for Custom Component: Access Modifiers

# Testing

To run the tests:

```
mvn compile
mvn test
```
To view reports: 
```
mvn jacoco: report
open target/site/jacoco/index.html
```
