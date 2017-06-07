# clean
rm -rf build/

# package
mkdir -p build/WEB-INF/classes/com/test/
cp resources/* build/WEB-INF/classes/
cp com/test/*.class build/WEB-INF/classes/com/test/

cp web.xml build/WEB-INF/
cp *.jsp build/
mkdir -p build/WEB-INF/lib
cp lib/* build/WEB-INF/lib
cd build/
jar -cvf ../shopcart.war .
cd ..

# deploy
cp shopcart.war /usr/local/Cellar/tomcat/8.5.9/libexec/webapps/
