**PROJECT TITLE: ACME - ACME to DOT generator

**GROUP: 2A

NAME1: Nelson Oliveira, NR1: ei09027, GRADE1: 17, CONTRIBUTION: 30%

NAME2: João Santos, NR2: ei09037, GRADE2: 17, CONTRIBUTION: 30%

NAME3: Margarida Pereira, NR3: ei09005, GRADE3: 16, CONTRIBUTION: 20%

NAME4: Ana Ferreira, NR4: ei09104, GRADE4: 16, CONTRIBUTION: 20%

** HOW TO EXECUTE: Go to the console, go to the project file where the JAR file is, and then type in "java -jar ACMEParser.jar". Then the program will run. Any grpahical outputs will be sent out to the folder with the file that has been ran.

** SUMMARY: This tool has the particularity of transforming any ACME Systems into a DOT file, including parsing correctly and verifying the syntax of any .acme file. These files include Families, Type Declarations, as well as others. Do notice that only System Declarations with Components, Connectors and Attachments are shown in the DOT file (PNG File).

**DEALING WITH SYNTACTIC ERRORS: It doesn't blow up after the first error, in fact, it does not continue parsing after the first error appears, but it does show which was the syntactic error, and where it's been made. After that, you can correct your file, and then re-run the tool.

**SEMANTIC ANALYSIS: The semantics are really simple within this project: Since you do not do any operations with the variables, nevertheless you use them to anything, you just declare them. So what this is limited to do is to check whether there are repeated declarations inside each block (uses a stack to keep trace of variables inside each individual block).

**INTERMEDIATE REPRESENTATIONS (IRs): Both HLIR and LLIR are similar. The tool will print out the HLIR as showing the normal dump of the root (function names), whereas the LLIR will print ou the function names and the tokens that have been used in each of those functions.

**CODE GENERATION: There's only code generation for System Declarations as said before. The only aspect that might influence the generation of the code would be the lack of connectors or attachments, or declarations of those kinds in the system. If there are no connectors, there's no way you can connect two components. If there's no components, but there's a connector, then the graphic won't show up as good.

**OVERVIEW: The tool works mainly with JavaCC, and makes use of the self-made Java Objects, such as a Symbol Table, or objects to store all of the data that the program might find. Everytime the program finds some particular kind of data for the DOTTY generation, it stores that kind of data in an object, to then later be parsed out to a DOT file. Nevertheless, the usage of a stack to check out all of the semantic errors is used within the .jjt file.

**TESTSUITE AND TEST INFRASTRUCTURE: The testsuite is composed by 4 examples, being each one a different case. A file that passes through, a file that gives out a syntactic error, another one for a semantics error and another one that simply passes because it's not a system structure. All of the other examples can be found in the folder "examples", where each test produces a different result.

**TASK DISTRIBUTION: The different tasks have been almost equally distributed throughout all of the group, being the following:

- Finding the Grammar: All of the members had a hard time finding examples for such an extinct and arcaic language, but regardless of that, we all did a great part of it.
- Delcaring Tokens and Lexical Analysis: It has been equally distributed, since the whole grammar, token identification and passage onto a .jjt file has been divided by 4.
- Syntactic Analysis: Since the grammar was blowing up everywhere with conflicts, the group tried to fix it together, but in the end, Nelson did quite the job with it, fixing all of the ambiguity that resided in it.
- Semantic Analysis: We can say that Ana and Margarida gave their best shot by putting all of the tokens inside a SymbolTable object, and organizing all of the insertions. Even though, Nelson applied a strategy with a Stack and an ArrayList<String>, so that all of the errors would be shown up later on.
- Intermediate Representation and Tree Construction: Here, João took care of the biggest of the affairs. All of the token insertion into each SimpleNode object has been divided between Nelson and João (some for Ana and Margarida too), so that in the end, João could generate both trees.
- Code Generation: Code Generation was all part of Nelson's job. Generating a .dot file isn't that hard, but took a while considering possible designs for the DOTTY file itself. After some browsing through the Graphviz Gallery (found at: http://www.graphviz.org/Gallery.php), it was pretty clear putting all the properties of each attribute (Role, Port, Connector, Component) would be fairly easy to do with a table. So, after some hours of dotty generation, it was rather manageable.

**PROS: Well, it does generate DOT files, and parses ACME's language! More positive aspects than those can't be really said.

**CONS: To be honest, no negative aspects are actually seen. Perhaps the way we have the main menu. Probably we can change that to generate all tests at the same time or something, instead of compiling one by one.