
# Contributing

## Tools needed

1. Git
2. Maven 3.9.3 or greater (optional, can also use `./mvnw` to download automatically)
3. JDK 21

Clone the repository. Then run the build command. For example:

```
git clone https://github.com/A248/MorePaperLib.git
cd MorePaperLib
./mvnw package -DskipTests -Dinvoker.skip=true
```

## Platforms supported

More features require more tests. Currently, tests follow the pattern in `src/it` in order to run code in different classpath environments.

