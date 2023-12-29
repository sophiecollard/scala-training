# Functional Patterns

This repository is a template for various assignment I give during Scala training. Its purpose is to deal with all the unpleasantness of getting SBT to work and allow trainees to focus on writing code and getting it to work.

Each task should be done in its dedicated module, which contains the bare minimum for you to get started:
- an empty file in which to start adding code.
- a skeleton for tests because yes, you are expected to test your work.

## Json

The first task is to write a comfortable Json ADT. You will do this in the `Json.scala` of the `json` module (although you should feel free to create additional files).

We'll be taking a slightly simplified definition of Json here, to avoid getting bogged down in technical details that are not terribly relevant to what we're doing.

In the context of this exercise, a Json value can be one of:
- a string.
- an integer.
- a boolean.
- null.
- an array of Json values.
- an object, which is a collection of key / value pairs where keys are strings and values are Json values.

For example:

```json
{
  "string" : "this is a string",
  "integer": 123,
  "bool"   : true,
  "null"   : null,
  "array"  : ["a string", null, 321],
  "object" : {
    "key": "value"
  }
}
```

To make things interesting though, you need to write comfortable creation helpers, so that users have to manipulate as little as possible of the underlying implementation when creating values.

### Json documents

For the sake of training, we're going to pretend that compound and scalar data types need to treated differently. What this means, concretely, is that you need to have a type representing all possible Json values, and another that is restricted to array and object.

### Pretty printing

Write a simple pretty printer for Json values - a function that turns them in their string representation. There's no need to do anything fancy like different whitespace handling mechanisms (human readable or compact, for example), but you do need to implement two version:
- one that prints any Json value.
- one that only prints Json documents.

### Null simplification

Another interesting Json interpreter is one that returns a transformed version of a Json document in which all `null` values in an object are removed.

For example, take the following input:

```json
{
  "is_null": null,
  "not_null" : {
    "not_null_either": 123,
    "also_null": null
  }
}
```

Your function should yield:

```json
{
  "not_null" : {
    "not_null_either": 123
  }
}
```

## Codecs

The second task is about practicing type classes. This will take place in the `codecs` module.

We want to have a generic way of encoding and decoding arbitrary types to Json values.

For the sake of the exercise, we'll consider the following very simple type:
```scala
case class User(name: String, age: Int, kind: User.Kind)
object User {
  sealed trait Kind
  object Kind {
    case object Privileged extends Kind
    case object Normal extends Kind
    case object Guest extends Kind
  }
}
```

### Json encoding
Write an `Encoder` type class that is essentially a function from some `A` to a Json value. This should be done in the `Encoder.scala` file.

Try and make it as comfortable to use as possible:
- instance creation should not involve `new Encoder...`.
- there should be an easy way to summon an implicit instance and work with it.

Make sure to implement the following default instances:
- primitive types: `String`, `Int`, `Boolean`.
- simple collections, such as `List` and `Vector`.
- tuples (let's say `Tuple2` and `Tuple3`, no need to do all 22 of them), making sure that nested tuples work.
- dates, encoded as ISO 8601 formatted strings.

You also need to implement combinators for:
- `Option` (where the empty case is encoded as `null`).
- `Either`, making sure to handle the `Either[Int, Int]` case properly.

Finally, write an encoder for `User`, reusing as much as possible of the basic building blocks you've written so far. For example, `Encoder[User.Kind]` should be the result of calling a method of `Encoder[String]`.

### Json decoding
This is the other side of encoding: write a `Decoder` that takes a Json value and turns it into some arbitrary `A`. This should be done in the `Decoder.scala` file.

The main difficulty will be that decoding can fail. For example, you cannot decode a Json string as an `Int`. Instead, you should encode the possibility for failure in your decoding function, by having it return something very much like `Either[String, A]`, where `String` is an error message.

For every `Encoder` instance or combinator you wrote, there'll need to be an equivalent in `Decoder`. Additionally, you should write:
- `or`, which given two decoders, will attempt the first one and fallback to the second one if that fails.

## Parsing

TODO

## Random number generator

TODO
