ARIES_DIR=/Users/davidb/clones/bosschaert_aries_jmx
JMX_TARGET_DIR=/Users/davidb/clones/osgi-build-jmx/licensed/repo
LIB_BASE=/Users/davidb/clones/osgi-build-jmx/cnf/repo/org.apache.aries.impl.jmx
LIB_FILE=$LIB_BASE/org.apache.aries.impl.jmx-4.2.0.lib

mkdir -p -v $JMX_TARGET_DIR/org.apache.aries.jmx
cp $ARIES_DIR/jmx/jmx-bundle/target/org.apache.aries.jmx-0.3.1-SNAPSHOT.jar $JMX_TARGET_DIR/org.apache.aries.jmx/org.apache.aries.jmx-0.999.jar

mkdir -p -v $JMX_TARGET_DIR/org.apache.aries.util
cp $ARIES_DIR/util/target/org.apache.aries.util-0.4-SNAPSHOT.jar $JMX_TARGET_DIR/org.apache.aries.util/org.apache.aries.util-0.999.jar

mkdir -p -v $LIB_BASE
echo "slf4j.api; version=1.5.10" > $LIB_FILE
echo "slf4j.simple; version=1.5.10" >> $LIB_FILE
echo "org.apache.aries.util; version=0.999" >> $LIB_FILE
echo "org.apache.aries.jmx; version=0.999" >> $LIB_FILE
