# User Admin

How to run it?

git clone https://github.com/jcaponte/useradmin.git

cd jcaponte

useradmin> mvn clean install

useradmin> cd useradmin-site

useradmin-site> mvn spring-boot:run

Now you can access useradmin site at https://localhost:8080/ 

if you need to use ssl then, 

how to generate ssl cetificate:

keytool -genkey -alias pagefactory -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore pagefactorykeystore.p12 -validity 3650
