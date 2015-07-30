# 表达式求值(exp-eval)

## 问题描述

要求实现一个可定义变量的四则运算**求值器**。  
该求值器支持`+` `-` `*` `/` 四种操作符，`=` 为赋值操作符。  
为了利于解析，每个操作符的左右**必须**存在一个空格。  
每个赋值表达式用`\n`分隔  

其精确的`E-BNF`描述如下，

```
exps  ::= {identifier " = " exp "\n"};
exp   ::= (ref [(" + " | " - " | " * " | " / ") ref]) | ;
ref   ::= identifier | number;
```

根据上述`E-BNF，`**`=`号后面不会出现有两个或两个以上操作符的情况**  
例如，

```
a = b + c / 2
b = c + d + e
```

## 示例说明

一般表达式：

```
a = b + c
b = 1
c = 1
```
对上述`a,b,c`进行求值的结果为，  
`a=2.0, b=1.0, c=1.0`  

特殊表达式：

```
a = b 
b = c 
c = a       // 一般循环依赖
d = d + 1   // 自己依赖自己，特殊循环依赖
e = e       // 自己依赖自己
f = 5 / 0   // 除零
```
对上述`a,b,c,d,e`求值，结果为，  

```
a=Double.NaN, b=Double.NaN, c=Double.NaN,   
d=Double.NaN, e=Double.NaN, f=Double.NaN
```
特殊之处在于，  

 * 需要解决循环定义问题，避免出现死循环  
 * 需要解决除零问题，此时结果为`Double.NaN`  


对于如下重复定义形式  

```
a = 1
a = 2
```
取得后一条的定义`a = 2`，忽略前一条定义`a = 1`  

## 答题建议

 * `clone`或`fork`成自己的repository  
 * 实现`src/main/scala/com/moilioncircle/ExpEval.scala`中的`evalExp`
 * 通过`src/test/scala/com/moilioncircle/TestExpEval.scala`进行测试
 * 如果`clone`到本地，可以使用` sbt test` 进行测试。
 * 如果`fork`到`github`，可以`Pull Request`，进行测试。

## 额外福利

 * 可申请加入，Scala深度交流群（479688557）。
 * 入群后，享有2个免答题测试的邀请名额。
 * 攻城狮朋友圈（www.moilioncircle.com）认证狮友。