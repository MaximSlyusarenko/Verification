# Verification
Static analizer for Verification course at ITMO University

## Что мы сделали
### 1. Анализатор пустых catch блоков
Находит пустые catch блоки, в котрых переменная не называется ignored. Наш небольшой тест - EmptyCatch
Тестировали на java-проекте maven, вывод на нем (две ошибки):
```
Analyzing started at 1529687411661
WARNING: 
ru.ifmo.ctddev.verification.staticanalyzer.EmptyCatch:
Method "bad": Empty exception handler:
Begin: Line: 10, column: 11, End:Line: 12, column: 9
" catch (ArrayIndexOutOfBoundsException e) {
}"
Begin: Line: 12, column: 11, End:Line: 14, column: 9
" catch (Exception e) {
}"

```
### 2. Анализатор бесполезных битовых операций
Находит такие операции, как a | 0, a & 0, a << 0, a >> 0. Наш небольшой тест - Operators
Тестировали на java-проекте maven, на нем ошибок не найдено
### 3. Анализатор одинаковых операндов у оператора
Находит одинаковые операнды у оператора, такие как a == a, a - a. Наш небольшой тест - Operators
Тестировали на java-проекте maven, на нем ошибок не найдено
### 4. Анализатор всегда истинных и всегда ложных выражений в if
Анализатор получает данные из if верхнего уровня (разбивает большое выражение по && и находит, какие из выражений будут истинными или ложными внутри данного if выражения). Потом на if внутреннего уровня проверяет, является ли данное выражение всегда истинным или всегда ложным, а также проверяет все его подвыражения, чтобы указать на лишние выражения. После этой проверки анализатор выводит все найденные выражения, после чего дополняет всегда верные выражения выражениями, которые будут внутри него верны, и запускает обход внутри себя, после этого обхода он удаляет те выражения, которые верны для него.
Наш небольшой тест - KnownExpressions
Тестировали на java-проекте maven, вывод на нем (семь ошибок):
```
Analyzing started at 1529688031251
WARNING: 
org.apache.maven.repository.legacy.DefaultWagonManager:
Method "connectWagon": Always true or always false expressions analyzer:
For if statement Begin: Line: 304, column: 14, End:Line: 317, column: 9
"if (repository.getAuthentication() != null) {
    wagon.connect(new Repository(repository.getId(), repository.getUrl()), authenticationInfo(repository));
} else if (repository.getProxy() != null) {
    wagon.connect(new Repository(repository.getId(), repository.getUrl()), proxyInfo(repository));
} else {
    wagon.connect(new Repository(repository.getId(), repository.getUrl()));
}"
known expressions: 
repository.getAuthentication() != null has value true
For if statement Begin: Line: 310, column: 14, End:Line: 317, column: 9
"if (repository.getProxy() != null) {
    wagon.connect(new Repository(repository.getId(), repository.getUrl()), proxyInfo(repository));
} else {
    wagon.connect(new Repository(repository.getId(), repository.getUrl()));
}"
known expressions: 
repository.getProxy() != null has value true

WARNING: 
org.apache.maven.artifact.repository.MavenArtifactRepository:
Method "basedir": Always true or always false expressions analyzer:
For if statement Begin: Line: 271, column: 21, End:Line: 279, column: 21
"// special case: if there is a windows drive letter, then keep the original return value
if (retValue.length() >= 2 && (retValue.charAt(1) == '|' || retValue.charAt(1) == ':')) {
    retValue = retValue.charAt(0) + ":" + retValue.substring(2);
} else if (index >= 0) {
    // leading / was previously stripped
    retValue = "/" + retValue;
}"
known expressions: 
(retValue.charAt(1) == '|' || retValue.charAt(1) == ':') has value true
retValue.length() >= 2 has value true
retValue.length() >= 2 && (retValue.charAt(1) == '|' || retValue.charAt(1) == ':') has value true

WARNING: 
org.apache.maven.toolchain.DefaultToolchainsBuilder:
Method "build": Always true or always false expressions analyzer:
For if statement Begin: Line: 63, column: 14, End:Line: 66, column: 9
"if (userToolchainsFile != null) {
    logger.debug("Toolchains configuration was not found at " + userToolchainsFile);
}"
known expressions: 
userToolchainsFile != null has value true

WARNING: 
org.apache.maven.cli.CleanArgument:
Method "cleanArgs": Always true or always false expressions analyzer:
For if statement Begin: Line: 63, column: 21, End:Line: 72, column: 21
"// if this is the case of "-Dfoo=bar", then we need to adjust the buffer.
if (addedToBuffer) {
    currentArg.setLength(currentArg.length() - 1);
} else // otherwise, we trim the trailing " and append to the buffer.
{
    // TODO introducing a space here...not sure what else to do but collapse whitespace
    currentArg.append(' ').append(cleanArgPart);
}"
known expressions: 
addedToBuffer has value true

WARNING: 
org.apache.maven.cli.MavenCli:
Method "populateRequest": Always true or always false expressions analyzer:
For if statement Begin: Line: 1542, column: 14, End:Line: 1551, column: 9
"if (!commandLine.hasOption(CLIManager.ALSO_MAKE) && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS)) {
    request.setMakeBehavior(MavenExecutionRequest.REACTOR_MAKE_DOWNSTREAM);
} else if (commandLine.hasOption(CLIManager.ALSO_MAKE) && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS)) {
    request.setMakeBehavior(MavenExecutionRequest.REACTOR_MAKE_BOTH);
}"
known expressions: 
!commandLine.hasOption(CLIManager.ALSO_MAKE) has value false
!commandLine.hasOption(CLIManager.ALSO_MAKE) && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS) has value false
commandLine.hasOption(CLIManager.ALSO_MAKE) has value true
For if statement Begin: Line: 1547, column: 14, End:Line: 1551, column: 9
"if (commandLine.hasOption(CLIManager.ALSO_MAKE) && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS)) {
    request.setMakeBehavior(MavenExecutionRequest.REACTOR_MAKE_BOTH);
}"
known expressions: 
commandLine.hasOption(CLIManager.ALSO_MAKE) has value true
commandLine.hasOption(CLIManager.ALSO_MAKE) && commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS) has value true
commandLine.hasOption(CLIManager.ALSO_MAKE_DEPENDENTS) has value true
```
