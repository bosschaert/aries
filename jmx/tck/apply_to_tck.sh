# Change these
ARIES_DIR=/Users/david/clones/bosschaert_aries_jmx
OSGI_GIT_DIR=/Users/david/clones/osgi-build-jmx
ARIES_TARGET_VERSION=0.999

JMX_TARGET_DIR=$OSGI_GIT_DIR/licensed/repo
LIB_BASE=$OSGI_GIT_DIR/cnf/repo/org.apache.aries.impl.jmx
LIB_FILE=$LIB_BASE/org.apache.aries.impl.jmx-4.2.0.lib

mkdir -p -v $JMX_TARGET_DIR/org.apache.aries.jmx
cp $ARIES_DIR/jmx/jmx-bundle/target/org.apache.aries.jmx-0.3.1-SNAPSHOT.jar $JMX_TARGET_DIR/org.apache.aries.jmx/org.apache.aries.jmx-$ARIES_TARGET_VERSION.jar

mkdir -p -v $JMX_TARGET_DIR/org.apache.aries.util
cp $ARIES_DIR/util/target/org.apache.aries.util-0.4-SNAPSHOT.jar $JMX_TARGET_DIR/org.apache.aries.util/org.apache.aries.util-$ARIES_TARGET_VERSION.jar

mkdir -p -v $LIB_BASE
echo "slf4j.api; version=1.5.10" > $LIB_FILE
echo "slf4j.simple; version=1.5.10" >> $LIB_FILE
echo "org.apache.aries.util; version=$ARIES_TARGET_VERSION" >> $LIB_FILE
echo "org.apache.aries.jmx; version=$ARIES_TARGET_VERSION" >> $LIB_FILE

cp $ARIES_DIR/jmx/tck/org.osgi.test.cases.jmx.bnd.bnd $OSGI_GIT_DIR/org.osgi.test.cases.jmx/bnd.bnd
