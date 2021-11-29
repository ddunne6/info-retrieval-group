 						Setup

----------------------------------------------------------------------------------------------------------------------------------------------

Download Assignment 2 Document Collection and add extracted folder here. (The file is large so it was put in the .gitignore)

Build
------------------------------
mvn package


################################# Run#############################################################################

 Index

--------------------------------
java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar index
--------------------------------------------------------------


If index is already created and you want to restart, 
cd ..
rm -r index/index_corpus

Then use the command: java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar index

 Query

CD to the smells-like-luceneit directory


Command: java -jar target/smells-like-lucene-it-0.0.1-SNAPSHOT.jar query
-----------------------------------------------------------------

Trec_Eval

cd .. 
trec_eval qrels-assignment2.part1 results/results_file
