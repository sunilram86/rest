# build
CP=".:lib/*:/usr/local/Cellar/tomcat/8.5.9/libexec/lib/servlet-api.jar"
javac com/test/ItemStore.java -classpath $CP
javac com/test/CartStore.java -classpath $CP
javac com/test/App.java -classpath $CP
