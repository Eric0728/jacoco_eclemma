http://tmq.qq.com/2016/08/java-code-coverage-tools-jacoco-principle/

https://www.ibm.com/developerworks/cn/java/j-lo-jacoco/index.html

http://www.eclemma.org/jacoco/

前言
随着敏捷开发的流行，编写单元测试已经成为业界共识。但如何来衡量单元测试的质量呢？有些管理者片面追求单元测试的数量，导致底下的开发人员投机取巧，编写出大量的重复测试，数量上去了，质量却依然原地踏步。相比单纯追求单元测试的数量，分析单元测试的代码覆盖率是一种更为可行的方式。JaCoCo（Java Code Coverage）就是一种分析单元测试覆盖率的工具，使用它运行单元测试后，可以给出代码中哪些部分被单元测试测到，哪些部分没有没测到，并且给出整个项目的单元测试覆盖情况百分比，看上去一目了然。EclEmma 是基于 JaCoCo 的一个 Eclipse 插件，开发人员可以方便的和其交互。因此，本文先从 EclEmma 入手，给读者一个直观的体验。
