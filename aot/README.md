# AOT and GraalVM native images 

## objectives
* what is GraalVM 
* WebAssembly!
* the happy path: a Spring Shell REPL
* `.json` configuration, the Oracle Reachability repository
* reflection: `@RegisterReflectionForBinding`, `@Reflective`, `@ReflectiveScan`, etc
* resource loading, serialization, proxies, etc. with `RuntimeHintsRegistrar`
* `BeanFactoryInitializationAotProcessor` for `Serializable`