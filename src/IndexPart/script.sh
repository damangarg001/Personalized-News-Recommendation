#!/usr/bin/bash
# $1=where i/p file is present
# $2=where o/p file should be placed
# $3=CoreNLP library Path
# $4=java file Path
	
inputDir="`readlink -e $1`"
libraryDir="`readlink -e $2`"
#javac -cp	./src/jsoup-1.7.3.jar:. ./src/test.java
#java -cp	./src/jsoup-1.7.3.jar:. ./src/test
cd $inputDir
i=0
for file in *;do
	NAME[`echo $i`]=$file
	i=`expr $i+1`
done
cd $libraryDir

for file in ${NAME[*]};do
java -cp stanford-corenlp-3.3.1.jar:stanford-corenlp-3.3.1-models.jar:xom.jar:joda-time.jar:jollyday.jar:ejml-0.23.jar -Xmx3g edu.stanford.nlp.pipeline.StanfordCoreNLP -annotators tokenize,ssplit,pos,lemma,ner  -file `echo $inputDir/$file` 
mv `echo $file.xml` `echo $inputDir/$file.xml`
done
