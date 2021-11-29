# smells-like-lucene-it

Open *smells-like-lucene-it/*

## Build
This will have the effect of installing all prerequisite packages

```mvn package```

## Create Index

```java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar index```

**Note:** Delete the *index/* folder to rebuild the index

## Query Index
```java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar query```

## Trec Eval

```trec_eval qrels-assignment2.part1 results/results_file```