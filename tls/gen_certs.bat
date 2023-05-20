@echo off

rd *.jks
echo
rd *.cert
echo f|xcopy cacerts client-ts.jks

set KEYTOOL = "C:\Program Files\Java\jdk-17\bin\keytool.exe"
echo "Creating Keystores"

echo "ourorg0"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias users0-ourorg0 -keyalg RSA -validity 365 -keystore ./users0-ourorg0.jks -storetype pkcs12 -ext SAN=dns:users0-ourorg0 -storepass users0-ourorg0pwd -keypass users0-ourorg0pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"
echo 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias feeds0-ourorg0 -keyalg RSA -validity 365 -keystore ./feeds0-ourorg0.jks -storetype pkcs12 -ext SAN=dns:feeds0-ourorg0 -storepass feeds0-ourorg0pwd -keypass feeds0-ourorg0pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias feeds1-ourorg0 -keyalg RSA -validity 365 -keystore ./feeds1-ourorg0.jks -storetype pkcs12 -ext SAN=dns:feeds1-ourorg0 -storepass feeds1-ourorg0pwd -keypass feeds1-ourorg0pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias feeds2-ourorg0 -keyalg RSA -validity 365 -keystore ./feeds2-ourorg0.jks -storetype pkcs12 -ext SAN=dns:feeds2-ourorg0 -storepass feeds2-ourorg0pwd -keypass feeds2-ourorg0pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"

echo "ourorg1"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias users0-ourorg1 -keyalg RSA -validity 365 -keystore ./users0-ourorg1.jks -storetype pkcs12 -ext SAN=dns:users0-ourorg1 -storepass users0-ourorg1pwd -keypass users0-ourorg1pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"
echo 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias feeds0-ourorg1 -keyalg RSA -validity 365 -keystore ./feeds0-ourorg1.jks -storetype pkcs12 -ext SAN=dns:feeds0-ourorg1 -storepass feeds0-ourorg1pwd -keypass feeds0-ourorg1pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias feeds1-ourorg1 -keyalg RSA -validity 365 -keystore ./feeds1-ourorg1.jks -storetype pkcs12 -ext SAN=dns:feeds1-ourorg1 -storepass feeds1-ourorg1pwd -keypass feeds1-ourorg1pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -genkey -alias feeds2-ourorg1 -keyalg RSA -validity 365 -keystore ./feeds2-ourorg1.jks -storetype pkcs12 -ext SAN=dns:feeds2-ourorg1 -storepass feeds2-ourorg1pwd -keypass feeds2-ourorg1pwd -dname "CN=Users.Users, OU=TP2, O=SD2223, L=LX, S=LX, C=PT"






echo "Exporting Certificates"

echo "ourorg0"

echo users0-ourorg0pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias users0-ourorg0 -keystore users0-ourorg0.jks -file users0-ourorg0.cert 

echo feeds0-ourorg0pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias feeds0-ourorg0 -keystore feeds0-ourorg0.jks -file feeds0-ourorg0.cert

echo feeds1-ourorg0pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias feeds1-ourorg0 -keystore feeds1-ourorg0.jks -file feeds1-ourorg0.cert

echo feeds2-ourorg0pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias feeds2-ourorg0 -keystore feeds2-ourorg0.jks -file feeds2-ourorg0.cert

echo "ourorg1"

echo users0-ourorg1pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias users0-ourorg1 -keystore users0-ourorg1.jks -file users0-ourorg1.cert 

echo feeds0-ourorg1pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias feeds0-ourorg1 -keystore feeds0-ourorg1.jks -file feeds0-ourorg1.cert

echo feeds1-ourorg1pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias feeds1-ourorg1 -keystore feeds1-ourorg1.jks -file feeds1-ourorg1.cert

echo feeds2-ourorg1pwd| "C:\Program Files\Java\jdk-17\bin\keytool.exe" -exportcert -alias feeds2-ourorg1 -keystore feeds2-ourorg1.jks -file feeds2-ourorg1.cert


echo "Creating Client Truststore"

echo "ourorg0"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file users0-ourorg0.cert -alias users0-ourorg0 -keystore client-ts.jks 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file feeds0-ourorg0.cert -alias feeds0-ourorg0 -keystore client-ts.jks 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file feeds1-ourorg0.cert -alias feeds1-ourorg0 -keystore client-ts.jks 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file feeds2-ourorg0.cert -alias feeds2-ourorg0 -keystore client-ts.jks

echo "ourorg1"

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file users0-ourorg1.cert -alias users0-ourorg1 -keystore client-ts.jks 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file feeds0-ourorg1.cert -alias feeds0-ourorg1 -keystore client-ts.jks 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file feeds1-ourorg1.cert -alias feeds1-ourorg1 -keystore client-ts.jks 

"C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file feeds2-ourorg1.cert -alias feeds2-ourorg1 -keystore client-ts.jks  