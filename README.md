# smells-like-lucene-it

Open *cd Assignment_2/info-retrieval-group/smells-like-lucene-it/*

## Build
This will have the effect of installing all prerequisite packages

```mvn package```

## Create Index

```java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar index```

**Note:** Delete the *index/* folder to rebuild the index

## Evaluation
```java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar evaluate```

This command will produce **two results files**: 'results_file_config_1' and 'results_file_config_2'.
This files represent results from the two application configurations that are discussed in our report.`

## Trec Eval

```cd```

```cd trec_eval```

```trec_eval "../Assignment_2/info-retrieval-group/qrels-assignment2.part1" "../Assignment_2/info-retrieval-group/results/results_file_config_1"```

```trec_eval "../Assignment_2/info-retrieval-group/qrels-assignment2.part1" "../Assignment_2/info-retrieval-group/results/results_file_config_2```
