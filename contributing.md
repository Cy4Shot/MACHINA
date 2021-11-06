# Contributing to Machina

## When contributing to MACHINA:
- Your code must be formatted as stated below.
- Your commit/PR should describe exactly what you have implemented.
- The feature added must have been discussed prior to the commit with lead developers.

1. The the order within each of these groups of members **must** be as follows:
- Public
- Protected
- Package
- Private

2. You **must** use tabs and NOT spaces!

3. You **must** follow these braces and brackets rules:
- Braces should be on the end of the current line instead of a new line
- `if`, `else`, `else if`,  `catch`, `finally`, `while` and `do...while` blocks, **must** use curly brackets, even if the block only has one line. (only single `return` statements do not need brackets, like `if (true) return 2;`)

4. You **must** use data generators, where possible, as much as you can.

5.  You **must** follow the Oracle Java Naming Conventions which are as follows:
- Packages: lower_snake_case
- Classes: UpperCamelCase (Avoid abbreviations unless the acronym or abbreviation is more widely used than the long form. For example HTML or URI/URL/URN)
- Interfaces: UpperCamelCase (Should describe what the interfaces does, do not prefix with I)
- Methods: camelCase (Should be verbs and well describe what the method does)
- Constants: UPPER_SNAKE_CASE - Sometimes known as SCREAMING_SNAKE_CASE (ANSI Constants should be avoided)
- Variables: camelCase (Should not start with `\_` or `$` and should not be a singular letter unless the letter makes sense. For example `int x = getPosX()`)

6. Java Identifiers **must** follow the following order:
- public/private/protected
- abstract
- static
- final
- default

1.  Imports **must** be formatted as follows:
- Sorted:
  - Static Imports
  - Non-Static Imports
- Imports should not be line-wrapped, no matter the length.
- No unused imports should be present.
- Wildcard imports should be avoided unless a very large number of classes (over 12) are imported from that package.
- No more than 2 wildcard import per file should exist.

1.  Square brackets for arrays **must** be at the type and not at the variable.
2.  Annotations **must** be on a separate line from the declaration unless it is annotating a parameter.
3.   Lambda Expressions **must** not contain a body if the body is only 1 line.
4.   Method References **must** be used in replacement for a lambda expression when possible.
5.   Parameter Types in lambda expressions **must** be omitted unless they increase readability.
6.   Redundant Parenthesis **must** be removed unless they are clearly increasing readability.
7.   Long literals **must** use uppercase `L`.
8.   Hexadecimal literals **must** use uppercase `A-F`.
9.   All other literals **must** use lowercase letters.

10. Where possible, ternary operators (`condition ? TRUE : FALSE`) should be used.
11. Redundant `return` statements should not exist.
12. When you are creating a class, you should add the `@author yourName` element in the javadoc of the said class.

For any other niche cases, refer to https://cr.openjdk.java.net/~alundblad/styleguide/index-v6.html