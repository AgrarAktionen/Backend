# Browser REST client _using the platform only_

This project implements a pure REST - Client implemented without any
foreign platforms (no Angular, no Vuejs, no React...).
It shows how to implement the [Model View Controller Architecture](https://aberger.at/blog/architecture/javafx/2019/10/26/mvc-pattern-javafx.html) in a browser client application.

It makes use of [Custom Elements](https://developer.mozilla.org/en-US/docs/Web/Web_Components/Using_custom_elements) and the [Shadow DOM](https://developer.mozilla.org/en-US/docs/Web/Web_Components/Using_shadow_DOM) present in all the browsers
we are interested in.

We also make use of 3 fundamental design principles:
- Single source of truth
- Read Only State
- Changes are made with pure virutal functions

We use typescript to implement this, but without the heavy overhead that would be neccessary with libraries like redux or immutablejs.

see model.ts and store.ts in the project as an example.

Although we do not use lit-html you can install the lit-html Plugin to get syntax coloring for the html templates.

We do mix 2 simple things: mutation and asynchronicity that mixed together 
can behave like [coke and menthos](https://www.youtube.com/watch?v=ZwyMcV9emmc). To avoid that we use [RxJs](http://reactivex.io/)