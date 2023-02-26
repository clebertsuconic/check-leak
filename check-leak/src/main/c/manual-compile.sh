gcc -I. -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -g -std=gnu11 -Wall  -fPIC -MD -o agent.o -c agent.c
gcc -fPIC -g -std=gnu11 -Wall  -shared -Wl,-soname,libcheckleak.so -o ../resources/platforms-lib/linux-amd64/libcheckleak.so agent.o
