# Contributing to Machina

## When contributing to MACHINA:
- Your code must be formatted as stated here: https://cr.openjdk.java.net/~alundblad/styleguide/index-v6.html.
- Your commit/PR should describe exactly what you have implemented.
- The feature added must have been discussed prior to the commit with Cy4 and / or the other main devs.

## Code Style
- You **must** use tabs and NOT spaces!
- You **must** use data generators, where possible, as much as you can.
- Where possible, ternary operators (`condition ? TRUE : FALSE`) should be used.
- Redundant `return` statements should not exist.
- When you are creating a class, you should add the `@author yourName` element in the javadoc of the said class.
- Curly brackets and braces **must** be on the end of the current line instead of a new line. (for example, the code below **is not** acceptable)
```
class DoNotDoThis
{

}
```
- You **must** follow java syntax naming conventions: https://www.javatpoint.com/java-naming-conventions
- Method References must be used in replacement for a lambda expression when possible.
- The task `gradlew updateLicenses` should be executed whenever there are new classes added by a PR. Even if you don't do so, the PR can still be accepted.

- Note: Eclipse treats `//@formatter:on` and `//@formatter:off` as switches for the formatter. If you see that kind of comment, its purpose is usually to stop eclipse's line wrapping for strings and methods. Please do not remove the comment
