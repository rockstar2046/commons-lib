**Project use [Maven][] manager,see [maven usage][]**.

## Understand the basics

Not sure what a pull request is, or how to submit one?  Take a look at GitHub's
excellent [help documentation][] first.

## Add Apache license header to all new classes

```java
/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ...;
```

## Update Apache license header to modified files as necessary

Always check the date range in the license header. For example, if you've
modified a file in 2014 whose header still reads.

```java
 * Copyright 2013 the original author or authors.
```

then be sure to update it to 2014 appropriately.

```java
 * Copyright 2013-2014 the original author or authors.
```

## Use @since tags for newly-added public API types and methods

e.g.

```java
/**
 * ...
 *
 * @author First Last
 * @since JDK1.6
 * @since commons.lang3
 * @since ...
 * @see ...
 */
```

## Use //~ tags for fields,Constructors,method.
These can appear only once.
e.g.
```java
//~ Instance fields ==================================================

/**
 * 
 */
...
```
```java
//~ Constructors ==================================================

/**
 * ${tags}
 */
...
```
```java

//~ Methods ==================================================

/**
 * ${tags}
 */
....
```


## Submit JUnit test cases for all behavior changes
Search the codebase to find related unit tests and add additional @Test methods.


## Contributors
see [contributors][].

-----
Thanks!

[help documentation]: http://help.github.com/send-pull-requests
[Maven]: http://maven.apache.org/index.html "maven"
[maven usage]: http://maven.apache.org/guides/getting-started/ "maven usage"
[contributors]: https://github.com/rockagen/commons-lib/graphs/contributors "see contributors"