# smells-like-lucene-it

Open *smells-like-lucene-it/*

## Build
This will have the effect of installing all prerequisite packages

```mvn package```

## Create Index

```java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar index```

**Note:** Delete the *index/* folder to rebuild the index

## Evaluation
```java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar evaluation```

This command will produce **two results files**: 'results_file_config_1' and 'results_file_config_2'.
This files represent results from the two application configurations that are discussed in our report.`

## Trec Eval

```trec_eval qrels-assignment2.part1 results/results_file_config_1```

```trec_eval qrels-assignment2.part1 results/results_file_config_2```
